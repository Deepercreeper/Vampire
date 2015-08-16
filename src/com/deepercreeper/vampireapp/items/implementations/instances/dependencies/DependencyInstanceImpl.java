package com.deepercreeper.vampireapp.items.implementations.instances.dependencies;

import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.DestinationType;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependencyInstance;

/**
 * The instance implementation of dependency instances.
 * 
 * @author vrl
 */
public class DependencyInstanceImpl implements DependencyInstance
{
	private final Dependency mDependency;
	
	private final CharacterInstance mChar;
	
	/**
	 * Creates a new dependency instance.
	 * 
	 * @param aDependency
	 *            The dependency type.
	 * @param aChar
	 *            The parent character.
	 */
	public DependencyInstanceImpl(final Dependency aDependency, final CharacterInstance aChar)
	{
		mDependency = aDependency;
		mChar = aChar;
	}
	
	@Override
	public DestinationType getDestinationType()
	{
		return mDependency.getDestinationType();
	}
	
	@Override
	public int getDestinationValue()
	{
		switch (getDestinationType())
		{
			case ITEM :
				return mChar.findItemInstance(mDependency.getItem()).getValue();
			case GENERATION :
				return mChar.getGeneration();
		}
		return 0;
	}
	
	@Override
	public Type getType()
	{
		return mDependency.getType();
	}
	
	@Override
	public int getValue(final int aDefault)
	{
		return mDependency.getValue().get(getDestinationValue(), aDefault);
	}
	
	@Override
	public int[] getValues(final int[] aDefault)
	{
		return mDependency.getValues().get(getDestinationValue(), aDefault);
	}
	
	@Override
	public boolean isActive()
	{
		switch (getDestinationType())
		{
			case ITEM :
				return mChar.findItemInstance(mDependency.getItem()) != null;
			case GENERATION :
				return true;
		}
		return false;
	}
}
