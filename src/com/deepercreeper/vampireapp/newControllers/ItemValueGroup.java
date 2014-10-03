package com.deepercreeper.vampireapp.newControllers;

import java.util.List;
import android.widget.LinearLayout;

public interface ItemValueGroup <T extends Item>
{
	public ValueController<T> getController();
	
	public void setCreation(boolean aCreation);
	
	public boolean isCreation();
	
	public ItemGroup<T> getGroup();
	
	public List<? extends ItemValue<T>> getValuesList();
	
	public int getValue();
	
	public ItemValue<T> getValue(String aName);
	
	public ItemValue<T> getValue(T aItem);
	
	public void updateValues(boolean aCanIncrease, boolean aCanDecrease);
	
	public void initLayout(LinearLayout aLayout);
}
