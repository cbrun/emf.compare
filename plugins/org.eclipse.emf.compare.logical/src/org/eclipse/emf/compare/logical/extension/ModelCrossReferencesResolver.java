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
package org.eclipse.emf.compare.logical.extension;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.EMFCompareLogicalPlugin;
import org.eclipse.emf.compare.logical.workspace.CrossResourceDependencies;
import org.eclipse.emf.compare.logical.workspace.WorkspaceModelDependencies;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * an {@link IModelResolver} implementation relying on workspace indexing and listening to make sure
 * subsequent calls to resolve cross model dependencies are efficients. The first call will trigger a
 * workspace indexing and as such might take quite long. Subsequent calls should be pretty much instantaneous.
 * Any file matching the @org.eclipse.emf.ecore@ or @org.eclipse.emf.ecore.xmi@ content type will be indexed.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ModelCrossReferencesResolver implements IModelResolver {
	/**
	 * The cross resources dependencies.
	 */
	private CrossResourceDependencies deps;

	/**
	 * {@inheritDoc}
	 */
	public void resolve(IFile iFile, Resource eResource, IProgressMonitor monitor) {
		/*
		 * We'll search in the scope of the whole workspace
		 */

		if (iFile.getProject() != null && iFile.getProject().getWorkspace() != null
				&& iFile.getProject().getWorkspace().getRoot() != null) {
			final WorkspaceModelDependencies collector = new WorkspaceModelDependencies();
			for (String contentTString : getContentTypes()) {
				collector.contentTypeAsXMI(contentTString);
			}
			collector.extensionAsXMI("ecore").extensionAsXMI("genmodel");
			if (deps == null) {
				try {
					deps = collector.crawl(monitor);
					collector.listen();
				} catch (CoreException e) {
					EMFCompareLogicalPlugin.getDefault().getLog().log(e.getStatus());
				}
			}
			final ResourceSet baseResourceSet = eResource.getResourceSet();
			final Iterable<IFile> modelsIDependOn = deps.getDependencies(Sets.newHashSet(iFile));
			for (IFile xRef : modelsIDependOn) {
				final URI xRefURI = URI.createPlatformResourceURI(xRef.getFullPath().toString(), false);
				baseResourceSet.getResource(xRefURI, true);

			}

		}

	}

	/**
	 * This method is usefull if you want to change the list of content type which are indexed by default.
	 * 
	 * @return the list of content types to consider.
	 */
	protected Set<String> getContentTypes() {
		return Sets.newHashSet("org.eclipse.emf.ecore", "org.eclipse.emf.ecore.xmi");
	}
}
