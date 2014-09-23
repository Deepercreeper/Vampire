package com.deepercreeper.vampireapp.newControllers;

import java.util.List;
import java.util.Set;

public interface ItemGroup <T extends Item>
{
	public void init(Set<T> aItems);
	
	public String getName();
	
	public List<T> getItems();
	
	public T getItem(String aName);
}
