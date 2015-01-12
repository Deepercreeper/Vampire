package com.deepercreeper.vampireapp.controllers.lists;

import java.util.List;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Namable;

public interface ListController <T extends Namable>
{
	T get(int aPos);
	
	T get(String aName);
	
	T getFirst();
	
	List<String> getNames();
	
	List<T> getValues();
	
	int indexOf(T aValue);
	
	public void init(List<T> aValues);
}
