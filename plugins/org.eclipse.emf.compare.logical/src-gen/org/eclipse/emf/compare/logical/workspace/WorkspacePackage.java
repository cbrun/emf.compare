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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to
 * represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.compare.logical.workspace.WorkspaceFactory
 * @model kind="package"
 * @generated
 */
public interface WorkspacePackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "workspace";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/compare/workspace";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "workspace";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	WorkspacePackage eINSTANCE = org.eclipse.emf.compare.logical.workspace.impl.WorkspacePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.logical.workspace.impl.ModelImpl
	 * <em>Model</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.logical.workspace.impl.ModelImpl
	 * @see org.eclipse.emf.compare.logical.workspace.impl.WorkspacePackageImpl#getModel()
	 * @generated
	 */
	int MODEL = 0;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL__DEPENDENCIES = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.compare.logical.workspace.impl.DependencyImpl
	 * <em>Dependency</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.compare.logical.workspace.impl.DependencyImpl
	 * @see org.eclipse.emf.compare.logical.workspace.impl.WorkspacePackageImpl#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 1;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__SOURCE = 1;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.logical.workspace.Model
	 * <em>Model</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.emf.compare.logical.workspace.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.compare.logical.workspace.Model#getDependencies <em>Dependencies</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Dependencies</em>'.
	 * @see org.eclipse.emf.compare.logical.workspace.Model#getDependencies()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Dependencies();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.compare.logical.workspace.Dependency
	 * <em>Dependency</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see org.eclipse.emf.compare.logical.workspace.Dependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.compare.logical.workspace.Dependency#getTarget <em>Target</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.emf.compare.logical.workspace.Dependency#getTarget()
	 * @see #getDependency()
	 * @generated
	 */
	EReference getDependency_Target();

	/**
	 * Returns the meta object for the container reference '
	 * {@link org.eclipse.emf.compare.logical.workspace.Dependency#getSource <em>Source</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Source</em>'.
	 * @see org.eclipse.emf.compare.logical.workspace.Dependency#getSource()
	 * @see #getDependency()
	 * @generated
	 */
	EReference getDependency_Source();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	WorkspaceFactory getWorkspaceFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.compare.logical.workspace.impl.ModelImpl
		 * <em>Model</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.logical.workspace.impl.ModelImpl
		 * @see org.eclipse.emf.compare.logical.workspace.impl.WorkspacePackageImpl#getModel()
		 * @generated
		 */
		EClass MODEL = eINSTANCE.getModel();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' containment reference list feature.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MODEL__DEPENDENCIES = eINSTANCE.getModel_Dependencies();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.compare.logical.workspace.impl.DependencyImpl <em>Dependency</em>}' class.
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.compare.logical.workspace.impl.DependencyImpl
		 * @see org.eclipse.emf.compare.logical.workspace.impl.WorkspacePackageImpl#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature. <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DEPENDENCY__TARGET = eINSTANCE.getDependency_Target();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' container reference feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DEPENDENCY__SOURCE = eINSTANCE.getDependency_Source();

	}

} // WorkspacePackage
