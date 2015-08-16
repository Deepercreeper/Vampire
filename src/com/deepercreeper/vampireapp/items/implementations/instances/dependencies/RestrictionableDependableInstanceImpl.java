package com.deepercreeper.vampireapp.items.implementations.instances.dependencies;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.RestrictionableInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependencyInstance;

/**
 * The default restrictionable dependable instance implementation.
 * 
 * @author Vincent
 */
public abstract class RestrictionableDependableInstanceImpl extends RestrictionableInstanceImpl implements DependableInstance
{
	private final Map<Type, DependencyInstance> mDependencies = new HashMap<Type, DependencyInstance>();
	
	/**
	 * Creates a new restrictionable dependable with no item controller.
	 * 
	 * @param aCharacter
	 *            The parent character.
	 */
	public RestrictionableDependableInstanceImpl(final CharacterInstance aCharacter)
	{
		super(aCharacter);
	}
	
	/**
	 * Creates a new restrictionable dependable.
	 * 
	 * @param aCharacter
	 *            The parent character.
	 * @param aController
	 *            The item controller.
	 */
	public RestrictionableDependableInstanceImpl(final CharacterInstance aCharacter, final ItemControllerInstance aController)
	{
		super(aCharacter, aController);
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
		return mDependencies.containsKey(aType);
	}
}
