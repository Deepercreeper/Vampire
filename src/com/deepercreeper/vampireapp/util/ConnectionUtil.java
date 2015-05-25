package com.deepercreeper.vampireapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.deepercreeper.vampireapp.activities.ItemProviderService;
import com.deepercreeper.vampireapp.items.ItemConsumer;

public class ConnectionUtil
{
	public static void loadItems(final Activity aContext, final ItemConsumer aConsumer)
	{
		final ItemConnection connection = new ItemConnection(aConsumer, aContext);
		final Intent itemProvider = new Intent(aContext, ItemProviderService.class);
		aContext.startService(itemProvider);
		aContext.bindService(itemProvider, connection, Context.BIND_AUTO_CREATE);
	}
	
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
		{
			// Do nothing
		}
	}
}
