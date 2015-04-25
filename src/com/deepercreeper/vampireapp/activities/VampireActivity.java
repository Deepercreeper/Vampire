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
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.host.HostController;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionController;
import com.deepercreeper.vampireapp.util.ConnectionController.ConnectionListener;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

/**
 * The main activity is the start class for the vampire app.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class VampireActivity extends Activity implements ItemConsumer, ConnectionListener
{
	private static final String			TAG					= "VampireActivity";
	
	private static final String			ARG_SECTION_NUMBER	= "section_number";
	
	private final ConnectionController	mConnection			= new ConnectionController(this);
	
	private Menu						mOptionsMenu;
	
	private CharController				mChars;
	
	private HostController				mHosts;
	
	private ItemProvider				mItems;
	
	private SectionsPagerAdapter		mSectionsPagerAdapter;
	
	private ViewPager					mViewPager;
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	protected void onDestroy()
	{
		mConnection.close();
		super.onDestroy();
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	private void init()
	{
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
		// mChars.sortChars();
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
	
	private void createChar(final boolean aFree)
	{
		final Intent intent = new Intent(this, CreateCharActivity.class);
		intent.putExtra(CreateCharActivity.CHAR_NAMES, mChars.getCharNames());
		intent.putExtra(CreateCharActivity.FREE_CREATION, aFree);
		startActivityForResult(intent, CreateCharActivity.CREATE_CHAR_REQUEST);
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (mConnection.hasBluetooth())
		{
			menu.findItem(R.id.bluetooth).setEnabled(false).setChecked(false);
			menu.findItem(R.id.network).setChecked(true);
		}
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
	
	private void setBluetooth(boolean aBluetooth)
	{
		mConnection.setBluetooth(aBluetooth);
		mOptionsMenu.findItem(R.id.bluetooth).setChecked(mConnection.isBluetooth());
		mOptionsMenu.findItem(R.id.network).setChecked( !mConnection.isBluetooth());
	}
	
	@Override
	protected void onResume()
	{
		mConnection.checkConnection();
		super.onResume();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu aMenu)
	{
		mOptionsMenu = aMenu;
		return super.onPrepareOptionsMenu(aMenu);
	}
	
	@Override
	public void connectionEnabled(boolean aEnabled)
	{
		if (mChars != null)
		{
			for (CharacterCompound charCompound : mChars.getCharacterCompoundsList())
			{
				charCompound.getPlayButton().setEnabled(aEnabled);
			}
			mHosts.setHostsEnabled(aEnabled);
		}
	}
	
	private class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		private SectionsPagerAdapter(final FragmentManager aManager)
		{
			super(aManager);
		}
		
		@Override
		public Fragment getItem(final int position)
		{
			return newFragmentInstance(position + 1);
		}
		
		@Override
		public int getCount()
		{
			return 3;
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
	
	private PlaceholderFragment newFragmentInstance(final int sectionNumber)
	{
		final PlaceholderFragment fragment = new PlaceholderFragment();
		final Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
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
}
