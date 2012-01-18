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
package org.eclipse.emf.compare.logical.extension;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This implementation of a resource visitor will allow us to browse all models in a given hierarchy.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelResourceVisitor implements IResourceVisitor {
	/** Content types of the files to consider as potential parents. */
	private static final String[] MODEL_CONTENT_TYPES = new String[] {
			"org.eclipse.emf.compare.ui.contenttype.ModelContentType", "org.eclipse.emf.ecore", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.emf.ecore.xmi",}; //$NON-NLS-1$

	/** Resource Set in which we should load the temporary resources. */
	private final ResourceSet set;

	/**
	 * Instantiates a resource visitor given the ResourceSet in which to load the temporary resources.
	 * 
	 * @param resourceSet
	 *            ResourceSet in which to load the temporary resources.
	 */
	public ModelResourceVisitor(ResourceSet resourceSet) {
		this.set = resourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
	 */
	public boolean visit(IResource resource) throws CoreException {
		if (resource instanceof IFile) {
			final IFile file = (IFile)resource;
			boolean isModel = false;
			for (String contentType : MODEL_CONTENT_TYPES) {
				if (hasContentType(file, contentType)) {
					isModel = true;
				}
			}

			if (isModel) {
				try {
					EclipseModelUtils.getResource(file, set);
					return true;
				} catch (IOException e) {
					// will return false;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * This will return <code>true</code> if and only if the given IFile has the given <em>contentTypeId</em>
	 * configured (as returned by {@link IContentTypeManager#findContentTypesFor(InputStream, String)
	 * Platform.getContentTypeManager().findContentTypesFor(InputStream, String)}.
	 * 
	 * @param resource
	 *            The resource from which to test the content types.
	 * @param contentTypeId
	 *            Fully qualified identifier of the content type this <em>resource</em> has to feature.
	 * @return <code>true</code> if the given {@link IFile} has the given content type.
	 */
	private boolean hasContentType(IFile resource, String contentTypeId) {
		final IContentTypeManager ctManager = Platform.getContentTypeManager();
		final IContentType expected = ctManager.getContentType(contentTypeId);
		if (expected == null) {
			return false;
		}

		InputStream resourceContent = null;
		IContentType[] contentTypes = null;
		try {
			resourceContent = resource.getContents();
			contentTypes = ctManager.findContentTypesFor(resourceContent, resource.getName());
		} catch (CoreException e) {
			ctManager.findContentTypesFor(resource.getName());
		} catch (IOException e) {
			ctManager.findContentTypesFor(resource.getName());
		} finally {
			if (resourceContent != null) {
				try {
					resourceContent.close();
				} catch (IOException e) {
					// would have already been caught by the outer try, leave the stream open
				}
			}
		}

		boolean hasContentType = false;
		if (contentTypes != null) {
			for (int i = 0; i < contentTypes.length && !hasContentType; i++) {
				if (contentTypes[i].isKindOf(expected)) {
					hasContentType = true;
				}
			}
		}
		return hasContentType;
	}
}
