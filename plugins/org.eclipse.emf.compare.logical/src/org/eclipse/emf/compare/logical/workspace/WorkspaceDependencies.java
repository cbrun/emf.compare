package org.eclipse.emf.compare.logical.workspace;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * @since 1.3
 */
public class WorkspaceDependencies {

	private static final Function<Resource, IFile> ERESOURCE_TO_IFILE = new Function<Resource, IFile>() {

		public IFile apply(Resource from) {
			URI eUri = from.getURI();
			assert eUri.isPlatformResource() == true;
			String platformString = eUri.toPlatformString(true);
			return (IFile)ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
		}
	};

	private ResourceSet set;

	private ECrossReferenceAdapter xRef;

	public WorkspaceDependencies(ResourceSet set) {
		this.set = set;
		this.xRef = new ECrossReferenceAdapter();
		this.set.eAdapters().add(xRef);
	}

	public Iterable<IFile> getInverseReferences(Iterable<IFile> roots) {
		Set<Resource> dependantEmfResources = Sets.newLinkedHashSet();
		for (IFile iFile : roots) {
			URI rootFileURI = URI.createPlatformResourceURI(iFile.getFullPath().toOSString(), true);

			Resource rootRes = set.getResource(rootFileURI, true);

			assert rootRes.getContents().get(0) instanceof Model;

			Model model = (Model)rootRes.getContents().get(0);
			for (EStructuralFeature.Setting inverseSetting : xRef.getInverseReferences(model, true)) {
				EObject inverseHost = inverseSetting.getEObject();
				if (inverseHost instanceof Dependency) {
					dependantEmfResources.add(inverseHost.eResource());
				}
			}
		}

		return Iterables.filter(Iterables.transform(dependantEmfResources, ERESOURCE_TO_IFILE),
				Predicates.notNull());
	}

	public Iterable<IFile> getDependencies(Set<IFile> roots) {
		Set<Resource> dependantEmfResources = Sets.newLinkedHashSet();
		for (IFile iFile : roots) {
			URI rootFileURI = URI.createPlatformResourceURI(iFile.getFullPath().toOSString(), true);

			Resource rootRes = set.getResource(rootFileURI, true);

			assert rootRes.getContents().get(0) instanceof Model;

			Model model = (Model)rootRes.getContents().get(0);
			for (Dependency dep : model.getDependencies()) {
				if (dep.getTarget().eResource() != null) {
					dependantEmfResources.add(dep.getTarget().eResource());
					// TODO find a way to show/discriminate non resolvable dependencies
				}
			}

		}
		return Iterables.filter(Iterables.transform(dependantEmfResources, ERESOURCE_TO_IFILE),
				Predicates.notNull());
	}

	public ResourceSet getResourceSet() {
		return set;
	}

}
