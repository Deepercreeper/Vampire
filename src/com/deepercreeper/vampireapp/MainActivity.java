package com.deepercreeper.vampireapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * The main activity is the start class for the vampire app.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class MainActivity extends Activity
{
	private Vampire	mVampire;
	
	/**
	 * Finds the view with the given id.
	 * 
	 * @param aId
	 *            The view id.
	 * @return the view with the given id.
	 */
	public View getView(final int aId)
	{
		return findViewById(aId);
	}
	
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
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVampire = new Vampire(this);
		loadChars();
	}
	
	private void loadChars()
	{
		// final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	}
}
