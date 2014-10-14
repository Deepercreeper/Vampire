package com.deepercreeper.vampireapp.controller;

public interface VariableValueGroup <T extends Item, S extends ItemValue<T>>
{
	public void addItem(T aItem);
	
	public void resize();
	
	public void clear();
}
