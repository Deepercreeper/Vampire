package com.deepercreeper.vampireapp.host;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.activities.CreateHostActivity;
import com.deepercreeper.vampireapp.activities.HostActivity;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.HostContextMenu;
import com.deepercreeper.vampireapp.util.view.HostContextMenu.HostListener;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This controller handles all existing hosts. They can be saved, loaded and played.
 * 
 * @author vrl
 */
public class HostController implements HostListener
{
	private static final String TAG = "HostController";
	
	private static final String HOSTS_LIST = "Hosts.lst";
	
	private final Map<String, Host> mHostsCache = new HashMap<String, Host>();
	
	private final List<String> mHostNames = new ArrayList<String>();
	
	private final Activity mContext;
	
	private LinearLayout mHostsList;
	
	/**
	 * Creates a new host controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public HostController(final Activity aContext)
	{
		mContext = aContext;
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
			final String data = DataUtil.loadFile(aName + ".hst", mContext);
			if (data != null)
			{
				host = new Host(data, mContext, true);
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
		final String data = DataUtil.loadFile(HOSTS_LIST, mContext);
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
		intent.putExtra(HostActivity.HOST, DataUtil.serialize(host));
		
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
		DataUtil.saveFile(DataUtil.serialize(aHost), aHost.getName() + ".hst", mContext);
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
		
		DataUtil.saveFile(hostNames.toString(), HOSTS_LIST, mContext);
	}
	
	/**
	 * Sets whether hosts can be played.
	 * 
	 * @param aEnabled
	 *            Whether any connection is able to make hosts be played.
	 */
	public void setHostsEnabled(final boolean aEnabled)
	{
		if (mHostsList == null)
		{
			return;
		}
		for (int i = 0; i < mHostsList.getChildCount(); i++ )
		{
			final View view = mHostsList.getChildAt(i);
			if (view instanceof LinearLayout)
			{
				final Button button = (Button) view.findViewById(R.id.compound_play_host_button);
				ViewUtil.setEnabled(button, aEnabled);
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
		
		for (final String host : mHostNames)
		{
			final LinearLayout hostCompound = (LinearLayout) View.inflate(mContext, R.layout.view_host_compound, null);
			
			final TextView hostName = (TextView) hostCompound.findViewById(R.id.compound_host_name_label);
			final Button playHost = (Button) hostCompound.findViewById(R.id.compound_play_host_button);
			
			hostName.setText(host);
			hostCompound.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(final View aV)
				{
					HostContextMenu.showCharacterContextMenu(HostController.this, mContext, host);
					return true;
				}
			});
			// TODO Make sure and remove
			// playHost.setEnabled(mConnection.isEnabled());
			playHost.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					playHost(host);
				}
			});
			mHostsList.addView(hostCompound);
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
