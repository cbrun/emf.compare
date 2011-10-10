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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.workspace.CrossResourceDependencies;
import org.eclipse.emf.compare.logical.workspace.WorkspaceModelCollector;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.common.collect.Sets;

public class ModelCrossReferencesResolver implements IModelResolver {

	private CrossResourceDependencies deps = null;

	public void resolve(IFile iFile, Resource eResource, IProgressMonitor monitor) {
		/*
		 * We'll search in the scope of the whole workspace
		 */

		if (iFile.getProject() != null && iFile.getProject().getWorkspace() != null
				&& iFile.getProject().getWorkspace().getRoot() != null) {
			final WorkspaceModelCollector collector = new WorkspaceModelCollector(Sets.newHashSet("xmi",
					"ecore", "odesign", "aird", "genmodel", "uml"));
			if (deps == null) {
				try {
					deps = collector.crawl(monitor);
					collector.listen();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ResourceSet baseResourceSet = eResource.getResourceSet();
			Iterable<IFile> modelsIDependOn = deps.getDependencies(Sets.newHashSet(iFile));
			for (IFile xRef : modelsIDependOn) {
				URI xRefURI = URI.createPlatformResourceURI(xRef.getFullPath().toString(), false);
				baseResourceSet.getResource(xRefURI, true);

			}

		}

	}
}
