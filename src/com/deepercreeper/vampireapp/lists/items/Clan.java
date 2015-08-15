package com.deepercreeper.vampireapp.lists.items;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;

/**
 * Each character has to have a single clan. The clan defines, which disciplines and restrictions<br>
 * are set for the character.
 * 
 * @author vrl
 */
public class Clan extends Named
{
	private final HashSet<RestrictionCreation> mRestrictions = new HashSet<RestrictionCreation>();
	
	/**
	 * Creates a new clan with the given name.
	 * 
	 * @param aName
	 *            The clan name.
	 */
	public Clan(final String aName)
	{
		super(aName);
	}
	
	/**
	 * Adds restrictions to this clan.
	 * 
	 * @param aRestriction
	 *            The clan restrictions.
	 */
	public void addRestrictions(final Set<RestrictionCreation> aRestriction)
	{
		mRestrictions.addAll(aRestriction);
	}
	
	/**
	 * @return a set of all restrictions of this clan.
	 */
	public Set<RestrictionCreation> getRestrictions()
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
	}
}
