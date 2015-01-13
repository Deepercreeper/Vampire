package com.deepercreeper.vampireapp.controllers.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import com.deepercreeper.vampireapp.controllers.actions.Action;
import com.deepercreeper.vampireapp.controllers.actions.Action.ActionType;
import com.deepercreeper.vampireapp.controllers.actions.ActionImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.GroupOptionImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.ItemControllerImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.ItemGroupImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.ItemImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.lists.Clan;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.restrictions.Condition;
import com.deepercreeper.vampireapp.controllers.restrictions.Condition.ConditionQuery;
import com.deepercreeper.vampireapp.controllers.restrictions.ConditionImpl;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;
import com.deepercreeper.vampireapp.controllers.restrictions.RestrictionImpl;

public class Creator
{
	private static final String	CONTROLLER	= "controller", ITEM = "item", GROUP = "group", GROUP_OPTION = "group-option", CLAN = "clan",
			CONDITION = "condition", RESTRICTION = "restriction", ACTION = "action";
	
	public static ClanController createClans(final Context aContext)
	{
		return createClans(getDocument(aContext));
	}
	
	private static ClanController createClans(final Document aDoc)
	{
		final ClanController controller = new ClanController();
		final List<Clan> clansList = new ArrayList<Clan>();
		final NodeList clans = aDoc.getElementsByTagName(CLAN);
		for (int i = 0; i < clans.getLength(); i++ )
		{
			if ( !(clans.item(i) instanceof Element))
			{
				continue;
			}
			final Element clanNode = (Element) clans.item(i);
			final Clan clan = new Clan(clanNode.getAttribute("name"));
			final Set<Restriction> restrictions = loadRestrictions(clanNode);
			for (final Restriction restriction : restrictions)
			{
				clan.addRestriction(restriction);
			}
			clansList.add(clan);
		}
		controller.init(clansList);
		return controller;
	}
	
	private static Set<Restriction> loadRestrictions(final Node aClanNode)
	{
		final Set<Restriction> restrictions = new HashSet<Restriction>();
		final NodeList children = aClanNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) (children.item(i));
			if (child.getTagName().equals(RESTRICTION))
			{
				final RestrictionType type = RestrictionType.get(child.getAttribute("type"));
				
				String itemName = null;
				int minimum = Integer.MIN_VALUE;
				int maximum = Integer.MAX_VALUE;
				int index = 0;
				List<String> items = null;
				
				if (child.hasAttribute("itemName"))
				{
					itemName = child.getAttribute("itemName");
				}
				if (child.hasAttribute("range"))
				{
					final String range = child.getAttribute("range");
					
					if (range.startsWith("="))
					{
						minimum = maximum = Integer.parseInt(range.substring(1));
					}
					else if (range.startsWith("<"))
					{
						maximum = Integer.parseInt(range.substring(1)) - 1;
					}
					else if (range.startsWith(">"))
					{
						minimum = Integer.parseInt(range.substring(1)) + 1;
					}
					else if (range.contains("-"))
					{
						minimum = Integer.parseInt(range.split("-")[0]);
						maximum = Integer.parseInt(range.split("-")[1]);
					}
				}
				if (child.hasAttribute("items"))
				{
					items = Arrays.asList(parseList(child.getAttribute("items")));
				}
				if (child.hasAttribute("index"))
				{
					index = Integer.parseInt(child.getAttribute("index"));
				}
				
				final Restriction restriction = new RestrictionImpl(type, itemName, minimum, maximum, items, index);
				
				if (child.hasChildNodes())
				{
					for (final Condition condition : loadConditions(child))
					{
						restriction.addCondition(condition);
					}
				}
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	private static Set<Condition> loadConditions(final Node aRestrictionNode)
	{
		final Set<Condition> conditions = new HashSet<Condition>();
		final NodeList children = aRestrictionNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) (children.item(i));
			if (child.getTagName().equals(CONDITION))
			{
				final ConditionQuery query = ConditionQuery.getQuery(child.getAttribute("query"));
				String itemName = null;
				int minimum = Integer.MIN_VALUE;
				int maximum = Integer.MAX_VALUE;
				int index = 0;
				if (child.hasAttribute("range"))
				{
					final String range = child.getAttribute("range");
					
					if (range.startsWith("="))
					{
						minimum = maximum = Integer.parseInt(range.substring(1));
					}
					else if (range.startsWith("<"))
					{
						maximum = Integer.parseInt(range.substring(1)) - 1;
					}
					else if (range.startsWith(">"))
					{
						minimum = Integer.parseInt(range.substring(1)) + 1;
					}
					else if (range.contains("-"))
					{
						minimum = Integer.parseInt(range.split("-")[0]);
						maximum = Integer.parseInt(range.split("-")[1]);
					}
				}
				if (child.hasAttribute("itemName"))
				{
					itemName = child.getAttribute("itemName");
				}
				if (child.hasAttribute("index"))
				{
					index = Integer.parseInt(child.getAttribute("index"));
				}
				conditions.add(new ConditionImpl(query, itemName, minimum, maximum, index));
			}
		}
		return conditions;
	}
	
	public static List<ItemController> createItems(final Context aContext)
	{
		return createControllers(getDocument(aContext));
	}
	
	private static List<ItemController> createControllers(final Document aDoc)
	{
		final List<ItemController> controllersList = new ArrayList<ItemController>();
		final NodeList controllers = aDoc.getElementsByTagName(CONTROLLER);
		for (int i = 0; i < controllers.getLength(); i++ )
		{
			if ( !(controllers.item(i) instanceof Element))
			{
				continue;
			}
			final Element controller = (Element) controllers.item(i);
			final ItemController itemController = new ItemControllerImpl(controller.getAttribute("name"));
			for (final ItemGroup group : loadGroups(controller))
			{
				itemController.addGroup(group);
			}
			for (final GroupOption groupOption : createGroupOptions(controller, itemController))
			{
				itemController.addGroupOption(groupOption);
			}
			controllersList.add(itemController);
		}
		return controllersList;
	}
	
	private static List<ItemGroup> loadGroups(final Node aController)
	{
		final List<ItemGroup> groupsList = new ArrayList<ItemGroup>();
		final NodeList children = aController.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) (children.item(i));
			if (child.getTagName().equals(GROUP))
			{
				final String name = child.getAttribute("name");
				final boolean mutable = Boolean.parseBoolean(child.getAttribute("mutable"));
				final boolean valueGroup = Boolean.parseBoolean(child.getAttribute("value"));
				int maxValue = 0;
				int startValue = 0;
				int maxLowLevelValue = 0;
				int freePointsCost = 0;
				int maxItems = Integer.MAX_VALUE;
				if (child.hasAttribute("maxItems"))
				{
					maxItems = Integer.parseInt(child.getAttribute("maxItems"));
				}
				if (valueGroup)
				{
					try
					{
						maxValue = Integer.parseInt(child.getAttribute("maxValue"));
					}
					catch (final NumberFormatException e)
					{
						maxValue = Integer.MAX_VALUE;
					}
					try
					{
						freePointsCost = Integer.parseInt(child.getAttribute("freePointsCost"));
					}
					catch (final NumberFormatException e)
					{
						freePointsCost = 0;
					}
					try
					{
						maxLowLevelValue = Integer.parseInt(child.getAttribute("maxLowLevelValue"));
					}
					catch (final NumberFormatException e)
					{
						maxLowLevelValue = maxValue;
					}
					startValue = Integer.parseInt(child.getAttribute("startValue"));
				}
				final ItemGroup group = new ItemGroupImpl(name, mutable, maxLowLevelValue, startValue, maxValue, freePointsCost, valueGroup, maxItems);
				for (final Item item : createItems(child, group, null))
				{
					group.addItem(item);
				}
				groupsList.add(group);
			}
		}
		return groupsList;
	}
	
	private static List<Item> createItems(final Node aParentNode, final ItemGroup aParentGroup, final Item aParentItem)
	{
		final List<Item> itemsList = new ArrayList<Item>();
		final NodeList children = aParentNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) (children.item(i));
			if (child.getTagName().equals(ITEM))
			{
				final String name = child.getAttribute("name");
				final boolean needsDescription = Boolean.parseBoolean(child.getAttribute("needsDescription"));
				final boolean parent = Boolean.parseBoolean(child.getAttribute("parent"));
				boolean valueItem = aParentGroup.isValueGroup();
				int[] values = null;
				int startValue = -1;
				
				if (child.hasAttribute("startValue"))
				{
					startValue = Integer.parseInt(child.getAttribute("startValue"));
				}
				if (child.hasAttribute("valueItem"))
				{
					valueItem = Boolean.parseBoolean(child.getAttribute("valueItem"));
				}
				if (valueItem)
				{
					if (child.hasAttribute("values"))
					{
						values = parseValues(child.getAttribute("values"));
					}
					else
					{
						values = aParentGroup.getDefaultValues();
					}
				}
				
				final Item item;
				final boolean mutableParent = Boolean.parseBoolean(child.getAttribute("mutableParent"));
				item = new ItemImpl(name, aParentGroup, needsDescription, parent, mutableParent, values, startValue, aParentItem);
				for (final Item childItem : createItems(child, aParentGroup, item))
				{
					item.addChild(childItem);
				}
				for (final Action action : loadActions(child))
				{
					item.addAction(action);
				}
				itemsList.add(item);
			}
		}
		return itemsList;
	}
	
	private static Set<Action> loadActions(final Node aItem)
	{
		final Set<Action> actions = new HashSet<Action>();
		final NodeList children = aItem.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) children.item(i);
			if (child.getTagName().equals(ACTION))
			{
				final ActionType type = Action.ActionType.get(child.getAttribute("type"));
				int minLevel = 0;
				int minDices = 0;
				String[] dices = null;
				String[] costDices = null;
				String[] cost = null;
				
				if (child.hasAttribute("minDices"))
				{
					minDices = Integer.parseInt(child.getAttribute("minDices"));
				}
				if (child.hasAttribute("dices"))
				{
					dices = parseList(child.getAttribute("dices"));
				}
				if (child.hasAttribute("costDices"))
				{
					costDices = parseList(child.getAttribute("costDices"));
				}
				if (child.hasAttribute("costs"))
				{
					cost = parseList(child.getAttribute("cost"));
				}
				if (child.hasAttribute("minLevel"))
				{
					minLevel = Integer.parseInt(child.getAttribute("minLevel"));
				}
				actions.add(new ActionImpl(child.getAttribute("name"), type, minLevel, minDices, dices, costDices, cost));
			}
		}
		return actions;
	}
	
	private static int[] parseValues(final String aValues)
	{
		final String[] integers = aValues.split(",");
		final int[] values = new int[integers.length];
		for (int i = 0; i < integers.length; i++ )
		{
			values[i] = Integer.parseInt(integers[i]);
		}
		return values;
	}
	
	private static String[] parseList(final String aList)
	{
		return aList.split(",");
	}
	
	private static List<GroupOption> createGroupOptions(final Node aController, final ItemController aParent)
	{
		final List<GroupOption> groupOptionsList = new ArrayList<GroupOption>();
		final NodeList children = aController.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) (children.item(i));
			if (child.getTagName().equals(GROUP_OPTION))
			{
				final String name = child.getAttribute("name");
				final boolean valuedGroupOption = Boolean.parseBoolean(child.getAttribute("value"));
				int[] maxValues = null;
				if (valuedGroupOption && child.hasAttribute("maxValues"))
				{
					maxValues = parseValues(child.getAttribute("maxValues"));
				}
				final GroupOption groupOption = new GroupOptionImpl(name, maxValues);
				for (final ItemGroup group : loadGroups(child, aParent))
				{
					groupOption.addGroup(group);
				}
				groupOptionsList.add(groupOption);
			}
		}
		return groupOptionsList;
	}
	
	private static List<ItemGroup> loadGroups(final Element aGroupOption, final ItemController aController)
	{
		final List<ItemGroup> groupsList = new ArrayList<ItemGroup>();
		final NodeList children = aGroupOption.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if ( !(children.item(i) instanceof Element))
			{
				continue;
			}
			final Element child = (Element) children.item(i);
			final String groupName = child.getAttribute("name");
			groupsList.add(aController.getGroup(groupName));
		}
		return groupsList;
	}
	
	private static Document getDocument(final Context aContext)
	{
		Document doc = null;
		try
		{
			final DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			final String postfix = "-" + Locale.getDefault().getLanguage();
			doc = DOMbuilder.parse(aContext.getAssets().open("data" + postfix + ".xml"));
		}
		catch (final Exception e)
		{
			return null;
		}
		return doc;
	}
}
