package com.deepercreeper.vampireapp.newControllers;

public interface Item extends Comparable<Item>
{
	public String getName();
	
	public String getDescription();
	
	public int getStartValue();
	
	public int getMaxValue();
	
	public ItemValue<? extends Item> createValue();
}