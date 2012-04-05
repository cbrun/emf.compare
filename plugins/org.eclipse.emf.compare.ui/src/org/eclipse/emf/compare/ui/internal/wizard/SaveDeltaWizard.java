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
package org.eclipse.emf.compare.ui.internal.wizard;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

/**
 * Wizard used by the "save" action of the
 * {@link org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class SaveDeltaWizard extends BasicNewFileResourceWizard {
	/**
	 * File extension of the files this wizard creates. If no extension is specified for the instantiation, we
	 * initialize this to "emfdiff".
	 */
	private final String fileExtension;

	/** Result of the comparison this wizard is meant to save. */
	private Comparison input;

	/**
	 * Creates a new file wizard given the file extension to use.
	 * 
	 * @param extension
	 *            Extension of the file(s) to generate.
	 */
	public SaveDeltaWizard(String extension) {
		super();
		if (extension == null) {
			fileExtension = "emfdiff"; //$NON-NLS-1$
		} else {
			fileExtension = extension;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see BasicNewFileResourceWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		final WizardNewFileCreationPage wizardNewFileCreationPage = (WizardNewFileCreationPage)getPage("newFilePage1"); //$NON-NLS-1$
		wizardNewFileCreationPage.setFileName("result." + fileExtension); //$NON-NLS-1$
		wizardNewFileCreationPage.setAllowExistingResources(true);

	}

	/**
	 * initalizes the wizard.
	 * 
	 * @param workbench
	 *            Current workbench.
	 * @param inputSnapshot
	 *            The {@link ComparisonSnapshot} to save.
	 */
	public void init(IWorkbench workbench, Comparison inputSnapshot) {
		super.init(workbench, new StructuredSelection());
		// ensures no modification will be made to the input
		input = EcoreUtil.copy(inputSnapshot);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see BasicNewFileResourceWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		boolean result = false;
		final String page = "newFilePage1"; //$NON-NLS-1$

		final String fileName = ((WizardNewFileCreationPage)getPage(page)).getFileName();
		if (!fileName.endsWith(fileExtension)) {
			((WizardNewFileCreationPage)getPage(page)).setFileName(fileName + '.' + fileExtension);
		}

		final IFile createdFile = ((WizardNewFileCreationPage)getPage(page)).createNewFile();
		if (createdFile != null) {
			try {
				Resource res = new XMIResourceImpl(URI.createFileURI(createdFile.getLocation().toOSString()));
				res.getContents().add(input);
				res.save(Collections.EMPTY_MAP);

			} catch (final IOException e) {
				EMFCompareUIPlugin.log(e, false);
			}
			result = true;
		}
		return result;
	}
}
