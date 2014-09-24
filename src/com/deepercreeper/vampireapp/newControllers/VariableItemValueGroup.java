package com.deepercreeper.vampireapp.newControllers;

public interface VariableItemValueGroup <T extends Item, S extends ItemValue<T>>
{
	public void addValue(S aValue);
	
	public void addValue(T aItem);
	
	public void addValue(String aName);
	
	public void removeValue(S aValue);
	
	public void removeValue(T aItem);
	
	public void removeValue(String aName);
}