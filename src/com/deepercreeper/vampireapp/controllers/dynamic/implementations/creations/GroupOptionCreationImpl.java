package com.deepercreeper.vampireapp.controllers.dynamic.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.GroupOptionCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class GroupOptionCreationImpl implements GroupOptionCreation
{
	private static final String						TAG				= "GroupOptionCreation";
	
	private static final GroupComparator			COMPARATOR		= new GroupComparator()
																	{
																		private String	mGroupName;
																		
																		private int		mValue;
																		
																		@Override
																		public void setGroupChangeValue(final String aGroupName, final int aValue)
																		{
																			mGroupName = aGroupName;
																			mValue = aValue;
																		}
																		
																		@Override
																		public int compare(final ItemGroupCreation aLhs, final ItemGroupCreation aRhs)
																		{
																			int leftValue = aLhs.getValue();
																			if (aLhs.getName().equals(mGroupName))
																			{
																				leftValue += mValue;
																			}
																			int rightValue = aRhs.getValue();
																			if (aRhs.getName().equals(mGroupName))
																			{
																				rightValue += mValue;
																			}
																			return leftValue - rightValue;
																		}
																	};
	
	private final GroupOption						mGroupOption;
	
	private final Map<ItemGroup, ItemGroupCreation>	mGroups			= new HashMap<ItemGroup, ItemGroupCreation>();
	
	private final List<ItemGroupCreation>			mGroupsList		= new ArrayList<ItemGroupCreation>();
	
	private final Context							mContext;
	
	private final LinearLayout						mContainer;
	
	private final LinearLayout						mGroupContainer;
	
	private final Button							mGroupButton;
	
	private boolean									mInitialized	= false;
	
	private boolean									mOpen			= false;
	
	public GroupOptionCreationImpl(final GroupOption aGroupOption, final ItemControllerCreation aController, final Context aContext)
	{
		mGroupOption = aGroupOption;
		mContext = aContext;
		mContainer = new LinearLayout(getContext());
		mGroupContainer = new LinearLayout(getContext());
		mGroupButton = new Button(getContext());
		init();
		for (final ItemGroup group : getGroupOption().getGroups())
		{
			addGroup(aController.getGroup(group));
		}
	}
	
	@Override
	public boolean canChangeGroupBy(final ItemGroup aGroup, final int aValue)
	{
		return canChangeGroupBy(getGroup(aGroup), aValue);
	}
	
	@Override
	public boolean canChangeGroupBy(final ItemGroupCreation aGroup, final int aValue)
	{
		if ( !getGroupOption().hasMaxValues())
		{
			return true;
		}
		final List<Integer> maxValues = new ArrayList<Integer>();
		{
			final int groupValue = aGroup.getValue();
			boolean isIrrelevantIncrease = true;
			for (final int maxValue : getGroupOption().getMaxValues())
			{
				maxValues.add(maxValue);
				if (isIrrelevantIncrease && (groupValue <= maxValue) != (groupValue + aValue <= maxValue))
				{
					isIrrelevantIncrease = false;
				}
			}
			if (isIrrelevantIncrease)
			{
				return true;
			}
		}
		
		Collections.sort(maxValues);
		
		final List<ItemGroupCreation> groups = new ArrayList<ItemGroupCreation>();
		
		for (final ItemGroupCreation group : getGroupsList())
		{
			groups.add(group);
		}
		
		COMPARATOR.setGroupChangeValue(aGroup.getName(), aValue);
		
		Collections.sort(groups, COMPARATOR);
		
		final boolean[] doneValues = new boolean[maxValues.size()];
		
		for (int groupIndex = groups.size() - 1; groupIndex >= 0; groupIndex-- )
		{
			boolean doneAnything = false;
			
			final ItemGroupCreation group = groups.get(groupIndex);
			int groupValue = groups.get(groupIndex).getValue();
			if (group.equals(aGroup))
			{
				groupValue += aValue;
			}
			
			for (int doneIndex = doneValues.length - 1; doneIndex >= 0; doneIndex-- )
			{
				if (doneValues[doneIndex])
				{
					continue;
				}
				
				if (groupValue > maxValues.get(doneIndex))
				{
					return false;
				}
				
				doneValues[doneIndex] = true;
				doneAnything = true;
				break;
			}
			if ( !doneAnything)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean canChangeGroupBy(final String aName, final int aValue)
	{
		return canChangeGroupBy(getGroupOption().getGroup(aName), aValue);
	}
	
	@Override
	public void clear()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.clear();
		}
	}
	
	@Override
	public void close()
	{
		mOpen = false;
		mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
	}
	
	@Override
	public int compareTo(final GroupOptionCreation aAnother)
	{
		if (aAnother == null)
		{
			return getGroupOption().compareTo(null);
		}
		return getGroupOption().compareTo(aAnother.getGroupOption());
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof GroupOptionCreation)
		{
			final GroupOptionCreation item = (GroupOptionCreation) aO;
			return getGroupOption().equals(item.getGroupOption());
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
	public ItemGroupCreation getGroup(final ItemGroup aGroup)
	{
		return mGroups.get(aGroup);
	}
	
	@Override
	public GroupOption getGroupOption()
	{
		return mGroupOption;
	}
	
	@Override
	public List<ItemGroupCreation> getGroupsList()
	{
		return mGroupsList;
	}
	
	@Override
	public String getName()
	{
		return getGroupOption().getName();
	}
	
	@Override
	public boolean hasGroup(final ItemGroup aGroup)
	{
		return mGroupOption.hasGroup(aGroup);
	}
	
	@Override
	public boolean hasGroup(final ItemGroupCreation aGroup)
	{
		return mGroupOption.hasGroup(aGroup.getItemGroup().getName());
	}
	
	@Override
	public boolean hasGroup(final String aName)
	{
		return mGroupOption.hasGroup(aName);
	}
	
	@Override
	public boolean isOpen()
	{
		return mOpen;
	}
	
	@Override
	public boolean isValueGroupOption()
	{
		return getGroupOption().isValueGroupOption();
	}
	
	@Override
	public void resize()
	{
		if (mGroupContainer.getAnimation() != null && !mGroupContainer.getAnimation().hasEnded())
		{
			mGroupContainer.getAnimation().cancel();
		}
		int height = 0;
		if (isOpen())
		{
			height = ViewUtil.calcHeight(mGroupContainer);
		}
		if (height != mGroupContainer.getHeight())
		{
			mGroupContainer.startAnimation(new ResizeAnimation(mGroupContainer, mGroupContainer.getWidth(), height));
		}
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		mGroupButton.setEnabled(aEnabled);
		if ( !aEnabled && isOpen())
		{
			close();
		}
	}
	
	@Override
	public void toggleGroup()
	{
		mOpen = !mOpen;
		if (mOpen)
		{
			mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
		}
		else
		{
			mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		resize();
	}
	
	@Override
	public void updateGroups()
	{
		boolean hasAnyItem = false;
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.updateItems();
			if ( !group.getItemsList().isEmpty() || group.getCreationMode().canAddItem(group))
			{
				hasAnyItem = true;
			}
		}
		setEnabled(hasAnyItem);
	}
	
	@Override
	public void release()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.release();
		}
		ViewUtil.release(getContainer());
		ViewUtil.release(mGroupButton);
		ViewUtil.release(mGroupContainer);
	}
	
	private void addGroup(final ItemGroupCreation aGroup)
	{
		if (getGroupsList().contains(aGroup))
		{
			Log.w(TAG, "Tried to add a group twice.");
			return;
		}
		mGroups.put(aGroup.getItemGroup(), aGroup);
		mGroupsList.add(aGroup);
		Collections.sort(mGroupsList);
		mGroupContainer.addView(aGroup.getContainer());
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mGroupButton.setLayoutParams(ViewUtil.getWrapHeight());
			mGroupButton.setText(getName());
		}
		
		if (mOpen)
		{
			mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
		}
		else
		{
			mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		
		if ( !mInitialized)
		{
			mGroupButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					toggleGroup();
				}
			});
		}
		getContainer().addView(mGroupButton);
		
		mGroupContainer.setLayoutParams(ViewUtil.getZeroHeight());
		
		if ( !mInitialized)
		{
			mGroupContainer.setOrientation(LinearLayout.VERTICAL);
		}
		getContainer().addView(mGroupContainer);
		
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.init();
			mGroupContainer.addView(group.getContainer());
		}
		mInitialized = true;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
