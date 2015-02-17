package com.deepercreeper.vampireapp.items;

import java.util.List;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;

public interface ItemProvider
{
	public List<ItemController> getControllers();
	
	public ItemController getController(String aName);
	
	public ClanController getClans();
	
	public DescriptionController getDescriptions();
	
	public NatureController getNatures();
	
	public int[] getDefaultHealth();
}
