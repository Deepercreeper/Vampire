package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import android.content.Context;

/**
 * Everything that is affected by the game time should implement this interface.
 * 
 * @author vrl
 */
public interface TimeListener
{
	/**
	 * There are different duration types.
	 * 
	 * @author vrl
	 */
	public enum Type
	{
		
		/**
		 * A round is equal to a very short time, that is used to make the game flow.<br>
		 * Typically a round is around a second, but does not count up to hours.
		 */
		ROUND(R.string.round, true),
		
		/**
		 * Represents an hour.
		 */
		HOUR(R.string.hour, true),
		
		/**
		 * Represents a day. By default a day is around 10 hours long.
		 */
		DAY(R.string.day, true),
		
		/**
		 * Sets the given hour and calculates the difference.
		 */
		SET( -1, false);
		
		/**
		 * @param aType
		 *            The type nameable.
		 * @return the type of the given nameable.
		 */
		public static Type getTypeOf(Nameable aType)
		{
			return valueOf(aType.getDisplayName());
		}
		
		/**
		 * @param aContext
		 *            The underlying context.
		 * @return a list of nameables that represent a type each.
		 * @note the display name of each nameable is replaced by the types real name.
		 */
		public static List<Nameable> getTypesList(Context aContext)
		{
			List<Nameable> list = new ArrayList<Nameable>();
			for (final Type type : values())
			{
				if (type.isTypicalType())
				{
					list.add(new Named(type.getName(aContext))
					{
						@Override
						public String getDisplayName()
						{
							return type.name();
						}
					});
				}
			}
			return list;
		}
		
		private final int mId;
		
		private final boolean mTypical;
		
		private Type(int aId, boolean aTypical)
		{
			mId = aId;
			mTypical = aTypical;
		}
		
		/**
		 * @param aContext
		 *            The underlying context.
		 * @return the duration type name.
		 */
		public String getName(Context aContext)
		{
			if (mId == -1)
			{
				return name();
			}
			return aContext.getString(mId);
		}
		
		/**
		 * @return whether this type is visible for users.
		 */
		public boolean isTypicalType()
		{
			return mTypical;
		}
	}
	
	/**
	 * The number of hours, a day has.
	 */
	public static final int HOURS_PER_DAY = 10;
	
	/**
	 * The hour when the sun sets.
	 */
	public static final int EVENING = 21;
	
	/**
	 * The hour when the sun rises.
	 */
	public static final int MORNING = 5;
	
	/**
	 * The given time has passed.
	 * 
	 * @param aType
	 *            The time type.
	 * @param aAmount
	 *            The amount of time that has passed.
	 */
	public void time(Type aType, int aAmount);
}
