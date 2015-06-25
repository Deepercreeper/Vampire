package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.EPControllerInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A controller handles a bunch of groups and a their group options.
 * 
 * @author vrl
 */
public interface ItemControllerInstance extends Saveable, Viewable
{
	/**
	 * Closes the whole controller.
	 */
	public void close();
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @return whether this controller has any item.
	 */
	public boolean hasAnyItem();
	
	/**
	 * @return a list of all items that have a description.
	 */
	public List<ItemInstance> getDescriptionValues();
	
	/**
	 * @return the experience controller.
	 */
	public EPControllerInstance getEP();
	
	/**
	 * Adds the given item to this controller to create a shortcut.
	 * 
	 * @param aItem
	 *            The item to add.
	 */
	public void addItem(ItemInstance aItem);
	
	/**
	 * @return the character.
	 */
	public CharacterInstance getCharacter();
	
	/**
	 * @param aGroup
	 *            the group type.
	 * @return the group with the given group type.
	 */
	public ItemGroupInstance getGroup(ItemGroup aGroup);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group with the given name.
	 */
	public ItemGroupInstance getGroup(String aName);
	
	/**
	 * @param aGroupOption
	 *            The group option type.
	 * @return the group option with the given group option type.
	 */
	public GroupOptionInstance getGroupOption(GroupOption aGroupOption);
	
	/**
	 * @param aName
	 *            The group option name.
	 * @return the group option with the given name.
	 */
	public GroupOptionInstance getGroupOption(String aName);
	
	/**
	 * @return a list of all group options.
	 */
	public List<GroupOptionInstance> getGroupOptionsList();
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroupInstance> getGroupsList();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public ItemInstance getItem(final String aName);
	
	/**
	 * @return the item controller type.
	 */
	public ItemController getItemController();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the value of the item with the given name.
	 */
	public int getItemValue(String aName);
	
	/**
	 * @return the character mode.
	 */
	public Mode getMode();
	
	/**
	 * @return the controller name.
	 */
	public String getName();
	
	/**
	 * @param aName
	 *            The group name.
	 * @return whether this controller contains a group with the given name.
	 */
	public boolean hasGroup(String aName);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return whether this controller has a item with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * Resizes the controller view.
	 */
	public void resize();
	
	/**
	 * Sets whether whole controller is enabled.
	 * 
	 * @param aEnabled
	 *            Whether the controller should be enabled.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Sets the character mode.
	 * 
	 * @param aMode
	 *            The new mode.
	 */
	public void setMode(Mode aMode);
	
	/**
	 * Updates all groups.
	 */
	public void updateGroups();
}
