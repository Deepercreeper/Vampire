package com.deepercreeper.vampireapp.controller.simplesItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.interfaces.Controller;

/**
 * A controller of simple item groups.
 * 
 * @author Vincent
 */
public class SimpleController implements Controller<SimpleItem>
{
	private final int[]								mAttributeCreationValues;
	
	private final int[]								mAbilityCreationValues;
	
	private final int								mVirtueCreationValue;
	
	private final HashMap<String, SimpleItemGroup>	mAttributes			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAttributeGroups	= new ArrayList<SimpleItemGroup>();
	
	private final HashMap<String, SimpleItemGroup>	mAbilities			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAbilityGroups		= new ArrayList<SimpleItemGroup>();
	
	private final SimpleItemGroup					mVirtues;
	
	/**
	 * Creates a new simple controller out of the given resources.
	 * 
	 * @param aResources
	 *            The context resources.
	 */
	public SimpleController(final Resources aResources)
	{
		
		mAttributeCreationValues = aResources.getIntArray(R.array.attributes_max_creation_values);
		mAbilityCreationValues = aResources.getIntArray(R.array.abilities_max_creation_values);
		mVirtueCreationValue = aResources.getInteger(R.integer.virtue_max_creation_value);
		for (final String attributesGroup : aResources.getStringArray(R.array.attribute_values))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(attributesGroup, 1, 4, 6, 5);
			mAttributes.put(group.getName(), group);
			mAttributeGroups.add(group);
		}
		for (final String abilitiesGroup : aResources.getStringArray(R.array.ability_values))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(abilitiesGroup, 0, 3, 6, 2);
			mAbilities.put(group.getName(), group);
			mAbilityGroups.add(group);
		}
		mVirtues = SimpleItemGroup.create(aResources.getString(R.string.virtue_values), 1, 5, 5, 2);
	}
	
	/**
	 * @return the maximum creation values for the attributes.
	 */
	public int[] getAttributeCreationValues()
	{
		return mAttributeCreationValues;
	}
	
	/**
	 * @return the maximum creation values for the abilities.
	 */
	public int[] getAbilityCreationValues()
	{
		return mAbilityCreationValues;
	}
	
	/**
	 * @return the maximum creation values for the virtues.
	 */
	public int getVirtueCreationValue()
	{
		return mVirtueCreationValue;
	}
	
	/**
	 * @return a list of all ability item groups.
	 */
	public List<SimpleItemGroup> getAbilities()
	{
		return mAbilityGroups;
	}
	
	/**
	 * @return a list of all attribute item groups.
	 */
	public List<SimpleItemGroup> getAttributes()
	{
		return mAttributeGroups;
	}
	
	/**
	 * @return the virtue item group.
	 */
	public SimpleItemGroup getVirtues()
	{
		return mVirtues;
	}
}
