package com.deepercreeper.vampireapp.items.implementations.creations.dependencies;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.RestrictionableCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependencyInstance;

/**
 * A default implementation for restrictionable dependable creations.
 * 
 * @author Vincent
 */
public abstract class RestrictionableDependableCreationImpl extends RestrictionableCreationImpl implements DependableInstance
{
	private final Map<Type, DependencyInstance> mDependencies = new HashMap<Type, DependencyInstance>();
	
	/**
	 * Creates a new creation restrictionable dependable without controller.
	 */
	public RestrictionableDependableCreationImpl()
	{}
	
	/**
	 * Creates a new creation restrictionable dependable.
	 * 
	 * @param aController
	 *            The item controller.
	 */
	public RestrictionableDependableCreationImpl(final ItemControllerCreation aController)
	{
		super(aController);
	}
	
	@Override
	public final void addDependency(final DependencyInstance aDependency)
	{
		mDependencies.put(aDependency.getType(), aDependency);
	}
	
	@Override
	public final DependencyInstance getDependency(final Type aType)
	{
		return mDependencies.get(aType);
	}
	
	@Override
	public int getMaxValue()
	{
		return 0;
	}
	
	@Override
	public int[] getMaxValues()
	{
		return null;
	}
	
	@Override
	public int getStartValue()
	{
		return 0;
	}
	
	@Override
	public int[] getValues()
	{
		return null;
	}
	
	@Override
	public final boolean hasDependency(final Type aType)
	{
		return mDependencies.containsKey(aType) && getDependency(aType).isActive();
	}
}
