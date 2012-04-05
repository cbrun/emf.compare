/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content.part.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ui.AdapterUtils;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider used by {@link ModelContentMergePropertiesPart}s displaying {@link Match2Elements}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class PropertyContentProvider implements IStructuredContentProvider {
	/** EObject which properties are provided by this content provider. */
	private EObject inputEObject;

	/**
	 * This <code>int</code> represents the side of the viewer part this content provider feeds. Must be one
	 * of
	 * <ul>
	 * <li>{@link EMFCompareConstants#RIGHT}</li>
	 * <li>{@link EMFCompareConstants#LEFT}</li>
	 * <li>{@link EMFCompareConstants#ANCESTOR}</li>
	 * </ul>
	 */
	private int partSide;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing needs to be disposed of here.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		Object[] elements = new Object[] {};

		if (inputElement instanceof Match) {
			final Match match = (Match)inputElement;

			if (partSide == EMFCompareConstants.LEFT) {
				inputEObject = match.getLeft();
			} else if (partSide == EMFCompareConstants.RIGHT) {
				inputEObject = match.getRight();
			} else if (((Match)inputElement).getOrigin() != null) {
				inputEObject = match.getOrigin();
			}
		}
		if (inputEObject != null) {
			final List<List<Object>> inputElements = new ArrayList<List<Object>>();
			// This will fetch the property source of the input object
			final AdapterFactory factory = AdapterUtils.getAdapterFactory();
			final IItemPropertySource inputPropertySource = (IItemPropertySource)factory.adapt(inputEObject,
					IItemPropertySource.class);
			// Iterates through the property descriptor to display only the "property" features of the input
			// object
			for (final IItemPropertyDescriptor descriptor : inputPropertySource
					.getPropertyDescriptors(inputEObject)) {
				/*
				 * Filtering out "advanced" properties can be done by hiding properties on which
				 * Arrays.binarySearch(descriptor.getFilterFlags(input),
				 * "org.eclipse.ui.views.properties.expert") returns an int > 0.
				 */
				final EStructuralFeature feature = (EStructuralFeature)descriptor.getFeature(inputEObject);
				final List<Object> row = new ArrayList<Object>();
				row.add(feature);
				row.add(descriptor.getPropertyValue(inputEObject));
				inputElements.add(row);
			}

			elements = inputElements.toArray();
			Arrays.sort(elements, new Comparator<Object>() {
				public int compare(Object first, Object second) {
					final String name1 = ((EStructuralFeature)((List<?>)first).get(0)).getName();
					final String name2 = ((EStructuralFeature)((List<?>)second).get(0)).getName();

					return name1.compareTo(name2);
				}
			});
		}
		return elements;
	}

	/**
	 * Returns the EObject which properties are currently displayed in the properties tab.
	 * 
	 * @return The EObject which properties are currently displayed in the properties tab.
	 */
	public EObject getInputEObject() {
		return inputEObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (viewer instanceof ModelContentMergePropertyTab) {
				final ModelContentMergePropertyTab properties = (ModelContentMergePropertyTab)viewer;
				properties.getTable().clearAll();
				partSide = properties.getSide();
			}
		}
	}
}
