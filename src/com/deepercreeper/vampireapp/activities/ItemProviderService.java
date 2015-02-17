package com.deepercreeper.vampireapp.activities;

import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;

public class ItemProviderService extends Service implements ItemProvider
{
	private static final String		TAG				= "ItemProvider";
	
	private final IBinder			mBinder			= new ItemBinder();
	
	private List<ItemController>	mControllers;
	
	private NatureController		mNatures;
	
	private ClanController			mClans;
	
	private DescriptionController	mDescriptions;
	
	private Health					mHealth;
	
	private Money					mMoney;
	
	private boolean					mInitializing	= false;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		mInitializing = true;
		
		LanguageUtil.init(this);
		
		Log.i(TAG, "Starting item provider.");
		mControllers = DataUtil.createItems(this);
		mClans = DataUtil.createClans(this);
		mNatures = new NatureController(getResources());
		mDescriptions = new DescriptionController(getResources());
		mHealth = DataUtil.loadHealth(this);
		mMoney = DataUtil.loadMoney(this);
		Log.i(TAG, "Started item provider.");
		
		mInitializing = false;
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId)
	{
		return Service.START_NOT_STICKY;
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
	
	public class ItemBinder extends Binder
	{
		public ItemProvider getItemProvider()
		{
			return ItemProviderService.this;
		}
	}
	
	@Override
	public List<ItemController> getControllers()
	{
		return mControllers;
	}
	
	@Override
	public ClanController getClans()
	{
		return mClans;
	}
	
	@Override
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	@Override
	public NatureController getNatures()
	{
		return mNatures;
	}
	
	@Override
	public Health getHealth()
	{
		return mHealth;
	}
	
	@Override
	public Money getMoney()
	{
		return mMoney;
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
}
