package com.deepercreeper.vampireapp.items.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.dependencies.DependencyCreationImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependableInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ComparatorUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Expander;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * An item controller creation implementation.
 * 
 * @author vrl
 */
public class ItemControllerCreationImpl extends DependableInstanceImpl implements ItemControllerCreation
{
	private static final String TAG = "ItemControllerCreation";
	
	private final ItemController mItemController;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final List<ItemGroupCreation> mGroupsList = new ArrayList<ItemGroupCreation>();
	
	private final Map<String, ItemGroupCreation> mGroups = new HashMap<String, ItemGroupCreation>();
	
	private final Map<String, ItemCreation> mItems = new HashMap<String, ItemCreation>();
	
	private final Set<RestrictionCreation> mInactiveRestrictions = new HashSet<RestrictionCreation>();
	
	private final Expander mExpander;
	
	private final CharacterCreation mChar;
	
	/**
	 * Creates a new item controller.
	 * 
	 * @param aController
	 *            The item controller type.
	 * @param aContext
	 *            The underlying context.
	 * @param aChar
	 *            The parent character.
	 */
	public ItemControllerCreationImpl(final ItemController aController, final Context aContext, final CharacterCreation aChar)
	{
		mItemController = aController;
		mContext = aContext;
		mChar = aChar;
		
		for (final Dependency dependency : getItemController().getDependencies())
		{
			addDependency(new DependencyCreationImpl(dependency, aChar));
		}
		
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_controller_creation, null);
		mExpander = Expander.handle(R.id.view_controller_creation_button, R.id.view_controller_creation_panel, mContainer);
		
		mExpander.init();
		mExpander.getButton().setText(getItemController().getDisplayName());
		
		for (final ItemGroup group : getItemController().getGroupsList())
		{
			addGroupSilent(new ItemGroupCreationImpl(group, getContext(), this, mChar));
		}
		
		sortGroups();
	}
	
	@Override
	public void addItem(final ItemCreation aItem)
	{
		mItems.put(aItem.getName(), aItem);
		if ( !mInactiveRestrictions.isEmpty())
		{
			final Set<RestrictionCreation> activeRestrictions = new HashSet<RestrictionCreation>();
			for (final RestrictionCreation restriction : mInactiveRestrictions)
			{
				if (restriction.getItemName().equals(aItem.getName()))
				{
					aItem.addRestriction(restriction);
					activeRestrictions.add(restriction);
				}
			}
			for (final RestrictionCreation restriction : activeRestrictions)
			{
				mInactiveRestrictions.remove(restriction);
			}
		}
	}
	
	@Override
	public void addRestriction(final RestrictionCreation aRestriction)
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
		if ( !hasMaxValues())
		{
			return true;
		}
		final List<Integer> maxValues = new ArrayList<Integer>();
		{
			final int groupValue = aGroup.getValue();
			boolean isIrrelevantIncrease = true;
			for (final int maxValue : getMaxValues())
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
		
		ComparatorUtil.ITEM_GROUP_CREATION_COMPARATOR.setGroupChangeValue(aGroup.getName(), aValue);
		
		Collections.sort(groups, ComparatorUtil.ITEM_GROUP_CREATION_COMPARATOR);
		
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
	public void clear()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.clear();
		}
		resize();
	}
	
	@Override
	public void close()
	{
		mExpander.close();
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
	public List<ItemGroupCreation> getGroupsList()
	{
		return mGroupsList;
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
	public int getMaxValue(final CreationRestrictionType... aTypes)
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int[] getMaxValues()
	{
		int[] maxValues = getItemController().getMaxValues();
		if (hasDependency(Type.MAX_VALUES))
		{
			maxValues = getDependency(Type.MAX_VALUES).getValues(maxValues);
		}
		return maxValues;
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
	public Set<RestrictionCreation> getRestrictions(final CreationRestrictionType... aTypes)
	{
		final Set<RestrictionCreation> restrictions = new HashSet<RestrictionCreation>();
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
	public int hashCode()
	{
		return getItemController().hashCode();
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		return mItems.containsKey(aName);
	}
	
	@Override
	public boolean hasMaxValues()
	{
		final int[] maxValues = getMaxValues();
		return maxValues != null && maxValues.length > 0;
	}
	
	@Override
	public boolean hasRestrictions(final CreationRestrictionType... aTypes)
	{
		for (final ItemCreation item : mItems.values())
		{
			if (item.hasRestrictions(aTypes))
			{
				return true;
			}
		}
		for (final ItemGroupCreation group : getGroupsList())
		{
			if (group.hasRestrictions(aTypes))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isEmpty()
	{
		if (getGroupsList().isEmpty())
		{
			return true;
		}
		for (final ItemGroupCreation group : getGroupsList())
		{
			if ( !group.getItemsList().isEmpty() || mChar.getMode().canAddItem(group))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isValueOk(final int aValue, final CreationRestrictionType... aTypes)
	{
		return true;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void removeItem(final String aName)
	{
		if (mItems.get(aName).hasRestrictions())
		{
			for (final RestrictionCreation restriction : mItems.get(aName).getRestrictions())
			{
				restriction.clear();
				mInactiveRestrictions.add(restriction);
			}
		}
		mItems.remove(aName);
	}
	
	@Override
	public void removeRestriction(final RestrictionCreation aRestriction)
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
		mExpander.resize();
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		ViewUtil.setEnabled(mExpander.getButton(), aEnabled);
		if ( !aEnabled && mExpander.isOpen())
		{
			close();
		}
	}
	
	@Override
	public String toString()
	{
		return getItemController().getDisplayName();
	}
	
	@Override
	public void updateUI()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.updateUI();
		}
		setEnabled( !isEmpty());
	}
	
	private void addGroupSilent(final ItemGroupCreation aGroup)
	{
		if (getGroupsList().contains(aGroup))
		{
			Log.w(TAG, "Tried to add a group twice.");
			return;
		}
		mGroups.put(aGroup.getName(), aGroup);
		getGroupsList().add(aGroup);
		Collections.sort(getGroupsList());
		mExpander.getContainer().addView(aGroup.getContainer());
	}
	
	private void sortGroups()
	{
		for (final ItemGroupCreation group : getGroupsList())
		{
			group.release();
		}
		Collections.sort(getGroupsList());
		for (final ItemGroupCreation group : getGroupsList())
		{
			mExpander.getContainer().addView(group.getContainer());
		}
	}
}
