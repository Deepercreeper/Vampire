package com.deepercreeper.vampireapp.newControllers;

import java.util.List;

public interface ItemValueGroup <T extends Item>
{
	public ItemGroup<T> getGroup();
	
	public List<? extends ItemValue<T>> getValues();
	
	public ItemValue<T> getValue(String aName);
	
	public ItemValue<T> getValue(T aItem);
}
