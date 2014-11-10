package com.deepercreeper.vampireapp.controller.interfaces;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.ViewGroup;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;

/**
 * Item values are controlled by groups. This is used to handle several items.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public interface ItemValueGroup <T extends Item, S extends ItemValue<T>>
{
	/**
	 * @return the context of this value group.
	 */
	public Context getContext();
	
	/**
	 * @return the parent controller of this group.
	 */
	public ValueController<T> getController();
	
	/**
	 * @return whether this group is in creation mode.
	 */
	public CharMode getCreationMode();
	
	/**
	 * @return a list of all values, that need a description and have more than 0 as value.
	 */
	public List<S> getDescriptionValues();
	
	/**
	 * @return the item group of this value group.
	 */
	public ItemGroup<T> getGroup();
	
	/**
	 * @return the point handler of this value group.
	 */
	public PointHandler getPoints();
	
	/**
	 * @return the sum of all temporary points inside this value group.
	 */
	public int getTempPoints();
	
	/**
	 * @return the update action of this value group.
	 */
	public UpdateAction getUpdateAction();
	
	/**
	 * @return the sum of all values inside this value group.
	 */
	public int getValue();
	
	/**
	 * @param aName
	 *            The name of the value item.
	 * @return the value with the given name.
	 */
	public S getValue(String aName);
	
	/**
	 * @return a map from all items to all values.
	 */
	public HashMap<T, S> getValues();
	
	/**
	 * @return a list of all value items.
	 */
	public List<S> getValuesList();
	
	/**
	 * Initializes this value group into the given layout.
	 * 
	 * @param aLayout
	 *            The layout to initialize the widgets into.
	 */
	public void initLayout(ViewGroup aLayout);
	
	/**
	 * Removes all widgets from their parent containers.
	 */
	public void release();
	
	/**
	 * Removes all temporary points from all values.
	 */
	public void resetTempPoints();
	
	/**
	 * Sets whether this group is in creation mode.
	 * 
	 * @param aMode
	 *            Whether this group represents the values inside a character creation.
	 */
	public void setCreationMode(CharMode aMode);
	
	/**
	 * Sets the current points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
	
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
