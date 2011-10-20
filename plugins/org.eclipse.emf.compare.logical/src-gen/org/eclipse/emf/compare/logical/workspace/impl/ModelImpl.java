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
package org.eclipse.emf.compare.logical.workspace.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspacePackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.logical.workspace.impl.ModelImpl#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ModelImpl extends EObjectImpl implements Model {
	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> dependencies;

	/**
	 * The default value of the '{@link #isLoadable() <em>Loadable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOADABLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isLoadable() <em>Loadable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadable()
	 * @generated
	 * @ordered
	 */
	protected boolean loadable = LOADABLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WorkspacePackage.Literals.MODEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Dependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectContainmentWithInverseEList<Dependency>(Dependency.class, this, WorkspacePackage.MODEL__DEPENDENCIES, WorkspacePackage.DEPENDENCY__SOURCE);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLoadable() {
		return loadable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadable(boolean newLoadable) {
		boolean oldLoadable = loadable;
		loadable = newLoadable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WorkspacePackage.MODEL__LOADABLE, oldLoadable, loadable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDependencies()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				return ((InternalEList<?>)getDependencies()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				return getDependencies();
			case WorkspacePackage.MODEL__LOADABLE:
				return isLoadable();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection<? extends Dependency>)newValue);
				return;
			case WorkspacePackage.MODEL__LOADABLE:
				setLoadable((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				getDependencies().clear();
				return;
			case WorkspacePackage.MODEL__LOADABLE:
				setLoadable(LOADABLE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WorkspacePackage.MODEL__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
			case WorkspacePackage.MODEL__LOADABLE:
				return loadable != LOADABLE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (loadable: ");
		result.append(loadable);
		result.append(')');
		return result.toString();
	}

} // ModelImpl
