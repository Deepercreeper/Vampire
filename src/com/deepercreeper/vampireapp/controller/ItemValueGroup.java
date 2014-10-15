package com.deepercreeper.vampireapp.controller;

import java.util.List;
import android.view.ViewGroup;

/**
 * Item values are controlled by groups. This is used to handle several items.
 * 
 * @author Vincent
 * @param <T>
 */
public interface ItemValueGroup <T extends Item>
{
	/**
	 * @return the parent controller of this group.
	 */
	public ValueController<T> getController();
	
	/**
	 * @return the item group of this value group.
	 */
	public ItemGroup<T> getGroup();
	
	/**
	 * @return the sum of all values inside this value group.
	 */
	public int getValue();
	
	/**
	 * @param aName
	 *            The name of the value item.
	 * @return the value with the given name.
	 */
	public ItemValue<T> getValue(String aName);
	
	/**
	 * @return a list of all value items.
	 */
	public List<? extends ItemValue<T>> getValuesList();
	
	/**
	 * Initializes this value group into the given layout.
	 * 
	 * @param aLayout
	 *            The layout to initialize the widgets into.
	 */
	public void initLayout(ViewGroup aLayout);
	
	/**
	 * @return whether this group is in creation mode.
	 */
	public boolean isCreation();
	
	/**
	 * Sets whether this group is in creation mode.
	 * 
	 * @param aCreation
	 *            Whether this group represents the values inside a character creation.
	 */
	public void setCreation(boolean aCreation);
	
	/**
	 * Updates all values and whether they can be increased and decreased.
	 * 
	 * @param aCanIncrease
	 *            Whether values can be increased.
	 * @param aCanDecrease
	 *            Whether values can be decreased.
	 */
	public void updateValues(boolean aCanIncrease, boolean aCanDecrease);
}
