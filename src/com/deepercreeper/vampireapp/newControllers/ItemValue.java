package com.deepercreeper.vampireapp.newControllers;

import android.widget.LinearLayout;

public interface ItemValue <T extends Item>
{
	public interface UpdateAction
	{
		public void update();
	}
	
	public boolean canDecrease();
	
	public boolean canDecrease(boolean aCreation);
	
	public boolean canIncrease();
	
	public boolean canIncrease(boolean aCreation);
	
	public void decrease();
	
	public void setIncreasable(boolean aEnabled);
	
	public void setDecreasable(boolean aEnabled);
	
	public LinearLayout getContainer();
	
	public T getItem();
	
	public int getValue();
	
	public void increase();
}
