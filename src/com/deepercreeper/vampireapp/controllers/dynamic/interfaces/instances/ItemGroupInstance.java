package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestrictionable;

public interface ItemGroupInstance extends InstanceRestrictionable, Comparable<ItemGroupInstance>
{
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public List<ItemInstance> getDescriptionItems();
	
	public EPHandler getEP();
	
	public ItemInstance getItem(Item aItem);
	
	public Element asElement(Document aDoc);
	
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
