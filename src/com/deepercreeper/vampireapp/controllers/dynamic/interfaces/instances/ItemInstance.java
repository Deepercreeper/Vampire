package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances;

import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.controllers.actions.Action;
import com.deepercreeper.vampireapp.controllers.actions.Action.ItemFinder;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestrictionable;

public interface ItemInstance extends InstanceRestrictionable
{
	public boolean canIncrease();
	
	public int getAbsoluteValue();
	
	public Set<Action> getActions();
	
	public int getAllValues();
	
	public CharacterInstance getCharacter();
	
	public ItemInstance getChildAt(int aIndex);
	
	public List<ItemInstance> getChildrenList();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public Element asElement(Document aDoc);
	
	public String getDescription();
	
	public List<ItemInstance> getDescriptionItems();
	
	public EPHandler getEP();
	
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
