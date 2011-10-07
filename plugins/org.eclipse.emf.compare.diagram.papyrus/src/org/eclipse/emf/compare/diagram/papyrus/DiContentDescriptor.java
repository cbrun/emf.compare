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
package org.eclipse.emf.compare.diagram.papyrus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.internal.content.Util;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.XMLContentDescriber;
import org.eclipse.papyrus.sashwindows.di.DiPackage;

/**
 * Content XML for Papyrus diagrams.
 * @author Mickael Barbero <a href="mailto:mickael.barbero@obeo.fr">mickael.barbero@obeo.fr</a>
 */
@SuppressWarnings("restriction")
public class DiContentDescriptor extends XMLContentDescriber {

	/**
	 * MAX_CHAR_READ.
	 */
	private static final int MAX_CHAR_READ = 200;
	/**
	 * SASH_WINDOWS_MNGR.
	 */
	private static final String SASH_WINDOWS_MNGR = DiPackage.Literals.SASH_WINDOWS_MNGR.getName();
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.content.XMLContentDescriber#describe(java.io.Reader, org.eclipse.core.runtime.content.IContentDescription)
	 */
	@Override
	public int describe(Reader input, IContentDescription description) throws IOException {
		final int describe = super.describe(input, description);
		if (describe == VALID && hasSashWindowsMngrDecl(input)) {
			return VALID;
		}
		return describe;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.content.XMLContentDescriber#describe(java.io.InputStream, org.eclipse.core.runtime.content.IContentDescription)
	 */
	@Override
	public int describe(InputStream input, IContentDescription description) throws IOException {
		final int describe = super.describe(input, description);
		if (describe == VALID && hasSashWindowsMngrDecl(input, getEncoding(input))) {
			return VALID;
		}
		return describe;
	}
	
	/**
	 * Check if the input contains a Sash Window manager.
	 * @param input The input.
	 * @return True if it contains the sash window manager.
	 * @throws IOException exception.
	 */
	private boolean hasSashWindowsMngrDecl(Reader input) throws IOException {
		final BufferedReader reader = new BufferedReader(input);
		String xmlDecl = new String();
		String line = reader.readLine();

		while (xmlDecl.length() < MAX_CHAR_READ && line  != null) {
			xmlDecl = xmlDecl + line;
			if (line.indexOf(SASH_WINDOWS_MNGR) != -1) {
				return true;
			}
			line = reader.readLine();
		}
		return false;
	}
	
	/**
	 * Check if the input contains a Sash Window manager.
	 * @param input The input.
	 * @param encoding The encoding.
	 * @return True if it contains the sash window manager.
	 * @throws IOException exception.
	 */
	private boolean hasSashWindowsMngrDecl(InputStream input, String encoding) throws IOException {
		final byte[] sashWindowsMngtBytes = SASH_WINDOWS_MNGR.getBytes(encoding);

		int c = input.read();
		final int read = 0;

		// count is incremented when subsequent read characters match the xmlDeclEnd bytes,
		// the end of xmlDecl is reached, when count equals the xmlDeclEnd length
		int count = 0;

		while (read < MAX_CHAR_READ && c != -1) {
			if (c == sashWindowsMngtBytes[count])
				count++;
			else
				count = 0;
			if (count == sashWindowsMngtBytes.length)
				return true;
			c = input.read();
		}
		return false;
	}
	
	
	/**
	 * Get the encoding of the input.
	 * @param input the input.
	 * @return The string encoding.
	 * @throws IOException exception.
	 */
	private String getEncoding(InputStream input) throws IOException {
		final byte[] bom = Util.getByteOrderMark(input);
		String xmlDeclEncoding = "UTF-8"; //$NON-NLS-1$
		input.reset();
		if (bom != null) {
			if (bom == IContentDescription.BOM_UTF_16BE)
				xmlDeclEncoding = "UTF-16BE"; //$NON-NLS-1$
			else if (bom == IContentDescription.BOM_UTF_16LE)
				xmlDeclEncoding = "UTF-16LE"; //$NON-NLS-1$
			// skip BOM to make comparison simpler
			input.skip(bom.length);
		}
		
		return xmlDeclEncoding;
	}
}