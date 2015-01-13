package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

import com.deepercreeper.vampireapp.controllers.actions.Action.ItemFinder;

public interface ItemInstance
{
	public void initActions(ItemFinder aFinder);
	
	public int getValue();
}
