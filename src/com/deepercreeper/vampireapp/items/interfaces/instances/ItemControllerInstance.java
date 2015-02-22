package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.Saveable;

public interface ItemControllerInstance extends Saveable
{
	public void close();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public boolean hasAnyItem();
	
	public List<ItemInstance> getDescriptionValues();
	
	public EPController getEP();
	
	public void addItem(ItemInstance aItem);
	
	public CharacterInstance getCharacter();
	
	public ItemGroupInstance getGroup(ItemGroup aGroup);
	
	public ItemGroupInstance getGroup(String aName);
	
	public GroupOptionInstance getGroupOption(GroupOption aGroupOption);
	
	public GroupOptionInstance getGroupOption(String aName);
	
	public List<GroupOptionInstance> getGroupOptionsList();
	
	public List<ItemGroupInstance> getGroupsList();
	
	public ItemInstance getItem(final String aName);
	
	public ItemController getItemController();
	
	public int getItemValue(String aName);
	
	public Mode getMode();
	
	public String getName();
	
	public boolean hasGroup(String aName);
	
	public boolean hasItem(String aName);
	
	public void init();
	
	public void release();
	
	public void resize();
	
	public void setEnabled(boolean aEnabled);
	
	public void setMode(Mode aMode);
	
	public void updateGroups();
}
