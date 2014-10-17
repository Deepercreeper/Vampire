package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.controller.BackgroundController;
import com.deepercreeper.vampireapp.controller.Mode;
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
		mCreator = null;
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
		mCreator.setCreationMode(Mode.CREATION);
		mState = State.CREATION;
		createCharacter();
	}
	
	private void createCharacter()
	{
		final TextView nameView = (TextView) findViewById(R.id.char_name_text);
		nameView.setText(mCreator.getName());
		nameView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				return;
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				return;
			}
			
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCreator.setName(nameView.getText().toString());
			}
		});
		
		final TextView conceptView = (TextView) findViewById(R.id.concept_text);
		conceptView.setText(mCreator.getConcept());
		conceptView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				return;
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				return;
			}
			
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCreator.setConcept(conceptView.getText().toString());
			}
		});
		
		ArrayAdapter<String> adapter;
		
		final Spinner natureSpinner = (Spinner) findViewById(R.id.nature_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		natureSpinner.setAdapter(adapter);
		natureSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mCreator.setNature(mNatureAndBehavior.get(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				return;
			}
		});
		natureSpinner.setSelection(mNatureAndBehavior.indexOf(mCreator.getNature()));
		
		final Spinner behaviorSpinner = (Spinner) findViewById(R.id.behavior_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		behaviorSpinner.setAdapter(adapter);
		behaviorSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mCreator.setBehavior(mNatureAndBehavior.get(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				return;
			}
		});
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
		
		final Button backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				initMain();
			}
		});
	}
	
	private void initBonusPoints()
	{
		release();
		setContentView(R.layout.free_points_view);
		mState = State.FREE_POINTS;
		mCreator.setCreationMode(Mode.FREE_POINTS);
		
		final ProgressBar pointsBar = (ProgressBar) findViewById(R.id.free_points_bar);
		pointsBar.setMax(CharCreator.START_FREE_POINTS);
		
		setFreePoints(mCreator.getFreePoints());
		setVolitionPoints(mCreator.getVolitionPoints());
		
		final Button showDescriptions = (Button) findViewById(R.id.show_descriptions_button);
		showDescriptions.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				// TODO Go to the description step.
			}
		});
		
		final LinearLayout simpleItemsPanel = (LinearLayout) findViewById(R.id.free_points_simple_items_panel);
		mCreator.getSimpleValues().initLayout(simpleItemsPanel);
		
		final LinearLayout disciplinesPanel = (LinearLayout) findViewById(R.id.free_points_disciplines_panel);
		mCreator.getDisciplines().initLayout(disciplinesPanel);
		
		final LinearLayout backgroundsPanel = (LinearLayout) findViewById(R.id.free_points_backgrounds_panel);
		mCreator.getBackgrounds().initLayout(backgroundsPanel);
		
		final ImageButton decreaseVolition = (ImageButton) findViewById(R.id.decrease_volition);
		decreaseVolition.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCreator.decreaseVolitionPoints();
			}
		});
		
		final ImageButton increaseVolition = (ImageButton) findViewById(R.id.increase_volition);
		increaseVolition.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCreator.increaseVolitionPoints();
			}
		});
		
		final Button showCreation = (Button) findViewById(R.id.show_creation_button);
		showCreation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCreator.resetFreePoints();
				initCreateCharacter(mCreator);
			}
		});
		
		final Button resetTempPoints = (Button) findViewById(R.id.reset_temp_points_button);
		resetTempPoints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCreator.resetFreePoints();
				setFreePoints(mCreator.getFreePoints());
			}
		});
	}
	
	public void setVolitionChangeEnabled(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		((ImageButton) findViewById(R.id.decrease_volition)).setEnabled(aCanDecrease);
		((ImageButton) findViewById(R.id.increase_volition)).setEnabled(aCanIncrease);
	}
	
	public void setVolitionPoints(final int aValue)
	{
		((TextView) findViewById(R.id.volition_value)).setText("" + aValue);
	}
	
	public void setFreePoints(final int aValue)
	{
		((TextView) findViewById(R.id.free_points_text)).setText("" + aValue);
		((ProgressBar) findViewById(R.id.free_points_bar)).setProgress(aValue);
		((Button) findViewById(R.id.show_descriptions_button)).setEnabled(aValue == 0);
	}
	
	private void clanChanged(final String aClan)
	{
		mCreator.setClan(mClans.get(aClan), false);
		
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
