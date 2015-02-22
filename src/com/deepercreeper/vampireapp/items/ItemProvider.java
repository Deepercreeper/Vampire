package com.deepercreeper.vampireapp.items;

import java.util.List;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;

public interface ItemProvider
{
	public ClanController getClans();
	
	public ItemController getController(String aName);
	
	public List<ItemController> getControllers();
	
	public DescriptionController getDescriptions();
	
	public String getGenerationItem();
	
	public Health getHealth();
	
	public Inventory getInventory();
	
	public Money getMoney();
	
	public NatureController getNatures();
}