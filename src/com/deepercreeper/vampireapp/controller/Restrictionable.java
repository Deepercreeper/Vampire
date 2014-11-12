package com.deepercreeper.vampireapp.controller;

import java.util.Set;

public interface Restrictionable
{
	public void removeRestriction(Restriction aRestriction);
	
	public void addRestriction(Restriction aRestriction);
	
	public Set<Restriction> getRestrictions();
	
	public boolean hasRestrictions();
	
	public int getMinValue();
	
	public int getMaxValue();
}
