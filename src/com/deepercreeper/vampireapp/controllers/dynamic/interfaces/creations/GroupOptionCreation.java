package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations;

import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;

public interface GroupOptionCreation extends Comparable<GroupOptionCreation>
{
	static interface GroupComparator extends Comparator<ItemGroupCreation>
	{
		public void setGroupChangeValue(String aGroupName, int aValue);
	}
	
	public boolean canChangeGroupBy(ItemGroup aGroup, int aValue);
	
	public boolean canChangeGroupBy(ItemGroupCreation aGroup, int aValue);
	
	public boolean canChangeGroupBy(String aName, int aValue);
	
	public void clear();
	
	public void close();
	
	public LinearLayout getContainer();
	
	public Context getContext();
	
	public ItemGroupCreation getGroup(ItemGroup aGroup);
	
	public void init();
	
	public GroupOption getGroupOption();
	
	public List<ItemGroupCreation> getGroupsList();
	
	public String getName();
	
	public boolean hasGroup(ItemGroup aGroup);
	
	public boolean hasGroup(ItemGroupCreation aGroup);
	
	public boolean hasGroup(String aName);
	
	public boolean isOpen();
	
	public boolean isValueGroupOption();
	
	public void resize();
	
	public void setEnabled(boolean aEnabled);
	
	public void toggleGroup();
	
	public void updateGroups();
	
	public void release();
}
