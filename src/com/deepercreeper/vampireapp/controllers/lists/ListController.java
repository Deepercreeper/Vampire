package com.deepercreeper.vampireapp.controllers.lists;

import java.util.List;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Namable;

public interface ListController <T extends Namable>
{
	public T getItemAtPosition(int aPos);
	
	public T getItemAtDisplayNamePosition(final int aPos);
	
	public List<String> getDisplayNames();
	
	public T getFirst();
	
	public T getItemWithDisplayName(String aName);
	
	public T getItemWithName(String aName);
	
	public List<String> getNames();
	
	public List<T> getValuesList();
	
	public int indexOf(T aValue);
	
	public int displayIndexOf(T aValue);
	
	public void init(List<T> aValues);
}
