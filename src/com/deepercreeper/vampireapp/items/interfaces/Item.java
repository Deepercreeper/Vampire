package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;
import com.deepercreeper.vampireapp.mechanics.Action;

/**
 * There are several items that define character property types.
 * 
 * @author Vincent
 */
public interface Item extends Namable
{
	public void addAction(Action aAction);
	
	public void addChild(Item aItem);
	
	public Action getAction(String aName);
	
	public List<Action> getActionsList();
	
	public Item getChild(String aName);
	
	public List<Item> getChildrenList();
	
	/**
	 * Each item has to have a display name.<br>
	 * It is displayed when the user touches any item name.
	 * 
	 * @return the item display name.
	 */
	public String getDescription();
	
	@Override
	public String getDisplayName();
	
	public int getEPCost();
	
	public int getEPCostMultiplicator();
	
	public int getEPCostNew();
	
	public int getFreePointsCost();
	
	public ItemGroup getItemGroup();
	
	public int getMaxLowLevelValue();
	
	public int getMaxValue();
	
	public Item getParentItem();
	
	public int getStartValue();
	
	public int[] getValues();
	
	public boolean hasActions();
	
	public boolean hasChild(Item aItem);
	
	public boolean hasChild(String aName);
	
	public boolean hasOrder();
	
	public boolean hasParentItem();
	
	public boolean isMutableParent();
	
	public boolean isParent();
	
	public boolean isValueItem();
	
	/**
	 * @return whether this item needs a description that has to be given by creating a character.
	 */
	public boolean needsDescription();
}
