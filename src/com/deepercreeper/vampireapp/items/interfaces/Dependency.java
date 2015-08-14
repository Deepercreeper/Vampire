package com.deepercreeper.vampireapp.items.interfaces;

import android.util.SparseIntArray;

/**
 * A dependency allows to set the value of any type of any dependable to a variable depending value.
 * 
 * @author Vincent
 */
public interface Dependency
{
	/**
	 * A dependency type.
	 * 
	 * @author Vincent
	 */
	public static enum Type
	{
		/**
		 * The maximum value of items.
		 */
		MAX_VALUE("maxValue");
		
		private final String mName;
		
		private Type(final String aName)
		{
			mName = aName;
		}
		
		/**
		 * @return the name of this type.
		 */
		public String getName()
		{
			return mName;
		}
		
		/**
		 * @param aName
		 *            The type name.
		 * @return the type with the given name.
		 */
		public static Type get(final String aName)
		{
			for (final Type type : values())
			{
				if (type.getName().equals(aName))
				{
					return type;
				}
			}
			return null;
		}
	}
	
	/**
	 * @return the destination item.
	 */
	public String getItem();
	
	/**
	 * @return the dependency type.
	 */
	public Type getType();
	
	/**
	 * @return the values map.
	 */
	public SparseIntArray getValues();
}
