package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.Toast;

public class MainActivity extends Activity
{
	private static final String							DELIM				= ":";
	
	private static final int							ATTRIBUTES_HEIGHT	= 1140, ABILITIES_HEIGHT = 3150, DISCIPLINES_HEIGHT = 460,
			BACKGROUNDS_HEIGHT = 620, PROPERTIES_HEIGHT = 1000;
	
	private final HashMap<String, Discipline>			mDisciplines		= new HashMap<>();
	
	private final HashMap<String, Clan>					mClans				= new HashMap<>();
	
	private final HashMap<String, Integer>				mClanGenerations	= new HashMap<>();
	
	private final List<String>							mClanNames			= new ArrayList<>();
	
	private final List<String>							mNatureAndBehavior	= new ArrayList<>();
	
	private final ItemHandler							mItems				= new ItemHandler();
	
	private final HashMap<Item, HashSet<Discipline>>	mItemsUse			= new HashMap<>();
	
	private CharCreator									mCreator;
	
	private boolean										mInitializedClans	= false;
	
	private boolean										mInitializedAttributes	= false, mInitializedAbilities = false,
			mInitializedDisciplines = false;
	
	private boolean										mShowAttributes			= false, mShowAbilities = false, mShowDisciplines = false,
			mShowBackgrounds = false, mShowProperties = false;
	
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
		mItems.init(getResources().getStringArray(R.array.attributes), getResources().getStringArray(R.array.abilities), getResources()
				.getStringArray(R.array.backgrounds), getResources().getStringArray(R.array.properties));
		// Initialize disciplines
		{
			final HashSet<Discipline> parentDisciplines = new HashSet<>();
			for (final String disciplineLine : getResources().getStringArray(R.array.disciplines))
			{
				final String[] disciplineData = disciplineLine.split(DELIM);
				Discipline discipline;
				if (disciplineData[0].startsWith(Discipline.PARENT_DISCIPLINE))
				{
					discipline = new Discipline(disciplineData[0].substring(1), disciplineData[1], true);
					parentDisciplines.add(discipline);
				}
				else
				{
					discipline = new Discipline(disciplineData[0], disciplineData[1], false);
					for (final Ability abilty : discipline.getAbilities())
					{
						for (final String itemName : abilty.getCosts())
						{
							final Item item = mItems.getItem(itemName);
							HashSet<Discipline> uses = mItemsUse.get(item);
							if (uses == null)
							{
								uses = new HashSet<>();
								mItemsUse.put(item, uses);
							}
							uses.add(discipline);
						}
					}
				}
				mDisciplines.put(discipline.getName(), discipline);
			}
			for (final Discipline parentDiscipline : parentDisciplines)
			{
				for (final String subDisciplineName : parentDiscipline.getSubDisciplineNames())
				{
					final Discipline subDiscipline = mDisciplines.get(subDisciplineName);
					parentDiscipline.addSubDiscipline(subDiscipline);
					subDiscipline.setSubDisciplineOf(parentDiscipline);
					mDisciplines.remove(subDisciplineName);
				}
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
					for (final String clanDiscipline : clanData[1].split(Clan.CLAN_DISCIPLIN_DELIM))
					{
						clan.addDisciplines(mDisciplines.get(clanDiscipline));
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
						initItems(true);
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
						initItems(false);
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
		
		final LinearLayout disciplinesPanel = (LinearLayout) findViewById(R.id.disciplines_panel);
		final Button showDisciplines = (Button) findViewById(R.id.show_disciplines);
		showDisciplines.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mShowDisciplines = !mShowDisciplines;
				Animation animation;
				int arrowId;
				if (mShowDisciplines)
				{
					if ( !mInitializedDisciplines)
					{
						mInitializedDisciplines = true;
						initDisciplines();
					}
					animation = new ResizeAnimation(disciplinesPanel, disciplinesPanel.getWidth(), disciplinesPanel.getHeight(), disciplinesPanel
							.getWidth(), DISCIPLINES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(disciplinesPanel, disciplinesPanel.getWidth(), disciplinesPanel.getHeight(), disciplinesPanel
							.getWidth(), 0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showDisciplines.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				disciplinesPanel.startAnimation(animation);
			}
		});
		
		final LinearLayout backgroundsPanel = (LinearLayout) findViewById(R.id.backgrounds_panel);
		final Button showBackgrounds = (Button) findViewById(R.id.show_backgrounds);
		showBackgrounds.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mShowBackgrounds = !mShowBackgrounds;
				Animation animation;
				int arrowId;
				if (mShowBackgrounds)
				{
					animation = new ResizeAnimation(backgroundsPanel, backgroundsPanel.getWidth(), backgroundsPanel.getHeight(), backgroundsPanel
							.getWidth(), BACKGROUNDS_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(backgroundsPanel, backgroundsPanel.getWidth(), backgroundsPanel.getHeight(), backgroundsPanel
							.getWidth(), 0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showBackgrounds.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				backgroundsPanel.startAnimation(animation);
			}
		});
		
		final Button addBackground = (Button) findViewById(R.id.add_background);
		addBackground.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addBackground();
			}
		});
		
		final LinearLayout propertiesPanel = (LinearLayout) findViewById(R.id.properties_panel);
		final Button showProperties = (Button) findViewById(R.id.show_properties);
		showProperties.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mShowProperties = !mShowProperties;
				Animation animation;
				int arrowId;
				if (mShowProperties)
				{
					animation = new ResizeAnimation(propertiesPanel, propertiesPanel.getWidth(), propertiesPanel.getHeight(), propertiesPanel
							.getWidth(), PROPERTIES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(propertiesPanel, propertiesPanel.getWidth(), propertiesPanel.getHeight(), propertiesPanel
							.getWidth(), 0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showProperties.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				propertiesPanel.startAnimation(animation);
			}
		});
		
		final Button addProperty = (Button) findViewById(R.id.add_property);
		addProperty.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addProperty();
			}
		});
	}
	
	private void addProperty()
	{
		final DialogFragment newFragment = new SelectPropertyDialog(mCreator, this, mItems);
		newFragment.show(getFragmentManager(), "new property");
	}
	
	private void addBackground()
	{
		if (mCreator.getBackgroundsCount() < Background.NUMBER_BACKGROUNDS)
		{
			final DialogFragment newFragment = new SelectBackgroundDialog(mCreator, this, mItems);
			newFragment.show(getFragmentManager(), "new background");
		}
	}
	
	public void addPropertyRow(final Property aProperty)
	{
		final LinearLayout layout = (LinearLayout) findViewById(R.id.properties_panel);
		final GridLayout grid = new GridLayout(this);
		grid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setPropertyRow(grid, aProperty);
		
		layout.addView(grid);
	}
	
	public void setPropertyRow(final GridLayout aGrid, final Property aProperty)
	{
		aGrid.removeAllViews();
		final LayoutParams editParams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
		editParams.gravity = Gravity.CENTER_VERTICAL;
		
		final ImageButton edit = new ImageButton(this);
		edit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
		edit.setLayoutParams(editParams);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final DialogFragment newFragment = new SelectPropertyDialog(aGrid, mCreator, MainActivity.this, mItems, aProperty);
				newFragment.show(getFragmentManager(), "edit property");
			}
		});
		aGrid.addView(edit);
		
		final TextView name = new TextView(this);
		name.setText(aProperty.getName());
		name.setClickable(true);
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(MainActivity.this, aProperty.getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		name.setLayoutParams(new LayoutParams(dpToPx(80), LayoutParams.WRAP_CONTENT));
		name.setGravity(Gravity.CENTER_VERTICAL);
		name.setEllipsize(TruncateAt.END);
		name.setSingleLine();
		aGrid.addView(name);
		
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
				mCreator.decreaseProperty(aProperty);
				applyValue(mCreator.getProperty(aProperty).getValue(), radios);
			}
		});
		aGrid.addView(sub);
		
		for (int i = 0; i < radios.length; i++ )
		{
			final RadioButton radio = new RadioButton(this);
			radio.setLayoutParams(new LayoutParams(dpToPx(25), LayoutParams.WRAP_CONTENT));
			radio.setClickable(false);
			aGrid.addView(radio);
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
				mCreator.increaseProperty(aProperty);
				applyValue(mCreator.getProperty(aProperty).getValue(), radios);
			}
		});
		applyValue(mCreator.getProperty(aProperty).getValue(), radios);
		aGrid.addView(add);
	}
	
	public void addBackgroundRow(final Background aBackground)
	{
		final LinearLayout layout = (LinearLayout) findViewById(R.id.backgrounds_panel);
		final GridLayout grid = new GridLayout(this);
		grid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setBackgroundRow(grid, aBackground);
		
		layout.addView(grid);
	}
	
	public void setBackgroundRow(final GridLayout aGrid, final Background aBackground)
	{
		aGrid.removeAllViews();
		final LayoutParams editParams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
		editParams.gravity = Gravity.CENTER_VERTICAL;
		
		final ImageButton edit = new ImageButton(this);
		edit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
		edit.setLayoutParams(editParams);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final DialogFragment newFragment = new SelectBackgroundDialog(aGrid, mCreator, MainActivity.this, mItems, aBackground);
				newFragment.show(getFragmentManager(), "edit background");
			}
		});
		aGrid.addView(edit);
		
		final TextView name = new TextView(this);
		name.setText(aBackground.getName());
		name.setLayoutParams(new LayoutParams(dpToPx(80), LayoutParams.WRAP_CONTENT));
		name.setGravity(Gravity.CENTER_VERTICAL);
		name.setEllipsize(TruncateAt.END);
		name.setSingleLine();
		aGrid.addView(name);
		
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
				mCreator.decreaseBackground(aBackground);
				applyValue(mCreator.getBackground(aBackground).getValue(), radios);
			}
		});
		aGrid.addView(sub);
		
		for (int i = 0; i < radios.length; i++ )
		{
			final RadioButton radio = new RadioButton(this);
			radio.setLayoutParams(new LayoutParams(dpToPx(25), LayoutParams.WRAP_CONTENT));
			radio.setClickable(false);
			aGrid.addView(radio);
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
				mCreator.increaseBackground(aBackground);
				applyValue(mCreator.getBackground(aBackground).getValue(), radios);
			}
		});
		applyValue(mCreator.getBackground(aBackground).getValue(), radios);
		aGrid.addView(add);
	}
	
	private void initDisciplines()
	{
		final LinearLayout layout = (LinearLayout) findViewById(R.id.disciplines_panel);
		
		for (final Discipline discipline : mCreator.getClan().getDisciplines())
		{
			if (discipline.isParentDiscipline())
			{
				for (final GridLayout grid : createParentDisciplineRow(discipline))
				{
					layout.addView(grid);
				}
			}
			else
			{
				layout.addView(createDisciplinesRow(discipline));
			}
		}
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
		
		// Reload disciplines
		final LinearLayout disciplinesPanel = (LinearLayout) findViewById(R.id.disciplines_panel);
		if (mShowDisciplines)
		{
			mShowDisciplines = false;
			disciplinesPanel.startAnimation(new ResizeAnimation(disciplinesPanel, disciplinesPanel.getWidth(), disciplinesPanel.getHeight(),
					disciplinesPanel.getWidth(), 0));
			final Button showDisciplines = (Button) findViewById(R.id.show_disciplines);
			showDisciplines.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		disciplinesPanel.removeAllViews();
		mInitializedDisciplines = false;
		
		((Button) findViewById(R.id.show_disciplines)).setEnabled( !mClans.get(aClan).getDisciplines().isEmpty());
	}
	
	private GridLayout[] createParentDisciplineRow(final Discipline aDiscipline)
	{
		final GridLayout[] grids = new GridLayout[3];
		int i = 0;
		
		final LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(30));
		final LayoutParams editParams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
		params.gravity = Gravity.CENTER_VERTICAL;
		editParams.gravity = Gravity.CENTER_VERTICAL;
		
		grids[i] = new GridLayout(this);
		grids[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		final TextView name = new TextView(this);
		name.setText(aDiscipline.getName() + ":");
		name.setClickable(true);
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(MainActivity.this, aDiscipline.getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		name.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		name.setEllipsize(TruncateAt.END);
		name.setSingleLine();
		grids[i].addView(name);
		i++ ;
		
		grids[i] = new GridLayout(this);
		grids[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		final TextView first = new TextView(this);
		first.setText("1.");
		first.setLayoutParams(params);
		first.setGravity(Gravity.CENTER_VERTICAL);
		first.setSingleLine();
		grids[i].addView(first);
		
		final ImageButton editFirst = new ImageButton(this);
		editFirst.setImageResource(android.R.drawable.ic_menu_add);
		editFirst.setLayoutParams(editParams);
		editFirst.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final DialogFragment newFragment = new SelectSubDisciplineDialog(grids[1], mCreator, aDiscipline, MainActivity.this, true);
				newFragment.show(getFragmentManager(), "1. " + aDiscipline.getName());
			}
		});
		grids[i].addView(editFirst);
		i++ ;
		
		grids[i] = new GridLayout(this);
		grids[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		final TextView second = new TextView(this);
		second.setText("2.");
		second.setLayoutParams(params);
		second.setSingleLine();
		second.setGravity(Gravity.CENTER_VERTICAL);
		grids[i].addView(second);
		
		final ImageButton editSecond = new ImageButton(this);
		editSecond.setLayoutParams(editParams);
		editSecond.setImageResource(android.R.drawable.ic_menu_add);
		editSecond.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final DialogFragment newFragment = new SelectSubDisciplineDialog(grids[2], mCreator, aDiscipline, MainActivity.this, false);
				newFragment.show(getFragmentManager(), "2. " + aDiscipline.getName());
			}
		});
		grids[i].addView(editSecond);
		
		return grids;
	}
	
	public void applySubDisciplines(final GridLayout aGrid, final Discipline aDiscipline, final boolean aFirst)
	{
		aGrid.removeAllViews();
		
		final LayoutParams editParams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
		editParams.gravity = Gravity.CENTER_VERTICAL;
		
		final TextView number = new TextView(this);
		number.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		number.setText(aFirst ? "1." : "2.");
		number.setSingleLine();
		number.setGravity(Gravity.CENTER_VERTICAL);
		aGrid.addView(number);
		
		final ImageButton edit = new ImageButton(this);
		edit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
		edit.setLayoutParams(editParams);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final DialogFragment newFragment = new SelectSubDisciplineDialog(aGrid, mCreator, aDiscipline, MainActivity.this, aFirst);
				newFragment.show(getFragmentManager(), (aFirst ? "1. " : "2. ") + aDiscipline.getName());
			}
		});
		aGrid.addView(edit);
		
		if (mCreator.getDiscipline(aDiscipline).hasSubDiscipline(aFirst))
		{
			final TextView name = new TextView(this);
			name.setText(mCreator.getDiscipline(aDiscipline).getSubDiscipline(aFirst).getDiscipline().getName());
			name.setClickable(true);
			name.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					Toast.makeText(MainActivity.this, mCreator.getDiscipline(aDiscipline).getSubDiscipline(aFirst).getDiscipline().getDescription(),
							Toast.LENGTH_LONG).show();
				}
			});
			name.setLayoutParams(new LayoutParams(dpToPx(80), LayoutParams.WRAP_CONTENT));
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setEllipsize(TruncateAt.END);
			name.setSingleLine();
			aGrid.addView(name);
			
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
					mCreator.decreaseSubDiscipline(aDiscipline, aFirst);
					applyValue(mCreator.getDiscipline(aDiscipline).getSubDiscipline(aFirst).getValue(), radios);
				}
			});
			aGrid.addView(sub);
			
			for (int i = 0; i < radios.length; i++ )
			{
				final RadioButton radio = new RadioButton(this);
				radio.setLayoutParams(new LayoutParams(dpToPx(25), LayoutParams.WRAP_CONTENT));
				radio.setClickable(false);
				aGrid.addView(radio);
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
					mCreator.increaseSubDiscipline(aDiscipline, aFirst);
					applyValue(mCreator.getDiscipline(aDiscipline).getSubDiscipline(aFirst).getValue(), radios);
				}
			});
			applyValue(mCreator.getDiscipline(aDiscipline).getValue(), radios);
			aGrid.addView(add);
		}
	}
	
	private GridLayout createDisciplinesRow(final Discipline aDiscipline)
	{
		final GridLayout grid = new GridLayout(this);
		grid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		final TextView name = new TextView(this);
		name.setText(aDiscipline.getName());
		name.setClickable(true);
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(MainActivity.this, aDiscipline.getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		final int pixels = dpToPx(122);
		name.setLayoutParams(new LinearLayout.LayoutParams(pixels, LayoutParams.WRAP_CONTENT));
		name.setGravity(Gravity.CENTER_VERTICAL);
		name.setEllipsize(TruncateAt.END);
		name.setSingleLine();
		grid.addView(name);
		
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
				mCreator.decreaseDiscipline(aDiscipline);
				applyValue(mCreator.getDiscipline(aDiscipline).getValue(), radios);
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
				mCreator.increaseDiscipline(aDiscipline);
				applyValue(mCreator.getDiscipline(aDiscipline).getValue(), radios);
			}
		});
		applyValue(mCreator.getDiscipline(aDiscipline).getValue(), radios);
		grid.addView(add);
		return grid;
	}
	
	private TableRow createItemRow(final Item aItem)
	{
		final TableRow row = new TableRow(this);
		row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		final TextView name = new TextView(this);
		name.setText(aItem.getName());
		name.setClickable(true);
		name.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(MainActivity.this, createUseText(aItem), Toast.LENGTH_LONG).show();
			}
		});
		final int pixels = dpToPx(122);
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
					mCreator.decreaseItem(aItem);
					applyValue(mCreator.getItem(aItem).getValue(), radios);
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
					mCreator.increaseItem(aItem);
					applyValue(mCreator.getItem(aItem).getValue(), radios);
				}
			});
			applyValue(aItem.getStartValue(), radios);
			grid.addView(add);
		}
		row.addView(grid);
		return row;
	}
	
	private String createUseText(final Item aItem)
	{
		final StringBuilder uses = new StringBuilder();
		if (mItemsUse.get(aItem) == null)
		{
			uses.append(aItem.getName());
		}
		else
		{
			uses.append(aItem.getName() + ": ");
			boolean first = true;
			for (final Discipline discipline : mItemsUse.get(aItem))
			{
				if (first)
				{
					uses.append(discipline.getName());
					first = false;
				}
				else
				{
					uses.append(", " + discipline.getName());
				}
			}
		}
		return uses.toString();
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
	
	private void initItems(final boolean aAttribute)
	{
		final TableLayout table;
		table = new TableLayout(this);
		table.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		for (final String parent : mItems.getParents(aAttribute))
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
			for (final Item item : mItems.getItems(parent, aAttribute))
			{
				table.addView(createItemRow(item));
			}
		}
		final LinearLayout layout = (LinearLayout) findViewById(aAttribute ? R.id.attributes_panel : R.id.abilities_panel);
		layout.addView(table);
	}
	
	private void loadChars()
	{
		// final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	}
}
