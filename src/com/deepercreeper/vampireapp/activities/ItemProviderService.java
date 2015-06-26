package com.deepercreeper.vampireapp.activities;

import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;

/**
 * This is an item provider service implementation, that makes it possible<br>
 * to load the whole item system just once. It provides the controllers, groups and items as much<br>
 * as the other information, that is needed, when a vampire is created.
 * 
 * @author Vincent
 */
public class ItemProviderService extends Service implements ItemProvider
{
	/**
	 * The binder that is able to provide the items of the vampire.
	 * 
	 * @author Vincent
	 */
	public class ItemBinder extends Binder
	{
		/**
		 * @return the underlying item provider that is able to return all the item information.
		 */
		public ItemProvider getItemProvider()
		{
			return ItemProviderService.this;
		}
	}
	
	private static final String		TAG				= "ItemProvider";
	
	private final IBinder			mBinder			= new ItemBinder();
	
	private List<ItemController>	mControllers;
	
	private NatureController		mNatures;
	
	private ClanController			mClans;
	
	private DescriptionController	mDescriptions;
	
	private Health					mHealth;
	
	private Currency					mCurrency;
	
	private Inventory				mInventory;
	
	private String					mGenerationItem;
	
	private boolean					mInitializing	= false;
	
	@Override
	public ClanController getClans()
	{
		return mClans;
	}
	
	@Override
	public ItemController getController(final String aName)
	{
		for (final ItemController controller : getControllers())
		{
			if (controller.getName().equals(aName))
			{
				return controller;
			}
		}
		Log.e(TAG, "Could not find controller.");
		return null;
	}
	
	@Override
	public List<ItemController> getControllers()
	{
		return mControllers;
	}
	
	@Override
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	@Override
	public String getGenerationItem()
	{
		return mGenerationItem;
	}
	
	@Override
	public Health getHealth()
	{
		return mHealth;
	}
	
	@Override
	public Inventory getInventory()
	{
		return mInventory;
	}
	
	@Override
	public Currency getCurrency()
	{
		return mCurrency;
	}
	
	@Override
	public NatureController getNatures()
	{
		return mNatures;
	}
	
	@Override
	public IBinder onBind(final Intent intent)
	{
		while (mInitializing)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (final InterruptedException e)
			{
				Log.e(TAG, "Interrupted provider thread.");
			}
		}
		return mBinder;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		mInitializing = true;
		
		LanguageUtil.init(this);
		
		Log.i(TAG, "Starting item provider.");
		mControllers = DataUtil.loadItems(this);
		mClans = DataUtil.loadClans(this);
		mNatures = new NatureController(getResources());
		mDescriptions = new DescriptionController(getResources());
		mHealth = DataUtil.loadHealth(this);
		mCurrency = DataUtil.loadCurrency(this);
		mInventory = DataUtil.loadInventory(this);
		mGenerationItem = DataUtil.loadGenerationItem(this);
		Log.i(TAG, "Started item provider.");
		
		mInitializing = false;
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId)
	{
		return Service.START_NOT_STICKY;
	}
}
