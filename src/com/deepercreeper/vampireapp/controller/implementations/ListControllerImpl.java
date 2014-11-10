package com.deepercreeper.vampireapp.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.deepercreeper.vampireapp.controller.interfaces.ListController;

/**
 * An implementation for list controllers.
 * 
 * @author Vincent
 * @param <T>
 *            The list element type.
 */
public abstract class ListControllerImpl <T extends Named> implements ListController<T>
{
	private final HashMap<String, T>	mValues		= new HashMap<String, T>();
	
	private final List<String>			mNames		= new ArrayList<String>();
	
	private final List<T>				mValuesList	= new ArrayList<T>();
	
	@Override
	public T get(final int aPos)
	{
		return mValues.get(mNames.get(aPos));
	}
	
	@Override
	public T get(final String aName)
	{
		return mValues.get(aName);
	}
	
	@Override
	public T getFirst()
	{
		return mValues.get(mNames.get(0));
	}
	
	@Override
	public List<String> getNames()
	{
		return mNames;
	}
	
	@Override
	public List<T> getValues()
	{
		return mValuesList;
	}
	
	@Override
	public int indexOf(final T aValue)
	{
		return mNames.indexOf(aValue.getName());
	}
	
	protected final void init(final List<T> aValues)
	{
		mValuesList.clear();
		mNames.clear();
		mValues.clear();
		mValuesList.addAll(aValues);
		Collections.sort(mValuesList);
		for (final T value : mValuesList)
		{
			mNames.add(value.getName());
			mValues.put(value.getName(), value);
		}
	}
}
