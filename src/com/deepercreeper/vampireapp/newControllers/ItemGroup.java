package com.deepercreeper.vampireapp.newControllers;

import java.util.List;

public interface ItemGroup <T extends Item>
{
	public void addItem(T aItem);
	
	public String getName();
	
	public List<T> getItems();
	
	public T getItem(String aName);
}
