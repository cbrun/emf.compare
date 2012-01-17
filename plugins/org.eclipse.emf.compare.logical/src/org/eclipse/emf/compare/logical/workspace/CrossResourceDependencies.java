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

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This class is responsible for providing IFile inverse references from a resource set providing a light
 * image of models and their dependencies (see {@link WorkspaceModelDependencies}.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class CrossResourceDependencies {
	/**
	 * Transforms an EResource to its corresponding IFile.
	 */
	private static final Function<Resource, IFile> ERESOURCE_TO_IFILE = new Function<Resource, IFile>() {

		public IFile apply(Resource from) {
			final URI eUri = from.getURI();
			final String platformString = eUri.toPlatformString(true);
			return (IFile)ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
		}
	};

	/**
	 * The resource set containing a light representation of models and their dependencies.
	 */
	private final ResourceSet set;

	/**
	 * Cross referencer adapter used to retrieve cross resource references.
	 */
	private final ECrossReferenceAdapter xRef;

	/**
	 * Create a new instance.
	 * 
	 * @param resourceSet
	 *            resource set providing a light image of models and their dependencies (see
	 *            {@link WorkspaceModelDependencies}.
	 */
	public CrossResourceDependencies(ResourceSet resourceSet) {
		this.set = resourceSet;
		this.xRef = new ECrossReferenceAdapter();
		this.set.eAdapters().add(xRef);
	}

	/**
	 * This method returns the list of files having at least one reference on one of the given files.
	 * 
	 * @param roots
	 *            the files you want to get inverse reference from.
	 * @return the list of files referencing the roots you gave.
	 */
	public Iterable<IFile> getInverseReferences(Iterable<IFile> roots) {
		final Set<Resource> dependantEmfResources = Sets.newLinkedHashSet();
		for (final IFile iFile : roots) {
			final URI rootFileURI = URI.createPlatformResourceURI(iFile.getFullPath().toOSString(), true);

			final Resource rootRes = set.getResource(rootFileURI, true);

			assert rootRes.getContents().get(0) instanceof Model;

			final Model model = (Model)rootRes.getContents().get(0);
			for (final EStructuralFeature.Setting inverseSetting : xRef.getInverseReferences(model, true)) {
				final EObject inverseHost = inverseSetting.getEObject();
				if (inverseHost instanceof Dependency) {
					dependantEmfResources.add(inverseHost.eResource());
				}
			}
		}

		return Iterables.filter(Iterables.transform(dependantEmfResources, ERESOURCE_TO_IFILE),
				Predicates.notNull());
	}

	/**
	 * This method returns the list of files referenced by at least one of the given files.
	 * 
	 * @param roots
	 *            originating files.
	 * @return the list of files referenced by the roots ones.
	 */
	public Iterable<IFile> getDependencies(Set<IFile> roots) {
		final Set<Resource> dependantEmfResources = Sets.newLinkedHashSet();
		for (final IFile iFile : roots) {
			final URI rootFileURI = URI.createPlatformResourceURI(iFile.getFullPath().toOSString(), true);

			final Resource rootRes = set.getResource(rootFileURI, true);

			assert rootRes.getContents().get(0) instanceof Model;

			final Model model = (Model)rootRes.getContents().get(0);
			for (final Dependency dep : model.getDependencies()) {
				if (dep.getTarget().eResource() != null) {
					dependantEmfResources.add(dep.getTarget().eResource());
					// TODO find a way to show/discriminate non resolvable dependencies ?
				}
			}

		}
		return Iterables.filter(Iterables.transform(dependantEmfResources, ERESOURCE_TO_IFILE),
				Predicates.notNull());
	}

}
