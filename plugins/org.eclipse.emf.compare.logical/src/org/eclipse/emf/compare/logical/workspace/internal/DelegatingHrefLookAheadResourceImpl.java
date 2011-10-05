package org.eclipse.emf.compare.logical.workspace.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspaceFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author cedric
 * @since 1.3
 */
public class DelegatingHrefLookAheadResourceImpl extends ResourceImpl {

	private static final Predicate<EObject> IS_PROXY = new Predicate<EObject>() {

		public boolean apply(EObject arg0) {
			return arg0.eIsProxy();
		}
	};

	private static final Predicate<EReference> TO_BE_CROSSREFERENCED = new Predicate<EReference>() {

		public boolean apply(EReference arg0) {
			return !arg0.isDerived();
		}

	};

	public DelegatingHrefLookAheadResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream is, Map<?, ?> options) throws IOException {

		Model root = WorkspaceFactory.eINSTANCE.createModel();
		getContents().add(root);

		try {
			XMIResourceImpl xmiRes = new XMIResourceImpl(uri);
			xmiRes.load(options);
			Set<String> foundDependencies = collectDependencies(xmiRes);
			for (String dependencyURI : foundDependencies) {
				Dependency newDep = WorkspaceFactory.eINSTANCE.createDependency();
				newDep.setTarget(forgeProxy(dependencyURI));
				root.getDependencies().add(newDep);
			}
		} catch (Throwable e) {
			// TODO add packages info
		}

	}

	private Set<String> collectDependencies(XMIResourceImpl xmiRes) throws UnsupportedEncodingException,
			IOException {
		Set<String> foundDependencies = Sets.newLinkedHashSet();
		Iterator<EObject> it = xmiRes.getAllContents();
		while (it.hasNext()) {
			EObject next = it.next();
			Iterable<InternalEObject> proxyReferences = Iterables.filter(
					Iterables.filter(allRefs(next), IS_PROXY), InternalEObject.class);
			for (InternalEObject eObject : proxyReferences) {
				foundDependencies.add(getResourceURI(eObject.eProxyURI()));
			}
		}
		return foundDependencies;
	}

	private String getResourceURI(URI uri) {
		if (uri.hasFragment()) {
			return uri.trimFragment().toString();
		}
		return uri.toString();
	}

	private Iterable<EObject> allRefs(EObject next) {
		List<EObject> referencedObjects = Lists.newArrayList();

		for (EReference ref : Iterables.filter(next.eClass().getEAllReferences(), TO_BE_CROSSREFERENCED)) {
			if (ref.isMany()) {
				Collection<EObject> referenced = (Collection<EObject>)next.eGet(ref, false);
				referencedObjects.addAll(referenced);
			} else {
				EObject referenced = (EObject)next.eGet(ref);
				if (referenced != null) {
					referencedObjects.add(referenced);
				}
			}
		}
		return referencedObjects;
	}

	private Model forgeProxy(String resourceURI) {

		URI uri = URI.createURI(resourceURI + "#/").resolve(getURI()); //$NON-NLS-1$

		Model targetedModel = WorkspaceFactory.eINSTANCE.createModel();
		((InternalEObject)targetedModel).eSetProxyURI(uri);
		return targetedModel;
	}
}
