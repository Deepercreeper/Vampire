package com.deepercreeper.vampireapp.controllers.dynamic.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CreationMode;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.GroupOptionCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class ItemControllerCreationImpl implements ItemControllerCreation
{
	private static final String									TAG						= "ItemControllerCreation";
	
	private final ItemController								mItemController;
	
	private final Context										mContext;
	
	private final LinearLayout									mContainer;
	
	private final List<ItemGroupCreation>						mGroupsList				= new ArrayList<ItemGroupCreation>();
	
	private final Map<String, ItemGroupCreation>				mGroups					= new HashMap<String, ItemGroupCreation>();
	
	private final List<GroupOptionCreation>						mGroupOptionsList		= new ArrayList<GroupOptionCreation>();
	
	private final Map<ItemGroupCreation, GroupOptionCreation>	mGroupOptions			= new HashMap<ItemGroupCreation, GroupOptionCreation>();
	
	private final Map<String, ItemCreation>						mItems					= new HashMap<String, ItemCreation>();
	
	private final Set<CreationRestriction>						mInactiveRestrictions	= new HashSet<CreationRestriction>();
	
	private boolean												mInitialized			= false;
	
	private PointHandler										mPoints;
	
	private CreationMode										mMode;
	
	public ItemControllerCreationImpl(final ItemController aController, final Context aContext, final CreationMode aMode, final PointHandler aPoints)
	{
		mItemController = aController;
		mContext = aContext;
		mMode = aMode;
		mPoints = aPoints;
		mContainer = new LinearLayout(getContext());
		init();
		for (final ItemGroup group : getItemController().getGroupsList())
		{
			addGroupSilent(new ItemGroupCreationImpl(group, getContext(), this, getCreationMode(), getPoints()));
		}
		for (final GroupOption groupOption : getItemController().getGroupOptionsList())
		{
			addGroupOptionSilent(new GroupOptionCreationImpl(groupOption, this, getContext()));
		}
	}
	
	@Override
	public void addItemName(final ItemCreation aItem)
	{
		mItems.put(aItem.getName(), aItem);
		if ( !mInactiveRestrictions.isEmpty())
		{
			final Set<CreationRestriction> activeRestrictions = new HashSet<CreationRestriction>();
			for (final CreationRestriction restriction : mInactiveRestrictions)
			{
				if (restriction.getItemName().equals(aItem.getName()))
				{
					aItem.addRestriction(restriction);
					activeRestrictions.add(restriction);
				}
			}
			for (final CreationRestriction restriction : activeRestrictions)
			{
				mInactiveRestrictions.remove(restriction);
			}
		}
	}
	
	@Override
	public void addRestriction(final CreationRestriction aRestriction)
	{
		if (mItems.containsKey(aRestriction.getItemName()))
		{
			mItems.get(aRestriction.getItemName()).addRestriction(aRestriction);
		}
		else if (mGroups.containsKey(aRestriction.getItemName()))
		{
			mGroups.get(aRestriction.getItemName()).addRestriction(aRestriction);
		}
		else
		{
			mInactiveRestrictions.add(aRestriction);
			aRestriction.setParent(this);
		}
	}
	
	@Override
	public boolean canChangeGroupBy(final ItemGroupCreation aGroup, final int aValue)
	{
		return mGroupOptions.get(aGroup).canChangeGroupBy(aGroup, aValue);
	}
	
	@Override
	public void clear()
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			group.clear();
		}
		resize();
	}
	
	@Override
	public void close()
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			group.close();
		}
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof ItemControllerCreation)
		{
			final ItemControllerCreation item = (ItemControllerCreation) aO;
			return getItemController().equals(item.getItemController());
		}
		return false;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public List<ItemCreation> getDescriptionValues()
	{
		final List<ItemCreation> items = new ArrayList<ItemCreation>();
		for (final ItemGroupCreation group : getGroupsList())
		{
			items.addAll(group.getDescriptionItems());
		}
		return items;
	}
	
	@Override
	public ItemGroupCreation getGroup(final ItemGroup aGroup)
	{
		return getGroup(aGroup.getName());
	}
	
	@Override
	public ItemGroupCreation getGroup(final String aName)
	{
		return mGroups.get(aName);
	}
	
	@Override
	public GroupOptionCreation getGroupOption(final GroupOption aGroupOption)
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			if (group.getGroupOption().equals(aGroupOption))
			{
				return group;
			}
		}
		return null;
	}
	
	@Override
	public GroupOptionCreation getGroupOption(final String aName)
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			if (group.getName().equals(aName))
			{
				return group;
			}
		}
		return null;
	}
	
	@Override
	public List<GroupOptionCreation> getGroupOptionsList()
	{
		return mGroupOptionsList;
	}
	
	@Override
	public List<ItemGroupCreation> getGroupsList()
	{
		return mGroupsList;
	}
	
	@Override
	public int getGroupValue(final String aName)
	{
		return getGroup(aName).getValue();
	}
	
	@Override
	public ItemCreation getItem(final String aName)
	{
		return mItems.get(aName);
	}
	
	@Override
	public ItemController getItemController()
	{
		return mItemController;
	}
	
	@Override
	public int getItemValue(final String aName)
	{
		return getItem(aName).getValue();
	}
	
	@Override
	public int getMaxValue(final CreationRestrictionType... aTypes)
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int getMinValue(final CreationRestrictionType... aTypes)
	{
		return Integer.MIN_VALUE;
	}
	
	@Override
	public String getName()
	{
		return getItemController().getName();
	}
	
	@Override
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public Set<CreationRestriction> getRestrictions(final CreationRestrictionType... aTypes)
	{
		final Set<CreationRestriction> restrictions = new HashSet<CreationRestriction>();
		for (final ItemCreation item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				restrictions.addAll(item.getRestrictions(aTypes));
			}
		}
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				restrictions.addAll(group.getRestrictions(aTypes));
			}
		}
		return restrictions;
	}
	
	@Override
	public int getTempPoints()
	{
		int tempPoints = 0;
		for (final ItemGroupCreation group : getGroupsList())
		{
			tempPoints += group.getTempPoints();
		}
		return tempPoints;
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.isValueGroup())
			{
				value += group.getValue();
			}
		}
		return value;
	}
	
	@Override
	public boolean hasGroup(final String aName)
	{
		return mGroups.containsKey(aName);
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		return mItems.containsKey(aName);
	}
	
	@Override
	public boolean hasRestrictions()
	{
		for (final ItemCreation item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				return true;
			}
		}
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
		}
		
		if ( !getGroupOptionsList().isEmpty())
		{
			for (final GroupOptionCreation groupOption : getGroupOptionsList())
			{
				groupOption.init();
				getContainer().addView(groupOption.getContainer());
			}
		}
		mInitialized = true;
	}
	
	@Override
	public boolean isValueOk(final int aValue, final CreationRestrictionType... aTypes)
	{
		return true;
	}
	
	@Override
	public void release()
	{
		for (final GroupOptionCreation groupOption : getGroupOptionsList())
		{
			groupOption.release();
		}
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void removeItemName(final String aName)
	{
		if (mItems.get(aName).hasRestrictions())
		{
			for (final CreationRestriction restriction : mItems.get(aName).getRestrictions())
			{
				restriction.clear();
				mInactiveRestrictions.add(restriction);
			}
		}
		mItems.remove(aName);
	}
	
	@Override
	public void removeRestriction(final CreationRestriction aRestriction)
	{
		mInactiveRestrictions.remove(aRestriction);
	}
	
	@Override
	public void resetTempPoints()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.isValueGroup())
			{
				group.resetTempPoints();
			}
		}
	}
	
	@Override
	public void resize()
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			group.resize();
		}
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.setCreationMode(getCreationMode());
		}
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		for (final GroupOptionCreation group : getGroupOptionsList())
		{
			group.setEnabled(aEnabled);
		}
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.setPoints(getPoints());
		}
	}
	
	@Override
	public String toString()
	{
		return getItemController().getDisplayName();
	}
	
	@Override
	public void updateGroups()
	{
		for (final GroupOptionCreation groupOption : getGroupOptionsList())
		{
			groupOption.updateGroups();
		}
	}
	
	@Override
	public void updateRestrictions()
	{
		for (final ItemCreation item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				item.updateRestrictions();
			}
		}
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				group.updateRestrictions();
			}
		}
	}
	
	private void addGroupOptionSilent(final GroupOptionCreation aGroupOption)
	{
		getGroupOptionsList().add(aGroupOption);
		for (final ItemGroup group : aGroupOption.getGroupOption().getGroups())
		{
			mGroupOptions.put(mGroups.get(group.getName()), aGroupOption);
		}
		Collections.sort(getGroupOptionsList());
		getContainer().addView(aGroupOption.getContainer());
	}
	
	private void addGroupSilent(final ItemGroupCreation aGroup)
	{
		getGroupsList().add(aGroup);
		mGroups.put(aGroup.getName(), aGroup);
	}
}
