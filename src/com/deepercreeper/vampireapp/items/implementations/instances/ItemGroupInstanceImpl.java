package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.InstanceRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class ItemGroupInstanceImpl extends InstanceRestrictionableImpl implements ItemGroupInstance
{
	private static final String				TAG				= "ItemGroupInstance";
	
	private final ItemGroup					mItemGroup;
	
	private final LinearLayout				mContainer;
	
	private final Context					mContext;
	
	private final ItemControllerInstance	mItemController;
	
	private final List<ItemInstance>		mItemsList		= new ArrayList<ItemInstance>();
	
	private final Map<Item, ItemInstance>	mItems			= new HashMap<Item, ItemInstance>();
	
	private final TextView					mTitleText;
	
	private final CharacterInstance			mCharacter;
	
	private boolean							mInitialized	= false;
	
	private Mode							mMode;
	
	private final EPHandler					mEP;
	
	public ItemGroupInstanceImpl(final Element aElement, final ItemControllerInstance aItemController, final Context aContext, final Mode aMode,
			final EPHandler aEP, final CharacterInstance aCharacter)
	{
		mItemGroup = aItemController.getItemController().getGroup(aElement.getAttribute("name"));
		mItemController = aItemController;
		mContext = aContext;
		setController(mItemController);
		mMode = aMode;
		mEP = aEP;
		mCharacter = aCharacter;
		mContainer = new LinearLayout(getContext());
		mTitleText = new TextView(getContext());
		
		init();
		
		final NodeList children = aElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if (children.item(i) instanceof Element)
			{
				final Element item = (Element) children.item(i);
				if ( !item.getTagName().equals("item"))
				{
					continue;
				}
				addItemSilent(new ItemInstanceImpl(item, this, getContext(), getMode(), getEP(), null, getCharacter()));
			}
		}
	}
	
	public ItemGroupInstanceImpl(final ItemGroupCreation aItemGroup, final ItemControllerInstance aItemController, final Context aContext,
			final Mode aMode, final EPHandler aEP, final CharacterInstance aCharacter)
	{
		mItemGroup = aItemGroup.getItemGroup();
		mContext = aContext;
		mItemController = aItemController;
		setController(mItemController);
		mMode = aMode;
		mEP = aEP;
		mCharacter = aCharacter;
		mContainer = new LinearLayout(getContext());
		mTitleText = new TextView(getContext());
		
		init();
		
		for (final ItemCreation item : aItemGroup.getItemsList())
		{
			if ( !getItemGroup().isMutable() || item.isImportant())
			{
				addItemSilent(new ItemInstanceImpl(item, this, getMode(), getEP(), null, getCharacter()));
			}
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element group = aDoc.createElement("group");
		group.setAttribute("name", getName());
		for (final ItemInstance item : getItemsList())
		{
			group.appendChild(item.asElement(aDoc));
		}
		return group;
	}
	
	@Override
	public int compareTo(final ItemGroupInstance aAnother)
	{
		if (aAnother == null)
		{
			return getItemGroup().compareTo(null);
		}
		return getItemGroup().compareTo(aAnother.getItemGroup());
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
	public List<ItemInstance> getDescriptionItems()
	{
		final List<ItemInstance> items = new ArrayList<ItemInstance>();
		for (final ItemInstance item : getItemsList())
		{
			if (item.hasDescription())
			{
				items.add(item);
			}
			if (item.isParent())
			{
				items.addAll(item.getDescriptionItems());
			}
		}
		return items;
	}
	
	@Override
	public EPHandler getEP()
	{
		return mEP;
	}
	
	@Override
	public ItemInstance getItem(final Item aItem)
	{
		return mItems.get(aItem);
	}
	
	@Override
	public ItemInstance getItem(final String aName)
	{
		return getItem(getItemGroup().getItem(aName));
	}
	
	@Override
	public ItemControllerInstance getItemController()
	{
		return mItemController;
	}
	
	@Override
	public ItemGroup getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public List<ItemInstance> getItemsList()
	{
		return mItemsList;
	}
	
	@Override
	public Mode getMode()
	{
		return mMode;
	}
	
	@Override
	public String getName()
	{
		return getItemGroup().getName();
	}
	
	@Override
	public int getValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the value of a non value group.");
			return 0;
		}
		int value = 0;
		for (final ItemInstance item : getItemsList())
		{
			value += item.getAllValues();
		}
		return value;
	}
	
	@Override
	public boolean hasItem(final Item aItem)
	{
		return mItems.containsKey(aItem);
	}
	
	@Override
	public boolean hasItem(final ItemInstance aItem)
	{
		return mItems.containsKey(aItem.getItem());
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		if (getItemGroup().hasItem(aName))
		{
			return hasItem(getItemGroup().getItem(aName));
		}
		return false;
	}
	
	@Override
	public int indexOfItem(final ItemInstance aItem)
	{
		return getItemsList().indexOf(aItem);
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mTitleText.setLayoutParams(ViewUtil.getWrapHeight());
			mTitleText.setText(getItemGroup().getDisplayName());
			mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			mTitleText.setGravity(Gravity.CENTER);
		}
		getContainer().addView(mTitleText);
		
		for (final ItemInstance item : getItemsList())
		{
			item.init();
			getContainer().addView(item.getContainer());
		}
		mInitialized = true;
	}
	
	@Override
	public boolean isValueGroup()
	{
		return getItemGroup().isValueGroup();
	}
	
	@Override
	public void release()
	{
		for (final ItemInstance item : getItemsList())
		{
			item.release();
		}
		ViewUtil.release(getContainer());
		ViewUtil.release(mTitleText);
	}
	
	@Override
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemInstance item : getItemsList())
		{
			item.setMode(aMode);
		}
	}
	
	@Override
	public String toString()
	{
		return getItemGroup().getDisplayName() + ": " + getValue();
	}
	
	@Override
	public void updateController()
	{
		getItemController().updateGroups();
	}
	
	@Override
	public void updateItems()
	{
		for (final ItemInstance item : getItemsList())
		{
			if (item.isValueItem() || item.isParent())
			{
				item.updateButtons();
			}
		}
	}
	
	@Override
	public void updateRestrictions()
	{
		// TODO Implement
	}
	
	private void addItemSilent(final ItemInstance aItem)
	{
		if (getItemsList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a child to a group twice.");
			return;
		}
		getItemsList().add(aItem);
		mItems.put(aItem.getItem(), aItem);
		getContainer().addView(aItem.getContainer());
		getItemController().resize();
		updateController();
	}
}
