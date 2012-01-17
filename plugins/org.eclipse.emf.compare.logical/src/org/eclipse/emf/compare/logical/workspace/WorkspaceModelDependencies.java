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

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

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
import org.eclipse.emf.compare.logical.workspace.internal.DependencyResourceFactory;
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
public class WorkspaceModelDependencies {
	/**
	 * resource set used to keep the data.
	 */
	private ResourceSet set;

	/**
	 * A fairly specific resource factory which will build instances of Model+Dependencies from an XMI based
	 * resource.
	 */
	private final Resource.Factory xmiFactory = new XMIDependencyResourceFactory();

	/**
	 * A fairly specific resource factory which will build instances of Model+Dependencies from an XMI based
	 * resource.
	 */
	private final Resource.Factory genericFactory = new DependencyResourceFactory();

	/**
	 * the content types to index and consider.
	 */
	private final Set<String> genericContentTypes = Sets.newLinkedHashSet();

	/**
	 * the content XMI specific content types.
	 */
	private final Set<String> xmiContentTypes = Sets.newLinkedHashSet();

	/**
	 * the file extension to index and consider using the generic loading mechanism.
	 */
	private final Set<String> genericFileExtensions = Sets.newLinkedHashSet();

	/**
	 * the file extension to index and consider using the XMI loading mechanism.
	 */
	private final Set<String> xmiFileExtensions = Sets.newLinkedHashSet();

	/**
	 * an image of the cross resource dependencies.
	 */
	private CrossResourceDependencies deps;

	/**
	 * Create a new instance ready for settings.
	 */
	public WorkspaceModelDependencies() {
		this.set = new ResourceSetImpl();
	}

	/**
	 * Tell the class to inspect files with the given file extension and consider those as XMI.
	 * 
	 * @param fileExtension
	 *            file extension.
	 * @return the current instance to chain commands.
	 */
	public WorkspaceModelDependencies extensionAsXMI(String fileExtension) {
		xmiFileExtensions.add(fileExtension);
		return this;
	}

	/**
	 * Tell the class to inspect files with the given content type and consider those as XMI.
	 * 
	 * @param contentTypeID
	 *            the content type id.
	 * @return the current instance to chain commands.
	 */
	public WorkspaceModelDependencies contentTypeAsXMI(String contentTypeID) {
		xmiContentTypes.add(contentTypeID);
		return this;
	}

	/**
	 * Tell the class to inspect files with the given file extension. If this file is an XMI you should
	 * probably use the extensionAsXMI() method.
	 * 
	 * @param fileExtension
	 *            file extension.
	 * @return the current instance to chain commands.
	 */
	public WorkspaceModelDependencies extensionAsModel(String fileExtension) {
		genericFileExtensions.add(fileExtension);
		return this;
	}

	/**
	 * Tell the class to inspect files with the given content type. If this file is an XMI you should probably
	 * use the contentTypeAsXMI() method.
	 * 
	 * @param contentTypeID
	 *            the content type id.
	 * @return the current instance to chain commands.
	 */
	public WorkspaceModelDependencies contentTypeAsModel(String contentTypeID) {
		genericContentTypes.add(contentTypeID);
		return this;
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
		final CountingVisitor counter = new CountingVisitor();
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
		 */
		public CountingVisitor() {
			super(allContentTypes(), allFileExtensions(), new NullProgressMonitor());
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
		for (String extension : genericFileExtensions) {
			set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(extension, genericFactory);
		}
		for (String extension : xmiFileExtensions) {
			set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(extension, xmiFactory);
		}
		for (final String contentType : genericContentTypes) {
			set.getResourceFactoryRegistry().getContentTypeToFactoryMap().put(contentType, genericFactory);
		}
		for (final String contentType : xmiContentTypes) {
			set.getResourceFactoryRegistry().getContentTypeToFactoryMap().put(contentType, xmiFactory);
		}

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		final int todo = getNumberOfFilesToCrawl();

		pm.beginTask("Analyzing workspace dependencies", todo); //$NON-NLS-1$
		root.accept(new AbstractModelResourceVisitor(allContentTypes(), allFileExtensions(), pm) {

			@Override
			protected void processModel(IFile file) {
				final URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
				set.getResource(fileURI, false);
				pm.worked(1);
			}
		});
		pm.done();

		deps = new CrossResourceDependencies(set);
		return deps;
	}

	/**
	 * return all the content types of interest for this indexer.
	 * 
	 * @return all the content types of interest for this indexer.
	 */
	private SetView<String> allContentTypes() {
		return Sets.union(genericContentTypes, xmiContentTypes);
	}

	/**
	 * return true if the file matches our content types.
	 * 
	 * @param file
	 *            any file.
	 * @return true if the file matches our content types.
	 */
	// CHECKSTYLE:OFF
	private boolean isOfInterest(IFile file) {
		// CHECKSTYLE:ON
		for (String fileExtension : allFileExtensions()) {
			if (fileExtension.equals(file.getFileExtension()))
				return true;
		}
		for (String contentTypeID : allContentTypes()) {
			if (AbstractModelResourceVisitor.hasContentType(file, contentTypeID))
				return true;
		}

		return false;
	}

	/**
	 * return all the file extensions of interest for this indexer.
	 * 
	 * @return all the file extensions of interest for this indexer.
	 */
	private SetView<String> allFileExtensions() {
		return Sets.union(genericFileExtensions, xmiFileExtensions);
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
