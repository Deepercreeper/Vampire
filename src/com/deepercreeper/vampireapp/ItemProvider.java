package com.deepercreeper.vampireapp;

import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.dynamic.Creator;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.lists.NatureController;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;

public class ItemProvider extends Service
{
	private static final String		TAG				= "ItemProvider";
	
	private final IBinder			mBinder			= new ItemBinder();
	
	private List<ItemController>	mControllers;
	
	private NatureController		mNatures;
	
	private ClanController			mClans;
	
	private DescriptionController	mDescriptions;
	
	private boolean					mInitializing	= false;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mInitializing = true;
		LanguageUtil.init(this);
		Log.i(TAG, "Starting item provider.");
		mControllers = Creator.createItems(this);
		mClans = Creator.createClans(this);
		mNatures = new NatureController(getResources());
		mDescriptions = new DescriptionController(getResources());
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
			return ItemProvider.this;
		}
	}
	
	public List<ItemController> getControllers()
	{
		return mControllers;
	}
	
	public ClanController getClans()
	{
		return mClans;
	}
	
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	public NatureController getNatures()
	{
		return mNatures;
	}
}
