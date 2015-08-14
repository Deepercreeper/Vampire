package com.deepercreeper.vampireapp.items.interfaces;

import android.util.SparseArray;
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
		MAX_VALUE("maxValue"),
		
		/**
		 * All possible item values.
		 */
		VALUES("values");
		
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
	 * Types of destinations.
	 * 
	 * @author vrl
	 */
	public static enum DestinationType
	{
		/**
		 * The destination is a normal item.
		 */
		ITEM("item"),
		
		/**
		 * The destination is the character generation.
		 */
		GENERATION("generation");
		
		private final String mName;
		
		private DestinationType(String aName)
		{
			mName = aName;
		}
		
		/**
		 * @return the type name.
		 */
		public String getName()
		{
			return mName;
		}
		
		/**
		 * @param aName
		 *            The destination type name.
		 * @return the destination type with the given name.
		 */
		public static DestinationType get(final String aName)
		{
			for (final DestinationType type : values())
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
	 * @return the destination type.
	 */
	public DestinationType getDestinationType();
	
	/**
	 * @return the value map.
	 */
	public SparseIntArray getValue();
	
	/**
	 * @return a map that defines the values for each value.
	 */
	public SparseArray<int[]> getValues();
}