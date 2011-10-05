/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.logical.workspace;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.logical.workspace.Dependency#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.logical.workspace.Dependency#getSource <em>Source</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.logical.workspace.WorkspacePackage#getDependency()
 * @model
 * @generated
 */
public interface Dependency extends EObject {
	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Model)
	 * @see org.eclipse.emf.compare.logical.workspace.WorkspacePackage#getDependency_Target()
	 * @model required="true"
	 * @generated
	 */
	Model getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.logical.workspace.Dependency#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Model value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.compare.logical.workspace.Model#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' container reference.
	 * @see #setSource(Model)
	 * @see org.eclipse.emf.compare.logical.workspace.WorkspacePackage#getDependency_Source()
	 * @see org.eclipse.emf.compare.logical.workspace.Model#getDependencies
	 * @model opposite="dependencies" required="true" transient="false"
	 * @generated
	 */
	Model getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.logical.workspace.Dependency#getSource <em>Source</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' container reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Model value);

} // Dependency
