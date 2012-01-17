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
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;

/**
 * This implementation of a resource visitor will allow us to browse all models in a given hierarchy and do
 * something on them. The "do something" part has to be done in a specialization of the processModel method.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractModelResourceVisitor implements IResourceVisitor {

	/** Content types of the files to consider as potential parents. */
	private final Set<String> contentTypesToConsider;

	/** file Extensions of the files to consider as models. */
	private final Set<String> fileExtensionsToConsider;

	/**
	 * progress monitor to check cancel from the user.
	 */
	private IProgressMonitor monitor;

	/**
	 * Instantiates a resource visitor given the ResourceSet in which to load the temporary resources.
	 * 
	 * @param contentTypes
	 *            : the list of content-type id's to process.
	 * @param fileExtensions
	 *            : the list of file Extensions to process.
	 * @param pm
	 *            : a progress monitor to check cancellation of the end user.
	 */
	public AbstractModelResourceVisitor(Set<String> contentTypes, Set<String> fileExtensions,
			IProgressMonitor pm) {
		this.contentTypesToConsider = contentTypes;
		this.fileExtensionsToConsider = fileExtensions;
		this.monitor = pm;
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
			if (fileExtensionsToConsider.contains(file.getFileExtension())) {
				isModel = true;
			} else {
				for (String contentType : contentTypesToConsider) {
					if (hasContentType(file, contentType)) {
						isModel = true;
					}
				}
			}

			if (isModel) {
				processModel(file);
			}
			return false;
		}
		return !monitor.isCanceled();
	}

	/**
	 * Process the model.
	 * 
	 * @param file
	 *            a file from the workspace matching at least one of the given the content types.
	 */
	protected abstract void processModel(final IFile file);

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
	public static boolean hasContentType(IFile resource, String contentTypeId) {
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
