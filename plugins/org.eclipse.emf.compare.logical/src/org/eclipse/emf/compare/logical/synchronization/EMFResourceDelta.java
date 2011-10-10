/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.synchronization;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.core.diff.IDiff;

/**
 * This subclass of an {@link AbstractEMFDelta} will describe changes in EMF Resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFResourceDelta extends AbstractEMFDelta {
	/** Keeps track of the local resource for which this instance holds deltas. */
	private Resource localResource;

	/** Keeps track of the remote resource for which this instance holds deltas. */
	private Resource remoteResource;

	/** Keeps track of the ancestor resource for which this instance holds deltas. */
	private Resource ancestorResource;

	/** The actual diff as interpretable by the platform. */
	private IDiff diff;

	/** The EMF Compare's {@link DiffModel} this delta reflects. */
	private DiffModel diffModel;

	/**
	 * Instantiates our resource delta given all necessary information.
	 * 
	 * @param parent
	 *            Our parent model delta.
	 * @param representedDiffModel
	 *            The diff model we represent.
	 * @param platformResourceDiff
	 *            The actual resource diff computed by the platform.
	 * @param localEmfResource
	 *            The local variant of the resource for which we'll hold deltas.
	 * @param remoteEmfResource
	 *            The remote variant of the resource for which we'll hold deltas.
	 * @param ancestorEmfResource
	 *            The ancestor variant of the resource for which we'll hold deltas.
	 */
	public EMFResourceDelta(AbstractEMFDelta parent, DiffModel representedDiffModel,
			IDiff platformResourceDiff, Resource localEmfResource, Resource remoteEmfResource,
			Resource ancestorEmfResource) {
		super(parent);
		this.diff = platformResourceDiff;
		this.diffModel = representedDiffModel;
		this.localResource = localEmfResource;
		this.remoteResource = remoteEmfResource;
		this.ancestorResource = ancestorEmfResource;

		createChildren();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#clear()
	 */
	@Override
	public void clear() {
		localResource = null;
		remoteResource = null;
		ancestorResource = null;
		diff = null;
		diffModel = null;

		super.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#getLocal()
	 */
	@Override
	public Object getLocal() {
		return localResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#getAncestor()
	 */
	@Override
	public Object getAncestor() {
		return ancestorResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#getRemote()
	 */
	@Override
	public Object getRemote() {
		return remoteResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#getDiff()
	 */
	@Override
	public IDiff getDiff() {
		return diff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#getPath()
	 */
	@Override
	public IPath getPath() {
		final IResource localIResource = EclipseModelUtils.findIResource(localResource);
		if (localIResource != null) {
			return localIResource.getFullPath();
		}
		return Path.EMPTY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#isDeltaFor(java.lang.Object)
	 */
	@Override
	public boolean isDeltaFor(Object object) {
		if (!(object instanceof Resource)) {
			// No use going any further
			return false;
		}

		boolean isDelta = false;
		final IResource localIResource = EclipseModelUtils.findIResource(localResource);
		final IResource compareTo = EclipseModelUtils.findIResource((Resource)object);
		if (localIResource != null && compareTo != null) {
			isDelta = localIResource.getFullPath().equals(compareTo.getFullPath());
		}
		return isDelta;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.AbstractEMFDelta#createChildren()
	 */
	@Override
	protected void createChildren() {
		// diff models contain a "parentless" diff group as root. Ignore it and switch to its children.
		if (diffModel.getOwnedElements().size() == 1) {
			final DiffGroup group = (DiffGroup)diffModel.getOwnedElements().get(0);
			for (DiffElement childDiff : group.getSubDiffElements()) {
				createChildDelta(childDiff);
			}
		}
	}
}
