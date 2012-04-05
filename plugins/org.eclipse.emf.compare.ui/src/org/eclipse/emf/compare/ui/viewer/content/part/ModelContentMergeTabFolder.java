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
package org.eclipse.emf.compare.ui.viewer.content.part;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.structuremergeviewer.DiffElement;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.ICompareEditorPartListener;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.util.EMFCompareEObjectUtils;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.diff.ModelContentMergeDiffTab;
import org.eclipse.emf.compare.ui.viewer.content.part.property.ModelContentMergePropertyTab;
import org.eclipse.emf.compare.ui.viewer.menus.ContextualMenuDescriptor;
import org.eclipse.emf.compare.ui.viewer.menus.ContextualMenuRegistry;
import org.eclipse.emf.compare.ui.viewer.menus.IContextualMenu;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Describes a part of a {@link ModelContentMergeViewer}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelContentMergeTabFolder {
	/** This keeps track of the parent viewer of this tab folder. */
	protected final ModelContentMergeViewer parentViewer;

	/**
	 * This <code>int</code> represents the side of this viewer part. Must be one of
	 * <ul>
	 * <li>{@link EMFCompareConstants#RIGHT}</li>
	 * <li>{@link EMFCompareConstants#LEFT}</li>
	 * <li>{@link EMFCompareConstants#ANCESTOR}</li>
	 * </ul>
	 */
	protected final int partSide;

	/** This is the content of the properties tab for this viewer part. */
	protected IModelContentMergeViewerTab properties;

	/** This is the view displayed by this viewer part. */
	protected CTabFolder tabFolder;

	/** Keeps references to the tabs contained within this folder. */
	protected final List<IModelContentMergeViewerTab> tabs = new ArrayList<IModelContentMergeViewerTab>();

	/** This is the content of the tree tab for this viewer part. */
	protected IModelContentMergeViewerTab tree;

	/** This contains all the listeners registered for this viewer part. */
	private final List<ICompareEditorPartListener> editorPartListeners = new ArrayList<ICompareEditorPartListener>();

	/**
	 * Instantiates a {@link ModelContentMergeTabFolder} given its parent {@link Composite} and its side.
	 * 
	 * @param viewer
	 *            Parent viewer of this viewer part.
	 * @param composite
	 *            Parent {@link Composite} for this part.
	 * @param side
	 *            Comparison side of this part. Must be one of {@link EMFCompareConstants#LEFT
	 *            EMFCompareConstants.RIGHT}, {@link EMFCompareConstants#RIGHT EMFCompareConstants.LEFT} or
	 *            {@link EMFCompareConstants#ANCESTOR EMFCompareConstants.ANCESTOR}.
	 */
	public ModelContentMergeTabFolder(ModelContentMergeViewer viewer, Composite composite, int side) {
		if (side != EMFCompareConstants.RIGHT && side != EMFCompareConstants.LEFT
				&& side != EMFCompareConstants.ANCESTOR) {
			throw new IllegalArgumentException(EMFCompareUIMessages.getString("IllegalSide", side)); //$NON-NLS-1$
		}

		parentViewer = viewer;
		partSide = side;
		createContents(composite);
	}

	/**
	 * Registers the given listener for notification. If the identical listener is already registered the
	 * method has no effect.
	 * 
	 * @param listener
	 *            The listener to register for changes of this input.
	 */
	public void addCompareEditorPartListener(ICompareEditorPartListener listener) {
		editorPartListeners.add(listener);
	}

	/**
	 * Disposes of all resources used by this folder.
	 */
	public void dispose() {
		properties.dispose();
		tree.dispose();
		tabs.clear();
		tabFolder.dispose();
		editorPartListeners.clear();
	}

	/**
	 * Returns a list of all diffs contained by the input DiffModel except for DiffGroups.
	 * 
	 * @return List of the DiffModel's differences.
	 */
	public List<Diff> getDiffAsList() {
		if (parentViewer.getInput() instanceof ModelCompareInput) {
			return ((ModelCompareInput)parentViewer.getInput()).getComparisonSnapshot().getDifferences();
		}
		return new ArrayList<Diff>();
	}

	/**
	 * Returns the properties tab of this tab folder.
	 * 
	 * @return The properties tab of this tab folder.
	 */
	public IModelContentMergeViewerTab getPropertyPart() {
		return properties;
	}

	/**
	 * Returns the tree tab of this tab folder.
	 * 
	 * @return The tree tab of this tab folder.
	 */
	public IModelContentMergeViewerTab getTreePart() {
		return tree;
	}

	/**
	 * This will be used when drawing the center part's marquees.
	 * 
	 * @param element
	 *            The DiffElement which we need UI variables for.
	 * @return The item corresponding to the given DiffElement, wrapped along with UI information.
	 */
	public ModelContentMergeTabItem getUIItem(Diff element) {
		final EObject data;
		if (partSide == EMFCompareConstants.ANCESTOR && element.getConflict() != null) {
			data = element.getMatch().getOrigin();
		} else if (partSide == EMFCompareConstants.LEFT) {
			data = EMFCompareEObjectUtils.getLeftElement(element);
		} else {
			data = EMFCompareEObjectUtils.getRightElement(element);
		}

		final EObject featureData;
		if (element instanceof AttributeChange) {
			featureData = ((AttributeChange)element).getAttribute();
		} else if (element instanceof ReferenceChange) {
			featureData = ((ReferenceChange)element).getReference();
		} else {
			featureData = null;
		}

		ModelContentMergeTabItem result = null;
		if (data != null) {
			result = tabs.get(tabFolder.getSelectionIndex()).getUIItem(data);
		}
		if (result == null && featureData != null) {
			result = tabs.get(tabFolder.getSelectionIndex()).getUIItem(featureData);
		}
		return result;
	}

	/**
	 * Returns the visible elements of the active tab.
	 * 
	 * @return The visible elements of the active tab.
	 */
	public List<ModelContentMergeTabItem> getVisibleElements() {
		return tabs.get(tabFolder.getSelectionIndex()).getVisibleElements();
	}

	/**
	 * Redraws this viewer part.
	 */
	public void layout() {
		tabs.get(tabFolder.getSelectionIndex()).redraw();
	}

	/**
	 * Shows the given item on the tree tab or its properties on the property tab.
	 * 
	 * @param diff
	 *            Item to scroll to.
	 */
	public void navigateToDiff(Diff diff) {
		final List<Diff> diffs = new ArrayList<Diff>();
		diffs.add(diff);
		navigateToDiff(diffs);
	}

	/**
	 * Ensures the first item of the given list of {@link DiffElement}s is visible, and sets the selection of
	 * the tree to all those items.
	 * 
	 * @param diffs
	 *            Items to select.
	 */
	public void navigateToDiff(List<Diff> diffs) {
		EObject target = null;
		// finds the object which properties should be found and expands the tree if needed
		// if (partSide == EMFCompareConstants.LEFT) {
		// target = EMFCompareEObjectUtils.getLeftElement(diffs.get(0));
		// } else if (partSide == EMFCompareConstants.RIGHT) {
		// target = EMFCompareEObjectUtils.getRightElement(diffs.get(0));
		// } else {
		// target = EMFCompareEObjectUtils.getAncestorElement(findMatchFromElement(EMFCompareEObjectUtils
		// .getLeftElement(diffs.get(0))));
		// }

		// FIXME : navigate from trees to diff.

		// if (target != null) {
		// // provide input to properties before showing diffs (as properties may be the active tab).
		// properties.setReflectiveInput(findMatchFromElement(target));
		// }

		tabs.get(tabFolder.getSelectionIndex()).showItems(diffs);

		parentViewer.getConfiguration().setProperty(EMFCompareConstants.PROPERTY_CONTENT_SELECTION,
				diffs.get(0));
		parentViewer.updateCenter();
	}

	/**
	 * Removes the given listener from this folder's listeners list. This will have no effect if the listener
	 * is not registered against this folder.
	 * 
	 * @param listener
	 *            The listener to remove from this folder.
	 */
	public void removeCompareEditorPartListener(ICompareEditorPartListener listener) {
		editorPartListeners.remove(listener);
	}

	/**
	 * Sets the receiver's size and location to the rectangular area specified by the arguments.
	 * 
	 * @param x
	 *            Desired x coordinate of the part.
	 * @param y
	 *            Desired y coordinate of the part.
	 * @param width
	 *            Desired width of the part.
	 * @param height
	 *            Desired height of the part.
	 */
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}

	/**
	 * Sets the receiver's size and location to given rectangular area.
	 * 
	 * @param bounds
	 *            Desired bounds for this receiver.
	 */
	public void setBounds(Rectangle bounds) {
		tabFolder.setBounds(bounds);
		resizeBounds();
	}

	/**
	 * Sets the input of this viewer part.
	 * 
	 * @param input
	 *            New input of this viewer part.
	 */
	public void setInput(Object input) {
		final IModelContentMergeViewerTab currentTab = tabs.get(tabFolder.getSelectionIndex());
		if (currentTab == properties && input instanceof EObject) {
			currentTab.setReflectiveInput(findMatchFromElement((EObject)input));
		} else {
			tabs.get(tabFolder.getSelectionIndex()).setReflectiveInput(input);
		}
	}

	/**
	 * Changes the current tab.
	 * 
	 * @param index
	 *            New tab to set selected.
	 */
	public void setSelectedTab(int index) {
		tabFolder.setSelection(index);
		resizeBounds();
	}

	/**
	 * Creates the contents of this viewer part given its parent composite.
	 * 
	 * @param composite
	 *            Parent composite of this viewer parts's widgets.
	 */
	protected void createContents(Composite composite) {
		tabFolder = createTabFolder(composite);
		final CTabItem treeTab = new CTabItem(tabFolder, SWT.NONE);
		treeTab.setText(EMFCompareUIMessages.getString("ModelContentMergeViewerTabFolder.tab1.name")); //$NON-NLS-1$

		final CTabItem propertiesTab = new CTabItem(tabFolder, SWT.NONE);
		propertiesTab.setText(EMFCompareUIMessages.getString("ModelContentMergeViewerTabFolder.tab2.name")); //$NON-NLS-1$

		final Composite treePanel = new Composite(tabFolder, SWT.NONE);
		treePanel.setLayout(new GridLayout());
		treePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		treePanel.setFont(composite.getFont());
		tree = createTreePart(treePanel);
		treeTab.setControl(treePanel);

		final Composite propertyPanel = new Composite(tabFolder, SWT.NONE);
		propertyPanel.setLayout(new GridLayout());
		propertyPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		propertyPanel.setFont(composite.getFont());
		properties = createPropertiesPart(propertyPanel);
		propertiesTab.setControl(propertyPanel);

		tabs.add(tree);
		tabs.add(properties);

		tabFolder.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				setSelectedTab(tabFolder.getSelectionIndex());
				fireSelectedtabChanged();
			}
		});
		tabFolder.setSelection(treeTab);

		createContextualMenus();
	}

	/**
	 * For each tabular viewer, it looks for available matching contextual menus to create them.
	 */
	private void createContextualMenus() {
		final Iterator<IModelContentMergeViewerTab> itTabs = tabs.iterator();
		while (itTabs.hasNext()) {
			final IModelContentMergeViewerTab tab = itTabs.next();
			createContextualMenus(tab);
		}
	}

	/**
	 * It looks for available matching contextual menus to create on the specified tab.
	 * 
	 * @param tab
	 *            The tabular viewer.
	 */
	private void createContextualMenus(final IModelContentMergeViewerTab tab) {
		final Iterator<IContextualMenu> menus = getAvailableContextualMenus(tab).iterator();
		while (menus.hasNext()) {
			final IContextualMenu menu = menus.next();
			createContextualMenu(tab, menu);
		}
	}

	/**
	 * It builds the menu on the specified tab, from the specified contextual menu builder.
	 * 
	 * @param tab
	 *            The tabular viewer.
	 * @param menu
	 *            The contextual menu builder.
	 * @since 1.3
	 */
	protected void createContextualMenu(final IModelContentMergeViewerTab tab, final IContextualMenu menu) {
		menu.create(parentViewer.getConfiguration(), (Viewer)tab, tab.getControl());
	}

	/**
	 * Get the available contextual menu builders defined on the specified tab, from the extension point:
	 * org.eclispe.emf.compare.ui.contextual.menus.
	 * 
	 * @param tab
	 *            The tabular viewer.
	 * @return The list of the contextual menu builders.
	 */
	private List<IContextualMenu> getAvailableContextualMenus(IModelContentMergeViewerTab tab) {
		final List<IContextualMenu> menus = new ArrayList<IContextualMenu>();
		final Iterator<ContextualMenuDescriptor> descriptors = ContextualMenuRegistry.INSTANCE
				.getDescriptors().iterator();
		while (descriptors.hasNext()) {
			final ContextualMenuDescriptor desc = descriptors.next();
			if (desc.getTargetClass().isInstance(tab)) {
				final IContextualMenu menu = desc.getExtension();
				menus.add(menu);
			}
		}
		return menus;
	}

	/**
	 * Creates the tab folder composite itself. Clients that wish to add their own tabs before the two default
	 * ones can override this and fo their work after the call to super.
	 * 
	 * @param parent
	 *            The parent Composite for the tab folder.
	 * @return The {@link CTabFolder} Composite that is to be used for the EMF Compare UI.
	 * @since 1.3
	 */
	protected CTabFolder createTabFolder(Composite parent) {
		return new CTabFolder(parent, SWT.BOTTOM);
	}

	/**
	 * Returns the {@link Match2Elements} containing the given {@link EObject} as its left or right element.
	 * 
	 * @param element
	 *            Element we seek the {@link Match2Elements} for.
	 * @return The {@link Match2Elements} containing the given {@link EObject} as its left or right element.
	 */
	protected EObject findMatchFromElement(EObject element) {
		EObject theElement = null;
		final Comparison match = ((ModelCompareInput)parentViewer.getInput()).getComparisonSnapshot();

		if (element instanceof Match) {
			return element;
		} else if (element instanceof Diff) {
			return ((Diff)element).getMatch();
		}

		return theElement;
	}

	/**
	 * Notifies All {@link ICompareEditorPartListener listeners} registered for this viewer part that the tab
	 * selection has been changed.
	 */
	protected void fireSelectedtabChanged() {
		for (final ICompareEditorPartListener listener : editorPartListeners) {
			listener.selectedTabChanged(tabFolder.getSelectionIndex());
		}
	}

	/**
	 * Notifies All {@link ICompareEditorPartListener listeners} registered for this viewer part that the user
	 * selection has changed on the properties or tree tab.
	 * 
	 * @param event
	 *            Source {@link SelectionChangedEvent Selection changed event} of the notification.
	 */
	protected void fireSelectionChanged(SelectionChangedEvent event) {
		for (final ICompareEditorPartListener listener : editorPartListeners) {
			listener.selectionChanged(event);
		}
	}

	/**
	 * Notifies All {@link ICompareEditorPartListener listeners} registered for this viewer part that the
	 * center part needs to be refreshed.
	 */
	protected void fireUpdateCenter() {
		for (final ICompareEditorPartListener listener : editorPartListeners) {
			listener.updateCenter();
		}
	}

	/**
	 * This will resize the tabs displayed by this content merge viewer.
	 */
	protected void resizeBounds() {
		tabs.get(tabFolder.getSelectionIndex()).getControl().setBounds(tabFolder.getClientArea());
	}

	/**
	 * Actual creation of the properties tab. Synchronization of the scroll bars and selections will be
	 * handled through {@link #createPropertiesPart(Composite)}.
	 * 
	 * @param parent
	 *            Parent {@link Composite} of the viewer tab that is to be created.
	 * @return The newly created {@link IModelContentMergeViewerTab} that'll be added to the current tab
	 *         folder.
	 * @since 1.1
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 */
	protected IModelContentMergeViewerTab createModelContentMergeViewerTab(Composite parent) {
		return new ModelContentMergePropertyTab(parent, partSide, this);
	}

	/**
	 * Handles the creation of the properties tab of this viewer part given the parent {@link Composite} under
	 * which to create it.
	 * 
	 * @param composite
	 *            Parent {@link Composite} of the table to create.
	 * @return The properties part displayed by this viewer part's properties tab.
	 */
	private IModelContentMergeViewerTab createPropertiesPart(Composite composite) {
		final IModelContentMergeViewerTab propertiesPart = createModelContentMergeViewerTab(composite);

		((Scrollable)propertiesPart.getControl()).getVerticalBar().addSelectionListener(
				new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}

					public void widgetSelected(SelectionEvent e) {
						parentViewer.updateCenter();
					}
				});

		propertiesPart.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				fireSelectionChanged(event);
			}
		});

		return propertiesPart;
	}

	/**
	 * Actual creation of the tree tab. Synchronization of the scroll bars and selections will be handled
	 * through {@link #createTreePart(Composite)}.
	 * 
	 * @param parent
	 *            Parent {@link Composite} of the viewer tab that is to be created.
	 * @return The newly created {@link IModelContentMergeViewerTab} that'll be added to the current tab
	 *         folder.
	 * @since 1.1
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 */
	protected IModelContentMergeViewerTab createModelContentMergeDiffTab(Composite parent) {
		return new ModelContentMergeDiffTab(parent, partSide, this);
	}

	/**
	 * Handles the creation of the tree tab of this viewer part given the parent {@link Composite} under which
	 * to create it.
	 * 
	 * @param composite
	 *            Parent {@link Composite} of the tree to create.
	 * @return The tree part displayed by this viewer part's tree tab.
	 */
	private IModelContentMergeViewerTab createTreePart(Composite composite) {
		final IModelContentMergeViewerTab treePart = createModelContentMergeDiffTab(composite);

		((Scrollable)treePart.getControl()).getVerticalBar().addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				fireUpdateCenter();
			}
		});

		((Tree)treePart.getControl()).addTreeListener(new TreeListener() {
			public void treeCollapsed(TreeEvent e) {
				((TreeItem)e.item).setExpanded(false);
				e.doit = false;
				parentViewer.update();
			}

			public void treeExpanded(TreeEvent e) {
				((TreeItem)e.item).setExpanded(true);
				e.doit = false;
				parentViewer.update();
			}
		});

		treePart.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				fireSelectionChanged(event);
			}
		});

		((Tree)treePart.getControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelectedElements().size() > 0) {
					final Item selected = tree.getSelectedElements().get(0);
					for (final Diff diff : ((ModelCompareInput)parentViewer.getInput())
							.getComparisonSnapshot().getDifferences()) {
						if (partSide == EMFCompareConstants.LEFT) {
							if (selected.getData().equals(EMFCompareEObjectUtils.getLeftElement(diff))) {
								parentViewer.setSelection(diff);
							}
						} else if (partSide == EMFCompareConstants.RIGHT) {
							if (selected.getData().equals(EMFCompareEObjectUtils.getRightElement(diff))) {
								parentViewer.setSelection(diff);
							}
						}
					}
					if (!selected.isDisposed() && selected.getData() instanceof EObject) {
						properties.setReflectiveInput(findMatchFromElement((EObject)selected.getData()));
					}
				}
			}
		});

		return treePart;
	}
}
