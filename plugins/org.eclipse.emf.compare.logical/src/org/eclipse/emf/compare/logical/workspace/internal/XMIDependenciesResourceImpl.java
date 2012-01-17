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
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspaceFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
		} catch (SAXException e) {
			root.setLoadable(false);
		} catch (ParserConfigurationException e) {
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
	 * @throws SAXException
	 *             when Sax can't load the XMI
	 * @throws ParserConfigurationException
	 *             when Sax is misconfigured
	 */
	// CHECKSTYLE:OFF
	private Set<String> collectDependencies(InputStream is) throws UnsupportedEncodingException, IOException,
			SAXException, ParserConfigurationException {
		// CHECKSTYLE:ON
		final Set<String> foundDependencies = new LinkedHashSet<String>();

		final SAXParserFactory factory = SAXParserFactory.newInstance();
		final InputSource input = new InputSource(is);
		final SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(input, new DefaultHandler() {

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				super.startElement(uri, localName, qName, attributes);
				final int length = attributes.getLength();
				// Each attribute
				for (int i = 0; i < length; i++) {
					// a dependency looks like this : href="SimpleMM1I%20/my/othernstance2.xmi#/
					final String value = attributes.getValue(i);
					if (value != null && value.indexOf('#') != -1 && value.indexOf('#') > 0) {
						final String baseURI = value.substring(0, value.indexOf('#'));
						foundDependencies.add(baseURI);
					}

				}
			}

		});

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
