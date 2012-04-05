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
package org.eclipse.emf.compare.ui.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility;
import org.eclipse.emf.compare.ui.viewer.structure.StructureViewer;
import org.eclipse.emf.compare.ui.views.StructureView;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Services to handle EMF Compare UI.
 * 
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 * @since 1.3
 */
public final class CompareServices {
	/**
	 * Constructor.
	 */
	private CompareServices() {
		// Hides default constructor
	}

	/**
	 * Initialize selection of the difference elements linked to the given model object identifiers.
	 * 
	 * @param objectIds
	 *            The model object identifiers.
	 * @param editor
	 *            The Compare Editor
	 */
	public static void setSelection(List<String> objectIds, IEditorPart editor) {
		final ISelectionProvider provider = editor.getEditorSite().getSelectionProvider();
		if (provider instanceof IInputProvider) {
			final List<EObject> objsToReference = new ArrayList<EObject>();
			// FIXME CNO : adapt API
			// if (((IInputProvider)provider).getInput() instanceof ModelCompareInput) {
			// Comparison root = ((ModelCompareInput)root).getComparisonSnapshot();
			// for (String id : objectIds) {
			// final EObject leftRoot = getElement((EObject)root, MatchSide.LEFT);
			// EObject obj = getEObject(leftRoot.eResource(), id);
			// if (obj == null) {
			// final EObject rightRoot = getElement((EObject)root, MatchSide.RIGHT);
			// obj = getEObject(rightRoot.eResource(), id);
			// }
			// if (obj != null) {
			// objsToReference.add(obj);
			// }
			// }
			// if (objsToReference.size() > 0) {
			// final EcoreUtil.CrossReferencer crossReferencer = createCrossReferencer((EObject)root);
			// editor.getEditorSite().getSelectionProvider().setSelection(
			// new StructuredSelection(getDiffsToSelect(objsToReference, crossReferencer)));
			// }
			// }
		}
	}

	/**
	 * Open an EMF Compare editor from a {@link CompareEditorInput}. After opening, the difference elements
	 * which reference the model objects identified by {@link objectIds} are selected.
	 * 
	 * @see CompareServices#openEditor(CompareEditorInput, String)
	 * @param input
	 *            The input of the editor.
	 * @param objectIds
	 *            The list of model object identifiers.
	 * @throws InvocationTargetException
	 *             exception.
	 * @throws InterruptedException
	 *             exception.
	 * @throws PartInitException
	 *             exception.
	 */
	public static void openEditor(final CompareEditorInput input, List<String> objectIds)
			throws InvocationTargetException, InterruptedException, PartInitException {
		openEditor(input);
		initSelection(objectIds, input);
	}

	/**
	 * Open an EMF Compare editor from a {@link CompareEditorInput}. After opening, the difference elements
	 * which reference the model object identified by {@link objectId} is selected. The model object
	 * identifier is the uri fragment of this object. It can be obtained calling
	 * <code>eObject.eResource().getURIFragment(eObject);</code> on the eobject model object.
	 * 
	 * @param input
	 *            The input of the editor.
	 * @param objectId
	 *            The model object identifier.
	 * @throws InvocationTargetException
	 *             exception.
	 * @throws InterruptedException
	 *             exception.
	 * @throws PartInitException
	 *             exception.
	 */
	public static void openEditor(final CompareEditorInput input, String objectId)
			throws InvocationTargetException, InterruptedException, PartInitException {
		final List<String> ids = new ArrayList<String>();
		ids.add(objectId);
		openEditor(input, ids);
	}

	/**
	 * Open a view to display the differences from a difference snapshot input.
	 * 
	 * @param input
	 *            The input.
	 * @throws InterruptedException
	 *             The exception.
	 * @throws InvocationTargetException
	 *             The exception.
	 * @throws PartInitException
	 *             The exception.
	 */
	public static void openView(final Comparison input) throws InterruptedException,
			InvocationTargetException, PartInitException {
		final IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
				"org.eclipse.emf.compare.ui.views.StructureView"); //$NON-NLS-1$
		if (part instanceof StructureView) {
			((StructureView)part).setInput(input);
			((StructureView)part).setDifferenceFilters(null);
			((StructureView)part).setDifferenceGroupingFacility(null);
		}

	}

	/**
	 * Open a view to display the differences from a difference snapshot input.
	 * 
	 * @param input
	 *            The input.
	 * @param filters
	 *            The filters to apply
	 * @param group
	 *            The ordering to apply.
	 * @throws InterruptedException
	 *             The exception.
	 * @throws InvocationTargetException
	 *             The exception.
	 * @throws PartInitException
	 *             The exception.
	 */
	public static void openView(final Comparison input, final List<IDifferenceFilter> filters,
			final IDifferenceGroupingFacility group) throws InterruptedException, InvocationTargetException,
			PartInitException {
		final IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
				"org.eclipse.emf.compare.ui.views.StructureView"); //$NON-NLS-1$
		if (part instanceof StructureView) {
			((StructureView)part).setInput(input);
			((StructureView)part).setDifferenceFilters(filters);
			((StructureView)part).setDifferenceGroupingFacility(group);
		}

	}

	/**
	 * Open a view to display the differences from an opened EMF compare editor. Open a view with a difference
	 * snapshot input.
	 * 
	 * @param editor
	 *            The compare editor.
	 * @throws InterruptedException
	 *             exception.
	 * @throws InvocationTargetException
	 *             exception.
	 * @throws PartInitException
	 *             exception.
	 */
	public static void openView(IEditorPart editor) throws InterruptedException, InvocationTargetException,
			PartInitException {
		final ISelectionProvider provider = editor.getEditorSite().getSelectionProvider();
		if (provider instanceof IInputProvider) {
			final Object root = ((IInputProvider)provider).getInput();
			if (root instanceof Comparison) {
				openView((Comparison)root);
			} else if (root instanceof ModelCompareInput) {
				openView(((ModelCompareInput)root).getComparisonSnapshot());
			}
		}
	}

	/**
	 * Open a view to display the differences from an opened EMF compare editor. Open a view with a difference
	 * snapshot input.
	 * 
	 * @param editor
	 *            The compare editor.
	 * @param filters
	 *            The filters to apply
	 * @param group
	 *            The ordering to apply.
	 * @throws InterruptedException
	 *             exception.
	 * @throws InvocationTargetException
	 *             exception.
	 * @throws PartInitException
	 *             exception.
	 */
	public static void openView(IEditorPart editor, final List<IDifferenceFilter> filters,
			final IDifferenceGroupingFacility group) throws InterruptedException, InvocationTargetException,
			PartInitException {
		final ISelectionProvider provider = editor.getEditorSite().getSelectionProvider();
		if (provider instanceof IInputProvider) {
			final Object root = ((IInputProvider)provider).getInput();
			if (root instanceof Comparison) {
				openView((Comparison)root, filters, group);
			} else if (root instanceof ModelCompareInput) {
				openView(((ModelCompareInput)root).getComparisonSnapshot(), filters, group);
			}
		}
	}

	/**
	 * Get a {@link StructureViewer} from the parent composite and a result of a model comparison.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param input
	 *            The result of model comparison.
	 * @return The {@link StructureViewer}
	 */
	public static StructureViewer getStructureViewer(Composite parent, Comparison input) {
		final StructureViewer viewer = new StructureViewer(parent, new CompareConfiguration());
		viewer.setInput(input);
		return viewer;
	}

	/**
	 * Get a {@link StructureViewer} from the parent composite, a result of a model comparison and a
	 * configuration to filter and group the differences.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param input
	 *            The result of model comparison.
	 * @param filters
	 *            The filters to apply.
	 * @param groupingFacility
	 *            The grouping to apply.
	 * @return The {@link StructureViewer}
	 */
	public static StructureViewer getStructureViewer(Composite parent, Comparison input,
			List<IDifferenceFilter> filters, IDifferenceGroupingFacility groupingFacility) {
		final StructureViewer viewer = new StructureViewer(parent, new CompareConfiguration(), filters,
				groupingFacility);
		viewer.setInput(input);
		return viewer;
	}

	/**
	 * Open an EMF Compare editor from a {@link CompareEditorInput}.
	 * 
	 * @param input
	 *            The input of the editor.
	 * @throws InterruptedException
	 *             exception.
	 * @throws InvocationTargetException
	 *             exception.
	 */
	protected static void openEditor(final CompareEditorInput input) throws InterruptedException,
			InvocationTargetException {
		input.run(new NullProgressMonitor());

		CompareUI.openCompareEditor(input);
	}

	/**
	 * From the root element of a difference model, it returns the first found element with the specified
	 * type, in all children.
	 * 
	 * @param rootDiff
	 *            The start point of the scan.
	 * @param type
	 *            The expected type.
	 * @return The difference element or null if not found.
	 */
	private static EObject getDiffElement(EObject rootDiff, EClass type) {
		final Iterator<EObject> it = rootDiff.eAllContents();
		while (it.hasNext()) {
			final EObject obj = it.next();
			if (type.isInstance(obj)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * Get the list of the differences to select from the identified model object.
	 * 
	 * @param obj
	 *            The model object to focus.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The list of differences to select.
	 */
	private static List<Diff> getDiffsToSelect(EObject obj, EcoreUtil.CrossReferencer crossReferencer) {
		final List<Diff> result = new ArrayList<Diff>();
		final Collection<Setting> settings = crossReferencer.get(obj);
		if (settings != null) {
			for (Setting setting : settings) {
				final EObject crossElt = setting.getEObject();
				if (crossElt instanceof Diff) {
					result.add((Diff)crossElt);
				}
			}
		}
		return result;
	}

	/**
	 * Get the list of the differences to select from the list of identified model objects.
	 * 
	 * @param objs
	 *            The model objects to focus.
	 * @param crossReferencer
	 *            The cross referencer.
	 * @return The list of differences to select.
	 */
	private static List<Diff> getDiffsToSelect(List<EObject> objs, EcoreUtil.CrossReferencer crossReferencer) {
		final List<Diff> result = new ArrayList<Diff>();
		for (EObject eObject : objs) {
			final List<Diff> diffs = getDiffsToSelect(eObject, crossReferencer);
			for (Diff diffElement : diffs) {
				if (!result.contains(diffElement)) {
					result.add(diffElement);
				}
			}
		}
		return result;
	}

	/**
	 * Retrieve the model object located in the specified resource (or its resource set) and identified by its
	 * uri fragment (id).
	 * 
	 * @param resource
	 *            The resource to scan.
	 * @param id
	 *            The identifier of the model object to find.
	 * @return The model object or null if not found.
	 */
	private static EObject getEObject(Resource resource, String id) {
		EObject result = null;
		if (resource.getResourceSet() != null) {
			final Iterator<Resource> resources = resource.getResourceSet().getResources().iterator();
			while (resources.hasNext()) {
				final Resource res = resources.next();
				result = res.getEObject(id);
				if (result != null) {
					break;
				}
			}
		} else {
			result = resource.getEObject(id);
		}
		return result;
	}

	/**
	 * Initialize selection of the difference elements linked to the given model object identifiers.
	 * 
	 * @param objectIds
	 *            The model object identifiers.
	 * @param editorInput
	 *            The compare editor input.
	 * @throws PartInitException
	 *             exception.
	 */
	private static void initSelection(List<String> objectIds, CompareEditorInput editorInput)
			throws PartInitException {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			if (workbenchWindow != null) {
				final IWorkbenchPage page = workbenchWindow.getActivePage();
				if (page != null) {
					final IEditorReference[] editors = page.getEditorReferences();
					for (IEditorReference iEditorReference : editors) {
						if (editorInput.equals(iEditorReference.getEditorInput())) {
							final IEditorPart editor = iEditorReference.getEditor(true);
							setSelection(objectIds, editor);
						}
					}
				}
			}
		}
	}

}
