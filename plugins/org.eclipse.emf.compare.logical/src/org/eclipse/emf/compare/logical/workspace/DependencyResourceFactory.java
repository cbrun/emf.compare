package org.eclipse.emf.compare.logical.workspace;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.workspace.internal.XMIHrefLookAheadResourceImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

/**
 * @author cedric
 * @since 1.3
 */
public class DependencyResourceFactory extends ResourceFactoryImpl {

	@Override
	public Resource createResource(URI uri) {
		return new XMIHrefLookAheadResourceImpl(uri);
	}

}
