package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.GroupOptionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.GroupOptionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class ItemControllerInstanceImpl implements ItemControllerInstance
{
	private static final String									TAG					= "ItemControllerInstance";
	
	private final ItemController								mItemController;
	
	private final Context										mContext;
	
	private final LinearLayout									mContainer;
	
	private final List<ItemGroupInstance>						mGroupsList			= new ArrayList<ItemGroupInstance>();
	
	private final Map<String, ItemGroupInstance>				mGroups				= new HashMap<String, ItemGroupInstance>();
	
	private final List<GroupOptionInstance>						mGroupOptionsList	= new ArrayList<GroupOptionInstance>();
	
	private final Map<ItemGroupInstance, GroupOptionInstance>	mGroupOptions		= new HashMap<ItemGroupInstance, GroupOptionInstance>();
	
	private final Map<String, ItemInstance>						mItems				= new HashMap<String, ItemInstance>();
	
	private boolean												mInitialized		= false;
	
	private final CharacterInstance								mCharacter;
	
	private Mode												mMode;
	
	private final EPController									mEP;
	
	public ItemControllerInstanceImpl(final Element aElement, final ItemProvider aItems, final Context aContext, final Mode aMode,
			final EPController aEP, final CharacterInstance aCharacter)
	{
		mItemController = aItems.getController(aElement.getAttribute("name"));
		mContext = aContext;
		mMode = aMode;
		mEP = aEP;
		mCharacter = aCharacter;
		mContainer = new LinearLayout(getContext());
		
		init();
		
		final NodeList children = aElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if (children.item(i) instanceof Element)
			{
				final Element group = (Element) children.item(i);
				if ( !group.getTagName().equals("group"))
				{
					continue;
				}
				addGroupSilent(new ItemGroupInstanceImpl(group, this, getContext(), getMode(), getEP(), getCharacter()));
			}
		}
		for (int i = 0; i < children.getLength(); i++ )
		{
			if (children.item(i) instanceof Element)
			{
				final Element groupOption = (Element) children.item(i);
				if ( !groupOption.getTagName().equals("group-option"))
				{
					continue;
				}
				addGroupOptionSilent(new GroupOptionInstanceImpl(groupOption, this, getContext(), getCharacter()));
			}
		}
	}
	
	public ItemControllerInstanceImpl(final ItemControllerCreation aItemController, final Context aContext, final Mode aMode, final EPController aEP,
			final CharacterInstance aCharacter)
	{
		mItemController = aItemController.getItemController();
		mContext = aContext;
		mMode = aMode;
		mEP = aEP;
		mCharacter = aCharacter;
		mContainer = new LinearLayout(getContext());
		
		init();
		
		for (final ItemGroupCreation group : aItemController.getGroupsList())
		{
			addGroupSilent(new ItemGroupInstanceImpl(group, this, getContext(), getMode(), getEP(), getCharacter()));
		}
		for (final GroupOptionCreation groupOption : aItemController.getGroupOptionsList())
		{
			addGroupOptionSilent(new GroupOptionInstanceImpl(groupOption, this, getContext(), getCharacter()));
		}
	}
	
	@Override
	public void addRestriction(final InstanceRestriction aRestriction)
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
			Log.w(TAG, "Tried to restrict a non added item or group.");
			aRestriction.clear();
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element controller = aDoc.createElement("controller");
		controller.setAttribute("name", getName());
		for (final GroupOptionInstance groupOption : getGroupOptionsList())
		{
			controller.appendChild(groupOption.asElement(aDoc));
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			controller.appendChild(group.asElement(aDoc));
		}
		return controller;
	}
	
	@Override
	public void addItem(final ItemInstance aItem)
	{
		mItems.put(aItem.getName(), aItem);
	}
	
	@Override
	public boolean hasAnyItem()
	{
		if (getGroupsList().isEmpty())
		{
			return false;
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.updateItems();
			if ( !group.getItemsList().isEmpty())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void close()
	{
		for (final GroupOptionInstance group : getGroupOptionsList())
		{
			group.close();
		}
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
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
	public List<ItemInstance> getDescriptionValues()
	{
		final List<ItemInstance> items = new ArrayList<ItemInstance>();
		for (final ItemGroupInstance group : getGroupsList())
		{
			items.addAll(group.getDescriptionItems());
		}
		return items;
	}
	
	@Override
	public EPController getEP()
	{
		return mEP;
	}
	
	@Override
	public ItemGroupInstance getGroup(final ItemGroup aGroup)
	{
		return getGroup(aGroup.getName());
	}
	
	@Override
	public ItemGroupInstance getGroup(final String aName)
	{
		return mGroups.get(aName);
	}
	
	@Override
	public GroupOptionInstance getGroupOption(final GroupOption aGroupOption)
	{
		return getGroupOption(aGroupOption.getName());
	}
	
	@Override
	public GroupOptionInstance getGroupOption(final String aName)
	{
		for (final GroupOptionInstance group : getGroupOptionsList())
		{
			if (group.getName().equals(aName))
			{
				return group;
			}
		}
		return null;
	}
	
	@Override
	public List<GroupOptionInstance> getGroupOptionsList()
	{
		return mGroupOptionsList;
	}
	
	@Override
	public List<ItemGroupInstance> getGroupsList()
	{
		return mGroupsList;
	}
	
	@Override
	public ItemInstance getItem(final String aName)
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
	public int getMaxValue(final InstanceRestrictionType... aTypes)
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int getMinValue(final InstanceRestrictionType... aTypes)
	{
		return Integer.MIN_VALUE;
	}
	
	@Override
	public Mode getMode()
	{
		return mMode;
	}
	
	@Override
	public String getName()
	{
		return getItemController().getName();
	}
	
	@Override
	public Set<InstanceRestriction> getRestrictions(final InstanceRestrictionType... aTypes)
	{
		final Set<InstanceRestriction> restrictions = new HashSet<InstanceRestriction>();
		for (final ItemInstance item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				restrictions.addAll(item.getRestrictions(aTypes));
			}
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				restrictions.addAll(group.getRestrictions(aTypes));
			}
		}
		return restrictions;
	}
	
	@Override
	public boolean hasRestrictions()
	{
		for (final ItemInstance item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				return true;
			}
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				return true;
			}
		}
		return false;
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
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mInitialized = true;
		}
		
		if ( !getGroupOptionsList().isEmpty())
		{
			for (final GroupOptionInstance groupOption : getGroupOptionsList())
			{
				if (groupOption.hasAnyItem())
				{
					groupOption.init();
					getContainer().addView(groupOption.getContainer());
				}
			}
		}
	}
	
	@Override
	public boolean isValueOk(final int aValue, final InstanceRestrictionType... aTypes)
	{
		return true;
	}
	
	@Override
	public void removeRestriction(final InstanceRestriction aRestriction)
	{
		// Nothing to do
	}
	
	@Override
	public void release()
	{
		for (final GroupOptionInstance groupOption : getGroupOptionsList())
		{
			groupOption.release();
		}
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void resize()
	{
		for (final GroupOptionInstance group : getGroupOptionsList())
		{
			group.resize();
		}
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		for (final GroupOptionInstance group : getGroupOptionsList())
		{
			group.setEnabled(aEnabled);
		}
	}
	
	@Override
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.setMode(getMode());
		}
	}
	
	@Override
	public void updateGroups()
	{
		for (final GroupOptionInstance groupOption : getGroupOptionsList())
		{
			groupOption.updateGroups();
		}
	}
	
	@Override
	public void updateRestrictions()
	{
		for (final ItemInstance item : mItems.values())
		{
			if (item.hasRestrictions())
			{
				item.updateRestrictions();
			}
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			if (group.hasRestrictions())
			{
				group.updateRestrictions();
			}
		}
	}
	
	private void addGroupOptionSilent(final GroupOptionInstance aGroupOption)
	{
		getGroupOptionsList().add(aGroupOption);
		for (final ItemGroup group : aGroupOption.getGroupOption().getGroups())
		{
			mGroupOptions.put(mGroups.get(group.getName()), aGroupOption);
		}
		Collections.sort(getGroupOptionsList());
		getContainer().addView(aGroupOption.getContainer());
	}
	
	private void addGroupSilent(final ItemGroupInstance aGroup)
	{
		getGroupsList().add(aGroup);
		mGroups.put(aGroup.getName(), aGroup);
	}
}
