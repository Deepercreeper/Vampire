package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * Contains item group creations and handles between group restrictions.
 * 
 * @author vrl
 */
public interface GroupOptionCreation extends Comparable<GroupOptionCreation>, Viewable
{
	/**
	 * @param aGroup
	 *            The group.
	 * @param aValue
	 *            The value.
	 * @return whether the value of the given group can be changed by the given value.
	 */
	public boolean canChangeGroupBy(ItemGroup aGroup, int aValue);
	
	/**
	 * @param aGroup
	 *            The group.
	 * @param aValue
	 *            The value.
	 * @return whether the value of the given group can be changed by the given value.
	 */
	public boolean canChangeGroupBy(ItemGroupCreation aGroup, int aValue);
	
	/**
	 * @param aName
	 *            The group name.
	 * @param aValue
	 *            The value.
	 * @return whether the value of the group with the given name can be changed by the given value.
	 */
	public boolean canChangeGroupBy(String aName, int aValue);
	
	/**
	 * Clears the whole group option.
	 */
	public void clear();
	
	/**
	 * Closes the group option view.
	 */
	public void close();
	
	/**
	 * @return whether this group option contains any item.
	 */
	public boolean hasAnyItem();
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @param aGroup
	 *            The group type.
	 * @return the group with the given group type.
	 */
	public ItemGroupCreation getGroup(ItemGroup aGroup);
	
	/**
	 * @return the group option type.
	 */
	public GroupOption getGroupOption();
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroupCreation> getGroupsList();
	
	/**
	 * @return the group option name.
	 */
	public String getName();
	
	/**
	 * @param aGroup
	 *            The group.
	 * @return whether this group option contains the given group.
	 */
	public boolean hasGroup(ItemGroup aGroup);
	
	/**
	 * @param aGroup
	 *            The group.
	 * @return whether this group option contains the given group.
	 */
	public boolean hasGroup(ItemGroupCreation aGroup);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return whether this group option contains a group with the given name.
	 */
	public boolean hasGroup(String aName);
	
	/**
	 * @return whether the view of this group option is open.
	 */
	public boolean isOpen();
	
	/**
	 * @return whether this is a value group option.
	 */
	public boolean isValueGroupOption();
	
	/**
	 * Resizes this group option.
	 */
	public void resize();
	
	/**
	 * Sets whether this group option should be enabled.
	 * 
	 * @param aEnabled
	 *            Whether this group option is enabled.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Opens or closes this group option.
	 */
	public void toggleGroup();
	
	/**
	 * Updates all groups inside this group option.
	 */
	public void updateGroups();
}
