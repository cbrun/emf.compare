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
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.workspace.internal.DependencyResourceFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class WorkspaceModelCollector {

	private ResourceSet set;

	private final DependencyResourceFactory factory = new DependencyResourceFactory();

	private final Set<String> contentTypes;

	private CrossResourceDependencies deps;

	public WorkspaceModelCollector(Set<String> contentTypesToConsider) {
		this.contentTypes = contentTypesToConsider;
	}

	public int getNumberOfFilesToCrawl() throws CoreException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		final CountingVisitor counter = new CountingVisitor();
		root.accept(counter);
		return counter.getCount();
	}

	class CountingVisitor implements IResourceVisitor {

		private int result = 0;

		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFile) {
				final IFile file = (IFile)resource;
				if (isOfInterest(file)) {
					result++;
				}
			}
			return true;
		}

		public int getCount() {
			return result;
		}

	}

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
		root.accept(new IResourceVisitor() {

			public boolean visit(IResource resource) throws CoreException {
				if (resource instanceof IFile) {
					final IFile file = (IFile)resource;
					if (pm.isCanceled()) {
						throw new RuntimeException("The user interrupted the operation."); //$NON-NLS-1$
					}
					if (isOfInterest(file)) {
						final URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
						set.getResource(fileURI, true);
						pm.worked(1);
					}
				}
				return true;
			}

		});
		pm.done();

		deps = new CrossResourceDependencies(set);

		return deps;
	}

	boolean isOfInterest(IFile file) {
		return contentTypes.contains(file.getFileExtension());
	}

	public void listen() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new DependenciesUpdater());
	}

	class DependenciesUpdater implements IResourceChangeListener {

		public void resourceChanged(IResourceChangeEvent event) {
			// Only listening to these.
			// if (event.getType() == IResourceDelta.POST_CHANGE)
			{
				final IResourceDelta delta = event.getDelta();
				try {
					class ResourceDeltaVisitor implements IResourceDeltaVisitor {
						protected ResourceSet resourceSet = deps.getResourceSet();

						protected Collection<Resource> changedResources = new ArrayList<Resource>();

						protected Collection<Resource> removedResources = new ArrayList<Resource>();

						protected Collection<Resource> addedResources = new ArrayList<Resource>();

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

					for (final Iterator<Resource> i = visitor.getChangedResources().iterator(); i.hasNext();) {
						final Resource resource = i.next();
						if (resource.isLoaded()) {
							resource.unload();
							try {
								resource.load(Collections.EMPTY_MAP);
							} catch (final IOException exception) {
								// TODO log or remove
							}
						}
					}

				} catch (final CoreException exception) {
					// TODO log or remove
				}
			}
		}

	}
}
