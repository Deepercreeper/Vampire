package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.view.ResizeListener;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A group option contains a few groups and manages the display button for them.
 * 
 * @author vrl
 */
public interface GroupOptionInstance extends Comparable<GroupOptionInstance>, Saveable, Viewable, ResizeListener
{
	/**
	 * @return the character.
	 */
	public CharacterInstance getCharacter();
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @param aGroup
	 *            The group type.
	 * @return the group with the given group type.
	 */
	public ItemGroupInstance getGroup(ItemGroup aGroup);
	
	/**
	 * @return the group option type.
	 */
	public GroupOption getGroupOption();
	
	/**
	 * @return a list of all groups inside this group option.
	 */
	public List<ItemGroupInstance> getGroupsList();
	
	/**
	 * @return the group option name.
	 */
	public String getName();
	
	/**
	 * @return whether this group option contains any item.
	 */
	public boolean hasAnyItem();
	
	/**
	 * @param aGroup
	 *            The group type.
	 * @return whether this group option contains a group with the given group type.
	 */
	public boolean hasGroup(ItemGroup aGroup);
	
	/**
	 * Closes the group option.
	 */
	public void close();
	
	/**
	 * @param aGroup
	 *            The group.
	 * @return whether this group option contains the given group.
	 */
	public boolean hasGroup(ItemGroupInstance aGroup);
	
	/**
	 * @param aName
	 *            the group name.
	 * @return whether this group option contains a group with the given name.
	 */
	public boolean hasGroup(String aName);
	
	/**
	 * @return whether this is a value group option.
	 */
	public boolean isValueGroupOption();
	
	/**
	 * Sets whether this group option should be enabled.
	 * 
	 * @param aEnabled
	 *            whether this group option is enabled.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Updates all groups inside this group option.
	 */
	public void updateGroups();
}
