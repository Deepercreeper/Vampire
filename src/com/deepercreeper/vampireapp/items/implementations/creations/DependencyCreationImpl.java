package com.deepercreeper.vampireapp.items.implementations.creations;

import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.DestinationType;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.instances.DependencyInstance;

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
	public DependencyCreationImpl(Dependency aDependency, CharacterCreation aChar)
	{
		mDependency = aDependency;
		mChar = aChar;
	}
	
	@Override
	public int getValue()
	{
		return mDependency.getValue().get(getDestinationvalue());
	}
	
	@Override
	public int[] getValues()
	{
		return mDependency.getValues().get(getDestinationvalue());
	}
	
	@Override
	public int getDestinationvalue()
	{
		switch (getDestinationType())
		{
			case ITEM :
				return mChar.findItem(mDependency.getItem()).getValue();
			case GENERATION :
				return mChar.getGenerationValue();
		}
		return 0;
	}
	
	@Override
	public boolean isActive()
	{
		switch (getDestinationType())
		{
			case ITEM :
				return mChar.findItem(mDependency.getItem()) != null;
			case GENERATION :
				return true;
		}
		return false;
	}
	
	@Override
	public DestinationType getDestinationType()
	{
		return mDependency.getDestinationType();
	}
	
	@Override
	public Type getType()
	{
		return mDependency.getType();
	}
}
