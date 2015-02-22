package com.deepercreeper.vampireapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.items.implementations.GroupOptionImpl;
import com.deepercreeper.vampireapp.items.implementations.ItemControllerImpl;
import com.deepercreeper.vampireapp.items.implementations.ItemGroupImpl;
import com.deepercreeper.vampireapp.items.implementations.ItemImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationConditionImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionImpl;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationCondition;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationCondition.ConditionQuery;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.mechanics.Action.ActionType;
import com.deepercreeper.vampireapp.mechanics.ActionImpl;

public class DataUtil
{
	private static final String	TAG				= "DataUtil";
	
	private static final String	CONTROLLER		= "controller";
	
	private static final String	ITEM			= "item";
	
	private static final String	GROUP			= "group";
	
	private static final String	GROUP_OPTION	= "group-option";
	
	private static final String	CLAN			= "clan";
	
	private static final String	CONDITION		= "condition";
	
	private static final String	RESTRICTION		= "restriction";
	
	private static final String	ACTION			= "action";
	
	private static Document		sData;
	
	public static String loadGenerationItem(final Context aContext)
	{
		return getSpecialItems(aContext).getAttribute("generationItem");
	}
	
	public static ClanController loadClans(final Context aContext)
	{
		return loadClans(getData(aContext));
	}
	
	public static Health loadHealth(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		return new Health(parseValues(element.getAttribute("defaultHealth")), element.getAttribute("healthCost"));
	}
	
	public static Inventory loadInventory(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		final Inventory inventory = new Inventory(parseValues(element.getAttribute("maxWeightSteps")), element.getAttribute("maxWeightItem"));
		return inventory;
	}
	
	public static List<ItemController> loadItems(final Context aContext)
	{
		return loadControllers(getData(aContext));
	}
	
	public static Money loadMoney(final Context aContext)
	{
		final Money money = new Money(parseArray(getSpecialItems(aContext).getAttribute("currencies")));
		return money;
	}
	
	public static List<String> parseList(final String aList)
	{
		return Arrays.asList(parseArray(aList));
	}
	
	public static String parseList(final List<String> aList)
	{
		final StringBuilder list = new StringBuilder();
		for (int i = 0; i < aList.size(); i++ )
		{
			if (i != 0)
			{
				list.append(",");
			}
			list.append(aList.get(i));
		}
		return list.toString();
	}
	
	public static String parseArray(final String[] aList)
	{
		final StringBuilder list = new StringBuilder();
		for (int i = 0; i < aList.length; i++ )
		{
			if (i != 0)
			{
				list.append(",");
			}
			list.append(aList[i]);
		}
		return list.toString();
	}
	
	public static String[] parseArray(final String aList)
	{
		return aList.split(",");
	}
	
	public static String parseValues(final int[] aValues)
	{
		final StringBuilder values = new StringBuilder();
		for (int i = 0; i < aValues.length; i++ )
		{
			if (i != 0)
			{
				values.append(",");
			}
			values.append(aValues[i]);
		}
		return values.toString();
	}
	
	public static int[] parseValues(final String aValues)
	{
		final String[] integers = aValues.split(",");
		final int[] values = new int[integers.length];
		for (int i = 0; i < integers.length; i++ )
		{
			values[i] = Integer.parseInt(integers[i]);
		}
		return values;
	}
	
	private static Document getData(final Context aContext)
	{
		if (sData == null)
		{
			sData = FilesUtil.loadDocument(aContext, "data", false);
		}
		return sData;
	}
	
	private static Element getSpecialItems(final Context aContext)
	{
		return (Element) getData(aContext).getElementsByTagName("special-items").item(0);
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
				final String name = child.getAttribute("name");
				String id = name;
				int minLevel = 0;
				int minDices = 0;
				String[] dices = null;
				String[] costDices = null;
				String[] cost = null;
				
				if (child.hasAttribute("id"))
				{
					id = child.getAttribute("id");
				}
				if (child.hasAttribute("minDices"))
				{
					minDices = Integer.parseInt(child.getAttribute("minDices"));
				}
				if (child.hasAttribute("dices"))
				{
					dices = parseArray(child.getAttribute("dices"));
				}
				if (child.hasAttribute("costDices"))
				{
					costDices = parseArray(child.getAttribute("costDices"));
				}
				if (child.hasAttribute("costs"))
				{
					cost = parseArray(child.getAttribute("cost"));
				}
				if (child.hasAttribute("minLevel"))
				{
					minLevel = Integer.parseInt(child.getAttribute("minLevel"));
				}
				actions.add(new ActionImpl(name, id, type, minLevel, minDices, dices, costDices, cost));
			}
		}
		return actions;
	}
	
	private static ClanController loadClans(final Document aDoc)
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
			final Set<CreationRestriction> restrictions = loadRestrictions(clanNode);
			for (final CreationRestriction restriction : restrictions)
			{
				clan.addRestriction(restriction);
			}
			clansList.add(clan);
		}
		controller.init(clansList);
		return controller;
	}
	
	private static Set<CreationCondition> loadConditions(final Node aRestrictionNode)
	{
		final Set<CreationCondition> conditions = new HashSet<CreationCondition>();
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
				conditions.add(new CreationConditionImpl(query, itemName, minimum, maximum, index));
			}
		}
		return conditions;
	}
	
	private static List<ItemController> loadControllers(final Document aDoc)
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
			final String name = controller.getAttribute("name");
			final ItemController itemController = new ItemControllerImpl(name);
			for (final ItemGroup group : loadGroups(controller))
			{
				itemController.addGroup(group);
			}
			for (final GroupOption groupOption : loadGroupOptions(controller, itemController))
			{
				itemController.addGroupOption(groupOption);
			}
			controllersList.add(itemController);
		}
		return controllersList;
	}
	
	private static List<GroupOption> loadGroupOptions(final Node aController, final ItemController aParent)
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
				final boolean freeMutable = Boolean.parseBoolean(child.getAttribute("freeMutable"));
				final boolean valueGroup = Boolean.parseBoolean(child.getAttribute("value"));
				final boolean order = Boolean.parseBoolean(child.getAttribute("order"));
				int maxValue = Integer.MAX_VALUE;
				int startValue = 0;
				int maxLowLevelValue = 0;
				int freePointsCost = 0;
				int epCost = 0;
				int epCostMultiplicator = 0;
				int epCostNew = 0;
				int maxItems = Integer.MAX_VALUE;
				if (child.hasAttribute("maxItems"))
				{
					maxItems = Integer.parseInt(child.getAttribute("maxItems"));
				}
				if (valueGroup)
				{
					if (child.hasAttribute("epCost"))
					{
						epCost = Integer.parseInt(child.getAttribute("epCost"));
					}
					if (child.hasAttribute("epCostMulti"))
					{
						epCostMultiplicator = Integer.parseInt(child.getAttribute("epCostMulti"));
					}
					if (child.hasAttribute("epCostNew"))
					{
						epCostNew = Integer.parseInt(child.getAttribute("epCostNew"));
					}
					if (child.hasAttribute("maxValue"))
					{
						maxValue = Integer.parseInt(child.getAttribute("maxValue"));
					}
					if (child.hasAttribute("freePointsCost"))
					{
						freePointsCost = Integer.parseInt(child.getAttribute("freePointsCost"));
					}
					if (child.hasAttribute("maxLowLevelValue"))
					{
						maxLowLevelValue = Integer.parseInt(child.getAttribute("maxLowLevelValue"));
					}
					else
					{
						maxLowLevelValue = maxValue;
					}
					if (child.hasAttribute("startValue"))
					{
						startValue = Integer.parseInt(child.getAttribute("startValue"));
					}
				}
				final ItemGroup group = new ItemGroupImpl(name, mutable, order, freeMutable, maxLowLevelValue, startValue, maxValue, freePointsCost,
						valueGroup, maxItems, epCost, epCostNew, epCostMultiplicator);
				for (final Item item : loadItems(child, group, null))
				{
					group.addItem(item);
				}
				groupsList.add(group);
			}
		}
		return groupsList;
	}
	
	private static List<Item> loadItems(final Node aParentNode, final ItemGroup aParentGroup, final Item aParentItem)
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
				final boolean order = Boolean.parseBoolean(child.getAttribute("order"));
				boolean valueItem = aParentGroup.isValueGroup();
				int[] values = null;
				int startValue = -1;
				int epCost = -1;
				int epCostNew = -1;
				int epCostMultiplicator = -1;
				
				if (child.hasAttribute("epCost"))
				{
					epCost = Integer.parseInt(child.getAttribute("epCost"));
				}
				if (child.hasAttribute("epCostNew"))
				{
					epCostNew = Integer.parseInt(child.getAttribute("epCostNew"));
				}
				if (child.hasAttribute("epCostMulti"))
				{
					epCostMultiplicator = Integer.parseInt(child.getAttribute("epCostMulti"));
				}
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
				item = new ItemImpl(name, aParentGroup, needsDescription, parent, mutableParent, order, values, startValue, epCost, epCostNew,
						epCostMultiplicator, aParentItem);
				for (final Item childItem : loadItems(child, aParentGroup, item))
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
	
	private static Set<CreationRestriction> loadRestrictions(final Node aClanNode)
	{
		final Set<CreationRestriction> restrictions = new HashSet<CreationRestriction>();
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
				final CreationRestrictionType type = CreationRestrictionType.get(child.getAttribute("type"));
				
				if (type == null)
				{
					Log.w(TAG, "Restriction type could not be found: " + child.getAttribute("type"));
				}
				
				final boolean creationRestriction = Boolean.parseBoolean(child.getAttribute("creationRestriction"));
				String itemName = null;
				int minimum = Integer.MIN_VALUE;
				int maximum = Integer.MAX_VALUE;
				int index = 0;
				int value = 0;
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
					items = Arrays.asList(parseArray(child.getAttribute("items")));
				}
				if (child.hasAttribute("index"))
				{
					index = Integer.parseInt(child.getAttribute("index"));
				}
				if (child.hasAttribute("value"))
				{
					value = Integer.parseInt(child.getAttribute("value"));
				}
				
				final CreationRestriction restriction = new CreationRestrictionImpl(type, itemName, minimum, maximum, items, index, value,
						creationRestriction);
				
				if (child.hasChildNodes())
				{
					for (final CreationCondition condition : loadConditions(child))
					{
						restriction.addCondition(condition);
					}
				}
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
}
