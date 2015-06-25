package com.deepercreeper.vampireapp.host;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.activities.CreateHostActivity;
import com.deepercreeper.vampireapp.activities.HostActivity;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.HostContextMenu;
import com.deepercreeper.vampireapp.util.view.HostContextMenu.HostListener;

/**
 * This controller handles all existing hosts. They can be saved, loaded and played.
 * 
 * @author vrl
 */
public class HostController implements HostListener
{
	/**
	 * The default host location. TODO Maybe remove this field since it won't be used.
	 */
	public static final String			DEFAULT_LOCATION	= "Germany";
	
	private static final String			TAG					= "HostController";
	
	private static final String			HOSTS_LIST			= "Hosts.lst";
	
	private final ConnectionController	mConnection;
	
	private final Map<String, Host>		mHostsCache			= new HashMap<String, Host>();
	
	private final List<String>			mHostNames			= new ArrayList<String>();
	
	private final ItemProvider			mItems;
	
	private final Activity				mContext;
	
	private LinearLayout				mHostsList;
	
	/**
	 * Creates a new host controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aItems
	 *            The item provider.
	 * @param aConnection
	 *            The connection controller.
	 */
	public HostController(final Activity aContext, final ItemProvider aItems, final ConnectionController aConnection)
	{
		mItems = aItems;
		mContext = aContext;
		mConnection = aConnection;
	}
	
	/**
	 * Creates a new host by asking the user.
	 */
	public void createHost()
	{
		final Intent intent = new Intent(mContext, CreateHostActivity.class);
		intent.putExtra(CreateHostActivity.HOST_NAMES, getHostNames().toArray(new String[getHostNames().size()]));
		mContext.startActivityForResult(intent, CreateHostActivity.CREATE_HOST_REQUEST);
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
	
	/**
	 * @return a list of all host names.
	 */
	public List<String> getHostNames()
	{
		return mHostNames;
	}
	
	/**
	 * Loads the given host from its file.
	 * 
	 * @param aName
	 *            The host name.
	 * @return the loaded host.
	 */
	public Host loadHost(final String aName)
	{
		Host host = mHostsCache.get(aName);
		if (host == null)
		{
			final String data = FilesUtil.loadFile(aName + ".hst", mContext);
			if (data != null)
			{
				host = new Host(data, mItems, mContext);
			}
		}
		if (host == null)
		{
			return null;
		}
		mHostsCache.put(aName, host);
		return host;
	}
	
	/**
	 * Loads all saved hosts from stored files.
	 */
	public void loadHosts()
	{
		final String data = FilesUtil.loadFile(HOSTS_LIST, mContext);
		if (data != null && !data.trim().isEmpty())
		{
			mHostNames.clear();
			for (final String host : data.split("\n"))
			{
				mHostNames.add(host);
			}
			sortHosts();
		}
	}
	
	@Override
	public void playHost(final String aName)
	{
		final Host host = loadHost(aName);
		
		final Intent intent = new Intent(mContext, HostActivity.class);
		intent.putExtra(HostActivity.HOST, FilesUtil.serialize(host));
		
		mContext.startActivityForResult(intent, HostActivity.PLAY_HOST_REQUEST);
	}
	
	/**
	 * Saves the given host to a file.
	 * 
	 * @param aHost
	 */
	public void saveHost(final Host aHost)
	{
		mHostsCache.put(aHost.getName(), aHost);
		if ( !mHostNames.contains(aHost.getName()))
		{
			mHostNames.add(aHost.getName());
		}
		FilesUtil.saveFile(FilesUtil.serialize(aHost), aHost.getName() + ".hst", mContext);
		sortHosts();
		saveHostsList();
	}
	
	/**
	 * Saves the list of hosts to a list file.
	 */
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
	
	/**
	 * Sets whether hosts can be played.
	 * 
	 * @param aEnabled
	 *            Whether any connection is able to make hosts be played.
	 */
	public void setHostsEnabled(final boolean aEnabled)
	{
		for (int i = 0; i < mHostsList.getChildCount(); i++ )
		{
			final View view = mHostsList.getChildAt(i);
			if (view instanceof TextView)
			{
				view.setEnabled(aEnabled);
			}
		}
	}
	
	/**
	 * Sets the hosts list view.
	 * 
	 * @param aHostsList
	 */
	public void setHostsList(final LinearLayout aHostsList)
	{
		mHostsList = aHostsList;
	}
	
	/**
	 * Sorts the display position of all hosts.
	 */
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
				mHostsList.addView(View.inflate(mContext, R.layout.horizontal_line, null));
			}
			
			final TextView hostView = new TextView(mContext);
			hostView.setLayoutParams(ViewUtil.getWrapHeight());
			hostView.setText(host);
			hostView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			hostView.setLongClickable(true);
			hostView.setEnabled(mConnection.isEnabled());
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
	
	/**
	 * Updates the data of the given host.
	 * 
	 * @param aHost
	 *            The host to update.
	 */
	public void updateHost(final Host aHost)
	{
		saveHost(aHost);
	}
}
