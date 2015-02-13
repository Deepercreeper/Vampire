package com.deepercreeper.vampireapp.character;

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
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.instances.ItemControllerInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.DescriptionInstanceController;
import com.deepercreeper.vampireapp.lists.controllers.instances.GenerationControllerInstance;
import com.deepercreeper.vampireapp.lists.controllers.instances.InsanityControllerInstance;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.Type;
import com.deepercreeper.vampireapp.util.Log;

public class CharacterInstance
{
	private static final String					TAG				= "CharacterInstance";
	
	private final ItemProvider					mItems;
	
	private final Context						mContext;
	
	private final GenerationControllerInstance			mGeneration;
	
	private final List<ItemControllerInstance>	mControllers	= new ArrayList<ItemControllerInstance>();
	
	private final DescriptionInstanceController	mDescriptions;
	
	private final InsanityControllerInstance			mInsanities;
	
	private final EPHandler						mEP;
	
	private final String						mName;
	
	private final String						mConcept;
	
	private final Nature						mNature;
	
	private final Nature						mBehavior;
	
	private final Clan							mClan;
	
	private Mode								mMode;
	
	public CharacterInstance(final CharacterCreation aCreator)
	{
		mItems = aCreator.getItems();
		mContext = aCreator.getContext();
		mGeneration = new GenerationControllerInstance(aCreator.getGeneration().getGeneration());
		mDescriptions = new DescriptionInstanceController(aCreator.getDescriptions());
		mInsanities = new InsanityControllerInstance(aCreator.getInsanities());
		mEP = new EPHandler();
		
		mName = aCreator.getName();
		mConcept = aCreator.getConcept();
		mNature = aCreator.getNature();
		mBehavior = aCreator.getBehavior();
		mClan = aCreator.getClan();
		
		mMode = Mode.DEFAULT;
		
		for (final ItemControllerCreation controller : aCreator.getControllers())
		{
			mControllers.add(new ItemControllerInstanceImpl(controller, mContext, mMode, mEP, this));
		}
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
		mName = meta.getAttribute("name");
		mConcept = meta.getAttribute("concept");
		mNature = mItems.getNatures().getItemWithName(meta.getAttribute("nature"));
		mBehavior = mItems.getNatures().getItemWithName(meta.getAttribute("behavior"));
		mGeneration = new GenerationControllerInstance(Integer.parseInt(meta.getAttribute("generation")));
		mClan = mItems.getClans().getItemWithName(meta.getAttribute("clan"));
		mEP = new EPHandler(Integer.parseInt(meta.getAttribute("ep")));
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
					mInsanities.addInsanity(insanity.getAttribute("name"), duration);
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
					descriptionsMap.put(description.getAttribute("key"), description.getAttribute("value"));
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
		
		Log.i(TAG, "Finished loading character.");
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
		meta.setAttribute("name", getName());
		meta.setAttribute("concept", getConcept());
		meta.setAttribute("nature", getNature().getName());
		meta.setAttribute("behavior", getBehavior().getName());
		meta.setAttribute("generation", "" + mGeneration.getGeneration());
		meta.setAttribute("clan", getClan().getName());
		meta.setAttribute("ep", "" + mEP.getExperience());
		meta.setAttribute("mode", mMode.name());
		root.appendChild(meta);
		
		// Insanities
		final Element insanities = doc.createElement("insanities");
		for (final String insanity : mInsanities.getInsanities())
		{
			final Element insanityElement = doc.createElement("insanity");
			final Duration duration = mInsanities.getDurationOf(insanity);
			insanityElement.setAttribute("name", insanity);
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
			descriptionItem.setAttribute("value", value);
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
