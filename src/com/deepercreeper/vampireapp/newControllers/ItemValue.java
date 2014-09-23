package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;

public interface ItemValue <T extends Item> extends Comparable<ItemValue<? super T>>
{
	public T getItem();
	
	public int getValue();
	
	public void increase();
	
	public void decrease();
	
	public Comparator<ItemValue<T>> getComparator();
}
