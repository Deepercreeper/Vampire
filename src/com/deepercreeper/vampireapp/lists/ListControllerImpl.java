package com.deepercreeper.vampireapp.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;

/**
 * An implementation for list controllers.
 * 
 * @author Vincent
 * @param <T>
 *            The list element type.
 */
public abstract class ListControllerImpl <T extends Nameable> implements ListController<T>
{
	private final Map<String, T>	mValues			= new HashMap<String, T>();
	
	private final Map<String, T>	mDisplayValues	= new HashMap<String, T>();
	
	private final List<String>		mNames			= new ArrayList<String>();
	
	private final List<String>		mDisplayNames	= new ArrayList<String>();
	
	private final List<T>			mValuesList		= new ArrayList<T>();
	
	@Override
	public int displayIndexOf(final T aValue)
	{
		return getDisplayNames().indexOf(aValue.getDisplayName());
	}
	
	@Override
	public List<String> getDisplayNames()
	{
		return mDisplayNames;
	}
	
	@Override
	public T getFirst()
	{
		return mDisplayValues.get(getDisplayNames().get(0));
	}
	
	@Override
	public T getItemAtDisplayNamePosition(final int aPos)
	{
		return getItemWithDisplayName(getDisplayNames().get(aPos));
	}
	
	@Override
	public T getItemAtPosition(final int aPos)
	{
		return mValues.get(getNames().get(aPos));
	}
	
	@Override
	public T getItemWithDisplayName(final String aName)
	{
		return mDisplayValues.get(aName);
	}
	
	@Override
	public T getItemWithName(final String aName)
	{
		return mValues.get(aName);
	}
	
	@Override
	public List<String> getNames()
	{
		return mNames;
	}
	
	@Override
	public List<T> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public int indexOf(final T aValue)
	{
		return getNames().indexOf(aValue.getName());
	}
	
	@Override
	public final void init(final List<T> aValues)
	{
		getValuesList().clear();
		getNames().clear();
		getDisplayNames().clear();
		mValues.clear();
		getValuesList().addAll(aValues);
		Collections.sort(getValuesList());
		for (final T value : getValuesList())
		{
			mNames.add(value.getName());
			mDisplayNames.add(value.getDisplayName());
			mDisplayValues.put(value.getDisplayName(), value);
			mValues.put(value.getName(), value);
		}
		Collections.sort(mDisplayNames);
	}
}
