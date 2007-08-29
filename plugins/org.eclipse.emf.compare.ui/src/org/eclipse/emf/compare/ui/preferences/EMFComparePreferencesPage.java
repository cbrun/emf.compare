/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.Messages;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page used for <b>EMFCompare</b>, it allows the user to define which files to compare with
 * <b>EMFCompare</b> and the colors to use for the differences' highlighting.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class EMFComparePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Adds our page to the default preferences dialog.
	 */
	public EMFComparePreferencesPage() {
		super(GRID);
		setPreferenceStore(EMFCompareUIPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.getString("EMFComparePreferencesPage.description")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {
		final ImageIntegerFieldEditor searchWindowEditor = new ImageIntegerFieldEditor(
				EMFCompareConstants.PREFERENCES_KEY_SEARCH_WINDOW,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_SEARCH_WINDOW, getFieldEditorParent());
		searchWindowEditor.getCLabelControl(getFieldEditorParent()).setToolTipText(
				Messages.getString("EMFComparePreferencesPage.searchWindowHelp")); //$NON-NLS-1$
		addField(searchWindowEditor);

		final Group colorGroup = new Group(getFieldEditorParent(), SWT.SHADOW_ETCHED_IN);
		colorGroup.setText(Messages.getString("EMFComparePreferencesPage.colorGroupTitle")); //$NON-NLS-1$
		addField(new ColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_HIGHLIGHT_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_HIGHLIGHT_COLOR, colorGroup));
		addField(new ColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_CHANGED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_CHANGED_COLOR, colorGroup));
		addField(new ColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_CONFLICTING_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_CONFLICTING_COLOR, colorGroup));
		addField(new ColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_ADDED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_ADDED_COLOR, colorGroup));
		addField(new ColorFieldEditor(EMFCompareConstants.PREFERENCES_KEY_REMOVED_COLOR,
				EMFCompareConstants.PREFERENCES_DESCRIPTION_REMOVED_COLOR, colorGroup));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFCompareConstants.PREFERENCES_KEY_SEARCH_WINDOW)) {
					EMFComparePlugin.getDefault().getPluginPreferences().setValue(
							EMFCompareConstants.PREFERENCES_KEY_SEARCH_WINDOW,
							getPreferenceStore().getInt(EMFCompareConstants.PREFERENCES_KEY_SEARCH_WINDOW));
				}
			}
		});
	}

	/**
	 * Creates an {@link IntegerFieldEditor} showing a {@link CLabel} with text and image instead of the
	 * default {@link Label}.
	 */
	private final class ImageIntegerFieldEditor extends IntegerFieldEditor {
		/** maximum number of characters this field accepts. */
		private static final int TEXT_LIMIT = 10;

		/** Label that will be used to display the image and text of this {@link FieldEditor}. */
		protected CLabel cLabel;

		/**
		 * Creates an integer field editor.
		 * 
		 * @param name
		 *            The name of the preference this field editor works on.
		 * @param labelText
		 *            The label text of the field editor.
		 * @param parent
		 *            The parent of the field editor's control.
		 */
		public ImageIntegerFieldEditor(String name, String labelText, Composite parent) {
			super(name, labelText, parent, TEXT_LIMIT);
		}

		/**
		 * Overrides {@link StringFieldEditor#doFillIntoGrid(Composite, int)} for our {@link CLabel} instead
		 * of the old {@link Label}.
		 * 
		 * @param parent
		 *            Parent {@link Composite} of the editor.
		 * @param numColumns
		 *            Number of widgets to display horizontaly.
		 * @see StringFieldEditor#doFillIntoGrid(Composite, int)
		 */
		@Override
		protected void doFillIntoGrid(Composite parent, int numColumns) {
			getLabelControl(parent);

			GridData gd = new GridData();
			gd.horizontalSpan = numColumns - 2;
			final GC gc = new GC(getTextControl(parent));
			try {
				final Point extent = gc.textExtent("W"); //$NON-NLS-1$
				gd.widthHint = TEXT_LIMIT * extent.x;
			} finally {
				gc.dispose();
			}
			getTextControl(parent).setLayoutData(gd);

			gd = new GridData();
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
			getCLabelControl(parent).setLayoutData(gd);
		}

		/**
		 * Allows us to show an image after the {@link Text} field.
		 * 
		 * @param parent
		 *            Parent control of the label.
		 * @return The {@link CLabel} to show.
		 */
		public CLabel getCLabelControl(Composite parent) {
			if (cLabel == null) {
				cLabel = new CLabel(parent, SWT.RIGHT);
				final Image icon = getHelpIcon();
				if (icon != null) {
					cLabel.setImage(icon);
				}
				cLabel.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent event) {
						cLabel = null;
					}
				});
			}
			return cLabel;
		}

		/**
		 * Creates and return the help icon to show in our label.
		 * 
		 * @return The help icon to show in our label.
		 */
		private Image getHelpIcon() {
			Image helpIcon = null;
			try {
				final ImageDescriptor descriptor = ImageDescriptor.createFromURL(FileLocator
						.toFileURL(Platform.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry(
								EMFCompareConstants.ICONS_PREFERENCES_HELP)));
				helpIcon = descriptor.createImage();
			} catch (IOException e) {
				// this try catch keeps the compiler happy, we should never be here
				assert false;
			}
			return helpIcon;
		}

		/**
		 * Overrides {@link StringFieldEditor#setEnabled(boolean, Composite)} to enable our {@link CLabel}
		 * instead of the old {@link Label}.
		 * 
		 * @param enabled
		 *            <code>True</code> if we should enable the edition of the {@link CLabel label},
		 *            <code>False</code> otherwise.
		 * @param parent
		 *            parent {@link Composite} of the {@link CLabel label}.
		 * @see StringFieldEditor#setEnabled(boolean, Composite)
		 */
		@Override
		public void setEnabled(boolean enabled, Composite parent) {
			getCLabelControl(parent).setEnabled(enabled);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#getNumberOfControls()
		 */
		@Override
		public int getNumberOfControls() {
			return 3;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.preference.StringFieldEditor#adjustForNumColumns(int)
		 */
		@Override
		protected void adjustForNumColumns(int numColumns) {
			final GridData gd = (GridData)getTextControl().getLayoutData();
			gd.horizontalSpan = numColumns - 2;
			// We only grab excess space if we have to
			// If another field editor has more columns then
			// we assume it is setting the width.
			gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
		}
	}
}
