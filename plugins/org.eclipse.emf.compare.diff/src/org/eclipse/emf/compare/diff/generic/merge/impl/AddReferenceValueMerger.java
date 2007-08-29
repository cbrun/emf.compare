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
package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link AddReferenceValue}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class AddReferenceValueMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final AddReferenceValue diff = (AddReferenceValue)this.diff;
		final EObject element = diff.getLeftElement();
		final EObject leftTarget = diff.getLeftAddedTarget();
		try {
			EFactory.eAdd(element, diff.getReference().getName(), leftTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof AddReferenceValue) {
				final AddReferenceValue link = (AddReferenceValue)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(diff.getReference().getEOpposite())
						&& link.getLeftAddedTarget().equals(element)) {
					removeFromContainer(link);
				}
			}
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final AddReferenceValue diff = (AddReferenceValue)this.diff;
		final EObject element = diff.getRightElement();
		final EObject rightTarget = diff.getRightAddedTarget();
		try {
			EFactory.eRemove(element, diff.getReference().getName(), rightTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof AddReferenceValue) {
				final AddReferenceValue link = (AddReferenceValue)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(diff.getReference().getEOpposite())
						&& link.getRightAddedTarget().equals(element)) {
					removeFromContainer(link);
				}
			}
		}
		super.undoInTarget();
	}
}