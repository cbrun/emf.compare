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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspaceFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

/**
 * A {@link DependencyResource} implementation specific to XMI. It is parsing the xmi input looking for
 * external references but not actually loading the model. It is way faster than
 * {@link DependenciesResourceImpl} but only works in case of XMI resources.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class XMIDependenciesResourceImpl extends ResourceImpl {
	/**
	 * the loading buffer size.
	 */
	private static final int BUFFER_SIZE = 0x10000;

	/**
	 * Create a new resource.
	 * 
	 * @param uri
	 *            uri to use to create the resource.
	 */
	public XMIDependenciesResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream is, Map<?, ?> options) throws IOException {
		final Model root = WorkspaceFactory.eINSTANCE.createModel();
		getContents().add(root);

		try {
			final Set<String> foundDependencies = collectDependencies(is);

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
	 * Collect the list of file dependencies from an inputstream containing an XMI serialization.
	 * 
	 * @param is
	 *            inputstream providing XMI.
	 * @return all the "other resources" dependencies.
	 * @throws UnsupportedEncodingException
	 *             when the given encoding is not supported.
	 * @throws IOException
	 *             in case of IO error.
	 */
	private Set<String> collectDependencies(InputStream is) throws UnsupportedEncodingException, IOException {
		final Set<String> foundDependencies = new LinkedHashSet<String>();

		// a depedency looks like this : href="SimpleMM1I%20/my/othernstance2.xmi#/

		final char[] buffer = new char[BUFFER_SIZE];
		final StringBuilder out = new StringBuilder();
		// TODO find the encoding...
		final Reader in = new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				out.append(buffer, 0, read);
			}
		} while (read >= 0);
		final Pattern p = Pattern.compile("([a-zA-Z/\\.:]+)#"); //$NON-NLS-1$

		final Matcher m = p.matcher(out.toString());
		while (m.find()) {
			final String depURI = m.group(1);
			foundDependencies.add(depURI);
		}
		return foundDependencies;
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
