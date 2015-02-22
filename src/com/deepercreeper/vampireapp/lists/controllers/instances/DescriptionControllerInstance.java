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
import com.deepercreeper.vampireapp.util.Saveable;

public class DescriptionControllerInstance implements Saveable
{
	private final Map<String, DescriptionInstance>	mValues	= new HashMap<String, DescriptionInstance>();
	
	public DescriptionControllerInstance(final Element aElement, final DescriptionController aController)
	{
		final Map<String, String> descriptionsMap = new HashMap<String, String>();
		for (int i = 0; i < aElement.getChildNodes().getLength(); i++ )
		{
			if (aElement.getChildNodes().item(i) instanceof Element)
			{
				final Element description = (Element) aElement.getChildNodes().item(i);
				if (description.getTagName().equals("description"))
				{
					descriptionsMap.put(description.getAttribute("key"), CodingUtil.decode(description.getAttribute("value")));
				}
			}
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
	
	public Set<String> getKeys()
	{
		return mValues.keySet();
	}
	
	public DescriptionInstance getValue(final String aName)
	{
		return mValues.get(aName);
	}
}
