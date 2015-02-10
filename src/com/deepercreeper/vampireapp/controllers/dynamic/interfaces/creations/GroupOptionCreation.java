package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;

public interface GroupOptionCreation extends Comparable<GroupOptionCreation>
{
	public boolean canChangeGroupBy(ItemGroup aGroup, int aValue);
	
	public boolean canChangeGroupBy(ItemGroupCreation aGroup, int aValue);
	
	public boolean canChangeGroupBy(String aName, int aValue);
	
	public void clear();
	
	public void close();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public ItemGroupCreation getGroup(ItemGroup aGroup);
	
	public GroupOption getGroupOption();
	
	public List<ItemGroupCreation> getGroupsList();
	
	public String getName();
	
	public boolean hasGroup(ItemGroup aGroup);
	
	public boolean hasGroup(ItemGroupCreation aGroup);
	
	public boolean hasGroup(String aName);
	
	public void init();
	
	public boolean isOpen();
	
	public boolean isValueGroupOption();
	
	public void release();
	
	public void resize();
	
	public void setEnabled(boolean aEnabled);
	
	public void toggleGroup();
	
	public void updateGroups();
}
