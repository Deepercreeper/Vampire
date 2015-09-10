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
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependableInstanceImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependencyInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.view.Expander;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * An item controller implementation.
 * 
 * @author vrl
 */
public class ItemControllerInstanceImpl extends DependableInstanceImpl implements ItemControllerInstance
{
	private static final String TAG = "ItemControllerInstance";
	
	private final ItemController mItemController;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final List<ItemGroupInstance> mGroupsList = new ArrayList<ItemGroupInstance>();
	
	private final Map<String, ItemGroupInstance> mGroups = new HashMap<String, ItemGroupInstance>();
	
	private final Map<String, ItemInstance> mItems = new HashMap<String, ItemInstance>();
	
	private final CharacterInstance mChar;
	
	private final ResizeListener mResizeListener;
	
	private final Expander mExpander;
	
	private final boolean mHost;
	
	/**
	 * Creates a new item controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItems
	 *            The item provider.
	 * @param aContext
	 *            the underlying context.
	 * @param aCharacter
	 *            The character
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public ItemControllerInstanceImpl(final Element aElement, final ItemProvider aItems, final Context aContext, final CharacterInstance aCharacter,
			final ResizeListener aResizeListener, final MessageListener aMessageListener, final boolean aHost)
	{
		mItemController = aItems.getController(aElement.getAttribute("name"));
		mContext = aContext;
		mResizeListener = aResizeListener;
		mHost = aHost;
		mChar = aCharacter;
		
		for (final Dependency dependency : getItemController().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, aCharacter));
			}
		}
		
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_controller_instance, null);
		mExpander = Expander.handle(R.id.view_controller_instance_button, R.id.view_controller_instance_panel, mContainer, mResizeListener);
		
		mExpander.init();
		mExpander.getButton().setText(getItemController().getDisplayName());
		
		for (final Element group : DataUtil.getChildren(aElement, "group"))
		{
			addGroupSilent(new ItemGroupInstanceImpl(group, this, getContext(), getCharacter(), aMessageListener, mHost));
		}
		sortGroups();
	}
	
	/**
	 * Creates a new item controller out of the given item controller creation.
	 * 
	 * @param aItemController
	 *            The item controller creation.
	 * @param aContext
	 *            The underlying context.
	 * @param aChar
	 *            The parent character.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public ItemControllerInstanceImpl(final ItemControllerCreation aItemController, final Context aContext, final CharacterInstance aChar,
			final ResizeListener aResizeListener, final MessageListener aMessageListener, final boolean aHost)
	{
		mItemController = aItemController.getItemController();
		mContext = aContext;
		mResizeListener = aResizeListener;
		mHost = aHost;
		mChar = aChar;
		
		for (final Dependency dependency : getItemController().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, aChar));
			}
		}
		
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_controller_instance, null);
		mExpander = Expander.handle(R.id.view_controller_instance_button, R.id.view_controller_instance_panel, mContainer, mResizeListener);
		
		mExpander.init();
		mExpander.getButton().setText(getItemController().getDisplayName());
		
		for (final ItemGroupCreation group : aItemController.getGroupsList())
		{
			addGroupSilent(new ItemGroupInstanceImpl(group, this, getContext(), getCharacter(), aMessageListener, mHost));
		}
		sortGroups();
	}
	
	@Override
	public void addItem(final ItemInstance aItem)
	{
		mItems.put(aItem.getName(), aItem);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element controller = aDoc.createElement("controller");
		controller.setAttribute("name", getName());
		for (final ItemGroupInstance group : getGroupsList())
		{
			controller.appendChild(group.asElement(aDoc));
		}
		return controller;
	}
	
	@Override
	public void close()
	{
		mExpander.close();
	}
	
	public void initActions()
	{
		for (final ItemInstance item : mItems.values())
		{
			item.initActions();
		}
	}
	
	@Override
	public Set<ActionInstance> getActions()
	{
		final Set<ActionInstance> actions = new HashSet<ActionInstance>();
		for (final ItemInstance item : mItems.values())
		{
			actions.addAll(item.getActions());
		}
		return actions;
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mChar;
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
	public String getName()
	{
		return getItemController().getName();
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
	public boolean isEmpty()
	{
		if (getGroupsList().isEmpty())
		{
			return true;
		}
		for (final ItemGroupInstance group : getGroupsList())
		{
			if ( !group.getItemsList().isEmpty())
			{
				return false;
			}
		}
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
			for (final RestrictionInstance restriction : mItems.get(aName).getRestrictions())
			{
				restriction.clear();
				// MARK What to do with inactive restrictions?
			}
		}
		mItems.remove(aName);
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
			mExpander.close();
		}
	}
	
	@Override
	public void updateUI()
	{
		for (final ItemGroupInstance group : getGroupsList())
		{
			group.updateUI();
		}
		setEnabled( !isEmpty() || mHost && hasMutableGroup());
	}
	
	private void addGroupSilent(final ItemGroupInstance aGroup)
	{
		if (getGroupsList().contains(aGroup))
		{
			Log.w(TAG, "Tried to add a group twice.");
			return;
		}
		mGroups.put(aGroup.getName(), aGroup);
		getGroupsList().add(aGroup);
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
			mExpander.getContainer().addView(group.getContainer());
		}
	}
}
