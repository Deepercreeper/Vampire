package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.character.instance.controllers.ActionsControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.EPControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.GenerationControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.HealthControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.InsanityControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.InventoryControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.ModeControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.MoneyControllerInstance;
import com.deepercreeper.vampireapp.character.instance.controllers.RestrictionControllerInstance;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.ItemControllerInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionControllerInstance;
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
	
	private final RestrictionControllerInstance mRestrictions;
	
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
	
	private boolean mOnline = false;
	
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
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), getContext(), mHost, this, mMessageListener, mResizeListener);
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
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.initActions();
		}
		
		mInventory = new InventoryControllerInstance(mItems.getInventory(), this, getContext(), mResizeListener, mMessageListener, mHost);
		mHealth = new HealthControllerInstance(aCreator.getHealth(), getContext(), mMessageListener, this, mHost);
		mTimeListeners.add(mHealth);
		
		mActions = mHost ? null : new ActionsControllerInstance(this, mItems.getActions(), mContext, mMessageListener);
		
		mRestrictions = new RestrictionControllerInstance(this, getContext(), mResizeListener, mMessageListener, mHost);
		for (final RestrictionInstance restriction : aCreator.getRestrictions(mMessageListener))
		{
			mRestrictions.addRestriction(restriction, true);
		}
		mTimeListeners.add(mRestrictions);
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
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.initActions();
		}
		
		// Health
		mHealth = new HealthControllerInstance(DataUtil.getElement(root, "health"), mContext, mMessageListener, this, mHost);
		mTimeListeners.add(mHealth);
		
		// Money
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), DataUtil.getElement(root, "money"), getContext(), mHost, this, mMessageListener,
				mResizeListener);
				
		// Inventory
		mInventory = new InventoryControllerInstance(DataUtil.getElement(root, "inventory"), mItems.getInventory(), this, getContext(),
				mResizeListener, mMessageListener, mHost);
				
		// Actions
		mActions = mHost ? null : new ActionsControllerInstance(this, mItems.getActions(), mContext, mMessageListener);
		
		// Restrictions
		mRestrictions = new RestrictionControllerInstance(DataUtil.getElement(root, "restrictions"), this, getContext(), mResizeListener,
				mMessageListener, mHost);
		mTimeListeners.add(mRestrictions);
		
		Log.i(TAG, "Finished loading character.");
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
		root.appendChild(mRestrictions.asElement(aDoc));
		return root;
	}
	
	/**
	 * Sets whether this character is online or just for viewing.
	 * 
	 * @param aOnline
	 *            The online state.
	 */
	public void setOnline(final boolean aOnline)
	{
		mOnline = aOnline;
		updateUI();
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
	 * @return the actions controller.
	 */
	public ActionsControllerInstance getActions()
	{
		return mActions;
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
	 * @return The current character generation.
	 */
	public int getGeneration()
	{
		return mGeneration.getGeneration();
	}
	
	/**
	 * @return the generation controller.
	 */
	public GenerationControllerInstance getGenerationController()
	{
		return mGeneration;
	}
	
	/**
	 * @return the characters health controller.
	 */
	public HealthControllerInstance getHealth()
	{
		return mHealth;
	}
	
	/**
	 * @return the insanities controller.
	 */
	public InsanityControllerInstance getInsanities()
	{
		return mInsanities;
	}
	
	/**
	 * @return the characters inventory controller.
	 */
	public InventoryControllerInstance getInventory()
	{
		return mInventory;
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
	
	/**
	 * @return the message listener.
	 */
	public MessageListener getMessageListener()
	{
		return mMessageListener;
	}
	
	/**
	 * @return the current character mode.
	 */
	public ModeControllerInstance getMode()
	{
		return mMode;
	}
	
	/**
	 * @return the characters money controller.
	 */
	public MoneyControllerInstance getMoney()
	{
		return mMoney;
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
	 * @return the resize listener of this character.
	 */
	public ResizeListener getResizeListener()
	{
		return mResizeListener;
	}
	
	/**
	 * @return the restriction controller.
	 */
	public RestrictionControllerInstance getRestrictions()
	{
		return mRestrictions;
	}
	
	/**
	 * @return whether this character is host sided.
	 */
	public boolean isHost()
	{
		return mHost;
	}
	
	/**
	 * @return whether this character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		for (final TimeListener listener : mTimeListeners)
		{
			listener.time(aType, aAmount);
		}
	}
	
	/**
	 * @return whether this character is online.
	 */
	public boolean isOnline()
	{
		return mOnline;
	}
	
	/**
	 * Updates all item controllers.
	 */
	public void updateUI()
	{
		if (mHealth != null)
		{
			mHealth.updateUI();
		}
		if (mInventory != null)
		{
			mInventory.updateUI();
		}
		if (mMoney != null)
		{
			mMoney.updateUI();
		}
		if (mMode != null)
		{
			mMode.updateUI();
		}
		if (mMessageListener != null)
		{
			mMessageListener.updateMessages();
		}
		if (mGeneration != null)
		{
			mGeneration.updateUI();
		}
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.updateUI();
		}
		if (mActions != null)
		{
			mActions.updateUI();
		}
		if (mRestrictions != null)
		{
			mRestrictions.updateUI();
		}
	}
}
