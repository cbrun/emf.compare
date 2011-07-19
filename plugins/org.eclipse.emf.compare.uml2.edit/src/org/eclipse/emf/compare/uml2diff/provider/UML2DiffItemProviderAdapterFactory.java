/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.compare.uml2diff.util.UML2DiffAdapterFactory;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UML2DiffItemProviderAdapterFactory extends UML2DiffAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UML2DiffItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLAssociationChangeLeftTargetItemProvider umlAssociationChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLAssociationChangeLeftTargetAdapter() {
		if (umlAssociationChangeLeftTargetItemProvider == null) {
			umlAssociationChangeLeftTargetItemProvider = new UMLAssociationChangeLeftTargetItemProvider(this);
		}

		return umlAssociationChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLAssociationChangeRightTargetItemProvider umlAssociationChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLAssociationChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLAssociationChangeRightTargetAdapter() {
		if (umlAssociationChangeRightTargetItemProvider == null) {
			umlAssociationChangeRightTargetItemProvider = new UMLAssociationChangeRightTargetItemProvider(this);
		}

		return umlAssociationChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLAssociationBranchChangeLeftTargetItemProvider umlAssociationBranchChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLAssociationBranchChangeLeftTargetAdapter() {
		if (umlAssociationBranchChangeLeftTargetItemProvider == null) {
			umlAssociationBranchChangeLeftTargetItemProvider = new UMLAssociationBranchChangeLeftTargetItemProvider(this);
		}

		return umlAssociationBranchChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLAssociationBranchChangeRightTargetItemProvider umlAssociationBranchChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLAssociationBranchChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLAssociationBranchChangeRightTargetAdapter() {
		if (umlAssociationBranchChangeRightTargetItemProvider == null) {
			umlAssociationBranchChangeRightTargetItemProvider = new UMLAssociationBranchChangeRightTargetItemProvider(this);
		}

		return umlAssociationBranchChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDependencyBranchChangeLeftTargetItemProvider umlDependencyBranchChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDependencyBranchChangeLeftTargetAdapter() {
		if (umlDependencyBranchChangeLeftTargetItemProvider == null) {
			umlDependencyBranchChangeLeftTargetItemProvider = new UMLDependencyBranchChangeLeftTargetItemProvider(this);
		}

		return umlDependencyBranchChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDependencyBranchChangeRightTargetItemProvider umlDependencyBranchChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDependencyBranchChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDependencyBranchChangeRightTargetAdapter() {
		if (umlDependencyBranchChangeRightTargetItemProvider == null) {
			umlDependencyBranchChangeRightTargetItemProvider = new UMLDependencyBranchChangeRightTargetItemProvider(this);
		}

		return umlDependencyBranchChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLGeneralizationSetChangeLeftTargetItemProvider umlGeneralizationSetChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLGeneralizationSetChangeLeftTargetAdapter() {
		if (umlGeneralizationSetChangeLeftTargetItemProvider == null) {
			umlGeneralizationSetChangeLeftTargetItemProvider = new UMLGeneralizationSetChangeLeftTargetItemProvider(this);
		}

		return umlGeneralizationSetChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLGeneralizationSetChangeRightTargetItemProvider umlGeneralizationSetChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLGeneralizationSetChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLGeneralizationSetChangeRightTargetAdapter() {
		if (umlGeneralizationSetChangeRightTargetItemProvider == null) {
			umlGeneralizationSetChangeRightTargetItemProvider = new UMLGeneralizationSetChangeRightTargetItemProvider(this);
		}

		return umlGeneralizationSetChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDependencyChangeLeftTargetItemProvider umlDependencyChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDependencyChangeLeftTargetAdapter() {
		if (umlDependencyChangeLeftTargetItemProvider == null) {
			umlDependencyChangeLeftTargetItemProvider = new UMLDependencyChangeLeftTargetItemProvider(this);
		}

		return umlDependencyChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDependencyChangeRightTargetItemProvider umlDependencyChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDependencyChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDependencyChangeRightTargetAdapter() {
		if (umlDependencyChangeRightTargetItemProvider == null) {
			umlDependencyChangeRightTargetItemProvider = new UMLDependencyChangeRightTargetItemProvider(this);
		}

		return umlDependencyChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExtendChangeLeftTargetItemProvider umlExtendChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLExtendChangeLeftTargetAdapter() {
		if (umlExtendChangeLeftTargetItemProvider == null) {
			umlExtendChangeLeftTargetItemProvider = new UMLExtendChangeLeftTargetItemProvider(this);
		}

		return umlExtendChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExtendChangeRightTargetItemProvider umlExtendChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLExtendChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLExtendChangeRightTargetAdapter() {
		if (umlExtendChangeRightTargetItemProvider == null) {
			umlExtendChangeRightTargetItemProvider = new UMLExtendChangeRightTargetItemProvider(this);
		}

		return umlExtendChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExecutionSpecificationChangeLeftTargetItemProvider umlExecutionSpecificationChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLExecutionSpecificationChangeLeftTargetAdapter() {
		if (umlExecutionSpecificationChangeLeftTargetItemProvider == null) {
			umlExecutionSpecificationChangeLeftTargetItemProvider = new UMLExecutionSpecificationChangeLeftTargetItemProvider(this);
		}

		return umlExecutionSpecificationChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLExecutionSpecificationChangeRightTargetItemProvider umlExecutionSpecificationChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLExecutionSpecificationChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLExecutionSpecificationChangeRightTargetAdapter() {
		if (umlExecutionSpecificationChangeRightTargetItemProvider == null) {
			umlExecutionSpecificationChangeRightTargetItemProvider = new UMLExecutionSpecificationChangeRightTargetItemProvider(this);
		}

		return umlExecutionSpecificationChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDestructionEventChangeLeftTargetItemProvider umlDestructionEventChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDestructionEventChangeLeftTargetAdapter() {
		if (umlDestructionEventChangeLeftTargetItemProvider == null) {
			umlDestructionEventChangeLeftTargetItemProvider = new UMLDestructionEventChangeLeftTargetItemProvider(this);
		}

		return umlDestructionEventChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLDestructionEventChangeRightTargetItemProvider umlDestructionEventChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLDestructionEventChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLDestructionEventChangeRightTargetAdapter() {
		if (umlDestructionEventChangeRightTargetItemProvider == null) {
			umlDestructionEventChangeRightTargetItemProvider = new UMLDestructionEventChangeRightTargetItemProvider(this);
		}

		return umlDestructionEventChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLIntervalConstraintChangeLeftTargetItemProvider umlIntervalConstraintChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLIntervalConstraintChangeLeftTargetAdapter() {
		if (umlIntervalConstraintChangeLeftTargetItemProvider == null) {
			umlIntervalConstraintChangeLeftTargetItemProvider = new UMLIntervalConstraintChangeLeftTargetItemProvider(this);
		}

		return umlIntervalConstraintChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLIntervalConstraintChangeRightTargetItemProvider umlIntervalConstraintChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLIntervalConstraintChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLIntervalConstraintChangeRightTargetAdapter() {
		if (umlIntervalConstraintChangeRightTargetItemProvider == null) {
			umlIntervalConstraintChangeRightTargetItemProvider = new UMLIntervalConstraintChangeRightTargetItemProvider(this);
		}

		return umlIntervalConstraintChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLMessageChangeLeftTargetItemProvider umlMessageChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLMessageChangeLeftTargetAdapter() {
		if (umlMessageChangeLeftTargetItemProvider == null) {
			umlMessageChangeLeftTargetItemProvider = new UMLMessageChangeLeftTargetItemProvider(this);
		}

		return umlMessageChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLMessageChangeRightTargetItemProvider umlMessageChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLMessageChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLMessageChangeRightTargetAdapter() {
		if (umlMessageChangeRightTargetItemProvider == null) {
			umlMessageChangeRightTargetItemProvider = new UMLMessageChangeRightTargetItemProvider(this);
		}

		return umlMessageChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeAttributeChangeLeftTargetItemProvider umlStereotypeAttributeChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeAttributeChangeLeftTargetAdapter() {
		if (umlStereotypeAttributeChangeLeftTargetItemProvider == null) {
			umlStereotypeAttributeChangeLeftTargetItemProvider = new UMLStereotypeAttributeChangeLeftTargetItemProvider(this);
		}

		return umlStereotypeAttributeChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeAttributeChangeRightTargetItemProvider umlStereotypeAttributeChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeAttributeChangeRightTargetAdapter() {
		if (umlStereotypeAttributeChangeRightTargetItemProvider == null) {
			umlStereotypeAttributeChangeRightTargetItemProvider = new UMLStereotypeAttributeChangeRightTargetItemProvider(this);
		}

		return umlStereotypeAttributeChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeUpdateAttributeItemProvider umlStereotypeUpdateAttributeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateAttribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeUpdateAttributeAdapter() {
		if (umlStereotypeUpdateAttributeItemProvider == null) {
			umlStereotypeUpdateAttributeItemProvider = new UMLStereotypeUpdateAttributeItemProvider(this);
		}

		return umlStereotypeUpdateAttributeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeApplicationAdditionItemProvider umlStereotypeApplicationAdditionItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationAddition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeApplicationAdditionAdapter() {
		if (umlStereotypeApplicationAdditionItemProvider == null) {
			umlStereotypeApplicationAdditionItemProvider = new UMLStereotypeApplicationAdditionItemProvider(this);
		}

		return umlStereotypeApplicationAdditionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeApplicationRemovalItemProvider umlStereotypeApplicationRemovalItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeApplicationRemoval}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeApplicationRemovalAdapter() {
		if (umlStereotypeApplicationRemovalItemProvider == null) {
			umlStereotypeApplicationRemovalItemProvider = new UMLStereotypeApplicationRemovalItemProvider(this);
		}

		return umlStereotypeApplicationRemovalItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeReferenceChangeLeftTargetItemProvider umlStereotypeReferenceChangeLeftTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeReferenceChangeLeftTargetAdapter() {
		if (umlStereotypeReferenceChangeLeftTargetItemProvider == null) {
			umlStereotypeReferenceChangeLeftTargetItemProvider = new UMLStereotypeReferenceChangeLeftTargetItemProvider(this);
		}

		return umlStereotypeReferenceChangeLeftTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeReferenceChangeRightTargetItemProvider umlStereotypeReferenceChangeRightTargetItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeReferenceChangeRightTargetAdapter() {
		if (umlStereotypeReferenceChangeRightTargetItemProvider == null) {
			umlStereotypeReferenceChangeRightTargetItemProvider = new UMLStereotypeReferenceChangeRightTargetItemProvider(this);
		}

		return umlStereotypeReferenceChangeRightTargetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeUpdateReferenceItemProvider umlStereotypeUpdateReferenceItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeUpdateReferenceAdapter() {
		if (umlStereotypeUpdateReferenceItemProvider == null) {
			umlStereotypeUpdateReferenceItemProvider = new UMLStereotypeUpdateReferenceItemProvider(this);
		}

		return umlStereotypeUpdateReferenceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UMLStereotypeReferenceOrderChangeItemProvider umlStereotypeReferenceOrderChangeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceOrderChange}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUMLStereotypeReferenceOrderChangeAdapter() {
		if (umlStereotypeReferenceOrderChangeItemProvider == null) {
			umlStereotypeReferenceOrderChangeItemProvider = new UMLStereotypeReferenceOrderChangeItemProvider(this);
		}

		return umlStereotypeReferenceOrderChangeItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (umlAssociationChangeLeftTargetItemProvider != null) umlAssociationChangeLeftTargetItemProvider.dispose();
		if (umlAssociationChangeRightTargetItemProvider != null) umlAssociationChangeRightTargetItemProvider.dispose();
		if (umlAssociationBranchChangeLeftTargetItemProvider != null) umlAssociationBranchChangeLeftTargetItemProvider.dispose();
		if (umlAssociationBranchChangeRightTargetItemProvider != null) umlAssociationBranchChangeRightTargetItemProvider.dispose();
		if (umlDependencyBranchChangeLeftTargetItemProvider != null) umlDependencyBranchChangeLeftTargetItemProvider.dispose();
		if (umlDependencyBranchChangeRightTargetItemProvider != null) umlDependencyBranchChangeRightTargetItemProvider.dispose();
		if (umlGeneralizationSetChangeLeftTargetItemProvider != null) umlGeneralizationSetChangeLeftTargetItemProvider.dispose();
		if (umlGeneralizationSetChangeRightTargetItemProvider != null) umlGeneralizationSetChangeRightTargetItemProvider.dispose();
		if (umlDependencyChangeLeftTargetItemProvider != null) umlDependencyChangeLeftTargetItemProvider.dispose();
		if (umlDependencyChangeRightTargetItemProvider != null) umlDependencyChangeRightTargetItemProvider.dispose();
		if (umlExtendChangeLeftTargetItemProvider != null) umlExtendChangeLeftTargetItemProvider.dispose();
		if (umlExtendChangeRightTargetItemProvider != null) umlExtendChangeRightTargetItemProvider.dispose();
		if (umlExecutionSpecificationChangeLeftTargetItemProvider != null) umlExecutionSpecificationChangeLeftTargetItemProvider.dispose();
		if (umlExecutionSpecificationChangeRightTargetItemProvider != null) umlExecutionSpecificationChangeRightTargetItemProvider.dispose();
		if (umlDestructionEventChangeLeftTargetItemProvider != null) umlDestructionEventChangeLeftTargetItemProvider.dispose();
		if (umlDestructionEventChangeRightTargetItemProvider != null) umlDestructionEventChangeRightTargetItemProvider.dispose();
		if (umlIntervalConstraintChangeLeftTargetItemProvider != null) umlIntervalConstraintChangeLeftTargetItemProvider.dispose();
		if (umlIntervalConstraintChangeRightTargetItemProvider != null) umlIntervalConstraintChangeRightTargetItemProvider.dispose();
		if (umlMessageChangeLeftTargetItemProvider != null) umlMessageChangeLeftTargetItemProvider.dispose();
		if (umlMessageChangeRightTargetItemProvider != null) umlMessageChangeRightTargetItemProvider.dispose();
		if (umlStereotypeAttributeChangeLeftTargetItemProvider != null) umlStereotypeAttributeChangeLeftTargetItemProvider.dispose();
		if (umlStereotypeAttributeChangeRightTargetItemProvider != null) umlStereotypeAttributeChangeRightTargetItemProvider.dispose();
		if (umlStereotypeUpdateAttributeItemProvider != null) umlStereotypeUpdateAttributeItemProvider.dispose();
		if (umlStereotypeApplicationAdditionItemProvider != null) umlStereotypeApplicationAdditionItemProvider.dispose();
		if (umlStereotypeApplicationRemovalItemProvider != null) umlStereotypeApplicationRemovalItemProvider.dispose();
		if (umlStereotypeReferenceChangeLeftTargetItemProvider != null) umlStereotypeReferenceChangeLeftTargetItemProvider.dispose();
		if (umlStereotypeReferenceChangeRightTargetItemProvider != null) umlStereotypeReferenceChangeRightTargetItemProvider.dispose();
		if (umlStereotypeUpdateReferenceItemProvider != null) umlStereotypeUpdateReferenceItemProvider.dispose();
		if (umlStereotypeReferenceOrderChangeItemProvider != null) umlStereotypeReferenceOrderChangeItemProvider.dispose();
	}

}