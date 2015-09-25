package com.deepercreeper.vampireapp.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.activities.ItemProviderService;
import com.deepercreeper.vampireapp.activities.MainActivity;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.inventory.Inventory;
import com.deepercreeper.vampireapp.connection.connector.ConnectionService;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.test.util.TestUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;

public class ClientTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	private static final int TIMEOUT = 5000;
	
	private Solo mSolo;
	
	public ClientTest()
	{
		super(MainActivity.class);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		mSolo = new Solo(getInstrumentation());
		
		getActivity();
	}
	
	@Override
	public void tearDown() throws Exception
	{
		mSolo.finishOpenedActivities();
	}
	
	public void testServicesRunning()
	{
		assertTrue("Could not load activity.", mSolo.waitForActivity(MainActivity.class));
		assertTrue("Item provider service was not started.", TestUtil.isServiceRunning(ItemProviderService.class, getActivity()));
		assertFalse("Connection service should not be started.", TestUtil.isServiceRunning(ConnectionService.class, getActivity()));
		
		mSolo.goBack();
		
		assertTrue("Didn't stop the activity after " + TIMEOUT + " ms.", TestUtil.waitForActivityDestroyed(mSolo, getActivity(), TIMEOUT));
		assertTrue("Item provider was stopped.", TestUtil.isServiceRunning(ItemProviderService.class, getActivity()));
	}
	
	public void testDataParsing()
	{
		Map<String, Item> items = new HashMap<>();
		
		// Health
		Health health = DataUtil.loadHealth(getActivity());
		assertNotNull("No health cost given.", health.getCost());
		items.put(health.getCost(), null);
		assertTrue("No health steps defined.", health.getSteps().length > 0);
		
		// Currency
		Currency currency = DataUtil.loadCurrency(getActivity());
		assertTrue(currency.getCurrencies().length > 0);
		for (String c : currency.getCurrencies())
		{
			assertNotNull("Currency is null.", c);
			assertNotNull("Currency has no maximum value.", currency.getMaxAmounts().get(c));
			assertTrue("Currency has negative maximum value.", currency.getMaxAmounts().get(c) >= 0);
			assertTrue("Currency not contained inside currencies.", currency.contains(c));
		}
		
		// Generation
		String generationItem = DataUtil.loadGenerationItem(getActivity());
		if (generationItem != null)
		{
			items.put(generationItem, null);
		}
		
		// Controllers
		List<ItemController> controllers = DataUtil.loadItems(getActivity());
		assertFalse("No controllers defined.", controllers.isEmpty());
		
		// Inventory
		Inventory inventory = DataUtil.loadInventory(getActivity());
		assertNotNull("No inventory height item defined.", inventory.getMaxWeightItem());
		items.put(inventory.getMaxWeightItem(), null);
		
		// Default actions
		Set<Action> defaultActions = DataUtil.loadDefaultActions(getActivity());
		for (Action action : defaultActions)
		{
			assertNotNull("Action has no name.", action.getName());
			for (String costDice : action.getCostDiceNames())
			{
				assertNotNull("Cost dice is null.", costDice);
				items.put(costDice, null);
			}
			for (String dice : action.getDiceNames())
			{
				assertNotNull("Dice is null.", dice);
				items.put(dice, null);
			}
			for (String cost : action.getCostNames().keySet())
			{
				assertNotNull("Cost is null.", cost);
				items.put(cost, null);
				assertNotNull("Cost amount is null.", action.getCostNames().get(cost));
				assertTrue("Cost amount is not positive.", action.getCostNames().get(cost) > 0);
			}
		}
		
		// Controller items
		for (String item : items.keySet())
		{
			boolean found = false;
			for (ItemController controller : controllers)
			{
				if (controller.hasItem(item))
				{
					found = true;
					items.put(item, controller.getItem(item));
				}
			}
			assertTrue("Item \"" + item + "\" not found inside controllers.", found);
			assertNotNull("Item \"" + item + "\" not saved inside controller.", items.get(item));
		}
		
		// Inventory
		for (int value : items.get(inventory.getMaxWeightItem()).getValues())
		{
			assertTrue("Max weight of value \"" + value + "\" not given.", inventory.getMaxWeightOf(value) >= 0);
		}
	}
}
