package com.deepercreeper.vampireapp.newControllers;

import android.widget.ImageButton;

public interface ItemValue <T extends Item>
{
	public boolean canDecrease();
	
	public boolean canDecrease(boolean aCreation);
	
	public boolean canIncrease();
	
	public boolean canIncrease(boolean aCreation);
	
	public void decrease();
	
	public ImageButton getDecreaseButton();
	
	public ImageButton getIncreaseButton();
	
	public T getItem();
	
	public int getValue();
	
	public void increase();
	
	public void setDecreaseButton(ImageButton aDecreaseButton);
	
	public void setIncreaseButton(ImageButton aIncreaseButton);
}
