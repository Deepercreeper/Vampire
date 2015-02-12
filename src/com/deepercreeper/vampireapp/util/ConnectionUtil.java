package com.deepercreeper.vampireapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.deepercreeper.vampireapp.ItemProvider;
import com.deepercreeper.vampireapp.ItemProviderImpl;
import com.deepercreeper.vampireapp.ItemProviderService;

public class ConnectionUtil
{
	private static final boolean	SERVICE_ITEMS	= false;
	
	public static ItemProvider loadItems(final Activity aContext)
	{
		if (SERVICE_ITEMS)
		{
			final ItemConnection connection = new ItemConnection();
			final Intent itemProvider = new Intent(aContext, ItemProviderService.class);
			aContext.startService(itemProvider);
			aContext.bindService(itemProvider, connection, Context.BIND_AUTO_CREATE);
			ItemProvider items = connection.getItems();
			aContext.unbindService(connection);
			return items;
		}
		return new ItemProviderImpl(aContext);
	}
	
	private static class ItemConnection implements ServiceConnection
	{
		private ItemProvider	mTempItems;
		
		@Override
		public void onServiceConnected(final ComponentName className, final IBinder binder)
		{
			final ItemProviderService.ItemBinder b = (ItemProviderService.ItemBinder) binder;
			mTempItems = b.getItemProvider();
		}
		
		@Override
		public void onServiceDisconnected(final ComponentName className)
		{
			mTempItems = null;
		}
		
		public ItemProvider getItems()
		{
			return mTempItems;
		}
	}
}
