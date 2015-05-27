package com.deepercreeper.vampireapp.activities;

import java.util.Locale;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.controllers.CharController;
import com.deepercreeper.vampireapp.character.instance.CharacterCompound;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.host.HostController;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

/**
 * The main activity is the start class for the vampire application.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class MainActivity extends Activity implements ItemConsumer, ConnectionListener
{
	private class PlaceholderFragment extends Fragment
	{
		private PlaceholderFragment()
		{}
		
		@Override
		public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
		{
			ViewGroup rootView = null;
			switch (getArguments().getInt(ARG_SECTION_NUMBER))
			{
				case 1 :
					rootView = (ViewGroup) inflater.inflate(R.layout.friends_fragment, container, false);
					break;
				case 2 :
					rootView = (ViewGroup) inflater.inflate(R.layout.characters_fragment, container, false);
					initChars(rootView);
					break;
				case 3 :
					rootView = (ViewGroup) inflater.inflate(R.layout.hosts_fragment, container, false);
					initHosts(rootView);
					break;
			}
			return rootView;
		}
	}
	
	private class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		private SectionsPagerAdapter(final FragmentManager aManager)
		{
			super(aManager);
		}
		
		@Override
		public int getCount()
		{
			return 3;
		}
		
		@Override
		public Fragment getItem(final int position)
		{
			return newFragmentInstance(position + 1);
		}
		
		@Override
		public CharSequence getPageTitle(final int position)
		{
			final Locale l = Locale.getDefault();
			switch (position)
			{
				case 0 :
					return getString(R.string.friends).toUpperCase(l);
				case 1 :
					return getString(R.string.characters).toUpperCase(l);
				case 2 :
					return getString(R.string.hosts).toUpperCase(l);
			}
			return null;
		}
	}
	
	private static final String		TAG					= "VampireActivity";
	
	private static final String		ARG_SECTION_NUMBER	= "section_number";
	
	private ConnectionController	mConnection;
	
	private Menu					mOptionsMenu;
	
	private CharController			mChars;
	
	private HostController			mHosts;
	
	private ItemProvider			mItems;
	
	private SectionsPagerAdapter	mSectionsPagerAdapter;
	
	private ViewPager				mViewPager;
	
	@Override
	public void cancel()
	{}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
		// TODO Use when non game communication is needed
	}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		if (mChars != null)
		{
			for (final CharacterCompound charCompound : mChars.getCharacterCompoundsList())
			{
				charCompound.setPlayingEnabled(aEnabled);
			}
			mHosts.setHostsEnabled(aEnabled);
		}
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		// TODO Use when non game communication is needed
	}
	
	/**
	 * Closes the main activity after closing the connection.
	 */
	public void exit()
	{
		mConnection.exit();
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		exit();
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
		switch (id)
		{
			case R.id.delete_chars :
				mChars.deleteChars();
				return true;
			case R.id.bluetooth :
				setBluetooth(true);
				return true;
			case R.id.network :
				setBluetooth(false);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(final Menu aMenu)
	{
		mOptionsMenu = aMenu;
		return super.onPrepareOptionsMenu(aMenu);
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		// TODO Use when non game communication is needed
	}
	
	@Override
	protected void onActivityResult(final int aRequestCode, final int aResultCode, final Intent aData)
	{
		if (aRequestCode == CreateCharActivity.CREATE_CHAR_REQUEST && aResultCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(CreateCharActivity.CHARACTER);
			CharacterInstance character = null;
			try
			{
				character = new CharacterInstance(xml, mItems, this);
			}
			catch (final IllegalArgumentException e)
			{
				Log.e(TAG, "Could not create character from xml.");
			}
			if (character != null)
			{
				mChars.addChar(character);
			}
		}
		else if (aRequestCode == PlayActivity.PLAY_CHAR_REQUEST && aResultCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(PlayActivity.CHARACTER);
			CharacterInstance character = null;
			try
			{
				character = new CharacterInstance(xml, mItems, this);
			}
			catch (final IllegalArgumentException e)
			{
				Log.e(TAG, "Could not create character from xml.");
			}
			if (character != null)
			{
				mChars.updateChar(character);
			}
		}
		else if (aRequestCode == HostActivity.PLAY_HOST_REQUEST && aRequestCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(HostActivity.HOST);
			final Host host = new Host(xml, mItems);
			mHosts.updateHost(host);
		}
		else
		{
			System.out.println("Unknown request code: " + aRequestCode + " " + aResultCode);
		}
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	protected void onResume()
	{
		if (mConnection != null)
		{
			mConnection.checkConnectionState();
		}
		super.onResume();
	}
	
	private void createChar(final boolean aFree)
	{
		final Intent intent = new Intent(this, CreateCharActivity.class);
		intent.putExtra(CreateCharActivity.CHAR_NAMES, mChars.getCharNames());
		intent.putExtra(CreateCharActivity.FREE_CREATION, aFree);
		startActivityForResult(intent, CreateCharActivity.CREATE_CHAR_REQUEST);
	}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this);
		
		if ( !mConnection.hasBluetooth())
		{
			mOptionsMenu.findItem(R.id.bluetooth).setEnabled(false).setChecked(false);
			mOptionsMenu.findItem(R.id.network).setChecked(true);
		}
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		
		mChars = new CharController(this, mItems, mConnection);
		mHosts = new HostController(this, mItems, mConnection);
		
		setContentView(R.layout.activity_main);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mViewPager.setCurrentItem(1);
	}
	
	private void initChars(final ViewGroup aRoot)
	{
		final Button createChar = (Button) aRoot.findViewById(R.id.create_character_button);
		final Button createFreeChar = (Button) aRoot.findViewById(R.id.create_character_free_button);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				createChar(false);
			}
		});
		createFreeChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				createChar(true);
			}
		});
		
		mChars.setCharsList((LinearLayout) aRoot.findViewById(R.id.characters_list));
		mChars.loadCharCompounds();
	}
	
	private void initHosts(final ViewGroup aRoot)
	{
		final Button createHost = (Button) aRoot.findViewById(R.id.create_host_button);
		
		createHost.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mHosts.createHost();
			}
		});
		
		mHosts.setHostsList((LinearLayout) aRoot.findViewById(R.id.hosts_list));
		mHosts.loadHosts();
	}
	
	private PlaceholderFragment newFragmentInstance(final int sectionNumber)
	{
		final PlaceholderFragment fragment = new PlaceholderFragment();
		final Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	private void setBluetooth(final boolean aBluetooth)
	{
		mConnection.setBluetooth(aBluetooth);
		mOptionsMenu.findItem(R.id.bluetooth).setChecked(mConnection.isBluetooth());
		mOptionsMenu.findItem(R.id.network).setChecked( !mConnection.isBluetooth());
	}
}