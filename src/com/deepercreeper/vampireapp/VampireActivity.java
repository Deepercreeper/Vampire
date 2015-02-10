package com.deepercreeper.vampireapp;

import java.util.List;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.dynamic.Creator;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.lists.NatureController;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The main activity is the start class for the vampire app.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class VampireActivity extends Activity
{
	private static final boolean	SERVICE_ITEMS	= false;
	
	private ServiceConnection		mConnection;
	
	private Vampire					mVampire;
	
	private ItemProvider			mItems;
	
	private List<ItemController>	mControllers;
	
	private NatureController		mNatures;
	
	private ClanController			mClans;
	
	private DescriptionController	mDescriptions;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewUtil.init(this);
		LanguageUtil.init(this);
		
		if (SERVICE_ITEMS)
		{
			mConnection = new ServiceConnection()
			{
				
				@Override
				public void onServiceConnected(final ComponentName className, final IBinder binder)
				{
					final ItemProvider.ItemBinder b = (ItemProvider.ItemBinder) binder;
					mItems = b.getItemProvider();
					Toast.makeText(VampireActivity.this, "Connected", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onServiceDisconnected(final ComponentName className)
				{
					mItems = null;
				}
			};
			
			final Intent itemProvider = new Intent(this, ItemProvider.class);
			startService(itemProvider);
			bindService(itemProvider, mConnection, Context.BIND_AUTO_CREATE);
			
			mControllers = mItems.getControllers();
			mClans = mItems.getClans();
			mNatures = mItems.getNatures();
			mDescriptions = mItems.getDescriptions();
		}
		else
		{
			mConnection = null;
			
			mControllers = Creator.createItems(this);
			mClans = Creator.createClans(this);
			mNatures = new NatureController(getResources());
			mDescriptions = new DescriptionController(getResources());
		}
		
		mVampire = new Vampire(this);
	}
	
	@Override
	public void onBackPressed()
	{
		mVampire.back();
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.delete_chars)
		{
			mVampire.deleteChars();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (SERVICE_ITEMS)
		{
			unbindService(mConnection);
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
