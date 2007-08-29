/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

/**
 * Basic wrapper for an {@link ITypedElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TypedElementWrapper implements ITypedElement {
	/** {@link EObject} this wrapper is build upon. */
	private final EObject wrappedObject;

	/**
	 * Constructs an instance given the object to wrap.
	 * 
	 * @param eObject
	 *            Object to wrap as an {@link ITypedElement}.
	 */
	public TypedElementWrapper(EObject eObject) {
		wrappedObject = eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITypedElement#getImage()
	 */
	public Image getImage() {
		return EMFCompareEObjectUtils.computeObjectImage(wrappedObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITypedElement#getName()
	 */
	public String getName() {
		return EMFCompareEObjectUtils.computeObjectName(wrappedObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ITypedElement#getType()
	 */
	public String getType() {
		return wrappedObject.getClass().getName();
	}

	/**
	 * Returns the object wrapped within this {@link TypedElementWrapper}.
	 * 
	 * @return The object wrapped within this {@link TypedElementWrapper}.
	 */
	public EObject getObject() {
		return wrappedObject;
	}
}
