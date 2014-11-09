package com.deepercreeper.vampireapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.ClanController;
import com.deepercreeper.vampireapp.controller.NatureController;
import com.deepercreeper.vampireapp.controller.PathController;
import com.deepercreeper.vampireapp.controller.backgrounds.BackgroundController;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;
import com.deepercreeper.vampireapp.controller.properties.PropertyController;
import com.deepercreeper.vampireapp.controller.simplesItems.SimpleController;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class Vampire
{
	private enum State
	{
		MAIN, CREATE_CHAR_1, CREATE_CHAR_2, CREATE_CHAR_3
	}
	
	private final MainActivity			mActivity;
	
	private final DisciplineController	mDisciplines;
	
	private final PropertyController	mProperties;
	
	private final SimpleController		mSimpleItems;
	
	private final BackgroundController	mBackgrounds;
	
	private final NatureController		mNatures;
	
	private final ClanController		mClans;
	
	private final PathController		mPaths;
	
	private CharCreator					mCharCreator;
	
	private State						mState;
	
	public Vampire(final MainActivity aActivity)
	{
		mActivity = aActivity;
		mDisciplines = new DisciplineController(mActivity.getResources());
		mProperties = new PropertyController(mActivity.getResources());
		mBackgrounds = new BackgroundController(mActivity.getResources());
		mSimpleItems = new SimpleController(mActivity.getResources());
		mClans = new ClanController(mActivity.getResources(), mDisciplines);
		mPaths = new PathController(mActivity.getResources());
		mNatures = new NatureController(mActivity.getResources());
		
		ViewUtil.init(mActivity);
		
		setState(State.MAIN);
	}
	
	public void setState(final State aState)
	{
		mState = aState;
		switch (mState)
		{
			case MAIN :
				initMain();
				break;
			case CREATE_CHAR_1 :
				initCreateChar1();
				break;
			case CREATE_CHAR_2 :
				initCreateChar2();
				break;
			case CREATE_CHAR_3 :
				break;
		}
	}
	
	public void back()
	{
		switch (mState)
		{
			case CREATE_CHAR_1 :
				initMain();
				break;
			case CREATE_CHAR_2 :
				initCreateChar1();
				break;
			case CREATE_CHAR_3 :
				initCreateChar2();
				break;
			case MAIN :
				mActivity.finish();
				break;
		}
	}
	
	private void initMain()
	{
		mCharCreator = null;
		mActivity.setContentView(R.layout.activity_main);
		final Button createChar = (Button) mActivity.getView(R.id.createCharacterButton);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_1);
			}
		});
	}
	
	private void initCreateChar1()
	{
		mActivity.setContentView(R.layout.create_character);
		if (mCharCreator == null)
		{
			mCharCreator = new CharCreator(this, mDisciplines, mProperties, mBackgrounds, mSimpleItems, mNatures.getFirst(), mNatures.getFirst(),
					mClans.getFirst());
		}
		else
		{
			mCharCreator.releaseViews();
		}
		
		mCharCreator.setCreationMode(CharMode.MAIN);
		
		final TextView nameTextView = (TextView) mActivity.getView(R.id.char_name_text);
		nameTextView.setText(mCharCreator.getName());
		nameTextView.addTextChangedListener(new TextWatcher()
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
				mCharCreator.setName(nameTextView.getText().toString());
			}
		});
		
		final TextView conceptTextView = (TextView) mActivity.getView(R.id.concept_text);
		conceptTextView.setText(mCharCreator.getConcept());
		conceptTextView.addTextChangedListener(new TextWatcher()
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
				mCharCreator.setConcept(conceptTextView.getText().toString());
			}
		});
		
		final Spinner natureSpinner = (Spinner) mActivity.getView(R.id.nature_spinner);
		natureSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mNatures.getNatures()));
		natureSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mCharCreator.setNature(mNatures.get(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				return;
			}
		});
		natureSpinner.setSelection(mNatures.indexOf(mCharCreator.getNature()));
		
		final Spinner behaviorSpinner = (Spinner) mActivity.getView(R.id.behavior_spinner);
		behaviorSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mNatures.getNatures()));
		behaviorSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mCharCreator.setBehavior(mNatures.get(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				return;
			}
		});
		behaviorSpinner.setSelection(mNatures.indexOf(mCharCreator.getBehavior()));
		
		final NumberPicker generationPicker = (NumberPicker) mActivity.getView(R.id.generation_picker);
		generationPicker.setMinValue(CharCreator.MIN_GENERATION);
		generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
		generationPicker.setValue(mCharCreator.getGeneration());
		generationPicker.setOnValueChangedListener(new OnValueChangeListener()
		{
			@Override
			public void onValueChange(final NumberPicker aPicker, final int aOldVal, final int aNewVal)
			{
				mCharCreator.setGeneration(aNewVal);
			}
		});
		
		final Spinner clanSpinner = (Spinner) mActivity.getView(R.id.clan_spinner);
		clanSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mClans.getClanNames()));
		clanSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				setClan(aPosition);
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Do nothing
			}
		});
		clanSpinner.setSelection(mClans.indexOf(mCharCreator.getClan()));
		
		final LinearLayout simpleItemsPanel = (LinearLayout) mActivity.getView(R.id.simple_items_panel);
		mCharCreator.getSimpleValues().initLayout(simpleItemsPanel);
		
		final LinearLayout disciplinesPanel = (LinearLayout) mActivity.getView(R.id.disciplines_panel);
		mCharCreator.getDisciplines().initLayout(disciplinesPanel);
		
		final LinearLayout backgroundsPanel = (LinearLayout) mActivity.getView(R.id.backgrounds_panel);
		mCharCreator.getBackgrounds().initLayout(backgroundsPanel);
		
		final LinearLayout propertiesPanel = (LinearLayout) mActivity.getView(R.id.properties_panel);
		mCharCreator.getProperties().initLayout(propertiesPanel);
		
		final Button nextButton = (Button) mActivity.getView(R.id.next_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_2);
			}
		});
		
		final Button backButton = (Button) mActivity.getView(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.MAIN);
			}
		});
	}
	
	private void initCreateChar2()
	{
		mActivity.setContentView(R.layout.free_points_view);
		mCharCreator.releaseViews();
		mCharCreator.setCreationMode(CharMode.POINTS);
		
		mCharCreator.resetPath();
		
		setVolitionPoints(mCharCreator.getVolitionPoints());
		setPathPoints(mCharCreator.getPathPoints());
		setFreePoints(mCharCreator.getFreePoints());
		setPath(mCharCreator.getPath());
		
		final ProgressBar pointsBar = (ProgressBar) mActivity.getView(R.id.free_points_bar);
		pointsBar.setMax(CharCreator.START_FREE_POINTS);
		
		final LinearLayout simpleItemsPanel = (LinearLayout) mActivity.getView(R.id.free_points_simple_items_panel);
		mCharCreator.getSimpleValues().initLayout(simpleItemsPanel);
		
		final LinearLayout disciplinesPanel = (LinearLayout) mActivity.getView(R.id.free_points_disciplines_panel);
		mCharCreator.getDisciplines().initLayout(disciplinesPanel);
		
		final LinearLayout backgroundsPanel = (LinearLayout) mActivity.getView(R.id.free_points_backgrounds_panel);
		mCharCreator.getBackgrounds().initLayout(backgroundsPanel);
		
		final ImageButton increaseVolition = (ImageButton) mActivity.getView(R.id.increase_volition);
		increaseVolition.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.increaseVolitionPoints();
			}
		});
		
		final ImageButton decreaseVolition = (ImageButton) mActivity.getView(R.id.decrease_volition);
		decreaseVolition.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.decreaseVolitionPoints();
			}
		});
		
		final CheckBox enablePath = (CheckBox) mActivity.getView(R.id.enable_path_checkbox);
		enablePath.setChecked(mCharCreator.hasPath());
		enablePath.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton aButtonView, final boolean aIsChecked)
			{
				if (aIsChecked)
				{
					mCharCreator.setPath(((Spinner) mActivity.getView(R.id.path_spinner)).getSelectedItem().toString());
				}
				else
				{
					mCharCreator.setPath(null);
				}
				((Spinner) mActivity.getView(R.id.path_spinner)).setEnabled(aIsChecked);
			}
		});
		
		final ImageButton increasePath = (ImageButton) mActivity.getView(R.id.increase_path);
		increasePath.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mCharCreator.increasePathPoints();
			}
		});
		
		final ImageButton decreasePath = (ImageButton) mActivity.getView(R.id.decrease_path);
		decreasePath.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mCharCreator.decreasePathPoints();
			}
		});
		
		final Button resetTempPoints = (Button) mActivity.getView(R.id.reset_temp_points_button);
		resetTempPoints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.resetFreePoints();
			}
		});
		
		final Button showCreation = (Button) mActivity.getView(R.id.show_creation_button);
		showCreation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.resetFreePoints();
				setState(State.CREATE_CHAR_1);
			}
		});
		
		final Button showDescriptions = (Button) mActivity.getView(R.id.show_descriptions_button);
		showDescriptions.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				// TODO Go to the description step.
			}
		});
	}
	
	public void setVolitionEnabled(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		((ImageButton) mActivity.getView(R.id.increase_volition)).setEnabled(aCanIncrease);
		((ImageButton) mActivity.getView(R.id.decrease_volition)).setEnabled(aCanDecrease);
	}
	
	public void setPathEnabled(final boolean aEnabled, final boolean aCanIncrease, final boolean aCanDecrease)
	{
		((ImageButton) mActivity.getView(R.id.decrease_path)).setEnabled(aCanDecrease && aEnabled);
		((ImageButton) mActivity.getView(R.id.increase_path)).setEnabled(aCanIncrease && aEnabled);
		((Spinner) mActivity.getView(R.id.path_spinner)).setEnabled(aEnabled);
	}
	
	public void setFreePoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.free_points_text)).setText("" + aValue);
		((ProgressBar) mActivity.getView(R.id.free_points_bar)).setProgress(aValue);
		((Button) mActivity.getView(R.id.show_descriptions_button)).setEnabled(aValue == 0);
	}
	
	public Context getContext()
	{
		return mActivity;
	}
	
	public void setVolitionPoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.volition_value)).setText("" + aValue);
	}
	
	public void setPathPoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.path_value)).setText("" + aValue);
	}
	
	private void setPath(final String aPath)
	{
		int pathPos = 0;
		if (aPath != null)
		{
			pathPos = mPaths.indexOf(aPath);
		}
		((Spinner) mActivity.getView(R.id.path_spinner)).setSelection(pathPos);
	}
	
	private void setClan(final int aClan)
	{
		mCharCreator.setClan(mClans.get(aClan));
		
		// Reload generations
		final NumberPicker generationPicker = (NumberPicker) mActivity.getView(R.id.generation_picker);
		if (mCharCreator.getClan().hasGeneration())
		{
			final int generation = mCharCreator.getClan().getGeneration();
			generationPicker.setMinValue(generation);
			generationPicker.setMaxValue(generation);
		}
		else
		{
			generationPicker.setMaxValue(CharCreator.MAX_GENERATION);
			generationPicker.setMinValue(CharCreator.MIN_GENERATION);
		}
	}
}
