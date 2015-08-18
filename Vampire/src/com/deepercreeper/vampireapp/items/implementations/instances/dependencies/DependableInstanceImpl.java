package com.deepercreeper.vampireapp.items.implementations.instances.dependencies;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependencyInstance;

/**
 * The default implementation for dependable instances.
 * 
 * @author Vincent
 */
public abstract class DependableInstanceImpl implements DependableInstance
{
	private final Map<Type, DependencyInstance> mDependencies = new HashMap<Type, DependencyInstance>();
	
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
