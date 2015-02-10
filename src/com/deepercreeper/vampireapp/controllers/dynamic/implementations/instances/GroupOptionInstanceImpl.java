package com.deepercreeper.vampireapp.controllers.dynamic.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.GroupOptionCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.GroupOptionInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class GroupOptionInstanceImpl implements GroupOptionInstance
{
	private static final String						TAG				= "GroupOptionInstance";
	
	private final GroupOption						mGroupOption;
	
	private final Map<ItemGroup, ItemGroupInstance>	mGroups			= new HashMap<ItemGroup, ItemGroupInstance>();
	
	private final List<ItemGroupInstance>			mGroupsList		= new ArrayList<ItemGroupInstance>();
	
	private final Context							mContext;
	
	private final LinearLayout						mContainer;
	
	private final LinearLayout						mGroupContainer;
	
	private final Button							mGroupButton;
	
	private final CharacterInstance					mCharacter;
	
	private boolean									mInitialized	= false;
	
	private boolean									mOpen			= false;
	
	public GroupOptionInstanceImpl(final Element aElement, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter)
	{
		mGroupOption = aItemController.getItemController().getGroupOption(aElement.getAttribute("name"));
		mContext = aContext;
		mContainer = new LinearLayout(getContext());
		mGroupContainer = new LinearLayout(getContext());
		mGroupButton = new Button(getContext());
		mCharacter = aCharacter;
		
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
				addGroupSilent(aItemController.getGroup(group.getAttribute("name")));
			}
		}
	}
	
	public GroupOptionInstanceImpl(final GroupOptionCreation aGroupOption, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter)
	{
		mGroupOption = aGroupOption.getGroupOption();
		mContext = aContext;
		mContainer = new LinearLayout(getContext());
		mGroupContainer = new LinearLayout(getContext());
		mGroupButton = new Button(getContext());
		mCharacter = aCharacter;
		
		init();
		
		for (final ItemGroupCreation group : aGroupOption.getGroupsList())
		{
			addGroupSilent(aItemController.getGroup(group.getItemGroup()));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element groupOption = aDoc.createElement("group-option");
		groupOption.setAttribute("name", getName());
		for (final ItemGroupInstance group : getGroupsList())
		{
			final Element groupElement = aDoc.createElement("group");
			groupElement.setAttribute("name", group.getName());
			groupOption.appendChild(groupElement);
		}
		return groupOption;
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
	}
	
	@Override
	public void close()
	{
		mOpen = false;
		mGroupButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public int compareTo(final GroupOptionInstance aAnother)
	{
		if (aAnother == null)
		{
			return getGroupOption().compareTo(null);
		}
		return getGroupOption().compareTo(aAnother.getGroupOption());
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public ItemGroupInstance getGroup(final ItemGroup aGroup)
	{
		return mGroups.get(aGroup);
	}
	
	@Override
	public GroupOption getGroupOption()
	{
		return mGroupOption;
	}
	
	@Override
	public List<ItemGroupInstance> getGroupsList()
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
		return getGroupOption().hasGroup(aGroup);
	}
	
	@Override
	public boolean hasGroup(final ItemGroupInstance aGroup)
	{
		return getGroupOption().hasGroup(aGroup.getName());
	}
	
	@Override
	public boolean hasGroup(final String aName)
	{
		return getGroupOption().hasGroup(aName);
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mGroupButton.setLayoutParams(ViewUtil.getWrapHeight());
			mGroupButton.setText(getGroupOption().getDisplayName());
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
		
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.init();
			mGroupContainer.addView(group.getContainer());
		}
		mInitialized = true;
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
	public void release()
	{
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.release();
		}
		ViewUtil.release(getContainer());
		ViewUtil.release(mGroupButton);
		ViewUtil.release(mGroupContainer);
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
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.updateItems();
			if ( !group.getItemsList().isEmpty())
			{
				hasAnyItem = true;
			}
		}
		setEnabled(hasAnyItem);
	}
	
	@Override
	public String toString()
	{
		return getGroupOption().getDisplayName();
	}
	
	private void addGroupSilent(final ItemGroupInstance aGroup)
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
}
