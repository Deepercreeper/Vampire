package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.EPController;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestrictionable;
import com.deepercreeper.vampireapp.util.Saveable;

public interface ItemGroupInstance extends InstanceRestrictionable, Comparable<ItemGroupInstance>, Saveable
{
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public List<ItemInstance> getDescriptionItems();
	
	public boolean hasOrder();
	
	public EPController getEP();
	
	public ItemInstance getItem(Item aItem);
	
	public ItemInstance getItem(String aName);
	
	public ItemControllerInstance getItemController();
	
	public ItemGroup getItemGroup();
	
	public List<ItemInstance> getItemsList();
	
	public CharacterInstance getCharacter();
	
	public Mode getMode();
	
	public String getName();
	
	public int getValue();
	
	public boolean hasItem(Item aItem);
	
	public boolean hasItem(ItemInstance aItem);
	
	public boolean hasItem(String aName);
	
	public int indexOfItem(ItemInstance aItem);
	
	public void init();
	
	public boolean isValueGroup();
	
	public void release();
	
	public void setMode(Mode aMode);
	
	public void updateController();
	
	public void updateItems();
}
