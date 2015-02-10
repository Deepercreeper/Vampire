package com.deepercreeper.vampireapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.character.CharacterCompound;
import com.deepercreeper.vampireapp.character.CharacterCreation;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.CreationMode;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionCreationValue;
import com.deepercreeper.vampireapp.controllers.dialog.CreateStringDialog;
import com.deepercreeper.vampireapp.controllers.dialog.CreateStringDialog.CreationListener;
import com.deepercreeper.vampireapp.controllers.dynamic.Creator;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.controllers.lists.Clan;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.lists.Nature;
import com.deepercreeper.vampireapp.controllers.lists.NatureController;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;
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
	
	private static final String						CHARACTER_LIST			= "Chars.lst";
	
	private static final String						TAG						= "Vampire";
	
	private final VampireActivity					mActivity;
	
	private final List<ItemController>				mControllers;
	
	private final NatureController					mNatures;
	
	private final ClanController					mClans;
	
	private final DescriptionController				mDescriptions;
	
	private final List<CharacterCompound>			mCharacterCompoundsList	= new ArrayList<CharacterCompound>();
	
	private final Map<String, CharacterCompound>	mCharacterCompounds		= new HashMap<String, CharacterCompound>();
	
	private final Map<String, CharacterInstance>	mCharacterCache			= new HashMap<String, CharacterInstance>();
	
	private LinearLayout							mCharsList;
	
	private CharacterCreation						mCharCreator;
	
	private State									mState;
	
	/**
	 * Creates a new vampire.
	 * 
	 * @param aActivity
	 *            The parent activity.
	 */
	public Vampire(final VampireActivity aActivity)
	{
		mActivity = aActivity;
		ViewUtil.init(mActivity);
		LanguageUtil.init(mActivity);
		mControllers = Creator.createItems(getContext());
		mClans = Creator.createClans(getContext());
		mNatures = new NatureController(getContext().getResources());
		mDescriptions = new DescriptionController(getContext().getResources());
		
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
	
	public void deleteChar(final String aName)
	{
		deleteCharFile(aName);
		final CharacterCompound charCompound = mCharacterCompounds.get(aName);
		charCompound.release();
		mCharacterCompoundsList.remove(charCompound);
		mCharacterCompounds.remove(aName);
		
		saveCharsList();
	}
	
	public void deleteCharFile(final String aName)
	{
		final File charFile = new File(mActivity.getFilesDir(), aName + ".chr");
		if ( !charFile.delete())
		{
			Log.e(TAG, "Could not delete character file.");
		}
	}
	
	public void deleteChars()
	{
		for (final File file : mActivity.getFilesDir().listFiles())
		{
			if (file.getName().endsWith(".chr") || file.getName().endsWith("lst"))
			{
				if ( !file.delete())
				{
					Log.e(TAG, "Could not delete file: " + file.getName());
				}
			}
		}
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			charCompound.release();
		}
		mCharacterCompoundsList.clear();
		mCharacterCompounds.clear();
	}
	
	public Clan getClan(final String aName)
	{
		return mClans.get(aName);
	}
	
	/**
	 * @return the current context.
	 */
	public Context getContext()
	{
		return mActivity;
	}
	
	public ItemController getController(final String aName)
	{
		for (final ItemController controller : mControllers)
		{
			if (controller.getName().equals(aName))
			{
				return controller;
			}
		}
		Log.e(TAG, "Could not find controller.");
		return null;
	}
	
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	public Nature getNature(final String aName)
	{
		return mNatures.get(aName);
	}
	
	public void loadChar(final String aName)
	{
		CharacterInstance character = mCharacterCache.get(aName);
		if (character == null)
		{
			try
			{
				character = new CharacterInstance(mActivity.openFileInput(aName + ".chr"), this);
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not load character.");
			}
		}
		if (character == null)
		{
			return;
		}
		mCharacterCache.put(character.getName(), character);
		Toast.makeText(getContext(), character.getConcept(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Sets the number of currently free points.
	 * 
	 * @param aValue
	 *            The new value.
	 */
	public void setFreePoints(final int aValue)
	{
		if (mState == State.CREATE_CHAR_2)
		{
			((TextView) mActivity.findViewById(R.id.free_points_text)).setText("" + aValue);
			((ProgressBar) mActivity.findViewById(R.id.free_points_bar)).setProgress(aValue);
			((Button) mActivity.findViewById(R.id.next_to_3_button)).setEnabled(aValue == 0);
		}
	}
	
	/**
	 * Sets whether the next button inside the third creation panel is enabled.
	 * 
	 * @param aOk
	 *            Whether the insanities are OK.
	 */
	public void setInsanitiesOk(final boolean aOk)
	{
		if (mState == State.CREATE_CHAR_3)
		{
			mActivity.findViewById(R.id.next_to_4_button).setEnabled(aOk);
		}
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
	
	public void reloadChars()
	{
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			charCompound.release();
		}
		Collections.sort(mCharacterCompoundsList);
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			mCharsList.addView(charCompound.getContainer());
		}
	}
	
	private void createChar()
	{
		final CharacterInstance character = new CharacterInstance(mCharCreator);
		try
		{
			character.save(mActivity.openFileOutput(character.getName() + ".chr", Context.MODE_PRIVATE));
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not open file stream.");
		}
		mCharCreator = null;
		mCharacterCache.put(character.getName(), character);
		final CharacterCompound charCompound = new CharacterCompound(character, this);
		mCharacterCompoundsList.add(charCompound);
		mCharacterCompounds.put(charCompound.getName(), charCompound);
		reloadChars();
		
		saveCharsList();
	}
	
	private void initCreateChar1()
	{
		mActivity.setContentView(R.layout.create_char_1);
		if (mCharCreator == null)
		{
			mCharCreator = new CharacterCreation(this, mControllers, mNatures.getFirst(), mNatures.getFirst(), mClans.getFirst(), mDescriptions);
		}
		mCharCreator.resetFreePoints();
		mCharCreator.releaseViews();
		
		mCharCreator.setCreationMode(CreationMode.MAIN);
		mCharCreator.getGeneration().release();
		
		final Button nextButton = (Button) mActivity.findViewById(R.id.next_to_2_button);
		final TextView nameTextView = (TextView) mActivity.findViewById(R.id.char_name_text);
		nameTextView.setText(mCharCreator.getName());
		nameTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCharCreator.setName(nameTextView.getText().toString());
				nextButton.setEnabled(isNameOk(mCharCreator.getName()) && isConceptOk(mCharCreator.getConcept()));
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
		
		final TextView conceptTextView = (TextView) mActivity.findViewById(R.id.concept_text);
		conceptTextView.setText(mCharCreator.getConcept());
		conceptTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mCharCreator.setConcept(conceptTextView.getText().toString());
				nextButton.setEnabled(isNameOk(mCharCreator.getName()) && isConceptOk(mCharCreator.getConcept()));
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
		
		final Spinner natureSpinner = (Spinner) mActivity.findViewById(R.id.nature_spinner);
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
		
		final Spinner behaviorSpinner = (Spinner) mActivity.findViewById(R.id.behavior_spinner);
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
		
		final LinearLayout generationPanel = (LinearLayout) mActivity.findViewById(R.id.generation_panel);
		mCharCreator.getGeneration().init(generationPanel);
		
		final Spinner clanSpinner = (Spinner) mActivity.findViewById(R.id.clan_spinner);
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
		
		final LinearLayout controllersPanel = (LinearLayout) mActivity.findViewById(R.id.controllers_panel);
		for (final ItemControllerCreation controller : mCharCreator.getControllers())
		{
			controller.init();
			controllersPanel.addView(controller.getContainer());
			controller.close();
			controller.updateGroups();
		}
		
		nextButton.setEnabled(isNameOk(mCharCreator.getName()));
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_2);
			}
		});
		
		final Button backButton = (Button) mActivity.findViewById(R.id.back_to_main_button);
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
		mCharCreator.setCreationMode(CreationMode.POINTS);
		
		setFreePoints(mCharCreator.getFreePoints());
		
		final ProgressBar pointsBar = (ProgressBar) mActivity.findViewById(R.id.free_points_bar);
		pointsBar.setMax(CharacterCreation.START_FREE_POINTS);
		
		final LinearLayout controllersPanel = (LinearLayout) mActivity.findViewById(R.id.controllers_2_panel);
		for (final ItemControllerCreation controller : mCharCreator.getControllers())
		{
			controller.init();
			controller.close();
			controllersPanel.addView(controller.getContainer());
			controller.updateGroups();
		}
		
		final Button resetTempPoints = (Button) mActivity.findViewById(R.id.reset_temp_points_button);
		resetTempPoints.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mCharCreator.resetFreePoints();
			}
		});
		
		final Button backButton = (Button) mActivity.findViewById(R.id.back_to_1_button);
		backButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				back();
			}
		});
		
		final Button nextButton = (Button) mActivity.findViewById(R.id.next_to_3_button);
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
		mCharCreator.setCreationMode(CreationMode.DESCRIPTIONS);
		
		setInsanitiesOk(mCharCreator.insanitiesOk());
		
		final TableLayout descriptionsPanel = (TableLayout) mActivity.findViewById(R.id.description_values_panel);
		for (final ItemCreation item : mCharCreator.getDescriptionValues())
		{
			final TableRow row = new TableRow(mActivity);
			row.setLayoutParams(ViewUtil.getTableWrapHeight());
			
			final TextView name = new TextView(mActivity);
			name.setLayoutParams(ViewUtil.getRowNameShort());
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setEllipsize(TruncateAt.END);
			name.setSingleLine();
			name.setText(item.getItem().getName() + ":");
			
			row.addView(name);
			
			final EditText description = new EditText(mActivity);
			description.setLayoutParams(ViewUtil.getRowTextSize());
			description.setHint(R.string.description);
			description.setEms(10);
			description.setSingleLine();
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
					return;
				}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{
					return;
				}
			});
			row.addView(description);
			
			descriptionsPanel.addView(row);
		}
		
		for (final DescriptionCreationValue description : mCharCreator.getDescriptions().getValues())
		{
			final TableRow row = new TableRow(mActivity);
			row.setLayoutParams(ViewUtil.getTableWrapHeight());
			
			final TextView name = new TextView(mActivity);
			name.setLayoutParams(ViewUtil.getRowNameShort());
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setSingleLine();
			name.setEllipsize(TruncateAt.END);
			name.setText(description.getName() + ":");
			
			row.addView(name);
			
			final EditText value = new EditText(mActivity);
			value.setLayoutParams(ViewUtil.getRowTextSize());
			value.setHint(R.string.description);
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
					return;
				}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{
					return;
				}
			});
			row.addView(value);
			
			descriptionsPanel.addView(row);
		}
		
		final TableLayout insanitiesTable = (TableLayout) mActivity.findViewById(R.id.insanities_panel);
		mCharCreator.initInsanities(insanitiesTable);
		
		final Button addInsanity = (Button) mActivity.findViewById(R.id.add_insanity_button);
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
		
		final Button backButton = (Button) mActivity.findViewById(R.id.back_to_2_button);
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
		
		final Button nextButton = (Button) mActivity.findViewById(R.id.next_to_4_button);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				createChar();
				setState(State.MAIN);
			}
		});
	}
	
	private void initMain()
	{
		mCharCreator = null;
		mActivity.setContentView(R.layout.activity_main);
		final Button createChar = (Button) mActivity.findViewById(R.id.create_character_button);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				setState(State.CREATE_CHAR_1);
			}
		});
		
		mCharsList = (LinearLayout) mActivity.findViewById(R.id.characters_list);
		
		loadCharCompounds();
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
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			if (charCompound.getName().equals(aName))
			{
				return false;
			}
		}
		return true;
	}
	
	private void loadCharCompounds()
	{
		String data = null;
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(mActivity.openFileInput(CHARACTER_LIST));
			final StringBuilder list = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1)
			{
				list.append((char) c);
			}
			data = list.toString();
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not load characters list.");
		}
		try
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not close reader.");
		}
		if (data != null && !data.trim().isEmpty())
		{
			for (final CharacterCompound charCompound : mCharacterCompoundsList)
			{
				charCompound.release();
			}
			mCharacterCompounds.clear();
			mCharacterCompoundsList.clear();
			
			for (final String character : data.split("\n"))
			{
				final CharacterCompound charCompound = new CharacterCompound(character, this);
				mCharacterCompoundsList.add(charCompound);
				mCharacterCompounds.put(charCompound.getName(), charCompound);
			}
			reloadChars();
		}
	}
	
	private void saveCharsList()
	{
		final StringBuilder characterNames = new StringBuilder();
		for (int i = 0; i < mCharacterCompoundsList.size(); i++ )
		{
			if (i > 0)
			{
				characterNames.append("\n");
			}
			characterNames.append(mCharacterCompoundsList.get(i));
		}
		OutputStreamWriter writer = null;
		try
		{
			writer = new OutputStreamWriter(mActivity.openFileOutput(CHARACTER_LIST, Context.MODE_PRIVATE));
			writer.append(characterNames.toString());
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not save characters list.");
		}
		if (writer != null)
		{
			try
			{
				writer.close();
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not close stream.");
			}
		}
	}
	
	private void setClan(final int aClan)
	{
		mCharCreator.setClan(mClans.get(aClan));
	}
}
