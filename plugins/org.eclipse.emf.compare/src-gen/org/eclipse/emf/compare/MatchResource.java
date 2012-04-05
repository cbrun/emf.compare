/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Match Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MatchResource element represents the mapping between two or three resources : left, right, and their optional common ancestor. The resource will be identified through its URI.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getLeftURI <em>Left URI</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getRightURI <em>Right URI</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getOriginURI <em>Origin URI</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.MatchResource#getOrigin <em>Origin</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource()
 * @model
 * @generated
 */
public interface MatchResource extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Left URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Cannot be null. Represents the URI of the left resource of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Left URI</em>' attribute.
	 * @see #setLeftURI(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_LeftURI()
	 * @model required="true"
	 * @generated
	 */
	String getLeftURI();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getLeftURI <em>Left URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left URI</em>' attribute.
	 * @see #getLeftURI()
	 * @generated
	 */
	void setLeftURI(String value);

	/**
	 * Returns the value of the '<em><b>Right URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Cannot be null. Represents the URI of the right resource of this mapping.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Right URI</em>' attribute.
	 * @see #setRightURI(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_RightURI()
	 * @model required="true"
	 * @generated
	 */
	String getRightURI();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getRightURI <em>Right URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right URI</em>' attribute.
	 * @see #getRightURI()
	 * @generated
	 */
	void setRightURI(String value);

	/**
	 * Returns the value of the '<em><b>Origin URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This can be null in the case of two-way comparisons. If assigned, it will represent the URI of the origin resource for this mapping; the common ancestor of both others.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Origin URI</em>' attribute.
	 * @see #setOriginURI(String)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_OriginURI()
	 * @model
	 * @generated
	 */
	String getOriginURI();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getOriginURI <em>Origin URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin URI</em>' attribute.
	 * @see #getOriginURI()
	 * @generated
	 */
	void setOriginURI(String value);

	/**
	 * Returns the value of the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left</em>' attribute.
	 * @see #setLeft(Resource)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_Left()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Resource getLeft();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getLeft <em>Left</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' attribute.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(Resource value);

	/**
	 * Returns the value of the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right</em>' attribute.
	 * @see #setRight(Resource)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_Right()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Resource getRight();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getRight <em>Right</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right</em>' attribute.
	 * @see #getRight()
	 * @generated
	 */
	void setRight(Resource value);

	/**
	 * Returns the value of the '<em><b>Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin</em>' attribute.
	 * @see #setOrigin(Resource)
	 * @see org.eclipse.emf.compare.ComparePackage#getMatchResource_Origin()
	 * @model transient="true"
	 * @generated
	 */
	Resource getOrigin();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.compare.MatchResource#getOrigin <em>Origin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin</em>' attribute.
	 * @see #getOrigin()
	 * @generated
	 */
	void setOrigin(Resource value);

} // MatchResource
