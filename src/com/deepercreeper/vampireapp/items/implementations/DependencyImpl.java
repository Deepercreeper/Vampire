package com.deepercreeper.vampireapp.items.implementations;

import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import android.util.SparseIntArray;

/**
 * The default implementation for dependencies.
 * 
 * @author Vincent
 */
public class DependencyImpl implements Dependency
{
	private final Type mType;
	
	private final String mItem;
	
	private final SparseIntArray mValues;
	
	/**
	 * Creates a new dependency.
	 * 
	 * @param aType
	 *            The type.
	 * @param aItem
	 *            The destination item.
	 * @param aValues
	 *            The value map.
	 */
	public DependencyImpl(final Type aType, final String aItem, final SparseIntArray aValues)
	{
		mType = aType;
		mItem = aItem;
		mValues = aValues;
	}
	
	@Override
	public Type getType()
	{
		return mType;
	}
	
	@Override
	public String getItem()
	{
		return mItem;
	}
	
	@Override
	public SparseIntArray getValues()
	{
		return mValues;
	}
}
