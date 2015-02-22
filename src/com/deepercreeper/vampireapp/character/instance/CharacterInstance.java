package com.deepercreeper.vampireapp.character.instance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.controllers.InventoryController;
import com.deepercreeper.vampireapp.character.controllers.MoneyController;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.ItemControllerInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.DescriptionInstanceController;
import com.deepercreeper.vampireapp.lists.controllers.instances.GenerationControllerInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.InsanityControllerInstance;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.Type;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.Log;

public class CharacterInstance implements ItemFinder
{
	private static final String					TAG				= "CharacterInstance";
	
	private final ItemProvider					mItems;
	
	private final Context						mContext;
	
	private final List<ItemControllerInstance>	mControllers	= new ArrayList<ItemControllerInstance>();
	
	private final List<TimeListener>			mTimeListeners	= new ArrayList<TimeListener>();
	
	private final GenerationControllerInstance	mGeneration;
	
	private final DescriptionInstanceController	mDescriptions;
	
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
		mDescriptions = new DescriptionInstanceController(aCreator.getDescriptions());
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
	}
	
	public CharacterInstance(final String aXML, final ItemProvider aItems, final Context aContext) throws IOException
	{
		Log.i(TAG, "Starting to load character xml.");
		mItems = aItems;
		mContext = aContext;
		
		final InputStream stream = new ByteArrayInputStream(aXML.getBytes(Charset.defaultCharset()));
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		}
		catch (final Exception e)
		{
			Log.e(TAG, "Could not read input stream.");
		}
		if (doc == null)
		{
			throw new IOException("Error while reading the XML data.");
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
		mInsanities = new InsanityControllerInstance();
		final Element insanities = (Element) root.getElementsByTagName("insanities").item(0);
		for (int i = 0; i < insanities.getChildNodes().getLength(); i++ )
		{
			if (insanities.getChildNodes().item(i) instanceof Element)
			{
				final Element insanity = (Element) insanities.getChildNodes().item(i);
				if (insanity.getTagName().equals("insanity"))
				{
					Duration duration;
					if (insanity.getAttribute("durationType").equals("forever"))
					{
						duration = Duration.FOREVER;
					}
					else
					{
						duration = new Duration(Type.valueOf(insanity.getAttribute("durationType")), Integer.parseInt(insanity
								.getAttribute("durationValue")));
					}
					mInsanities.addInsanity(CodingUtil.decode(insanity.getAttribute("name")), duration);
				}
			}
		}
		
		// Descriptions
		final Map<String, String> descriptionsMap = new HashMap<String, String>();
		final Element descriptions = (Element) root.getElementsByTagName("descriptions").item(0);
		for (int i = 0; i < descriptions.getChildNodes().getLength(); i++ )
		{
			if (descriptions.getChildNodes().item(i) instanceof Element)
			{
				final Element description = (Element) descriptions.getChildNodes().item(i);
				if (description.getTagName().equals("description"))
				{
					descriptionsMap.put(description.getAttribute("key"), CodingUtil.decode(description.getAttribute("value")));
				}
			}
		}
		mDescriptions = new DescriptionInstanceController(descriptionsMap, mItems.getDescriptions());
		
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
		
		// Money
		mMoney = new MoneyController(mItems.getMoney(), (Element) root.getElementsByTagName("money").item(0), getContext());
		
		// Inventory
		mInventory = new InventoryController((Element) root.getElementsByTagName("inventory").item(0), mItems.getInventory(), this, getContext());
		
		addTimeListeners();
		Log.i(TAG, "Finished loading character.");
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
	
	private void addTimeListeners()
	{
		mTimeListeners.add(mInsanities);
		mTimeListeners.add(mHealth);
	}
	
	public HealthControllerInstance getHealth()
	{
		return mHealth;
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
	
	public MoneyController getMoney()
	{
		return mMoney;
	}
	
	public InventoryController getInventory()
	{
		return mInventory;
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	public void update()
	{
		for (final ItemControllerInstance controller : getControllers())
		{
			controller.updateGroups();
		}
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
	
	public String getName()
	{
		return mName;
	}
	
	public String getConcept()
	{
		return mConcept;
	}
	
	public Clan getClan()
	{
		return mClan;
	}
	
	public Nature getNature()
	{
		return mNature;
	}
	
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemControllerInstance controller : mControllers)
		{
			controller.setMode(mMode);
		}
	}
	
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	public List<ItemControllerInstance> getControllers()
	{
		return mControllers;
	}
	
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
	}
	
	public String serialize()
	{
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (final ParserConfigurationException e)
		{
			Log.e(TAG, "Could not create a XML document.");
		}
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
		final Element insanities = doc.createElement("insanities");
		for (final String insanity : mInsanities.getInsanities())
		{
			final Element insanityElement = doc.createElement("insanity");
			final Duration duration = mInsanities.getDurationOf(insanity);
			insanityElement.setAttribute("name", CodingUtil.encode(insanity));
			if (duration == Duration.FOREVER)
			{
				insanityElement.setAttribute("durationType", "forever");
			}
			else
			{
				insanityElement.setAttribute("durationType", duration.getType().name());
				insanityElement.setAttribute("durationValue", "" + duration.getValue());
			}
			insanities.appendChild(insanityElement);
		}
		root.appendChild(insanities);
		
		// Descriptions
		final Element descriptions = doc.createElement("descriptions");
		for (final String descriptionKey : mDescriptions.getKeys())
		{
			// TODO Rewrite Descriptions
			final Element descriptionItem = doc.createElement("description");
			descriptionItem.setAttribute("key", descriptionKey);
			String value = mDescriptions.getValue(descriptionKey).getValue();
			if (value == null)
			{
				value = "";
			}
			descriptionItem.setAttribute("value", CodingUtil.encode(value));
			descriptions.appendChild(descriptionItem);
		}
		root.appendChild(descriptions);
		
		// Controllers
		final Element controllers = doc.createElement("controllers");
		for (final ItemControllerInstance controller : mControllers)
		{
			controllers.appendChild(controller.asElement(doc));
		}
		root.appendChild(controllers);
		
		// TODO Restrictions
		
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		final StreamResult result = new StreamResult(stream);
		try
		{
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), result);
		}
		catch (final TransformerException e)
		{
			Log.e(TAG, "Could not write document into stream.");
		}
		try
		{
			stream.close();
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not close stream.");
		}
		
		return new String(stream.toByteArray(), Charset.defaultCharset());
	}
}
