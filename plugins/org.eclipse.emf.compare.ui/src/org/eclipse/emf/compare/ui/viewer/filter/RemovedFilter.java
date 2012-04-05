/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.filter;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

/**
 * A filter to mask all the removed elements between two versions of model.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class RemovedFilter implements IDifferenceFilter {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter#hides(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public boolean hides(Diff element) {
		return element.getKind().equals(DifferenceKind.DELETE);
	}
}
