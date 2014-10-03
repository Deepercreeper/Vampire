package com.deepercreeper.vampireapp.newControllers;

import android.widget.ImageButton;

public interface ItemValue <T extends Item>
{
	public T getItem();
	
	public int getValue();
	
	public boolean canIncrease();
	
	public boolean canDecrease();
	
	public boolean canIncrease(boolean aCreation);
	
	public boolean canDecrease(boolean aCreation);
	
	public void setIncreaseButton(ImageButton aIncreaseButton);
	
	public void setDecreaseButton(ImageButton aDecreaseButton);
	
	public ImageButton getIncreaseButton();
	
	public ImageButton getDecreaseButton();
	
	public void increase();
	
	public void decrease();
}
