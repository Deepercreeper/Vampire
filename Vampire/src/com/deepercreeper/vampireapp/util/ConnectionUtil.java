package com.deepercreeper.vampireapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.deepercreeper.vampireapp.activities.ItemProviderService;
import com.deepercreeper.vampireapp.items.ItemConsumer;

/**
 * This utility is used to find a service that provides all the needed items.
 * 
 * @author vrl
 */
public class ConnectionUtil
{
	private static class ItemConnection implements ServiceConnection
	{
		private final ItemConsumer	mConsumer;
		
		private final Activity		mActivity;
		
		private ItemConnection(final ItemConsumer aConsumer, final Activity aActivity)
		{
			mConsumer = aConsumer;
			mActivity = aActivity;
		}
		
		@Override
		public void onServiceConnected(final ComponentName className, final IBinder binder)
		{
			final ItemProviderService.ItemBinder b = (ItemProviderService.ItemBinder) binder;
			mConsumer.consumeItems(b.getItemProvider());
			mActivity.unbindService(this);
		}
		
		@Override
		public void onServiceDisconnected(final ComponentName aName)
		{}
	}
	
	/**
	 * Loads all items by binding a service. When a service is found and bound<br>
	 * the connector will invoke the {@link ItemConsumer#consumeItems(com.deepercreeper.vampireapp.items.ItemProvider)} with the found provider.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aConsumer
	 *            The item consumer that requests the item provider.
	 */
	public static void loadItems(final Activity aContext, final ItemConsumer aConsumer)
	{
		final ItemConnection connection = new ItemConnection(aConsumer, aContext);
		final Intent itemProvider = new Intent(aContext, ItemProviderService.class);
		aContext.startService(itemProvider);
		aContext.bindService(itemProvider, connection, Context.BIND_AUTO_CREATE);
	}
}
