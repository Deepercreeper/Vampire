package com.deepercreeper.vampireapp.controllers.lists;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.controllers.implementations.Named;

/**
 * Each character has to have a single clan. The clan defines, which disciplines and restrictions<br>
 * are set for the character.
 * 
 * @author vrl
 */
public class Clan extends Named
{
	private static final String					NAME_DELIM		= ":", GENERATION_DELIM = ";", CLAN_DISCIPLIN_DELIM = ",";
	
	private final HashSet<CreationRestriction>	mRestrictions	= new HashSet<CreationRestriction>();
	
	public Clan(final String aName)
	{
		super(aName);
	}
	
	public void addRestriction(final CreationRestriction aRestriction)
	{
		mRestrictions.add(aRestriction);
	}
	
	/**
	 * @return a set of all restrictions of this clan.
	 */
	public Set<CreationRestriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof Clan)
		{
			final Clan c = (Clan) aO;
			return getName().equals(c.getName());
		}
		return false;
	}
	
	/**
	 * @return a description for this clan containing name and disciplines.
	 */
	public String getDescription()
	{
		return getName();
		// TODO Implement
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}
}
