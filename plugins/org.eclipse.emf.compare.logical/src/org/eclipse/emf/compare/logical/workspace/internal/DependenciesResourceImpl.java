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
package org.eclipse.emf.compare.logical.workspace.internal;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspaceFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * A {@link DependencyResource} implemented by loading the model behind the scene (not resolving proxies) and
 * browsing it collecting all the uris to provide the list of dependencies. This implementation is the least
 * efficient one you can get but on the other hand it is not specific to the serialization format.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class DependenciesResourceImpl extends ResourceImpl {
	/**
	 * Predicate returning true if the EObject is a proxy.
	 */
	private static final Predicate<EObject> IS_PROXY = new Predicate<EObject>() {

		public boolean apply(EObject arg0) {
			return arg0.eIsProxy();
		}
	};

	/**
	 * Predicate returning true if the EReference should be cross referenced.
	 */
	private static final Predicate<EReference> TO_BE_CROSSREFERENCED = new Predicate<EReference>() {

		public boolean apply(EReference arg0) {
			return !arg0.isDerived();
		}

	};

	/**
	 * Function transforming a complete EMF EObject into a string representing just the Resource path
	 * (trimming any present fragment).
	 */
	private static final Function<InternalEObject, String> URI_TO_RESOURCE = new Function<InternalEObject, String>() {

		public String apply(InternalEObject eObj) {
			final URI from = eObj.eProxyURI();
			if (from.hasFragment()) {
				return from.trimFragment().toString();
			}
			return from.toString();
		}
	};

	/**
	 * Create a new resource.
	 * 
	 * @param uri
	 *            uri to use to create the resource.
	 */
	public DependenciesResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream is, Map<?, ?> options) throws IOException {

		final Model root = WorkspaceFactory.eINSTANCE.createModel();
		getContents().add(root);

		/*
		 * FIXME : we should use the proper resource factory here.
		 */
		final XMIResourceImpl xmiRes = new XMIResourceImpl(uri);
		xmiRes.load(options);

		try {
			final Set<String> foundDependencies = collectDependencies(xmiRes);

			for (final String uri : foundDependencies) {
				final Dependency newDep = WorkspaceFactory.eINSTANCE.createDependency();
				newDep.setTarget(forgeProxy(uri));
				root.getDependencies().add(newDep);
			}
		} catch (WrappedException e) {
			root.setLoadable(false);
		}

	}

	/**
	 * Collect the cross resources dependencies from an XMIResource.
	 * 
	 * @param xmiRes
	 *            resource to process.
	 * @return the list of resource URI which the given resource depend on.
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws IOException
	 *             on error accessing the file.
	 */
	private Set<String> collectDependencies(Resource xmiRes) throws UnsupportedEncodingException, IOException {
		final Set<String> foundDependencies = Sets.newLinkedHashSet();
		final Iterator<EObject> it = EcoreUtil.getAllProperContents(xmiRes, false);

		while (it.hasNext()) {
			final EObject next = it.next();
			final Collection<EObject> proxiesEObjects = Collections2.filter(allRefs(next), IS_PROXY);
			foundDependencies.addAll(Collections2.transform(
					Sets.newHashSet(Iterables.filter(proxiesEObjects, InternalEObject.class)),
					URI_TO_RESOURCE));
		}
		return foundDependencies;
	}

	/**
	 * Method returning all the referenced objects which should be considered for the cross referencing.
	 * 
	 * @param from
	 *            the source object.
	 * @return all the referenced objects which should be cross referenced.
	 */
	private Collection<EObject> allRefs(EObject from) {
		final List<EObject> referencedObjects = Lists.newArrayList();

		for (final EReference ref : Iterables
				.filter(from.eClass().getEAllReferences(), TO_BE_CROSSREFERENCED)) {
			if (ref.isMany()) {
				final Collection<EObject> referenced = (Collection<EObject>)from.eGet(ref, false);
				referencedObjects.addAll(referenced);
			} else {
				final EObject referenced = (EObject)from.eGet(ref);
				if (referenced != null) {
					referencedObjects.add(referenced);
				}
			}
		}
		return referencedObjects;
	}

	/**
	 * Forge an EObject proxy targeting the root element of the given resource uri.
	 * 
	 * @param resourceURI
	 *            the resource URI.
	 * @return a proxified {@link Model} which should be present at the root of the given resource uri.
	 */
	private Model forgeProxy(String resourceURI) {
		final URI uri = URI.createURI(resourceURI + "#/").resolve(getURI()); //$NON-NLS-1$
		final Model targetedModel = WorkspaceFactory.eINSTANCE.createModel();
		((InternalEObject)targetedModel).eSetProxyURI(uri);
		return targetedModel;
	}
}
