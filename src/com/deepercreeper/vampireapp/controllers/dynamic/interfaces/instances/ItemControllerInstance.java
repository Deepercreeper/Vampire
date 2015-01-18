package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

public interface ItemControllerInstance
{
	public ItemInstance getItem(String aName);
	
	public ItemGroupInstance getGroup(String aName);
	
	public boolean hasItem(String aItem);
}
