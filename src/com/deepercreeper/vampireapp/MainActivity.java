package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.controller.BackgroundController;
import com.deepercreeper.vampireapp.controller.DisciplineController;
import com.deepercreeper.vampireapp.controller.PropertyController;
import com.deepercreeper.vampireapp.controller.SimpleController;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class MainActivity extends Activity
{
	private static final String	DELIM	= ":";
	
	private enum State
	{
		MAIN, CREATION, FREE_POINTS;
	}
	
	private DisciplineController			mDisciplines;
	
	private PropertyController				mProperties;
	
	private SimpleController				mSimpleItems;
	
	private BackgroundController			mBackgrounds;
	
	private State							mState;
	
	private final HashMap<String, Clan>		mClans				= new HashMap<String, Clan>();
	
	private final HashMap<String, Integer>	mClanGenerations	= new HashMap<String, Integer>();
	
	private final List<String>				mClanNames			= new ArrayList<String>();
	
	private final List<String>				mNatureAndBehavior	= new ArrayList<String>();
	
	private CharCreator						mCreator;
	
	private boolean							mInitializedClans	= false;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init();
		loadChars();
		initMain();
	}
	
	private void initMain()
	{
		release();
		setContentView(R.layout.activity_main);
		mState = State.MAIN;
		final Button createChar = (Button) findViewById(R.id.createCharacterButton);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				initCreateCharacter(new CharCreator(MainActivity.this, mDisciplines, mProperties, mBackgrounds, mSimpleItems, mNatureAndBehavior
						.get(0), mNatureAndBehavior.get(0), mClans.get(mClanNames.get(0))));
			}
		});
	}
	
	private void release()
	{
		if (mCreator != null)
		{
			mCreator.release();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		switch (mState)
		{
			case CREATION :
				mCreator = null;
				initMain();
				break;
			case FREE_POINTS :
				initCreateCharacter(mCreator);
				break;
			case MAIN :
				super.onBackPressed();
				break;
			default :
				break;
		}
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
	
	private void init()
	{
		ViewUtil.init(this);
		
		mDisciplines = new DisciplineController(getResources());
		mProperties = new PropertyController(getResources());
		mBackgrounds = new BackgroundController(getResources());
		mSimpleItems = new SimpleController(getResources());
		
		// Initialize clans
		{
			for (final String line : getResources().getStringArray(R.array.clan))
			{
				final String[] clanData = line.split(DELIM);
				final Clan clan = new Clan(clanData[0]);
				if (clanData.length > 1)
				{
					for (final String clanDiscipline : clanData[1].split(Clan.CLAN_DISCIPLIN_DELIM))
					{
						clan.addDiscipline(mDisciplines.getDisciplines().getItem(clanDiscipline));
					}
				}
				mClans.put(clanData[0], clan);
				mClanNames.add(clanData[0]);
			}
			Collections.sort(mClanNames);
		}
		// Initialize clan generations
		{
			for (final String line : getResources().getStringArray(R.array.clan_generations))
			{
				final String[] data = line.split(DELIM);
				mClanGenerations.put(data[0], Integer.parseInt(data[1]));
			}
		}
		// Initialize nature and behavior
		{
			for (final String natureAndBehavior : getResources().getStringArray(R.array.nature_and_behavior))
			{
				mNatureAndBehavior.add(natureAndBehavior);
			}
			Collections.sort(mNatureAndBehavior);
		}
	}
	
	private void initCreateCharacter(final CharCreator aCreator)
	{
		release();
		mCreator = aCreator;
		setContentView(R.layout.create_character);
		mState = State.CREATION;
		createCharacter();
	}
	
	private void createCharacter()
	{
		ArrayAdapter<String> adapter;
		
		final Spinner natureSpinner = (Spinner) findViewById(R.id.nature_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		natureSpinner.setAdapter(adapter);
		natureSpinner.setSelection(mNatureAndBehavior.indexOf(mCreator.getNature()));
		
		final Spinner behaviorSpinner = (Spinner) findViewById(R.id.behavior_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		behaviorSpinner.setAdapter(adapter);
		behaviorSpinner.setSelection(mNatureAndBehavior.indexOf(mCreator.getBehavior()));
		
		final NumberPicker generationPicker = (NumberPicker) findViewById(R.id.generation_picker);
		generationPicker.setMinValue(CharCreator.MIN_GENERATION);
		generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
		generationPicker.setValue(mCreator.getGeneration());
		generationPicker.setOnValueChangedListener(new OnValueChangeListener()
		{
			@Override
			public void onValueChange(final NumberPicker aPicker, final int aOldVal, final int aNewVal)
			{
				mCreator.setGeneration(aNewVal);
			}
		});
		
		mInitializedClans = false;
		
		final Spinner clanSpinner = (Spinner) findViewById(R.id.clan_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mClanNames);
		clanSpinner.setAdapter(adapter);
		clanSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				clanChanged((String) aParent.getSelectedItem());
				if (mInitializedClans)
				{
					Toast.makeText(MainActivity.this, mCreator.getClan().getDescription(), Toast.LENGTH_LONG).show();
				}
				else
				{
					mInitializedClans = true;
				}
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Do nothing
			}
		});
		clanSpinner.setSelection(mClanNames.indexOf(mCreator.getClan().getName()));
		
		final LinearLayout simpleItemsPanel = (LinearLayout) findViewById(R.id.simple_items_panel);
		mCreator.getSimpleValues().initLayout(simpleItemsPanel);
		
		final LinearLayout disciplinesPanel = (LinearLayout) findViewById(R.id.disciplines_panel);
		mCreator.getDisciplines().initLayout(disciplinesPanel);
		
		final LinearLayout backgroundsPanel = (LinearLayout) findViewById(R.id.backgrounds_panel);
		mCreator.getBackgrounds().initLayout(backgroundsPanel);
		
		final LinearLayout propertiesPanel = (LinearLayout) findViewById(R.id.properties_panel);
		mCreator.getProperties().initLayout(propertiesPanel);
		
		final Button nextButton = (Button) findViewById(R.id.next_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				initBonusPoints();
			}
		});
	}
	
	private void initBonusPoints()
	{
		release();
		setContentView(R.layout.free_points_view);
		mState = State.FREE_POINTS;
		
		final int freePoints = getResources().getInteger(R.integer.free_points);
		
		final TextView pointsText = (TextView) findViewById(R.id.free_points_text);
		pointsText.setText("" + freePoints);
		
		final ProgressBar pointsBar = (ProgressBar) findViewById(R.id.free_points_bar);
		pointsBar.setMax(freePoints);
		pointsBar.setProgress(freePoints);
		
		final Button showDescriptions = (Button) findViewById(R.id.show_descriptions_button);
		showDescriptions.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCreator.setFreePoints(Math.max(0, mCreator.getFreePoints() - 1));
				pointsText.setText("" + mCreator.getFreePoints());
				pointsBar.setProgress(mCreator.getFreePoints());
			}
		});
		
		mCreator.setFreePoints(freePoints);
	}
	
	private void clanChanged(final String aClan)
	{
		mCreator.setClan(mClans.get(aClan));
		
		// Reload generations
		final NumberPicker generationPicker = (NumberPicker) findViewById(R.id.generation_picker);
		if (mClanGenerations.containsKey(aClan))
		{
			final int generation = mClanGenerations.get(aClan);
			generationPicker.setMinValue(generation);
			generationPicker.setMaxValue(generation);
		}
		else
		{
			generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
			generationPicker.setMinValue(CharCreator.MIN_GENERATION);
		}
	}
	
	private void loadChars()
	{
		// final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	}
}
