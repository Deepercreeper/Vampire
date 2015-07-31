package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.GroupOptionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.GroupOptionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.view.Expander;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * A group option instance implementation.
 * 
 * @author vrl
 */
public class GroupOptionInstanceImpl implements GroupOptionInstance
{
	private static final String TAG = "GroupOptionInstance";
	
	private final GroupOption mGroupOption;
	
	private final Map<ItemGroup, ItemGroupInstance> mGroups = new HashMap<ItemGroup, ItemGroupInstance>();
	
	private final List<ItemGroupInstance> mGroupsList = new ArrayList<ItemGroupInstance>();
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final CharacterInstance mCharacter;
	
	private final ResizeListener mResizeListener;
	
	private final Expander mExpander;
	
	private final boolean mHost;
	
	private boolean mInitialized = false;
	
	/**
	 * Creates a new group option out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItemController
	 *            The item controller.
	 * @param aContext
	 *            The underlying context.
	 * @param aCharacter
	 *            The character.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aHost
	 *            Whether this is a host sided group option.
	 */
	public GroupOptionInstanceImpl(final Element aElement, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter, final ResizeListener aResizeListener, final boolean aHost)
	{
		mGroupOption = aItemController.getItemController().getGroupOption(aElement.getAttribute("name"));
		mContext = aContext;
		mCharacter = aCharacter;
		mResizeListener = aResizeListener;
		mHost = aHost;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.group_option_instance_view, null);
		mExpander = Expander.handle(R.id.view_group_option_instance_button, R.id.view_group_option_instance_panel, mContainer, mResizeListener);
		
		init();
		
		for (Element group : DataUtil.getChildren(aElement, "group"))
		{
			addGroupSilent(aItemController.getGroup(group.getAttribute("name")));
		}
		sortGroups();
	}
	
	/**
	 * Creates a new group option out of the given group option creation.
	 * 
	 * @param aGroupOption
	 *            The group option creation.
	 * @param aItemController
	 *            The item controller.
	 * @param aContext
	 *            The underlying context.
	 * @param aCharacter
	 *            The character.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aHost
	 *            Whether this is a host sided group option.
	 */
	public GroupOptionInstanceImpl(final GroupOptionCreation aGroupOption, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter, final ResizeListener aResizeListener, final boolean aHost)
	{
		mGroupOption = aGroupOption.getGroupOption();
		mContext = aContext;
		mCharacter = aCharacter;
		mResizeListener = aResizeListener;
		mHost = aHost;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.group_option_instance_view, null);
		mExpander = Expander.handle(R.id.view_group_option_instance_button, R.id.view_group_option_instance_panel, mContainer, mResizeListener);
		
		init();
		
		for (final ItemGroupCreation group : aGroupOption.getGroupsList())
		{
			addGroupSilent(aItemController.getGroup(group.getItemGroup()));
		}
		sortGroups();
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
	public int compareTo(final GroupOptionInstance aAnother)
	{
		if (aAnother == null)
		{
			return getGroupOption().compareTo(null);
		}
		return getGroupOption().compareTo(aAnother.getGroupOption());
	}
	
	@Override
	public void close()
	{
		mExpander.close();
	}
	
	@Override
	public void resize()
	{
		mExpander.resize();
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
	public boolean hasMutableGroup()
	{
		if (getGroupsList().isEmpty())
		{
			return false;
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			if (group.isMutable())
			{
				return true;
			}
		}
		return false;
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
			if ( !group.getItemsList().isEmpty())
			{
				return true;
			}
		}
		return false;
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
			mExpander.init();
			mExpander.getButton().setText(getGroupOption().getDisplayName());
			
			mInitialized = true;
		}
		
		sortGroups();
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
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		ViewUtil.setEnabled(mExpander.getButton(), aEnabled);
		if ( !aEnabled && mExpander.isOpen())
		{
			mExpander.close();
		}
	}
	
	@Override
	public String toString()
	{
		return getGroupOption().getDisplayName();
	}
	
	@Override
	public void updateGroups()
	{
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.updateItems();
		}
		setEnabled(hasAnyItem() || mHost && hasMutableGroup());
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
		mExpander.getContainer().addView(aGroup.getContainer());
	}
	
	private void sortGroups()
	{
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.release();
		}
		Collections.sort(getGroupsList());
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.init();
			mExpander.getContainer().addView(group.getContainer());
		}
	}
}
