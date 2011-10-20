/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.EMFCompareLogicalPlugin;
import org.eclipse.emf.compare.logical.extension.AbstractModelResourceVisitor;
import org.eclipse.emf.compare.logical.workspace.internal.XMIDependencyResourceFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This class is responsible for maintaining a consistent view of model to model dependencies (and inverse
 * dependencies) from the Eclipse workspace.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class WorkspaceModelDependenciesSynchronizer {
	/**
	 * resource set used to keep the data.
	 */
	private ResourceSet set;

	/**
	 * A fairly specific resource factory which will build instances of Model+Dependencies from any kind of
	 * resource.
	 */
	private final Resource.Factory factory = new XMIDependencyResourceFactory();

	/**
	 * the content types to index and consider.
	 */
	private final Set<String> contentTypes;

	/**
	 * an image of the cross resource dependencies.
	 */
	private CrossResourceDependencies deps;

	/**
	 * Create a new instance.
	 * 
	 * @param contentTypesToConsider
	 *            the list of content types to index and consider while crawling and listening to the
	 *            workspace.
	 */
	public WorkspaceModelDependenciesSynchronizer(Set<String> contentTypesToConsider) {
		this.contentTypes = contentTypesToConsider;
	}

	/**
	 * return the number of files we'll need to load.
	 * 
	 * @return return the number of files we'll need to load.
	 * @throws CoreException
	 *             in case of error from the underlying platform.
	 */
	private int getNumberOfFilesToCrawl() throws CoreException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();
		final CountingVisitor counter = new CountingVisitor(contentTypes);
		root.accept(counter);
		return counter.getCount();
	}

	/**
	 * A simple visitor counting files from a given content type.
	 * 
	 * @author Cedric Brun <cedric.brun@obeo.fr>
	 */
	class CountingVisitor extends AbstractModelResourceVisitor {
		/**
		 * total number of files to consider.
		 */
		private int result;

		/**
		 * Create a {@link CountingVisitor}.
		 * 
		 * @param contentTypesToConsider
		 *            the list of content types to consider while counting files..
		 */
		public CountingVisitor(Set<String> contentTypesToConsider) {
			super(contentTypesToConsider, new NullProgressMonitor());
		}

		/**
		 * return the number of files to consider.
		 * 
		 * @return the number of files to consider.
		 */
		public int getCount() {
			return result;
		}

		@Override
		protected void processModel(IFile file) {
			result++;
		}

	}

	/**
	 * Crawl the workspace initializing the index.
	 * 
	 * @param pm
	 *            a progress monitor to provide feedback and check for user cancellation.
	 * @return the state of cross resource dependencies.
	 * @throws CoreException
	 *             on error from the underlying platform.
	 */
	public CrossResourceDependencies crawl(final IProgressMonitor pm) throws CoreException {
		set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getContentTypeToFactoryMap().put("*", factory); //$NON-NLS-1$
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", factory); //$NON-NLS-1$
		for (final String contentType : contentTypes) {
			set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(contentType, factory);
		}

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		final int todo = getNumberOfFilesToCrawl();

		pm.beginTask("Analyzing workspace dependencies", todo); //$NON-NLS-1$
		root.accept(new AbstractModelResourceVisitor(contentTypes, pm) {

			@Override
			protected void processModel(IFile file) {
				final URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
				set.getResource(fileURI, true);
				pm.worked(1);
			}
		});
		pm.done();

		deps = new CrossResourceDependencies(set);
		return deps;
	}

	/**
	 * return true if the file matches our content types.
	 * 
	 * @param file
	 *            any file.
	 * @return true if the file matches our content types.
	 */
	private boolean isOfInterest(IFile file) {
		for (String contentTypeID : contentTypes) {
			if (AbstractModelResourceVisitor.hasContentType(file, contentTypeID))
				return true;
		}
		return false;
	}

	/**
	 * Trigger the workspace listening so that any subsequent call on the returned
	 * {@link CrossResourceDependencies} will be up to date regarding latest events from the workspace.
	 */
	public void listen() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new DependenciesUpdater());
	}

	/**
	 * A workspace listener maintaing a resource set.
	 * 
	 * @author Cedric Brun <cedric.brun@obeo.fr>
	 */
	class DependenciesUpdater implements IResourceChangeListener {
		/**
		 * {@inheritDoc}
		 */
		public void resourceChanged(IResourceChangeEvent event) {
			final IResourceDelta delta = event.getDelta();
			try {

				/**
				 * Resource delta visitor called when we get an event from the workspace.
				 */
				class ResourceDeltaVisitor implements IResourceDeltaVisitor {
					protected ResourceSet resourceSet = set;

					protected Collection<Resource> changedResources = new ArrayList<Resource>();

					protected Collection<Resource> removedResources = new ArrayList<Resource>();

					/**
					 * {@inheritDoc}
					 */
					public boolean visit(IResourceDelta delta) {
						if (delta.getFlags() != IResourceDelta.MARKERS
								&& delta.getResource().getType() == IResource.FILE) {
							if ((delta.getKind() & (IResourceDelta.CHANGED | IResourceDelta.REMOVED)) != 0) {
								final Resource resource = resourceSet.getResource(
										URI.createURI(delta.getFullPath().toString()), false);
								if (resource != null) {
									if ((delta.getKind() & IResourceDelta.REMOVED) != 0) {
										removedResources.add(resource);
									} else {
										changedResources.add(resource);
									}
								}
							} else if (delta.getKind() == IResourceDelta.ADDED) {
								//
								final IFile file = (IFile)delta.getResource();
								if (isOfInterest(file)) {
									final URI fileURI = URI.createPlatformResourceURI(file.getFullPath()
											.toOSString(), true);
									set.getResource(fileURI, true);
								}
							}
						}

						return true;
					}

					public Collection<Resource> getChangedResources() {
						return changedResources;
					}

					public Collection<Resource> getRemovedResources() {
						return removedResources;
					}
				}

				final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
				/*
				 * looks like delta might be null on some cases (experienced it on major workspace changes
				 * like deleting all the projects)
				 */
				if (delta != null)
					delta.accept(visitor);

				for (final Resource removed : visitor.getRemovedResources()) {
					removed.unload();
					set.getResources().remove(removed);
				}

				for (Resource changed : visitor.getChangedResources()) {
					if (changed.isLoaded()) {
						changed.unload();
						try {
							changed.load(Collections.EMPTY_MAP);
						} catch (final IOException exception) {
							/*
							 * we used to load the resource but now we can't anymore
							 */

						}
					}
				}

			} catch (final CoreException exception) {
				EMFCompareLogicalPlugin.getDefault().getLog().log(exception.getStatus());
			}
		}
	}
}
