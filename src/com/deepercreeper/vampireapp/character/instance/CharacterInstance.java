package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.ItemControllerInstanceImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.InstanceRestrictionImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;
import com.deepercreeper.vampireapp.lists.controllers.instances.DescriptionControllerInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.GenerationControllerInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.InsanityControllerInstance;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;

/**
 * This represents an existing character that can be played, saved and loaded.
 * 
 * @author vrl
 */
public class CharacterInstance implements ItemFinder, TimeListener, Saveable
{
	private static final String					TAG				= "CharacterInstance";
	
	private final ItemProvider					mItems;
	
	private final Context						mContext;
	
	private final List<ItemControllerInstance>	mControllers	= new ArrayList<ItemControllerInstance>();
	
	private final List<TimeListener>			mTimeListeners	= new ArrayList<TimeListener>();
	
	private final List<InstanceRestriction>		mRestrictions	= new ArrayList<InstanceRestriction>();
	
	private final GenerationControllerInstance	mGeneration;
	
	private final DescriptionControllerInstance	mDescriptions;
	
	private final InsanityControllerInstance	mInsanities;
	
	private final EPControllerInstance			mEP;
	
	private final String						mName;
	
	private final String						mConcept;
	
	private final Nature						mNature;
	
	private final Nature						mBehavior;
	
	private final Clan							mClan;
	
	private final HealthControllerInstance		mHealth;
	
	private final MoneyControllerInstance		mMoney;
	
	private final InventoryControllerInstance	mInventory;
	
	private final boolean						mHost;
	
	private final ResizeListener				mResizeListener;
	
	private Mode								mMode;
	
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
		mContext = aCreator.getContext();
		mGeneration = new GenerationControllerInstance(aCreator.getGenerationValue(), this, mHost, aMessageListener);
		mDescriptions = new DescriptionControllerInstance(aCreator.getDescriptions());
		mInsanities = new InsanityControllerInstance(aCreator.getInsanities());
		mEP = new EPControllerInstance(getContext(), aMessageListener, mHost, this);
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), getContext(), mHost, aMessageListener, mResizeListener, this);
		mTimeListeners.add(mInsanities);
		
		mName = aCreator.getName();
		mConcept = aCreator.getConcept();
		mNature = aCreator.getNature();
		mBehavior = aCreator.getBehavior();
		mClan = aCreator.getClan();
		
		mMode = Mode.DEFAULT;
		
		for (final ItemControllerCreation controller : aCreator.getControllers())
		{
			mControllers.add(new ItemControllerInstanceImpl(controller, getContext(), mMode, mEP, this, aControllerResizeListener, aMessageListener,
					mHost));
		}
		
		mInventory = new InventoryControllerInstance(mItems.getInventory(), this, mContext, mResizeListener, aMessageListener, this, mHost);
		mHealth = new HealthControllerInstance(aCreator.getHealth(), getContext(), this, aMessageListener, mHost);
		mTimeListeners.add(mHealth);
		
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
			final ResizeListener aResizeListener, final ResizeListener aControllerResizeListener, final boolean aHost)
			throws IllegalArgumentException
	{
		Log.i(TAG, "Starting to load character xml.");
		mItems = aItems;
		mContext = aContext;
		mHost = aHost;
		mResizeListener = aResizeListener;
		
		final Document doc = FilesUtil.loadDocument(aXML);
		if (doc == null)
		{
			throw new IllegalArgumentException();
		}
		
		Log.i(TAG, "Finished parsing character xml.");
		
		// Root element
		final Element root = (Element) doc.getElementsByTagName("character").item(0);
		
		// Meta data
		final Element meta = (Element) root.getElementsByTagName("meta").item(0);
		mName = CodingUtil.decode(meta.getAttribute("name"));
		mConcept = CodingUtil.decode(meta.getAttribute("concept"));
		mNature = mItems.getNatures().getItemWithName(meta.getAttribute("nature"));
		mBehavior = mItems.getNatures().getItemWithName(meta.getAttribute("behavior"));
		mClan = mItems.getClans().getItemWithName(meta.getAttribute("clan"));
		mEP = new EPControllerInstance(Integer.parseInt(meta.getAttribute("ep")), getContext(), aMessageListener, mHost, this);
		mMode = Mode.valueOf(meta.getAttribute("mode"));
		
		// Generation
		mGeneration = new GenerationControllerInstance((Element) root.getElementsByTagName("generation").item(0), this, mHost, aMessageListener);
		
		// Insanities
		mInsanities = new InsanityControllerInstance((Element) root.getElementsByTagName("insanities").item(0));
		mTimeListeners.add(mInsanities);
		
		// Descriptions
		mDescriptions = new DescriptionControllerInstance((Element) root.getElementsByTagName("descriptions").item(0), mItems.getDescriptions());
		
		// Controllers
		final Element controllers = (Element) root.getElementsByTagName("controllers").item(0);
		for (int i = 0; i < controllers.getChildNodes().getLength(); i++ )
		{
			if (controllers.getChildNodes().item(i) instanceof Element)
			{
				final Element controller = (Element) controllers.getChildNodes().item(i);
				if (controller.getTagName().equals("controller"))
				{
					mControllers.add(new ItemControllerInstanceImpl(controller, mItems, mContext, mMode, mEP, this, aControllerResizeListener,
							aMessageListener, mHost));
				}
			}
		}
		
		// Health
		mHealth = new HealthControllerInstance((Element) root.getElementsByTagName("health").item(0), mContext, this, aMessageListener, mHost);
		mTimeListeners.add(mHealth);
		
		// Money
		mMoney = new MoneyControllerInstance(mItems.getCurrency(), (Element) root.getElementsByTagName("money").item(0), getContext(), mHost,
				aMessageListener, mResizeListener, this);
		
		// Inventory
		mInventory = new InventoryControllerInstance((Element) root.getElementsByTagName("inventory").item(0), mItems.getInventory(), this,
				getContext(), mResizeListener, aMessageListener, this, mHost);
		
		// Restrictions
		final Element restrictions = (Element) root.getElementsByTagName("restrictions").item(0);
		final NodeList restrictionsList = restrictions.getChildNodes();
		for (int i = 0; i < restrictionsList.getLength(); i++ )
		{
			if (restrictionsList.item(i) instanceof Element)
			{
				final Element restriction = (Element) restrictionsList.item(i);
				if (restriction.getTagName().equals("restriction"))
				{
					addRestriction(new InstanceRestrictionImpl(restriction));
				}
			}
		}
		
		Log.i(TAG, "Finished loading character.");
	}
	
	/**
	 * @return the resize listener of this character.
	 */
	public ResizeListener getResizeListener()
	{
		return mResizeListener;
	}
	
	/**
	 * @return whether this character is host sided.
	 */
	public boolean isHost()
	{
		return mHost;
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
			final ItemInstance item = findItem(aRestriction.getItemName());
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
			final ItemInstance item = findItem(aRestriction.getItemName());
			if (item != null)
			{
				item.addRestriction(aRestriction);
				mRestrictions.add(aRestriction);
				mTimeListeners.add(aRestriction);
			}
		}
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
	public ItemInstance findItem(final String aName)
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
		meta.setAttribute("mode", mMode.name());
		root.appendChild(meta);
		
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
	 * Sets the characters current mode to the given one.
	 * 
	 * @param aMode
	 *            The mode.
	 */
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemControllerInstance controller : mControllers)
		{
			controller.setMode(mMode);
		}
	}
	
	/**
	 * Updates all item controllers.
	 */
	public void update()
	{
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.updateGroups();
		}
	}
}
