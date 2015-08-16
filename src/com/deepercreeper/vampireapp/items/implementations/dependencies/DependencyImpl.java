package com.deepercreeper.vampireapp.items.implementations.dependencies;

import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * The default implementation for dependencies.
 * 
 * @author Vincent
 */
public class DependencyImpl implements Dependency
{
	private final Type mType;
	
	private final DestinationType mDestinationType;
	
	private final String mItem;
	
	private final SparseIntArray mValue;
	
	private final SparseArray<int[]> mValues;
	
	/**
	 * Creates a new dependency.
	 * 
	 * @param aType
	 *            The type.
	 * @param aDestinationType
	 *            The destination type.
	 * @param aItem
	 *            The destination item.
	 * @param aValue
	 *            The value map.
	 * @param aValues
	 *            The values map.
	 */
	public DependencyImpl(final Type aType, DestinationType aDestinationType, final String aItem, final SparseIntArray aValue,
			SparseArray<int[]> aValues)
	{
		mType = aType;
		mDestinationType = aDestinationType;
		mItem = aItem;
		mValue = aValue;
		mValues = aValues;
	}
	
	@Override
	public boolean equals(Object aO)
	{
		if (aO instanceof Dependency)
		{
			Dependency dependency = (Dependency) aO;
			return getType().equals(dependency.getType());
		}
		return false;
	}
	
	@Override
	public DestinationType getDestinationType()
	{
		return mDestinationType;
	}
	
	@Override
	public String getItem()
	{
		return mItem;
	}
	
	@Override
	public Type getType()
	{
		return mType;
	}
	
	@Override
	public SparseIntArray getValue()
	{
		return mValue;
	}
	
	@Override
	public SparseArray<int[]> getValues()
	{
		return mValues;
	}
	
	@Override
	public int hashCode()
	{
		return getType().hashCode();
	}
}
