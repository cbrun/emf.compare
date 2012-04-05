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
package org.eclipse.emf.compare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.swt.graphics.Image;

/**
 * Input to be used for a 2 or 3-way comparison in a
 * {@link org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer ModelContentMergeViewer}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelCompareInput implements ICompareInput {

	/** Keeps a list of all the differences (without DiffGroup) detected. */
	private List<DiffElement> diffList;

	/** Memorizes all listeners registered for this {@link ICompareInput compare input}. */
	private final List<ICompareInputChangeListener> inputChangeListeners = new ArrayList<ICompareInputChangeListener>();

	/** If a comparison has already been made, this will hold the corresponding snapshot. */
	private Comparison comparisonSnapshot;

	/**
	 * Creates a CompareInput given the resulting {@link org.eclipse.emf.compare.match.diff.match.MatchModel
	 * match} and {@link org.eclipse.emf.compare.match.diff.diff.DiffModel diff} of the comparison.
	 */
	public ModelCompareInput(Comparison comparison) {
		this.comparisonSnapshot = comparison;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#addCompareInputChangeListener(ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
		inputChangeListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {
		final List<Diff> differences = new ArrayList<Diff>();
		// if (diff instanceof DiffModel) {
		// differences.addAll(((DiffModel)diff).getOwnedElements());
		// } else {
		// for (final DiffModel aDiff : ((DiffResourceSet)diff).getDiffModels()) {
		// differences.addAll(aDiff.getOwnedElements());
		// }
		// }
		// FIXME merge
		doCopy(differences, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * Copies a single {@link Diff} or a {@link DiffGroup} in the given direction.
	 * 
	 * @param element
	 *            {@link Diff Element} to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 */
	public void copy(Diff element, boolean leftToRight) {
		doCopy(element, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * Copies a list of {@link Diff}s or {@link DiffGroup}s in the given direction.
	 * 
	 * @param elements
	 *            {@link Diff Element}s to copy.
	 * @param leftToRight
	 *            Direction of the copy.
	 */
	public void copy(List<Diff> elements, boolean leftToRight) {
		doCopy(elements, leftToRight);
		fireCompareInputChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		if (this.comparisonSnapshot.isThreeWay()) {
			for (MatchResource mRes : this.comparisonSnapshot.getMatchedResources()) {
				if (mRes.getOrigin() != null) {
					return createTypedElement(mRes.getOrigin());
				}
			}
			for (Match meObj : this.comparisonSnapshot.getMatches()) {
				if (meObj.getOrigin() != null) {
					return createTypedElement(meObj.getOrigin());
				}
			}

		}
		return null;
	}

	/**
	 * Returns the comparison snapshot initially fed to this compare input.
	 * 
	 * @return The comparison snapshot initially fed to this compare input.
	 * @since 1.3
	 */
	public Comparison getComparisonSnapshot() {
		return comparisonSnapshot;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getImage()
	 */
	public Image getImage() {
		return EMFCompareEObjectUtils.computeObjectImage(this.comparisonSnapshot);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getKind()
	 */
	public int getKind() {
		if (getAncestor() != null) {
			return EMFCompareConstants.ENABLE_ANCESTOR;
		}
		return EMFCompareConstants.NO_CHANGE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		for (MatchResource mRes : this.comparisonSnapshot.getMatchedResources()) {
			if (mRes.getLeft() != null) {
				return createTypedElement(mRes.getLeft());
			}
		}
		for (Match meObj : this.comparisonSnapshot.getMatches()) {
			if (meObj.getLeft() != null) {
				return createTypedElement(meObj.getLeft());
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getName()
	 */
	public String getName() {
		return EMFCompareEObjectUtils.computeObjectName(this.comparisonSnapshot);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		for (MatchResource mRes : this.comparisonSnapshot.getMatchedResources()) {
			if (mRes.getRight() != null) {
				return createTypedElement(mRes.getRight());
			}
		}
		for (Match meObj : this.comparisonSnapshot.getMatches()) {
			if (meObj.getRight() != null) {
				return createTypedElement(meObj.getRight());
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ICompareInput#removeCompareInputChangeListener(ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
		inputChangeListeners.remove(listener);
	}

	/**
	 * Applies the changes implied by a given {@link Diff} in the direction specified by
	 * <code>leftToRight</code>.
	 * 
	 * @param element
	 *            {@link Diff} containing the copy information.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> otherwise.
	 */
	protected void doCopy(Diff element, boolean leftToRight) {
		// FIXME merge
	}

	/**
	 * Applies the changes implied by a list of {@link Diff} in the direction specified by
	 * <code>leftToRight</code>.
	 * 
	 * @param elements
	 *            {@link Diff}s containing the copy information.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> otherwise.
	 */
	protected void doCopy(List<Diff> elements, boolean leftToRight) {
		// FIXME merge
	}

	/**
	 * Notifies all {@link ICompareInputChangeListener listeners} registered for this
	 * {@link ModelCompareInput input} that a change occured.
	 */
	protected void fireCompareInputChanged() {
		diffList.clear();
		diffList = null;
		for (final ICompareInputChangeListener listener : inputChangeListeners) {
			listener.compareInputChanged(this);
		}
	}

	/**
	 * Creates the {@link ITypedElement} that is to be returned from all three of {@link #getAncestor()},
	 * {@link #getLeft()} and {@link #getRight()}.
	 * 
	 * @param eObject
	 *            EObject we are to wrap within an {@link ITypedElement}.
	 * @return {@link ITypedElement} to be returned for this compare input's sides.
	 * @since 1.3
	 */
	protected ITypedElement createTypedElement(Notifier eObject) {
		return new TypedElementWrapper(eObject);
	}

	public void saveRight(byte[] bytes) {
		// FIXME save new right content

	}

	public void saveLeft(byte[] bytes) {
		// FIXME save new left content

	}
}
