package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

import java.util.List;
import java.util.Set;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.controllers.actions.Action;
import com.deepercreeper.vampireapp.controllers.actions.Action.ItemFinder;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestrictionable;

public interface ItemInstance extends InstanceRestrictionable
{
	public Set<Action> getActions();
	
	public void initActions(ItemFinder aFinder);
	
	public Item getItem();
	
	public boolean canDecrease();
	
	public boolean canIncrease();
	
	public boolean hasDescription();
	
	public void clear();
	
	public void decrease();
	
	public void init();
	
	public void updateButtons();
	
	public int getAbsoluteValue();
	
	public int getAllValues();
	
	public List<ItemInstance> getDescriptionItems();
	
	public ItemInstance getChildAt(int aIndex);
	
	public int indexOfChild(ItemInstance aItem);
	
	public boolean hasChildAt(int aIndex);
	
	public List<ItemInstance> getChildrenList();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public int getEPCost();
	
	public ItemGroupInstance getItemGroup();
	
	public String getName();
	
	public ItemInstance getParentItem();
	
	public EPHandler getEP();
	
	public boolean hasChild(Item aItem);
	
	public boolean hasChildren();
	
	public boolean hasEnoughEP();
	
	public boolean hasParentItem();
	
	public void increase();
	
	public boolean isParent();
	
	public boolean isValueItem();
	
	public void updateCharacter();
	
	public com.deepercreeper.vampireapp.character.CharacterInstance getCharacter();
	
	public void refreshValue();
	
	public void release();
	
	public void setMode(Mode aMode);
	
	public void setDecreasable();
	
	public void setIncreasable();
	
	public void setEP(EPHandler aEP);
	
	public String getDescription();
	
	public Mode getMode();
	
	public int getValue();
}
