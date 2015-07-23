package com.deepercreeper.vampireapp.character.instance;

/**
 * Represents the modes a character can be inside.
 * 
 * @author vrl
 */
public enum Mode
{
	/**
	 * Default state. Nothing special.
	 */
	DEFAULT(true, false, true),
	
	/**
	 * The character is sleeping. No actions possible.
	 */
	SLEEPING(false, true, false),
	
	/**
	 * The character has been hurt so much, that he can't move anymore.
	 */
	KO(false, false, false),
	
	/**
	 * The character is raging and can't handle some actions.
	 */
	RAGE(false, false, true);
	
	private final boolean mCanClientLeave;
	
	private final boolean mCanClientEnter;
	
	private final boolean mCanUseAction;
	
	private Mode(final boolean aCanClientLeave, final boolean aCanClientEnter, final boolean aCanUseActions)
	{
		mCanClientLeave = aCanClientLeave;
		mCanClientEnter = aCanClientEnter;
		mCanUseAction = aCanUseActions;
	}
	
	/**
	 * @return whether the client is able to enter this mode from every leavable other mode.
	 */
	public boolean canClientEnter()
	{
		return mCanClientEnter;
	}
	
	/**
	 * @return whether the client is able to leave this mode.
	 */
	public boolean canClientLeave()
	{
		return mCanClientLeave;
	}
	
	/**
	 * @return whether the client is able to use actions inside this mode.
	 */
	public boolean canUseAction()
	{
		return mCanUseAction;
	}
}
