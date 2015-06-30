package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation.CharCreationListener;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.lists.items.DescriptionCreation;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog.CreationListener;

/**
 * This activity is used to create a character. Either a free character,<br>
 * that is able to have everything or a new character, that is bounded to<br>
 * the creation system.
 * 
 * @author Vincent
 */
public class CreateCharActivity extends Activity implements CharCreationListener, ItemConsumer
{
	private enum State
	{
		GENERAL, FREE_POINTS, DESCRIPTIONS
	}
	
	/**
	 * The extra key for a list of already used character names, so the new name is not already in use.
	 */
	public static final String	CHAR_NAMES			= "CHAR_NAMES";
	
	/**
	 * The extra key for the serialized character data. Used for sending the created character back to the main activity.
	 */
	public static final String	CHARACTER			= "CHARACTER";
	
	/**
	 * The extra key for whether creating a free or a system restricted character.
	 */
	public static final String	FREE_CREATION		= "FREE_CREATION";
	
	/**
	 * The request code for creating a new character.
	 */
	public static final int		CREATE_CHAR_REQUEST	= 1;
	
	private String[]			mCharNames;
	
	private boolean				mFreeCreation;
	
	private State				mState;
	
	private ItemProvider		mItems;
	
	private CharacterCreation	mChar;
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	@Override
	public void onBackPressed()
	{
		switch (mState)
		{
			case GENERAL :
				setResult(RESULT_CANCELED);
				finish();
				break;
			case FREE_POINTS :
				setState(State.GENERAL);
				break;
			case DESCRIPTIONS :
				if (mFreeCreation)
				{
					setState(State.GENERAL);
				}
				else
				{
					setState(State.FREE_POINTS);
				}
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu aMenu)
	{
		getMenuInflater().inflate(R.menu.create_char, aMenu);
		return true;
	}
	
	@Override
	public void setFreePoints(final int aValue)
	{
		if (mState == State.FREE_POINTS)
		{
			((TextView) findViewById(R.id.cc_free_points_label)).setText("" + aValue);
			((ProgressBar) findViewById(R.id.cc_free_points_bar)).setProgress(aValue);
			ViewUtil.setEnabled((findViewById(R.id.cc_free_points_next_button)), aValue == 0);
		}
	}
	
	@Override
	public void setInsanitiesOk(final boolean aOk)
	{
		if (mState == State.DESCRIPTIONS)
		{
			ViewUtil.setEnabled(findViewById(R.id.cc_descriptions_next_button), aOk);
		}
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	private void init()
	{
		mCharNames = getIntent().getStringArrayExtra(CHAR_NAMES);
		mFreeCreation = getIntent().getBooleanExtra(FREE_CREATION, false);
		
		setState(State.GENERAL);
	}
	
	private void initDescriptions()
	{
		setContentView(R.layout.create_char_descriptions);
		
		mChar.releaseViews();
		
		mChar.setCreationMode(CreationMode.DESCRIPTIONS);
		
		final LinearLayout descriptionsPanel = (LinearLayout) findViewById(R.id.cc_descriptions_list);
		final LinearLayout insanitiesPanel = (LinearLayout) findViewById(R.id.cc_insanities_list);
		final Button addInsanity = (Button) findViewById(R.id.cc_add_insanity_button);
		final Button backButton = (Button) findViewById(R.id.cc_descriptions_back_button);
		final Button nextButton = (Button) findViewById(R.id.cc_descriptions_next_button);
		
		backButton.requestFocus();
		
		setInsanitiesOk(mChar.insanitiesOk());
		
		for (final ItemCreation item : mChar.getDescriptionValues())
		{
			final EditText description = (EditText) View.inflate(this, R.layout.description_view, null);
			description.setHint(item.getItem().getDisplayName());
			description.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void afterTextChanged(final Editable aS)
				{
					item.setDescription(description.getText().toString());
				}
				
				@Override
				public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
				{
					// Do nothing
				}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{
					// Do nothing
				}
			});
			descriptionsPanel.addView(description);
		}
		
		for (final DescriptionCreation description : mChar.getDescriptions().getValuesList())
		{
			final EditText value = new EditText(this);
			value.setLayoutParams(ViewUtil.getWrapHeight());
			value.setHint(description.getDisplayName());
			value.setEms(10);
			value.setSingleLine();
			value.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void afterTextChanged(final Editable aS)
				{
					description.setValue(value.getText().toString());
				}
				
				@Override
				public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
				{
					// Do nothing
				}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{
					// Do nothing
				}
			});
			descriptionsPanel.addView(value);
		}
		
		mChar.initInsanities(insanitiesPanel);
		
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
						mChar.addInsanity(aString);
					}
				};
				CreateStringDialog.showCreateStringDialog(getString(R.string.add_insanity), getString(R.string.add_insanity_message),
						CreateCharActivity.this, listener);
			}
		});
		
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mChar.clearDescriptions();
				mChar.releaseInsanities();
				onBackPressed();
			}
		});
		
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final CharacterInstance character = new CharacterInstance(mChar, null, null, false);
				final Intent intent = new Intent();
				intent.putExtra(CHARACTER, FilesUtil.serialize(character));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	private void initFreePoints()
	{
		setContentView(R.layout.create_char_free_points);
		
		mChar.releaseViews();
		
		mChar.setCreationMode(CreationMode.POINTS);
		
		setFreePoints(mChar.getFreePoints());
		
		final ProgressBar pointsBar = (ProgressBar) findViewById(R.id.cc_free_points_bar);
		final LinearLayout controllersPanel = (LinearLayout) findViewById(R.id.cc_free_points_controllers_list);
		final Button resetTempPoints = (Button) findViewById(R.id.cc_reset_temp_points_button);
		final Button backButton = (Button) findViewById(R.id.cc_free_points_back_button);
		final Button nextButton = (Button) findViewById(R.id.cc_free_points_next_button);
		
		backButton.requestFocus();
		
		pointsBar.setMax(CharacterCreation.START_FREE_POINTS);
		
		for (final ItemControllerCreation controller : mChar.getControllers())
		{
			controller.init();
			controllersPanel.addView(controller.getContainer());
			controller.close();
			controller.updateGroups();
		}
		
		resetTempPoints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mChar.resetFreePoints();
			}
		});
		
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				onBackPressed();
			}
		});
		
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.DESCRIPTIONS);
			}
		});
	}
	
	private void initGeneral()
	{
		setContentView(R.layout.create_char_general);
		
		CreationMode mode = CreationMode.MAIN;
		if (mFreeCreation)
		{
			mode = CreationMode.FREE_MAIN;
		}
		
		if (mChar == null)
		{
			mChar = new CharacterCreation(mItems, this, this, mode);
		}
		
		mChar.resetFreePoints();
		mChar.releaseViews();
		mChar.getGeneration().release();
		
		mChar.setCreationMode(mode);
		
		final TextView nameTextView = (TextView) findViewById(R.id.cc_name_text);
		final TextView conceptTextView = (TextView) findViewById(R.id.cc_concept_text);
		final Spinner natureSpinner = (Spinner) findViewById(R.id.cc_nature_spinner);
		final Spinner behaviorSpinner = (Spinner) findViewById(R.id.cc_behavior_spinner);
		final NumberPicker generationPicker = (NumberPicker) findViewById(R.id.cc_generation_picker);
		final Spinner clanSpinner = (Spinner) findViewById(R.id.cc_clan_spinner);
		final LinearLayout controllersPanel = (LinearLayout) findViewById(R.id.cc_controllers_list);
		final Button nextButton = (Button) findViewById(R.id.cc_general_next_button);
		final Button backButton = (Button) findViewById(R.id.cc_general_back_button);
		
		nameTextView.requestFocus();
		
		nameTextView.setText(mChar.getName());
		nameTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mChar.setName(nameTextView.getText().toString());
				ViewUtil.setEnabled(nextButton, isNameOk(mChar.getName()) && isConceptOk(mChar.getConcept()));
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				// Do nothing
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				// Do nothing
			}
		});
		
		conceptTextView.setText(mChar.getConcept());
		conceptTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mChar.setConcept(conceptTextView.getText().toString());
				ViewUtil.setEnabled(nextButton, isNameOk(mChar.getName()) && isConceptOk(mChar.getConcept()));
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				// Do nothing
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				// Do nothing
			}
		});
		
		natureSpinner
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems.getNatures().getDisplayNames()));
		natureSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mChar.setNature(mItems.getNatures().getItemAtDisplayNamePosition(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Do nothing
			}
		});
		natureSpinner.setSelection(mItems.getNatures().displayIndexOf(mChar.getNature()));
		
		behaviorSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems.getNatures()
				.getDisplayNames()));
		behaviorSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mChar.setBehavior(mItems.getNatures().getItemAtDisplayNamePosition(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Do nothing
			}
		});
		behaviorSpinner.setSelection(mItems.getNatures().displayIndexOf(mChar.getBehavior()));
		
		mChar.getGeneration().init(generationPicker, mFreeCreation);
		
		clanSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mItems.getClans().getDisplayNames()));
		clanSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mChar.setClan(mItems.getClans().getItemAtDisplayNamePosition(aPosition));
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{
				// Do nothing
			}
		});
		clanSpinner.setSelection(mItems.getClans().displayIndexOf(mChar.getClan()));
		
		for (final ItemControllerCreation controller : mChar.getControllers())
		{
			controller.init();
			controllersPanel.addView(controller.getContainer());
			controller.close();
			controller.updateGroups();
		}
		
		if (mFreeCreation)
		{
			mChar.getHealth().init();
			controllersPanel.addView(mChar.getHealth().getContainer());
		}
		
		ViewUtil.setEnabled(nextButton, isNameOk(mChar.getName()));
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				if (mFreeCreation)
				{
					setState(State.DESCRIPTIONS);
				}
				else
				{
					setState(State.FREE_POINTS);
				}
			}
		});
		
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				onBackPressed();
			}
		});
	}
	
	private boolean isConceptOk(final String aConcept)
	{
		if (aConcept == null || aConcept.trim().isEmpty())
		{
			return false;
		}
		return true;
	}
	
	private boolean isNameOk(final String aName)
	{
		if (aName == null || aName.trim().isEmpty())
		{
			return false;
		}
		for (final String name : mCharNames)
		{
			if (name.equals(aName))
			{
				return false;
			}
		}
		return true;
	}
	
	private void setState(final State aState)
	{
		mState = aState;
		switch (mState)
		{
			case GENERAL :
				initGeneral();
				break;
			case FREE_POINTS :
				initFreePoints();
				break;
			case DESCRIPTIONS :
				initDescriptions();
				break;
		}
	}
}
