package com.deepercreeper.vampireapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main activity is the start class for the vampire app.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class VampireActivity extends Activity
{
	private Vampire	mVampire;
	
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
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVampire = new Vampire(this);
	}
}
