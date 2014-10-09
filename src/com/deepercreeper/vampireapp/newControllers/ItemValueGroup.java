package com.deepercreeper.vampireapp.newControllers;

import java.util.List;
import android.view.ViewGroup;

public interface ItemValueGroup <T extends Item>
{
	public ValueController<T> getController();
	
	public ItemGroup<T> getGroup();
	
	public int getValue();
	
	public ItemValue<T> getValue(String aName);
	
	public ItemValue<T> getValue(T aItem);
	
	public List<? extends ItemValue<T>> getValuesList();
	
	public void initLayout(ViewGroup aLayout);
	
	public boolean isCreation();
	
	public void setCreation(boolean aCreation);
	
	public void updateValues(boolean aCanIncrease, boolean aCanDecrease);
}
