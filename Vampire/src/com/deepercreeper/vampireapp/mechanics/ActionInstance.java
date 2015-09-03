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
	 * @return whether this action can be used at the current item level and item costs.
	 */
	public boolean canUse();
	
	/**
	 * Determines, how many dices can be used to use this action and invokes then {@linkplain ActionInstance#use(int, boolean)}.
	 */
	public void use();
	
	/**
	 * Uses this action and then sends a message to the host.
	 * 
	 * @param aDices
	 *            The amount of dices to use for this action.
	 * @param aAsk
	 *            Whether this use is only asking the host or really an action.
	 */
	public void use(int aDices, boolean aAsk);
	
	/**
	 * @return whether the user has the possibility of spending a number of values for an additional dice.
	 */
	public boolean hasCostDices();
	
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
