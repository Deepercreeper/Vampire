package com.deepercreeper.vampireapp.host;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.activities.HostActivity;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.CreateStringDialog.CreationListener;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog.StringSelectionListener;

public class HostController
{
	private static final String		TAG					= "HostController";
	
	private static final String		HOSTS_LIST			= "Hosts.lst";
	
	private static final String		DEFAULT_LOCATION	= "Germany";
	
	private final Map<String, Host>	mHostsCache			= new HashMap<String, Host>();
	
	private final List<String>		mHostNames			= new ArrayList<String>();
	
	private final ItemProvider		mItems;
	
	private final Activity			mContext;
	
	public HostController(final Activity aContext, final ItemProvider aItems)
	{
		mItems = aItems;
		mContext = aContext;
	}
	
	public void loadHosts()
	{
		final String data = FilesUtil.loadFile(HOSTS_LIST, mContext);
		if (data != null && !data.trim().isEmpty())
		{
			for (final String host : data.split("\n"))
			{
				mHostNames.add(host);
			}
			Collections.sort(mHostNames);
		}
	}
	
	public void createHost()
	{
		final CreationListener listener = new CreationListener()
		{
			@Override
			public void create(final String aString)
			{
				if (getHostNames().contains(aString.trim()) || aString.trim().isEmpty())
				{
					createHost();
				}
				else
				{
					saveHost(new Host(aString.trim(), mItems, DEFAULT_LOCATION));
				}
			}
		};
		CreateStringDialog.showCreateStringDialog(mContext.getString(R.string.create_host_title), mContext.getString(R.string.create_host), mContext,
				listener);
	}
	
	public void updateHost(final Host aHost)
	{
		saveHost(aHost);
	}
	
	public void deleteHost(final String aName)
	{
		final File hostFile = new File(mContext.getFilesDir(), aName + ".hst");
		if ( !hostFile.delete())
		{
			Log.e(TAG, "Could not delete character file.");
		}
		mHostNames.remove(aName);
		saveHostsList();
	}
	
	public void saveHost(final Host aHost)
	{
		mHostNames.add(aHost.getName());
		FilesUtil.saveFile(aHost.serialize(), aHost.getName() + ".hst", mContext);
		saveHostsList();
	}
	
	public Host loadHost(final String aName)
	{
		Host host = mHostsCache.get(aName);
		if (host == null)
		{
			final String data = FilesUtil.loadFile(aName + ".hst", mContext);
			if (data != null)
			{
				host = new Host(data, mItems);
			}
		}
		if (host == null)
		{
			return null;
		}
		mHostsCache.put(aName, host);
		return host;
	}
	
	public void play()
	{
		final StringSelectionListener listener = new StringSelectionListener()
		{
			@Override
			public void select(final String aItem)
			{
				play(aItem);
			}
		};
		SelectItemDialog.showSelectionDialog(mHostNames, mContext.getString(R.string.choose_host), mContext, listener);
	}
	
	public void play(final String aName)
	{
		final Host host = loadHost(aName);
		
		final Intent intent = new Intent(mContext, HostActivity.class);
		intent.putExtra(HostActivity.HOST, host.serialize());
		
		mContext.startActivityForResult(intent, HostActivity.PLAY_HOST_REQUEST);
	}
	
	public void saveHostsList()
	{
		final StringBuilder hostNames = new StringBuilder();
		for (int i = 0; i < mHostNames.size(); i++ )
		{
			if (i > 0)
			{
				hostNames.append("\n");
			}
			hostNames.append(mHostNames.get(i));
		}
		
		FilesUtil.saveFile(hostNames.toString(), HOSTS_LIST, mContext);
	}
	
	public List<String> getHostNames()
	{
		return mHostNames;
	}
}
