package com.deepercreeper.vampireapp.mechanics;

import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;

/**
 * An interface that determines which methods an action instance has to have.
 * 
 * @author Vincent
 */
public interface ActionInstance extends Viewable, Comparable<ActionInstance>
{
	/**
	 * @param aLevel
	 *            The action item value.
	 * @return whether this action can be used at the current item level and item costs.
	 */
	public boolean canUse(final int aLevel);
	
	/**
	 * @return the action type.
	 */
	public Action getAction();
	
	/**
	 * By default the number of dices is the number of minimum dices added to all necessary<br>
	 * item values for this action.
	 * 
	 * @return the default number of dices.
	 */
	public int getDefaultDices();
	
	/**
	 * @return the parent item or {@code null}.
	 */
	public ItemInstance getParent();
	
	/**
	 * @return whether this action has a parent item.
	 */
	public boolean hasParent();
}
