package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;

/**
 * A controller handles a bunch of groups and a their group options.
 * 
 * @author vrl
 */
public interface ItemControllerInstance extends Saveable, Viewable, DependableInstance
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
	 * @return a set of all item actions.
	 */
	public Set<ActionInstance> getActions();
	
	/**
	 * @return a list of all items that have a description.
	 */
	public List<ItemInstance> getDescriptionValues();
	
	/**
	 * Adds the given item to this controller to create a shortcut.
	 * 
	 * @param aItem
	 *            The item to add.
	 */
	public void addItem(ItemInstance aItem);
	
	/**
	 * Removes the given name from the controller item shortcuts map.
	 * 
	 * @param aName
	 *            The name to remove.
	 */
	public void removeItem(String aName);
	
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
	 * @return whether this controller is empty.
	 */
	public boolean isEmpty();
	
	/**
	 * @return whether this controller has any mutable group.
	 */
	public boolean hasMutableGroup();
	
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
	 * Updates the user interface.
	 */
	public void updateUI();
}
