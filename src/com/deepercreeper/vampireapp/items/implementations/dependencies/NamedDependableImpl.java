package com.deepercreeper.vampireapp.items.implementations.dependencies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.Dependable;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;

/**
 * The default implementation for named dependables.
 * 
 * @author Vincent
 */
public abstract class NamedDependableImpl extends Named implements Dependable
{
	private final Map<Type, Dependency> mDependencies = new HashMap<Type, Dependency>();
	
	/**
	 * Creates a new named dependable.
	 * 
	 * @param aName
	 *            The name.
	 */
	public NamedDependableImpl(final String aName)
	{
		super(aName);
	}
	
	@Override
	public void addDependency(final Dependency aDependency)
	{
		mDependencies.put(aDependency.getType(), aDependency);
	}
	
	@Override
	public boolean hasDependencies()
	{
		return !mDependencies.isEmpty();
	}
	
	@Override
	public Collection<Dependency> getDependencies()
	{
		return mDependencies.values();
	}
}
