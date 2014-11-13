package com.deepercreeper.vampireapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.backgrounds.BackgroundController;
import com.deepercreeper.vampireapp.controller.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controller.descriptions.DescriptionValue;
import com.deepercreeper.vampireapp.controller.dialog.CreateStringDialog;
import com.deepercreeper.vampireapp.controller.dialog.CreateStringDialog.CreationListener;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.lists.ClanController;
import com.deepercreeper.vampireapp.controller.lists.NatureController;
import com.deepercreeper.vampireapp.controller.lists.Path;
import com.deepercreeper.vampireapp.controller.lists.PathController;
import com.deepercreeper.vampireapp.controller.properties.PropertyController;
import com.deepercreeper.vampireapp.controller.simplesItems.SimpleController;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * This controls the whole vampire app and all of its actions.
 * 
 * @author vrl
 */
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
	
	private final DescriptionController	mDescriptions;
	
	private CharCreator					mCharCreator;
	
	private State						mState;
	
	/**
	 * Creates a new vampire.
	 * 
	 * @param aActivity
	 *            The parent activity.
	 */
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
		mDescriptions = new DescriptionController(mActivity.getResources());
		
		ViewUtil.init(mActivity);
		
		setState(State.MAIN);
	}
	
	/**
	 * Invoked when the back button is hit.
	 */
	public void back()
	{
		switch (mState)
		{
			case CREATE_CHAR_1 :
				setState(State.MAIN);
				break;
			case CREATE_CHAR_2 :
				setState(State.CREATE_CHAR_1);
				break;
			case CREATE_CHAR_3 :
				setState(State.CREATE_CHAR_2);
				break;
			case MAIN :
				mActivity.finish();
				break;
		}
	}
	
	/**
	 * @return the current context.
	 */
	public Context getContext()
	{
		return mActivity;
	}
	
	/**
	 * Sets the number of currently free points.
	 * 
	 * @param aValue
	 *            The new value.
	 */
	public void setFreePoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.free_points_text)).setText("" + aValue);
		((ProgressBar) mActivity.getView(R.id.free_points_bar)).setProgress(aValue);
		((Button) mActivity.getView(R.id.next_to_3_button)).setEnabled(aValue == 0);
	}
	
	/**
	 * Enables or disables the path spinner and point controller.
	 * 
	 * @param aEnabled
	 *            Whether a path is selected.
	 * @param aCanIncrease
	 *            Whether increasing is possible.
	 * @param aCanDecrease
	 *            Whether decreasing is possible.
	 */
	public void setPathEnabled(final boolean aEnabled, final boolean aCanIncrease, final boolean aCanDecrease)
	{
		((ImageButton) mActivity.getView(R.id.decrease_path_button)).setEnabled(aCanDecrease && aEnabled);
		((ImageButton) mActivity.getView(R.id.increase_path_button)).setEnabled(aCanIncrease && aEnabled);
		((Spinner) mActivity.getView(R.id.path_spinner)).setEnabled(aEnabled);
	}
	
	/**
	 * Sets the number of free points spent for the path.
	 * 
	 * @param aValue
	 *            The value;
	 */
	public void setPathPoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.path_value)).setText("" + aValue);
	}
	
	/**
	 * Sets the state of this vampire.
	 * 
	 * @param aState
	 */
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
				initCreateChar3();
				break;
		}
	}
	
	/**
	 * Sets whether the volition points can be increased or decreased.
	 * 
	 * @param aCanIncrease
	 *            Whether increasing is possible.
	 * @param aCanDecrease
	 *            Whether decreasing is possible.
	 */
	public void setVolitionEnabled(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		((ImageButton) mActivity.getView(R.id.increase_volition_button)).setEnabled(aCanIncrease);
		((ImageButton) mActivity.getView(R.id.decrease_volition_button)).setEnabled(aCanDecrease);
	}
	
	/**
	 * Sets the number of volition points.
	 * 
	 * @param aValue
	 *            The new value.
	 */
	public void setVolitionPoints(final int aValue)
	{
		((TextView) mActivity.getView(R.id.volition_value)).setText("" + aValue);
	}
	
	private void initCreateChar1()
	{
		mActivity.setContentView(R.layout.create_char_1);
		if (mCharCreator == null)
		{
			mCharCreator = new CharCreator(this, mDisciplines, mProperties, mBackgrounds, mSimpleItems, mNatures.getFirst(), mNatures.getFirst(),
					mClans.getFirst(), mDescriptions);
		}
		else
		{
			mCharCreator.releaseViews();
		}
		
		mCharCreator.setCreationMode(CharMode.MAIN);
		mCharCreator.getGeneration().release();
		
		final TextView nameTextView = (TextView) mActivity.getView(R.id.char_name_text);
		nameTextView.setText(mCharCreator.getName());
		nameTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCharCreator.setName(nameTextView.getText().toString());
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				return;
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				return;
			}
		});
		
		final TextView conceptTextView = (TextView) mActivity.getView(R.id.concept_text);
		conceptTextView.setText(mCharCreator.getConcept());
		conceptTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCharCreator.setConcept(conceptTextView.getText().toString());
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				return;
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				return;
			}
		});
		
		final Spinner natureSpinner = (Spinner) mActivity.getView(R.id.nature_spinner);
		natureSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mNatures.getNames()));
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
		behaviorSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mNatures.getNames()));
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
		
		final LinearLayout generationPanel = (LinearLayout) mActivity.getView(R.id.generation_panel);
		mCharCreator.getGeneration().init(generationPanel);
		
		final Spinner clanSpinner = (Spinner) mActivity.getView(R.id.clan_spinner);
		clanSpinner.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, mClans.getNames()));
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
		
		final Button nextButton = (Button) mActivity.getView(R.id.next_to_2_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_2);
			}
		});
		
		final Button backButton = (Button) mActivity.getView(R.id.back_to_main_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				back();
			}
		});
	}
	
	private void initCreateChar2()
	{
		mActivity.setContentView(R.layout.create_char_2);
		mCharCreator.releaseViews();
		mCharCreator.setCreationMode(CharMode.POINTS);
		
		mCharCreator.resetPath();
		
		setVolitionPoints(mCharCreator.getVolitionPoints());
		setPathPoints(mCharCreator.getPathPoints());
		setFreePoints(mCharCreator.getFreePoints());
		setPath(mCharCreator.getPath());
		
		final ProgressBar pointsBar = (ProgressBar) mActivity.getView(R.id.free_points_bar);
		pointsBar.setMax(CharCreator.START_FREE_POINTS);
		
		final LinearLayout simpleItemsPanel = (LinearLayout) mActivity.getView(R.id.simple_items_2_panel);
		mCharCreator.getSimpleValues().initLayout(simpleItemsPanel);
		
		final LinearLayout disciplinesPanel = (LinearLayout) mActivity.getView(R.id.disciplines_2_panel);
		mCharCreator.getDisciplines().initLayout(disciplinesPanel);
		
		final LinearLayout backgroundsPanel = (LinearLayout) mActivity.getView(R.id.backgrounds_2_panel);
		mCharCreator.getBackgrounds().initLayout(backgroundsPanel);
		
		final ImageButton increaseVolition = (ImageButton) mActivity.getView(R.id.increase_volition_button);
		increaseVolition.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.increaseVolitionPoints();
			}
		});
		
		final ImageButton decreaseVolition = (ImageButton) mActivity.getView(R.id.decrease_volition_button);
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
					mCharCreator.setPath(mPaths.get(((Spinner) mActivity.getView(R.id.path_spinner)).getSelectedItem().toString()));
				}
				else
				{
					mCharCreator.setPath(null);
				}
				((Spinner) mActivity.getView(R.id.path_spinner)).setEnabled(aIsChecked);
			}
		});
		
		final ImageButton increasePath = (ImageButton) mActivity.getView(R.id.increase_path_button);
		increasePath.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mCharCreator.increasePathPoints();
			}
		});
		
		final ImageButton decreasePath = (ImageButton) mActivity.getView(R.id.decrease_path_button);
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
		
		final Button backButton = (Button) mActivity.getView(R.id.back_to_1_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.resetFreePoints();
				back();
			}
		});
		
		final Button nextButton = (Button) mActivity.getView(R.id.next_to_3_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_3);
			}
		});
	}
	
	private void initCreateChar3()
	{
		mActivity.setContentView(R.layout.create_char_3);
		mCharCreator.releaseViews();
		mCharCreator.setCreationMode(CharMode.DESCRIPTIONS);
		
		final TableLayout descriptionsPanel = (TableLayout) mActivity.getView(R.id.description_values_panel);
		for (final ItemValue<?> value : mCharCreator.getDescriptionValues())
		{
			final TableRow row = new TableRow(mActivity);
			row.setLayoutParams(ViewUtil.instance().getTableWrapHeight());
			
			final TextView name = new TextView(mActivity);
			name.setLayoutParams(ViewUtil.instance().getRowNameShort());
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setEllipsize(TruncateAt.END);
			name.setSingleLine();
			name.setText(value.getItem().getName() + ":");
			
			row.addView(name);
			
			final EditText description = new EditText(mActivity);
			description.setLayoutParams(ViewUtil.instance().getRowTextSize());
			description.setHint(R.string.description);
			description.setEms(10);
			description.setSingleLine();
			description.addTextChangedListener(new TextWatcher()
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
					value.setDescription(description.getText().toString());
				}
			});
			row.addView(description);
			
			descriptionsPanel.addView(row);
		}
		
		for (final DescriptionValue description : mCharCreator.getDescriptions().getValues())
		{
			final TableRow row = new TableRow(mActivity);
			row.setLayoutParams(ViewUtil.instance().getTableWrapHeight());
			
			final TextView name = new TextView(mActivity);
			name.setLayoutParams(ViewUtil.instance().getRowNameShort());
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setSingleLine();
			name.setEllipsize(TruncateAt.END);
			name.setText(description.getName() + ":");
			
			row.addView(name);
			
			final EditText value = new EditText(mActivity);
			value.setLayoutParams(ViewUtil.instance().getRowTextSize());
			value.setHint(R.string.description);
			value.setEms(10);
			value.setSingleLine();
			value.addTextChangedListener(new TextWatcher()
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
					description.setValue(value.getText().toString());
				}
			});
			row.addView(value);
			
			descriptionsPanel.addView(row);
		}
		
		final TableLayout insanitiesTable = (TableLayout) mActivity.getView(R.id.insanities_panel);
		mCharCreator.initInsanities(insanitiesTable);
		
		final Button addInsanity = (Button) mActivity.getView(R.id.add_insanity_button);
		addInsanity.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final CreationListener listener = new CreationListener()
				{
					@Override
					public void create(final String aString)
					{
						mCharCreator.addInsanity(aString);
					}
				};
				CreateStringDialog.showCreateStringDialog(mActivity.getString(R.string.add_insanity),
						mActivity.getString(R.string.add_insanity_message), mActivity, listener);
			}
		});
		
		final Button backButton = (Button) mActivity.getView(R.id.back_to_2_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.clearDescriptions();
				mCharCreator.releaseInsanities();
				back();
			}
		});
		
		final Button nextButton = (Button) mActivity.getView(R.id.next_to_4_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				// TODO Go to the next step.
			}
		});
	}
	
	private void initMain()
	{
		mCharCreator = null;
		mActivity.setContentView(R.layout.activity_main);
		final Button createChar = (Button) mActivity.getView(R.id.create_character_button);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_1);
			}
		});
	}
	
	private void setClan(final int aClan)
	{
		mCharCreator.setClan(mClans.get(aClan));
	}
	
	private void setPath(final Path aPath)
	{
		int pathPos = 0;
		if (aPath != null)
		{
			pathPos = mPaths.indexOf(aPath);
		}
		((Spinner) mActivity.getView(R.id.path_spinner)).setSelection(pathPos);
	}
}
