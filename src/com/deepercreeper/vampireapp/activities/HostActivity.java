package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

public class HostActivity extends Activity implements ItemConsumer
{
	public static final int		PLAY_HOST_REQUEST	= 3;
	
	public static final String	HOST				= "HOST";
	
	private Host				mHost;
	
	private ItemProvider		mItems;
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		mHost = new Host(getIntent().getStringExtra(HOST), mItems);
		
		init();
	}
	
	private void init()
	{
		setContentView(R.layout.host);
		
		final TextView name = (TextView) findViewById(R.id.host_name);
		final Button exit = (Button) findViewById(R.id.exit);
		
		name.setText(mHost.getName());
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit();
			}
		});
	}
	
	private void exit()
	{
		final Intent intent = new Intent();
		intent.putExtra(HOST, mHost.serialize());
		setResult(RESULT_OK, intent);
		finish();
	}
}
