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
package org.eclipse.emf.compare.ui.viewer.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.internal.CompareEditorInputNavigator;
import org.eclipse.compare.structuremergeviewer.DiffElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility.UIDifferenceGroup;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;

/**
 * This class aims to provide services about querying elements visible in the current
 * {@link ModelStructureMergeViewer}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public final class ModelStructureMergeViewerService {

	/**
	 * Constructor.
	 */
	private ModelStructureMergeViewerService() {
		// Do nothing
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare editor.
	 * 
	 * @param compareEditor
	 *            The EMF Compare editor.
	 * @return all visible {@link DiffElement}s from the given compare editor.
	 */
	public static List<Diff> getVisibleDiffElements(CompareEditor compareEditor) {
		final IEditorInput editorInput = compareEditor.getEditorInput();
		if (editorInput instanceof CompareEditorInput) {
			final CompareEditorInput compareEditorInput = (CompareEditorInput)editorInput;
			final CompareConfiguration compareConfiguration = compareEditorInput.getCompareConfiguration();
			final ICompareNavigator compareNavigator = compareEditorInput.getNavigator();
			if (compareNavigator instanceof CompareEditorInputNavigator) {
				return getVisibleDiffElements(compareConfiguration, compareNavigator);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare configuration and compare navigator.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @param compareNavigator
	 *            The compare navigator.
	 * @return all visible {@link DiffElement}s from the given compare configuration and compare navigator.
	 */
	public static List<Diff> getVisibleDiffElements(final CompareConfiguration compareConfiguration,
			final ICompareNavigator compareNavigator) {
		final List<Diff> ret = new ArrayList<Diff>();
		final CompareEditorInputNavigator compareEditorInputNavigator = (CompareEditorInputNavigator)compareNavigator;
		final Object[] panes = compareEditorInputNavigator.getPanes();
		Viewer viewer = null;
		for (Object pane : panes) {
			if (pane instanceof CompareViewerSwitchingPane) {
				final CompareViewerSwitchingPane compareViewerSwitchingPane = (CompareViewerSwitchingPane)pane;
				viewer = compareViewerSwitchingPane.getViewer();
				if (viewer instanceof ModelStructureMergeViewer) {
					final ITreeContentProvider contentProvider = (ITreeContentProvider)((ModelStructureMergeViewer)viewer)
							.getContentProvider();
					final Object[] elements = contentProvider.getElements(viewer.getInput());
					for (Object object : elements) {
						if (!(object instanceof Diff)) {
							ret.addAll(allChildren(object, contentProvider));
						} else if (object instanceof Diff) {
							ret.add((Diff)object);
						}
					}
					return ret;
				}
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare in and compare configuration in the
	 * given viewer.
	 * 
	 * @param input
	 *            The compare input of the editor.
	 * @param parent
	 *            The parent composite.
	 * @param configuration
	 *            The compare configuration.
	 * @return Returns all visible {@link DiffElement}s from the given compare in and compare configuration in
	 *         the given viewer
	 * @deprecated
	 */
	@Deprecated
	public static List<Diff> getvisibleDiffElements(ICompareInput input, Composite parent,
			CompareConfiguration configuration) {
		final List<Diff> ret = new ArrayList<Diff>();

		final ContentViewer contentViewer = (ContentViewer)CompareUI.findStructureViewer(null, input, parent,
				configuration);
		final ITreeContentProvider contentProvider = (ITreeContentProvider)contentViewer.getContentProvider();

		final Object[] elements = contentProvider.getElements(contentViewer.getInput());
		for (Object object : elements) {
			if (object instanceof Diff) {
				ret.add((Diff)object);
			}
			ret.addAll(allChildren(object, contentProvider));
		}
		return ret;
	}

	/**
	 * Returns all children from given root element using the given {@link ITreeContentProvider}.
	 * 
	 * @param element
	 *            The root element.
	 * @param contentProvider
	 *            The content provider.
	 * @return all children from given root element using the given {@link ITreeContentProvider}.
	 */
	private static List<Diff> allChildren(Object element, ITreeContentProvider contentProvider) {
		final List<Diff> ret = new ArrayList<Diff>();
		final Object[] children = contentProvider.getChildren(element);
		if (children != null) {
			for (Object object : children) {
				if (!(object instanceof Diff)) {
					ret.addAll(allChildren(object, contentProvider));
				} else if (object instanceof Diff) {
					ret.add((Diff)object);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare editor grouped by selected groups.
	 * 
	 * @param compareEditor
	 *            The Compare Editor.
	 * @return all visible {@link DiffElement}s from the given compare editor grouped by selected groups.
	 */
	public static Map<UIDifferenceGroup, List<Diff>> getGroupedDiffElements(CompareEditor compareEditor) {
		final IEditorInput editorInput = compareEditor.getEditorInput();
		if (editorInput instanceof CompareEditorInput) {
			final CompareEditorInput compareEditorInput = (CompareEditorInput)editorInput;
			final CompareConfiguration compareConfiguration = compareEditorInput.getCompareConfiguration();
			final ICompareNavigator compareNavigator = compareEditorInput.getNavigator();
			if (compareNavigator instanceof CompareEditorInputNavigator) {
				return getGroupedDiffElements(compareConfiguration, compareNavigator);
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare configuration and compare navigator
	 * grouped by selected groups.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @param compareNavigator
	 *            The compare navigator.
	 * @return all visible {@link DiffElement}s from the given compare configuration and compare navigator
	 *         grouped by selected groups.
	 */
	public static Map<UIDifferenceGroup, List<Diff>> getGroupedDiffElements(
			final CompareConfiguration compareConfiguration, final ICompareNavigator compareNavigator) {
		final Map<UIDifferenceGroup, List<Diff>> ret = new HashMap<UIDifferenceGroup, List<Diff>>();
		final CompareEditorInputNavigator compareEditorInputNavigator = (CompareEditorInputNavigator)compareNavigator;
		final Object[] panes = compareEditorInputNavigator.getPanes();
		for (Object pane : panes) {
			if (pane instanceof CompareViewerSwitchingPane) {
				final CompareViewerSwitchingPane compareViewerSwitchingPane = (CompareViewerSwitchingPane)pane;
				final Viewer viewer = compareViewerSwitchingPane.getViewer();
				if (viewer instanceof ModelStructureMergeViewer) {
					final ITreeContentProvider contentProvider = (ITreeContentProvider)((ModelStructureMergeViewer)viewer)
							.getContentProvider();
					final Object[] elements = contentProvider.getElements(viewer.getInput());
					for (Object object : elements) {
						if (object instanceof UIDifferenceGroup) {
							final UIDifferenceGroup diffGroup = (UIDifferenceGroup)object;
							ret.put(diffGroup, allChildren(object, contentProvider));
						}
					}
					return ret;
				}
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * Returns all visible elements from the given compare in and compare configuration in the given viewer
	 * grouped by selected groups.
	 * 
	 * @param input
	 *            The compare input of the editor.
	 * @param parent
	 *            The parent composite.
	 * @param configuration
	 *            The compare configuration.
	 * @return all visible elements from the given compare in and compare configuration in the given viewer
	 *         grouped by selected groups.
	 * @deprecated
	 */
	@Deprecated
	public static Map<UIDifferenceGroup, List<Diff>> getGroupedDiffElements(ICompareInput input,
			Composite parent, CompareConfiguration configuration) {
		final Map<UIDifferenceGroup, List<Diff>> ret = new HashMap<UIDifferenceGroup, List<Diff>>();

		final ContentViewer contentViewer = (ContentViewer)CompareUI.findStructureViewer(null, input, parent,
				configuration);
		final ITreeContentProvider contentProvider = (ITreeContentProvider)contentViewer.getContentProvider();

		final Object[] elements = contentProvider.getElements(contentViewer.getInput());
		for (Object object : elements) {
			if (object instanceof UIDifferenceGroup) {
				final UIDifferenceGroup diffGroup = (UIDifferenceGroup)object;
				ret.put(diffGroup, allChildren(object, contentProvider));
			}

		}

		return ret;
	}

}
