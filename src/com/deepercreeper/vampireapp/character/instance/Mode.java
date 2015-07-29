package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import android.content.Context;

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
	DEFAULT(R.string.normal, true, false, true, true, true),
	
	/**
	 * The character is sleeping. No actions possible.
	 */
	SLEEPING(R.string.sleeping, false, true, false, false, false),
	
	/**
	 * The character has been hurt so much, that he can't move anymore.
	 */
	KO(R.string.ko, false, false, false, false, false),
	
	/**
	 * The character is raging and can't handle some actions.
	 */
	RAGE(R.string.rage, false, false, true, false, false);
	
	private final boolean mCanClientLeave;
	
	private final boolean mCanClientEnter;
	
	private final boolean mCanUseAction;
	
	private final boolean mCanIncreaseItems;
	
	private final boolean mCanHeal;
	
	private final int mResourceId;
	
	private Mode(final int aResourceId, final boolean aCanClientLeave, final boolean aCanClientEnter, final boolean aCanUseActions,
			final boolean aCanIncreaseItems, final boolean aCanHeal)
	{
		mCanClientLeave = aCanClientLeave;
		mCanClientEnter = aCanClientEnter;
		mCanUseAction = aCanUseActions;
		mCanIncreaseItems = aCanIncreaseItems;
		mCanHeal = aCanHeal;
		mResourceId = aResourceId;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return The name of this mode.
	 */
	public String getName(final Context aContext)
	{
		return aContext.getString(mResourceId);
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
	
	/**
	 * @return whether the client can increase items inside this mode.
	 */
	public boolean canIncreaseItems()
	{
		return mCanIncreaseItems;
	}
	
	/**
	 * @return whether the client can heal himself inside this mode.
	 */
	public boolean canHeal()
	{
		return mCanHeal;
	}
	
	/**
	 * @param aMode
	 *            The mode.
	 * @param aContext
	 *            The underlying context.
	 * @return a nameable that represents the given mode.
	 */
	public static Nameable getNameableOf(final Mode aMode, final Context aContext)
	{
		return new Named(aMode.name())
		{
			@Override
			public String getDisplayName()
			{
				return aMode.getName(aContext);
			}
		};
	}
	
	/**
	 * @param aMode
	 *            The current mode.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided invoke.
	 * @return a list of nameables, representing the possible list of modes.
	 */
	public static List<Nameable> getModesList(final Mode aMode, final Context aContext, final boolean aHost)
	{
		final List<Nameable> list = new ArrayList<Nameable>();
		list.add(getNameableOf(aMode, aContext));
		if ( !aHost && !aMode.mCanClientLeave)
		{
			return list;
		}
		for (final Mode mode : values())
		{
			if (mode != aMode && (aHost || mode.mCanClientEnter))
			{
				list.add(getNameableOf(mode, aContext));
			}
		}
		return list;
	}
	
	/**
	 * @param aNameable
	 *            The nameable.
	 * @return The mode represented by the given nameable.
	 */
	public static Mode getModeOf(final Nameable aNameable)
	{
		return valueOf(aNameable.getName());
	}
}
