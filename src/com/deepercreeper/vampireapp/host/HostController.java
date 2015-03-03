package com.deepercreeper.vampireapp.host;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.activities.HostActivity;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.CreateStringDialog.CreationListener;
import com.deepercreeper.vampireapp.util.view.HostContextMenu;
import com.deepercreeper.vampireapp.util.view.HostContextMenu.HostListener;

public class HostController implements HostListener
{
	private static final String		TAG					= "HostController";
	
	private static final String		HOSTS_LIST			= "Hosts.lst";
	
	private static final String		DEFAULT_LOCATION	= "Germany";
	
	private final Map<String, Host>	mHostsCache			= new HashMap<String, Host>();
	
	private final List<String>		mHostNames			= new ArrayList<String>();
	
	private final ItemProvider		mItems;
	
	private final Activity			mContext;
	
	private LinearLayout			mHostsList;
	
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
			sortHosts();
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
	
	public void setHostsList(final LinearLayout aHostsList)
	{
		mHostsList = aHostsList;
	}
	
	@Override
	public void deleteHost(final String aName)
	{
		final File hostFile = new File(mContext.getFilesDir(), aName + ".hst");
		if ( !hostFile.delete())
		{
			Log.e(TAG, "Could not delete character file.");
		}
		mHostNames.remove(aName);
		sortHosts();
		saveHostsList();
	}
	
	public void saveHost(final Host aHost)
	{
		mHostNames.add(aHost.getName());
		FilesUtil.saveFile(aHost.serialize(), aHost.getName() + ".hst", mContext);
		sortHosts();
		saveHostsList();
	}
	
	public void sortHosts()
	{
		mHostsList.removeAllViews();
		
		Collections.sort(mHostNames);
		
		boolean first = true;
		
		for (final String host : mHostNames)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				final View line = new View(mContext);
				line.setLayoutParams(ViewUtil.getLine(mContext));
				line.setBackgroundColor(mContext.getResources().getColor(color.darker_gray));
				mHostsList.addView(line);
			}
			
			final TextView hostView = new TextView(mContext);
			hostView.setLayoutParams(ViewUtil.getWrapHeight());
			hostView.setText(host);
			hostView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			hostView.setLongClickable(true);
			hostView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(final View aV)
				{
					HostContextMenu.showCharacterContextMenu(HostController.this, mContext, host);
					return false;
				}
			});
			mHostsList.addView(hostView);
		}
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
	
	@Override
	public void playHost(final String aName)
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
