package com.deepercreeper.vampireapp.activities;

import java.util.Locale;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.CharController;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.host.HostController;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.Log;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The main activity is the start class for the vampire application.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class MainActivity extends Activity implements ItemConsumer
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
					rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_friends, container, false);
					break;
				case 2 :
					rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_characters, container, false);
					initChars(rootView);
					break;
				case 3 :
					rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_hosts, container, false);
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
	
	private static final String TAG = "VampireActivity";
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private CharController mChars;
	
	private HostController mHosts;
	
	private ItemProvider mItems = null;
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	private ViewPager mViewPager;
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	/**
	 * Closes the main activity after closing the connection.
	 */
	public void exit()
	{
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		exit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu aMenu)
	{
		getMenuInflater().inflate(R.menu.main, aMenu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem aItem)
	{
		final int id = aItem.getItemId();
		switch (id)
		{
			case R.id.delete_chars :
				mChars.deleteChars();
				return true;
		}
		
		return super.onOptionsItemSelected(aItem);
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
				character = new CharacterInstance(xml, mItems, this, null, null, null, false);
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
		else if (aRequestCode == ClientActivity.PLAY_CLIENT_REQUEST && aResultCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(ClientActivity.CHARACTER);
			CharacterInstance character = null;
			try
			{
				character = new CharacterInstance(xml, mItems, this, null, null, null, false);
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
		else if (aRequestCode == HostActivity.PLAY_HOST_REQUEST && aResultCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(HostActivity.HOST);
			final Host host = new Host(xml, this, true);
			mHosts.updateHost(host);
		}
		else if (aRequestCode == CreateHostActivity.CREATE_HOST_REQUEST && aResultCode == RESULT_OK)
		{
			final String name = aData.getStringExtra(CreateHostActivity.HOST_NAME);
			mHosts.saveHost(new Host(name, this, false));
		}
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
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
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		
		mChars = new CharController(this);
		mHosts = new HostController(this);
		
		setContentView(R.layout.activity_main);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mViewPager.setCurrentItem(1);
	}
	
	private void initChars(final ViewGroup aRoot)
	{
		final Button createChar = (Button) aRoot.findViewById(R.id.f_create_char_button);
		final Button createFreeChar = (Button) aRoot.findViewById(R.id.f_create_free_char_button);
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
		
		mChars.setCharsList((LinearLayout) aRoot.findViewById(R.id.f_chars_list));
		mChars.loadCharCompounds();
	}
	
	private void initHosts(final ViewGroup aRoot)
	{
		final Button createHost = (Button) aRoot.findViewById(R.id.f_create_host_button);
		
		createHost.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mHosts.createHost();
			}
		});
		
		mHosts.setHostsList((LinearLayout) aRoot.findViewById(R.id.f_hosts_list));
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
}
