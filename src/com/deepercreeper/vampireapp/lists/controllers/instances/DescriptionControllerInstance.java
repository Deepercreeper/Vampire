package com.deepercreeper.vampireapp.lists.controllers.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.creations.DescriptionControllerCreation;
import com.deepercreeper.vampireapp.lists.items.Description;
import com.deepercreeper.vampireapp.lists.items.DescriptionCreation;
import com.deepercreeper.vampireapp.lists.items.DescriptionInstance;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;

/**
 * A controller for all character descriptions.
 * 
 * @author vrl
 */
public class DescriptionControllerInstance implements Saveable
{
	private final Map<String, DescriptionInstance> mValues = new HashMap<String, DescriptionInstance>();
	
	/**
	 * Creates a new description controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aController
	 *            The description controller.
	 */
	public DescriptionControllerInstance(final Element aElement, final DescriptionController aController)
	{
		final Map<String, String> descriptionsMap = new HashMap<String, String>();
		for (Element description : DataUtil.getChildren(aElement, "description"))
		{
			descriptionsMap.put(description.getAttribute("key"), CodingUtil.decode(description.getAttribute("value")));
		}
		for (final Description description : aController.getValuesList())
		{
			String value = descriptionsMap.get(description.getName());
			if (value == null)
			{
				value = "";
			}
			mValues.put(description.getName(), new DescriptionInstance(description, value));
		}
	}
	
	/**
	 * Creates a new description controller out of the given description controller creation.
	 * 
	 * @param aController
	 */
	public DescriptionControllerInstance(final DescriptionControllerCreation aController)
	{
		for (final DescriptionCreation value : aController.getValuesList())
		{
			mValues.put(value.getName(), new DescriptionInstance(value.getItem(), value.getValue()));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("descriptions");
		for (final String descriptionKey : getKeys())
		{
			final Element descriptionItem = aDoc.createElement("description");
			descriptionItem.setAttribute("key", descriptionKey);
			String value = getValue(descriptionKey).getValue();
			if (value == null)
			{
				value = "";
			}
			descriptionItem.setAttribute("value", CodingUtil.encode(value));
			element.appendChild(descriptionItem);
		}
		return element;
	}
	
	/**
	 * @return a set of all description keys.
	 */
	public Set<String> getKeys()
	{
		return mValues.keySet();
	}
	
	/**
	 * @param aName
	 *            The description name.
	 * @return the description value of the given description name.
	 */
	public DescriptionInstance getValue(final String aName)
	{
		return mValues.get(aName);
	}
}
