package com.deepercreeper.vampireapp.newControllers;


public interface ItemValue <T extends Item>
{
	public T getItem();
	
	public int getValue();
	
	public void increase();
	
	public void decrease();
}
