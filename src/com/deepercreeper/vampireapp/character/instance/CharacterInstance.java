package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.controllers.InventoryController;
import com.deepercreeper.vampireapp.character.controllers.MoneyController;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
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
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;

public class CharacterInstance implements ItemFinder
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
	
	private final EPController					mEP;
	
	private final String						mName;
	
	private final String						mConcept;
	
	private final Nature						mNature;
	
	private final Nature						mBehavior;
	
	private final Clan							mClan;
	
	private final HealthControllerInstance		mHealth;
	
	private final MoneyController				mMoney;
	
	private final InventoryController			mInventory;
	
	private Mode								mMode;
	
	public CharacterInstance(final CharacterCreation aCreator)
	{
		mItems = aCreator.getItems();
		mContext = aCreator.getContext();
		mGeneration = new GenerationControllerInstance(aCreator.getGenerationValue(), this);
		mDescriptions = new DescriptionControllerInstance(aCreator.getDescriptions());
		mInsanities = new InsanityControllerInstance(aCreator.getInsanities());
		mEP = new EPController(getContext());
		mMoney = new MoneyController(mItems.getMoney(), getContext());
		
		mName = aCreator.getName();
		mConcept = aCreator.getConcept();
		mNature = aCreator.getNature();
		mBehavior = aCreator.getBehavior();
		mClan = aCreator.getClan();
		
		mMode = Mode.DEFAULT;
		
		for (final ItemControllerCreation controller : aCreator.getControllers())
		{
			mControllers.add(new ItemControllerInstanceImpl(controller, getContext(), mMode, mEP, this));
		}
		
		mInventory = new InventoryController(mItems.getInventory(), this, mContext);
		mHealth = new HealthControllerInstance(aCreator.getHealth(), getContext(), this);
		
		for (final InstanceRestriction restriction : aCreator.getRestrictions())
		{
			addRestriction(restriction);
		}
		
		Log.i(TAG, "Restrictions: " + getRestrictions());
	}
	
	public CharacterInstance(final String aXML, final ItemProvider aItems, final Context aContext) throws IllegalArgumentException
	{
		Log.i(TAG, "Starting to load character xml.");
		mItems = aItems;
		mContext = aContext;
		
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
		mGeneration = new GenerationControllerInstance(Integer.parseInt(meta.getAttribute("generation")), this);
		mClan = mItems.getClans().getItemWithName(meta.getAttribute("clan"));
		mEP = new EPController(Integer.parseInt(meta.getAttribute("ep")), getContext());
		mMode = Mode.valueOf(meta.getAttribute("mode"));
		
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
					mControllers.add(new ItemControllerInstanceImpl(controller, mItems, mContext, mMode, mEP, this));
				}
			}
		}
		
		// Health
		mHealth = new HealthControllerInstance((Element) root.getElementsByTagName("health").item(0), mContext, this);
		mTimeListeners.add(mHealth);
		
		// Money
		mMoney = new MoneyController(mItems.getMoney(), (Element) root.getElementsByTagName("money").item(0), getContext());
		
		// Inventory
		mInventory = new InventoryController((Element) root.getElementsByTagName("inventory").item(0), mItems.getInventory(), this, getContext());
		
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
	
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	public Clan getClan()
	{
		return mClan;
	}
	
	public String getConcept()
	{
		return mConcept;
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	public List<ItemControllerInstance> getControllers()
	{
		return mControllers;
	}
	
	public int getEP()
	{
		return mEP.getExperience();
	}
	
	public EPController getEPHandler()
	{
		return mEP;
	}
	
	public int getGeneration()
	{
		return mGeneration.getGeneration();
	}
	
	public HealthControllerInstance getHealth()
	{
		return mHealth;
	}
	
	public InventoryController getInventory()
	{
		return mInventory;
	}
	
	public MoneyController getMoney()
	{
		return mMoney;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public Nature getNature()
	{
		return mNature;
	}
	
	public List<InstanceRestriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	public void init()
	{
		mEP.init();
		mHealth.init();
		mMoney.init();
		mInventory.init();
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.init();
		}
	}
	
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
	}
	
	public void release()
	{
		mEP.release();
		mHealth.release();
		mMoney.release();
		mInventory.release();
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.release();
		}
	}
	
	public void removeRestriction(final InstanceRestriction aRestriction)
	{
		mRestrictions.remove(aRestriction);
		mTimeListeners.remove(aRestriction);
	}
	
	public String serialize()
	{
		final Document doc = FilesUtil.createDocument();
		if (doc == null)
		{
			return null;
		}
		
		// Root element
		final Element root = doc.createElement("character");
		doc.appendChild(root);
		
		// Meta data
		final Element meta = doc.createElement("meta");
		meta.setAttribute("name", CodingUtil.encode(getName()));
		meta.setAttribute("concept", CodingUtil.encode(getConcept()));
		meta.setAttribute("nature", getNature().getName());
		meta.setAttribute("behavior", getBehavior().getName());
		meta.setAttribute("generation", "" + mGeneration.getGeneration());
		meta.setAttribute("clan", getClan().getName());
		meta.setAttribute("ep", "" + mEP.getExperience());
		meta.setAttribute("mode", mMode.name());
		root.appendChild(meta);
		
		// Health
		root.appendChild(mHealth.asElement(doc));
		
		// Money
		root.appendChild(mMoney.asElement(doc));
		
		// Inventory
		root.appendChild(mInventory.asElement(doc));
		
		// Insanities
		root.appendChild(mInsanities.asElement(doc));
		
		// Descriptions
		root.appendChild(mDescriptions.asElement(doc));
		
		// Controllers
		final Element controllers = doc.createElement("controllers");
		for (final ItemControllerInstance controller : mControllers)
		{
			controllers.appendChild(controller.asElement(doc));
		}
		root.appendChild(controllers);
		
		// Restrictions
		final Element restrictionElement = doc.createElement("restrictions");
		for (final InstanceRestriction restriction : getRestrictions())
		{
			restrictionElement.appendChild(restriction.asElement(doc));
		}
		root.appendChild(restrictionElement);
		
		return FilesUtil.readDocument(doc);
	}
	
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemControllerInstance controller : mControllers)
		{
			controller.setMode(mMode);
		}
	}
	
	public void update()
	{
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.updateGroups();
		}
	}
}