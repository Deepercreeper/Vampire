package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private static final String					DELIM			= ":";
	
	private static final int					ATTRIBUTES_HEIGHT	= 1140, ABILITIES_HEIGHT = 3150, ATTRIBUTES_TABLE_ID = 1, ABILITIES_TABLE_ID = 2;
	
	private final HashMap<String, Discipline>	mDisciplines		= new HashMap<>();
	
	private final HashMap<String, Clan>			mClans				= new HashMap<>();
	
	private final HashMap<String, Integer>		mClanGenerations	= new HashMap<>();
	
	private final List<String>					mClanNames			= new ArrayList<>();
	
	private final List<String>					mNatureAndBehavior	= new ArrayList<>();
	
	private final ItemHandler					mItems				= new ItemHandler();
	
	private CharCreator							mCreator;
	
	private AttributeListener					mAttributeListener;
	
	private boolean								mInitializedAttributes	= false, mInitializedAbilities = false;
	
	private boolean								mShowAttributes			= false, mShowAbilities = false;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		loadChars();
		final Button createChar = (Button) findViewById(R.id.createCharacterButton);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				createCharacter();
			}
		});
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
		mItems.init(getResources().getStringArray(R.array.attributes), getResources().getStringArray(R.array.abilities));
		// Initialize disciplines
		{
			for (final String discipline : getResources().getStringArray(R.array.disciplines))
			{
				final String[] disciplineData = discipline.split(DELIM);
				mDisciplines.put(disciplineData[0], new Discipline(disciplineData[0], disciplineData[1]));
			}
		}
		// Initialize clans
		{
			for (final String line : getResources().getStringArray(R.array.clan))
			{
				final String[] clanData = line.split(DELIM);
				final Clan clan = new Clan(clanData[0]);
				if (clanData.length > 1)
				{
					clan.addDisciplines(clanData[1]);
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
		// Initialize maximum creation values
		{
			CharCreator.init(getResources().getIntArray(R.array.attribute_points), getResources().getIntArray(R.array.ability_points));
		}
	}
	
	private void createCharacter()
	{
		setContentView(R.layout.create_character);
		
		mCreator = new CharCreator(mItems.createItems(true), mItems.createItems(false), mNatureAndBehavior.get(0), mNatureAndBehavior.get(0),
				mClans.get(mClanNames.get(0)));
		
		ArrayAdapter<String> adapter;
		
		final Spinner natureSpinner = (Spinner) findViewById(R.id.nature_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		natureSpinner.setAdapter(adapter);
		
		final Spinner behaviorSpinner = (Spinner) findViewById(R.id.behavior_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mNatureAndBehavior);
		behaviorSpinner.setAdapter(adapter);
		
		final NumberPicker generationPicker = (NumberPicker) findViewById(R.id.generation_picker);
		generationPicker.setMinValue(CharCreator.MIN_GENERATION);
		generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
		generationPicker.setValue(mCreator.getGeneration());
		
		final Spinner clanSpinner = (Spinner) findViewById(R.id.clan_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mClanNames);
		clanSpinner.setAdapter(adapter);
		clanSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				if (mClanGenerations.containsKey(aParent.getSelectedItem()))
				{
					final int generation = mClanGenerations.get(aParent.getSelectedItem());
					generationPicker.setMinValue(generation);
					generationPicker.setMaxValue(generation);
				}
				else
				{
					generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
					generationPicker.setMinValue(CharCreator.MIN_GENERATION);
				}
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Should not happen
			}
		});
		
		final LinearLayout attributesPanel = (LinearLayout) findViewById(R.id.attributes_panel);
		
		final Button showAttributes = (Button) findViewById(R.id.show_attributes);
		showAttributes.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mShowAttributes = !mShowAttributes;
				Animation animation;
				int arrowId;
				if (mShowAttributes)
				{
					if ( !mInitializedAttributes)
					{
						mInitializedAttributes = true;
						initAttributes(true, mCreator);
					}
					animation = new ResizeAnimation(attributesPanel, attributesPanel.getWidth(), attributesPanel.getHeight(), attributesPanel
							.getWidth(), ATTRIBUTES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(attributesPanel, attributesPanel.getWidth(), attributesPanel.getHeight(), attributesPanel
							.getWidth(), 0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showAttributes.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				attributesPanel.startAnimation(animation);
			}
		});
		
		final LinearLayout abilitiesPanel = (LinearLayout) findViewById(R.id.abilities_panel);
		final Button showAbilities = (Button) findViewById(R.id.show_abilities);
		showAbilities.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mShowAbilities = !mShowAbilities;
				Animation animation;
				int arrowId;
				if (mShowAbilities)
				{
					if ( !mInitializedAbilities)
					{
						mInitializedAbilities = true;
						initAttributes(false, mCreator);
					}
					animation = new ResizeAnimation(abilitiesPanel, abilitiesPanel.getWidth(), abilitiesPanel.getHeight(), abilitiesPanel.getWidth(),
							ABILITIES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(abilitiesPanel, abilitiesPanel.getWidth(), abilitiesPanel.getHeight(), abilitiesPanel.getWidth(),
							0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showAbilities.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				abilitiesPanel.startAnimation(animation);
			}
		});
	}
	
	private TableRow createItemRow(final Item aItem, final CharCreator aCreator)
	{
		final TableRow row = new TableRow(this);
		row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		final TextView name = new TextView(this);
		name.setText(aItem.getName());
		final int pixels = dpToPx(120);
		name.setLayoutParams(new TableRow.LayoutParams(pixels, LayoutParams.WRAP_CONTENT));
		name.setGravity(Gravity.CENTER_VERTICAL);
		name.setEllipsize(TruncateAt.END);
		name.setSingleLine();
		row.addView(name);
		
		final GridLayout grid = new GridLayout(this);
		grid.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		{
			final RadioButton[] radios = new RadioButton[CreationItem.MAX_VALUE];
			
			final LayoutParams params = new LayoutParams(dpToPx(30), dpToPx(30));
			params.gravity = Gravity.TOP;
			final ImageButton sub = new ImageButton(this);
			sub.setContentDescription("Sub");
			sub.setLayoutParams(params);
			sub.setImageResource(android.R.drawable.ic_media_previous);
			sub.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					aCreator.decreaseItem(aItem);
					applyValue(aCreator.getItem(aItem).getValue(), radios);
				}
			});
			grid.addView(sub);
			
			for (int i = 0; i < radios.length; i++ )
			{
				final RadioButton radio = new RadioButton(this);
				radio.setLayoutParams(new LayoutParams(dpToPx(25), LayoutParams.WRAP_CONTENT));
				radio.setClickable(false);
				grid.addView(radio);
				radios[i] = radio;
			}
			
			final ImageButton add = new ImageButton(this);
			add.setContentDescription("Add");
			add.setLayoutParams(params);
			add.setImageResource(android.R.drawable.ic_media_next);
			add.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					aCreator.increaseItem(aItem);
					applyValue(aCreator.getItem(aItem).getValue(), radios);
				}
			});
			applyValue(aItem.getStartValue(), radios);
			grid.addView(add);
		}
		row.addView(grid);
		return row;
	}
	
	private void applyValue(final int aValue, final RadioButton[] aRadios)
	{
		for (int i = 0; i < aRadios.length; i++ )
		{
			aRadios[i].setChecked(i < aValue);
		}
	}
	
	private int dpToPx(final int aDP)
	{
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aDP, getResources().getDisplayMetrics()));
	}
	
	private void initAttributes(final boolean aAttributes, final CharCreator aCreator)
	{
		final TableLayout table;
		table = new TableLayout(this);
		table.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		for (final String parent : mItems.getParents(aAttributes))
		{
			final TableRow row = new TableRow(this);
			row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			row.addView(new Space(this));
			final TextView parentView = new TextView(this);
			final LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			parentView.setLayoutParams(params);
			parentView.setText(parent);
			parentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			row.addView(parentView);
			
			table.addView(row);
			for (final Item item : mItems.getItems(parent, aAttributes))
			{
				table.addView(createItemRow(item, aCreator));
			}
		}
		final LinearLayout layout = (LinearLayout) findViewById(aAttributes ? R.id.attributes_panel : R.id.abilities_panel);
		layout.addView(table);
	}
	
	private void loadChars()
	{
		// final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	}
}
