/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.workspace;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.logical.workspace.Model#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.compare.logical.workspace.WorkspacePackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {
	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' containment reference list. The list contents
	 * are of type {@link org.eclipse.emf.compare.logical.workspace.Dependency}. It is bidirectional and its
	 * opposite is '{@link org.eclipse.emf.compare.logical.workspace.Dependency#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' containment reference list isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dependencies</em>' containment reference list.
	 * @see org.eclipse.emf.compare.logical.workspace.WorkspacePackage#getModel_Dependencies()
	 * @see org.eclipse.emf.compare.logical.workspace.Dependency#getSource
	 * @model opposite="source" containment="true"
	 * @generated
	 */
	EList<Dependency> getDependencies();

} // Model
