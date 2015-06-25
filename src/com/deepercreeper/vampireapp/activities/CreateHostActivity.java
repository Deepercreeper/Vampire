package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * Used to create a new host.
 * 
 * @author Vincent
 */
public class CreateHostActivity extends Activity
{
	/**
	 * The request code for a new host creation.
	 */
	public static final int		CREATE_HOST_REQUEST	= 4;
	
	/**
	 * The key for all existing host names.
	 */
	public static final String	HOST_NAMES			= "HOST_NAMES";
	
	/**
	 * The key for the created host name.
	 */
	public static final String	HOST_NAME			= "HOST_NAME";
	
	private String[]			mHostNames;
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		init();
	}
	
	private void exit(final String aName)
	{
		if (aName == null)
		{
			setResult(RESULT_CANCELED);
		}
		else
		{
			final Intent intent = new Intent();
			intent.putExtra(HOST_NAME, aName);
			setResult(RESULT_OK, intent);
		}
		finish();
	}
	
	private void init()
	{
		mHostNames = getIntent().getStringArrayExtra(HOST_NAMES);
		
		setContentView(R.layout.create_host);
		
		final EditText name = (EditText) findViewById(R.id.host_name);
		final Button finish = (Button) findViewById(R.id.finish);
		final Button back = (Button) findViewById(R.id.back);
		
		name.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{}
			
			@Override
			public void afterTextChanged(final Editable aS)
			{
				updateButtons();
			}
		});
		finish.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit(name.getText().toString().trim());
			}
		});
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit(null);
			}
		});
		
		updateButtons();
	}
	
	private void updateButtons()
	{
		boolean enabled = true;
		final String name = ((EditText) findViewById(R.id.host_name)).getText().toString().trim();
		if (name.isEmpty())
		{
			enabled = false;
		}
		else for (final String hostName : mHostNames)
		{
			if (name.equals(hostName))
			{
				enabled = false;
				break;
			}
		}
		ViewUtil.setEnabled(findViewById(R.id.finish), enabled);
	}
	
	@Override
	public void onBackPressed()
	{
		exit(null);
	}
}
