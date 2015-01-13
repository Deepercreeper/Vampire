package com.deepercreeper.vampireapp.controllers.dynamic.interfaces;

import java.util.List;
import com.deepercreeper.vampireapp.controllers.actions.Action;

/**
 * There are several items that define character property types.
 * 
 * @author Vincent
 */
public interface Item extends Namable
{
	public void addChild(Item aItem);
	
	public Item getChild(String aName);
	
	public List<Item> getChildrenList();
	
	/**
	 * Each item has to have a display name.<br>
	 * It is displayed when the user touches any item name.
	 * 
	 * @return the item display name.
	 */
	public String getDisplayName();
	
	public void addAction(Action aAction);
	
	public boolean hasActions();
	
	public Action getAction(String aName);
	
	public List<Action> getActionsList();
	
	public int getFreePointsCost();
	
	public ItemGroup getItemGroup();
	
	public int getMaxValue();
	
	public int getMaxLowLevelValue();
	
	public Item getParentItem();
	
	public int getStartValue();
	
	public int[] getValues();
	
	public boolean hasChild(Item aItem);
	
	public boolean hasChild(String aName);
	
	public boolean hasParentItem();
	
	public boolean isMutableParent();
	
	public boolean isParent();
	
	public boolean isValueItem();
	
	/**
	 * @return whether this item needs a description that has to be given by creating a character.
	 */
	public boolean needsDescription();
}
