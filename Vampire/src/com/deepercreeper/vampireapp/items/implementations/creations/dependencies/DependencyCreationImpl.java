package com.deepercreeper.vampireapp.items.implementations.creations.dependencies;

import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.DestinationType;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependencyInstance;

/**
 * The creation implementation of dependency instances.
 * 
 * @author vrl
 */
public class DependencyCreationImpl implements DependencyInstance
{
	private final Dependency mDependency;
	
	private final CharacterCreation mChar;
	
	/**
	 * Creates a new dependency creation.
	 * 
	 * @param aDependency
	 *            The dependency type.
	 * @param aChar
	 *            The parent character.
	 */
	public DependencyCreationImpl(final Dependency aDependency, final CharacterCreation aChar)
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
				return mChar.findItem(mDependency.getItem()).getValue();
			case GENERATION :
				return mChar.getGenerationValue();
			case CORE_GENERATION :
				return mChar.getGeneration().getGeneration();
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
				return mChar.findItem(mDependency.getItem()) != null;
			case GENERATION :
			case CORE_GENERATION :
				return true;
		}
		return false;
	}
}
