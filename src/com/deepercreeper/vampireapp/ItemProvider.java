package com.deepercreeper.vampireapp;

import java.util.List;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.lists.NatureController;

public interface ItemProvider
{
	public List<ItemController> getControllers();
	
	public ItemController getController(String aName);
	
	public ClanController getClans();
	
	public DescriptionController getDescriptions();
	
	public NatureController getNatures();
}
