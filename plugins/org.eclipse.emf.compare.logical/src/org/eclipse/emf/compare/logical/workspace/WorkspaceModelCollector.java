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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author cedric
 * @since 1.3
 */
public class WorkspaceModelCollector {

	private ResourceSet set;

	private DependencyResourceFactory factory = new DependencyResourceFactory();

	private Set<String> contentTypes;

	private WorkspaceDependencies deps;

	public WorkspaceModelCollector(Set<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

	public int getNumberOfFilesToCrawl() throws CoreException {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		CountingVisitor counter = new CountingVisitor();
		root.accept(counter);
		return counter.getCount();
	}

	class CountingVisitor implements IResourceVisitor {

		private int result = 0;

		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFile) {
				IFile file = (IFile)resource;
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

	public WorkspaceDependencies crawl(final IProgressMonitor pm) throws CoreException {
		set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getContentTypeToFactoryMap().put("*", factory); //$NON-NLS-1$
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", factory); //$NON-NLS-1$
		for (String contentType : contentTypes) {
			set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(contentType, factory);
		}

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		int todo = getNumberOfFilesToCrawl();

		pm.beginTask("Analyzing workspace dependencies", todo); //$NON-NLS-1$
		root.accept(new IResourceVisitor() {

			public boolean visit(IResource resource) throws CoreException {
				if (resource instanceof IFile) {
					IFile file = (IFile)resource;
					if (pm.isCanceled()) {
						throw new RuntimeException("The user interrupted the operation."); //$NON-NLS-1$
					}
					if (isOfInterest(file)) {
						URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
						set.getResource(fileURI, true);
						pm.worked(1);
					}
				}
				return true;
			}

		});
		pm.done();

		deps = new WorkspaceDependencies(set);

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
				IResourceDelta delta = event.getDelta();
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
									Resource resource = resourceSet.getResource(
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
									IFile file = (IFile)delta.getResource();
									if (isOfInterest(file)) {
										URI fileURI = URI.createPlatformResourceURI(file.getFullPath()
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

					ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
					/*
					 * looks like delta might be null on some cases (experienced it on major workspace changes
					 * like deleting all the projects)
					 */
					if (delta != null)
						delta.accept(visitor);

					for (Resource removed : visitor.getRemovedResources()) {
						removed.unload();
						set.getResources().remove(removed);
					}

					for (Iterator<Resource> i = visitor.getChangedResources().iterator(); i.hasNext();) {
						Resource resource = i.next();
						if (resource.isLoaded()) {
							resource.unload();
							try {
								resource.load(Collections.EMPTY_MAP);
							} catch (IOException exception) {
								// TODO log or remove
							}
						}
					}

				} catch (CoreException exception) {
					// TODO log or remove
				}
			}
		}

	}
}
