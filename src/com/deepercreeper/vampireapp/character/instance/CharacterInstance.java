package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.ItemControllerInstanceImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.InstanceRestrictionImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;
import com.deepercreeper.vampireapp.lists.controllers.instances.DescriptionControllerInstance;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import android.content.Context;

/**
 * This represents an existing character that can be played, saved and loaded.
 * 
 * @author vrl
 */
public class CharacterInstance implements ItemFinder, TimeListener, Saveable
{
	private static final String TAG = "CharacterInstance";
	
	private final ItemProvider mItems;
	
	private final Context mContext;
	
	private final MessageListener mMessageListener;
	
	private final List<ItemControllerInstance> mControllers = new ArrayList<ItemControllerInstance>();
	
	private final List<TimeListener> mTimeListeners = new ArrayList<TimeListener>();
	
	private final List<InstanceRestriction> mRestrictions = new ArrayList<InstanceRestriction>();
	
	private final GenerationControllerInstance mGeneration;
	
	private final DescriptionControllerInstance mDescriptions;
	
	private final InsanityControllerInstance mInsanities;
	
	private final EPControllerInstance mEP;
	
	private final String mName;
	
	private final String mConcept;
	
	private final Nature mNature;
	
	private final Nature mBehavior;
	
	private final Clan mClan;
	
	private final HealthControllerInstance mHealth;
	
	private final MoneyControllerInstance mMoney;
	
	private final ActionsControllerInstance mActions;
	
	private final InventoryControllerInstance mInventory;
	
	private final boolean mHost;
	
	private final ResizeListener mResizeListener;
	
	private final ModeControllerInstance mMode;
	
	/**
	 * Creates a new character out of the given character creation.
	 * 
	 * @param aCreator
	 *            The character creation.
	 * @param aMessageListener
	 *            The listener for changes that occur for this character.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aControllerResizeListener
	 *            The controller resize listener.
	 * @param aHost
	 *            Whether this character is a host side character.
	 */
	public CharacterInstance(final CharacterCreation aCreator, final MessageListener aMessageListener, final ResizeListener aResizeListener,
			final ResizeListener aControllerResizeListener, final boolean aHost)
	{
		mHost = aHost;
		mResizeListener = aResizeListener;
		mItems = aCreator.getItems();
		mMessageListener = aMessageListener;
		mContext = aCreator.getContext();
		mGeneration = new GenerationControllerInstance(aCreator.getGenerationValue(), this, mHost, mMessageListener);
		mDescriptions = new DescriptionControllerInstance(aCreator.getDescriptions());
		mInsanities = new InsanityControllerInstance(aCreator.getInsanities(), getContext(), mHost, mMessageListener, mResizeListener);
		mEP = new EPControllerInstance(getContext(), mMessageListener, mHost, this);
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), getContext(), mHost, mMessageListener, mResizeListener);
		mTimeListeners.add(mInsanities);
		
		mName = aCreator.getName();
		mConcept = aCreator.getConcept();
		mNature = aCreator.getNature();
		mBehavior = aCreator.getBehavior();
		mClan = aCreator.getClan();
		
		mMode = new ModeControllerInstance(this, getContext(), mMessageListener, mHost);
		
		for (final ItemControllerCreation controller : aCreator.getControllers())
		{
			mControllers.add(new ItemControllerInstanceImpl(controller, getContext(), this, aControllerResizeListener, mMessageListener, mHost));
		}
		
		mInventory = new InventoryControllerInstance(mItems.getInventory(), this, getContext(), mResizeListener, mMessageListener, mHost);
		mHealth = new HealthControllerInstance(aCreator.getHealth(), getContext(), mMessageListener, this, mHost);
		mTimeListeners.add(mHealth);
		
		mActions = mHost ? null : new ActionsControllerInstance(this, mContext);
		
		for (final InstanceRestriction restriction : aCreator.getRestrictions())
		{
			addRestriction(restriction);
		}
		
		Log.i(TAG, "Restrictions: " + getRestrictions());
	}
	
	/**
	 * Creates a new character out of the given XML data it was saved to.
	 * 
	 * @param aXML
	 *            The XML data.
	 * @param aItems
	 *            The item provider.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The listener for changes that occur for this character.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aControllerResizeListener
	 *            The controller resize listener.
	 * @param aHost
	 *            Whether this character is a host side character.
	 * @throws IllegalArgumentException
	 *             if the XML document can't be parsed.
	 */
	public CharacterInstance(final String aXML, final ItemProvider aItems, final Context aContext, final MessageListener aMessageListener,
			final ResizeListener aResizeListener, final ResizeListener aControllerResizeListener, final boolean aHost) throws IllegalArgumentException
	{
		Log.i(TAG, "Starting to load character xml.");
		mItems = aItems;
		mContext = aContext;
		mHost = aHost;
		mMessageListener = aMessageListener;
		mResizeListener = aResizeListener;
		
		final Document doc = DataUtil.loadDocument(aXML);
		if (doc == null)
		{
			throw new IllegalArgumentException();
		}
		
		Log.i(TAG, "Finished parsing character xml.");
		
		// Root element
		final Element root = DataUtil.getElement(doc, "character");
		
		// Meta data
		final Element meta = DataUtil.getElement(root, "meta");
		mName = CodingUtil.decode(meta.getAttribute("name"));
		mConcept = CodingUtil.decode(meta.getAttribute("concept"));
		mNature = mItems.getNatures().getItemWithName(meta.getAttribute("nature"));
		mBehavior = mItems.getNatures().getItemWithName(meta.getAttribute("behavior"));
		mClan = mItems.getClans().getItemWithName(meta.getAttribute("clan"));
		mEP = new EPControllerInstance(Integer.parseInt(meta.getAttribute("ep")), getContext(), mMessageListener, mHost, this);
		
		// Mode
		mMode = new ModeControllerInstance(DataUtil.getElement(root, "mode"), this, getContext(), mMessageListener, mHost);
		
		// Generation
		mGeneration = new GenerationControllerInstance(DataUtil.getElement(root, "generation"), this, mHost, mMessageListener);
		
		// Insanities
		mInsanities = new InsanityControllerInstance(DataUtil.getElement(root, "insanities"), getContext(), mHost, mMessageListener, mResizeListener);
		mTimeListeners.add(mInsanities);
		
		// Descriptions
		mDescriptions = new DescriptionControllerInstance(DataUtil.getElement(root, "descriptions"), mItems.getDescriptions());
		
		// Controllers
		for (final Element controller : DataUtil.getChildren(DataUtil.getElement(root, "controllers"), "controller"))
		{
			mControllers.add(new ItemControllerInstanceImpl(controller, mItems, mContext, this, aControllerResizeListener, mMessageListener, mHost));
		}
		
		// Health
		mHealth = new HealthControllerInstance(DataUtil.getElement(root, "health"), mContext, mMessageListener, this, mHost);
		mTimeListeners.add(mHealth);
		
		// Money
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), DataUtil.getElement(root, "money"), getContext(), mHost, mMessageListener,
				mResizeListener);
				
		// Inventory
		mInventory = new InventoryControllerInstance(DataUtil.getElement(root, "inventory"), mItems.getInventory(), this, getContext(),
				mResizeListener, mMessageListener, mHost);
				
		// Actions
		mActions = mHost ? null : new ActionsControllerInstance(this, mContext);
		
		// Restrictions
		for (final Element restriction : DataUtil.getChildren(DataUtil.getElement(root, "restrictions"), "restriction"))
		{
			addRestriction(new InstanceRestrictionImpl(restriction));
		}
		
		Log.i(TAG, "Finished loading character.");
	}
	
	/**
	 * @return the insanities controller.
	 */
	public InsanityControllerInstance getInsanities()
	{
		return mInsanities;
	}
	
	/**
	 * @return the resize listener of this character.
	 */
	public ResizeListener getResizeListener()
	{
		return mResizeListener;
	}
	
	/**
	 * @return the message listener.
	 */
	public MessageListener getMessageListener()
	{
		return mMessageListener;
	}
	
	@Override
	public ItemGroupInstance findGroupInstance(final String aName)
	{
		for (final ItemControllerInstance controller : getControllers())
		{
			if (controller.hasGroup(aName))
			{
				return controller.getGroup(aName);
			}
		}
		return null;
	}
	
	/**
	 * @return whether this character is host sided.
	 */
	public boolean isHost()
	{
		return mHost;
	}
	
	@Override
	public Item findItem(final String aName)
	{
		for (final ItemController controller : mItems.getControllers())
		{
			final Item item = controller.getItem(aName);
			if (item != null)
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Adds a new restriction to this character.
	 * 
	 * @param aRestriction
	 *            The new restriction.
	 */
	public void addRestriction(final InstanceRestriction aRestriction)
	{
		if (aRestriction.getType() == InstanceRestrictionType.ITEM_CHILD_EP_COST_MULTI_AT)
		{
			final ItemInstance item = findItemInstance(aRestriction.getItemName());
			if (item != null)
			{
				if (item.hasChildAt(aRestriction.getIndex()))
				{
					final ItemInstance child = item.getChildAt(aRestriction.getIndex());
					final InstanceRestriction restriction = new InstanceRestrictionImpl(InstanceRestrictionType.ITEM_EP_COST_MULTI, child.getName(),
							aRestriction.getMinimum(), aRestriction.getMaximum(), aRestriction.getItems(), 0, aRestriction.getValue(),
							aRestriction.getDuration());
					child.addRestriction(restriction);
					mRestrictions.add(restriction);
					mTimeListeners.add(restriction);
				}
			}
		}
		else if (aRestriction.getType() == InstanceRestrictionType.ITEM_CHILD_EP_COST_NEW
				|| aRestriction.getType() == InstanceRestrictionType.ITEM_EP_COST
				|| aRestriction.getType() == InstanceRestrictionType.ITEM_EP_COST_MULTI
				|| aRestriction.getType() == InstanceRestrictionType.ITEM_EP_COST_NEW || aRestriction.getType() == InstanceRestrictionType.ITEM_VALUE)
		{
			final ItemInstance item = findItemInstance(aRestriction.getItemName());
			if (item != null)
			{
				item.addRestriction(aRestriction);
				mRestrictions.add(aRestriction);
				mTimeListeners.add(aRestriction);
			}
		}
	}
	
	/**
	 * @return the actions controller.
	 */
	public ActionsControllerInstance getActions()
	{
		return mActions;
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		for (final TimeListener listener : mTimeListeners)
		{
			listener.time(aType, aAmount);
		}
	}
	
	@Override
	public List<Item> getItemsList()
	{
		final List<Item> items = new ArrayList<Item>();
		for (final ItemController controller : mItems.getControllers())
		{
			items.addAll(controller.getItemsList());
		}
		return items;
	}
	
	@Override
	public ItemInstance findItemInstance(final String aName)
	{
		for (final ItemControllerInstance controller : mControllers)
		{
			if (controller.hasItem(aName))
			{
				return controller.getItem(aName);
			}
		}
		return null;
	}
	
	/**
	 * @return this characters behavior.
	 */
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	/**
	 * @return this characters clan.
	 */
	public Clan getClan()
	{
		return mClan;
	}
	
	/**
	 * @return this characters concept.
	 */
	public String getConcept()
	{
		return mConcept;
	}
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext()
	{
		return mContext;
	}
	
	/**
	 * @return a list of all item controllers.
	 */
	public List<ItemControllerInstance> getControllers()
	{
		return mControllers;
	}
	
	/**
	 * @return the current amount of experience this character has.
	 */
	public int getEP()
	{
		return mEP.getExperience();
	}
	
	/**
	 * @return the experience controller of this character.
	 */
	public EPControllerInstance getEPController()
	{
		return mEP;
	}
	
	/**
	 * @return the generation controller.
	 */
	public GenerationControllerInstance getGenerationController()
	{
		return mGeneration;
	}
	
	/**
	 * @return The current character generation.
	 */
	public int getGeneration()
	{
		return mGeneration.getGeneration();
	}
	
	/**
	 * @return the characters health controller.
	 */
	public HealthControllerInstance getHealth()
	{
		return mHealth;
	}
	
	/**
	 * @return the characters inventory controller.
	 */
	public InventoryControllerInstance getInventory()
	{
		return mInventory;
	}
	
	/**
	 * @return the characters money controller.
	 */
	public MoneyControllerInstance getMoney()
	{
		return mMoney;
	}
	
	/**
	 * @return the current character mode.
	 */
	public ModeControllerInstance getMode()
	{
		return mMode;
	}
	
	/**
	 * @return the character name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return the characters nature.
	 */
	public Nature getNature()
	{
		return mNature;
	}
	
	/**
	 * @return a list of all current character restrictions.
	 */
	public List<InstanceRestriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	/**
	 * @return whether this character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
	}
	
	/**
	 * removes the given character restriction.
	 * 
	 * @param aRestriction
	 *            The restriction to remove.
	 */
	public void removeRestriction(final InstanceRestriction aRestriction)
	{
		mRestrictions.remove(aRestriction);
		mTimeListeners.remove(aRestriction);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element root = aDoc.createElement("character");
		
		// Meta data
		final Element meta = aDoc.createElement("meta");
		meta.setAttribute("name", CodingUtil.encode(getName()));
		meta.setAttribute("concept", CodingUtil.encode(getConcept()));
		meta.setAttribute("nature", getNature().getName());
		meta.setAttribute("behavior", getBehavior().getName());
		meta.setAttribute("clan", getClan().getName());
		meta.setAttribute("ep", "" + mEP.getExperience());
		root.appendChild(meta);
		
		// Mode
		root.appendChild(mMode.asElement(aDoc));
		
		// Generation
		root.appendChild(mGeneration.asElement(aDoc));
		
		// Health
		root.appendChild(mHealth.asElement(aDoc));
		
		// Money
		root.appendChild(mMoney.asElement(aDoc));
		
		// Inventory
		root.appendChild(mInventory.asElement(aDoc));
		
		// Insanities
		root.appendChild(mInsanities.asElement(aDoc));
		
		// Descriptions
		root.appendChild(mDescriptions.asElement(aDoc));
		
		// Controllers
		final Element controllers = aDoc.createElement("controllers");
		for (final ItemControllerInstance controller : mControllers)
		{
			controllers.appendChild(controller.asElement(aDoc));
		}
		root.appendChild(controllers);
		
		// Restrictions
		final Element restrictionElement = aDoc.createElement("restrictions");
		for (final InstanceRestriction restriction : getRestrictions())
		{
			restrictionElement.appendChild(restriction.asElement(aDoc));
		}
		root.appendChild(restrictionElement);
		return root;
	}
	
	/**
	 * Updates all item controllers.
	 */
	public void updateUI()
	{
		if (mHealth != null)
		{
			mHealth.update();
		}
		if (mInventory != null)
		{
			mInventory.update();
		}
		if (mMoney != null)
		{
			mMoney.update();
		}
		if (mMessageListener != null)
		{
			mMessageListener.updateMessages();
		}
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.updateUI();
		}
		if (mActions != null)
		{
			mActions.update();
		}
	}
}
