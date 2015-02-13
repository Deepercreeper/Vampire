package com.deepercreeper.vampireapp.controllers.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Namable;
import com.deepercreeper.vampireapp.controllers.lists.ListController;
import com.deepercreeper.vampireapp.util.LanguageUtil;

/**
 * An implementation for list controllers.
 * 
 * @author Vincent
 * @param <T>
 *            The list element type.
 */
public abstract class ListControllerImpl <T extends Namable> implements ListController<T>
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
	
	public List<String> getDisplayNames()
	{
		final List<String> displayNames = new ArrayList<String>();
		for (final String name : getNames())
		{
			displayNames.add(LanguageUtil.instance().getValue(name));
		}
		return displayNames;
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
	
	@Override
	public final void init(final List<T> aValues)
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
