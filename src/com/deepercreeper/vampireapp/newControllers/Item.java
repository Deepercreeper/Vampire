package com.deepercreeper.vampireapp.newControllers;

public interface Item extends Comparable<Item>
{
	public String getDescription();
	
	public int getMaxStartValue();
	
	public int getMaxValue();
	
	public String getName();
	
	public int getStartValue();
}
