package com.deepercreeper.vampireapp.newControllers;

import java.util.List;

public interface ItemGroup <T extends Item>
{
	public T getItem(String aName);
	
	public List<T> getItems();
	
	public String getName();
}
