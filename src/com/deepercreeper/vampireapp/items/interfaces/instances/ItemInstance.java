package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import java.util.Set;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestrictionable;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;
import com.deepercreeper.vampireapp.util.Saveable;

public interface ItemInstance extends InstanceRestrictionable, Comparable<ItemInstance>, Saveable
{
	public boolean canIncrease();
	
	public int getAbsoluteValue();
	
	public boolean hasOrder();
	
	public Set<Action> getActions();
	
	public int getAllValues();
	
	public CharacterInstance getCharacter();
	
	public ItemInstance getChildAt(int aIndex);
	
	public List<ItemInstance> getChildrenList();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public String getDescription();
	
	public List<ItemInstance> getDescriptionItems();
	
	public EPController getEP();
	
	public int getEPCost();
	
	public Item getItem();
	
	public ItemGroupInstance getItemGroup();
	
	public Mode getMode();
	
	public String getName();
	
	public ItemInstance getParentItem();
	
	public int getValue();
	
	public boolean hasChild(Item aItem);
	
	public boolean hasChildAt(int aIndex);
	
	public boolean hasChildren();
	
	public boolean hasDescription();
	
	public boolean hasEnoughEP();
	
	public boolean hasParentItem();
	
	public void increase();
	
	public int indexOfChild(ItemInstance aItem);
	
	public void init();
	
	public void initActions(ItemFinder aFinder);
	
	public boolean isParent();
	
	public boolean isValueItem();
	
	public boolean masterCanDecrease();
	
	public boolean masterCanIncrease();
	
	public void masterDecrease();
	
	public void masterIncrease();
	
	public void refreshValue();
	
	public void release();
	
	public void setIncreasable();
	
	public void setMode(Mode aMode);
	
	public void updateButtons();
	
	public void updateCharacter();
}
