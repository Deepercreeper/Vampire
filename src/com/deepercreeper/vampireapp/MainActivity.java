package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.AttributeListener.Attribute;

public class MainActivity extends Activity
{
	private static final String					KEY_CHARS		= "Chars";
	
	private static final String					NAME_DELIM		= ":";
	
	private static final int					ATTRIBUTES_HEIGHT	= 1200, ABILITIES_HEIGHT = 3200;
	
	private final HashMap<String, Discipline>	mDisciplines		= new HashMap<>();
	
	private final HashMap<String, Clan>			mClans				= new HashMap<>();
	
	private final AttributeListener				mAttributeListener	= new AttributeListener();
	
	private boolean								mInitializedAttributes	= false, mInitializedAbilities = false;
	
	private boolean								mShowAttributes			= false, mShowAbilities = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadChars();
		Button createChar = (Button) findViewById(R.id.createCharacterButton);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View aV)
			{
				createCharacter();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) { return true; }
		return super.onOptionsItemSelected(item);
	}
	
	private void createCharacter()
	{
		setContentView(R.layout.create_character);
		
		// Load disciplines
		{
			String[] disciplines = getResources().getStringArray(R.array.disciplines);
			for (String discipline : disciplines)
			{
				String[] data = discipline.split(NAME_DELIM);
				mDisciplines.put(data[0], new Discipline(data[0], data[1]));
			}
		}
		
		// Load clans
		{
			String[] clanList = getResources().getStringArray(R.array.clan);
			for (int i = 0; i < clanList.length; i++ )
			{
				String[] clanData = clanList[i].split(NAME_DELIM);
				Clan clan = new Clan(clanData[0]);
				if (clanData.length > 1)
				{
					clan.addDisciplines(clanData[1]);
				}
				mClans.put(clanData[0], clan);
			}
		}
		
		// Natures and behaviors
		String[] natureAndBehavior = getResources().getStringArray(R.array.nature_and_behavior);
		Arrays.sort(natureAndBehavior);
		
		// Clan names
		ArrayList<String> clanNames = new ArrayList<>();
		for (String clanName : mClans.keySet())
		{
			clanNames.add(clanName);
		}
		Collections.sort(clanNames);
		
		ArrayAdapter<String> adapter;
		
		Spinner natureSpinner = (Spinner) findViewById(R.id.nature_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, natureAndBehavior);
		natureSpinner.setAdapter(adapter);
		
		Spinner behaviorSpinner = (Spinner) findViewById(R.id.behavior_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, natureAndBehavior);
		behaviorSpinner.setAdapter(adapter);
		
		final NumberPicker generationPicker = (NumberPicker) findViewById(R.id.generation_picker);
		generationPicker.setMinValue(8);
		generationPicker.setMaxValue(12);
		generationPicker.setValue(12);
		
		final Spinner clanSpinner = (Spinner) findViewById(R.id.clan_spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, clanNames);
		clanSpinner.setAdapter(adapter);
		clanSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> aParent, View aView, int aPosition, long aId)
			{
				if (clanSpinner.getSelectedItem().equals("Caitiff"))
				{
					generationPicker.setMaxValue(13);
					generationPicker.setMinValue(13);
				}
				else
				{
					generationPicker.setMaxValue(12);
					generationPicker.setMinValue(8);
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> aParent)
			{
				// Should not happen
			}
		});
		
		final LinearLayout attributesPanel = (LinearLayout) findViewById(R.id.attributes_panel);
		final Button showAttributes = (Button) findViewById(R.id.show_attributes);
		showAttributes.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View aV)
			{
				mShowAttributes = !mShowAttributes;
				Animation animation;
				int arrowId;
				if (mShowAttributes)
				{
					if ( !mInitializedAttributes)
					{
						mInitializedAttributes = true;
						initAttributes();
					}
					animation = new ResizeAnimation(attributesPanel, attributesPanel.getWidth(), attributesPanel.getHeight(), attributesPanel.getWidth(), ATTRIBUTES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(attributesPanel, attributesPanel.getWidth(), attributesPanel.getHeight(), attributesPanel.getWidth(), 0);
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
			public void onClick(View aV)
			{
				mShowAbilities = !mShowAbilities;
				Animation animation;
				int arrowId;
				if (mShowAbilities)
				{
					if ( !mInitializedAbilities)
					{
						mInitializedAbilities = true;
						initAbilities();
					}
					animation = new ResizeAnimation(abilitiesPanel, abilitiesPanel.getWidth(), abilitiesPanel.getHeight(), abilitiesPanel.getWidth(), ABILITIES_HEIGHT);
					arrowId = android.R.drawable.arrow_up_float;
				}
				else
				{
					animation = new ResizeAnimation(abilitiesPanel, abilitiesPanel.getWidth(), abilitiesPanel.getHeight(), abilitiesPanel.getWidth(), 0);
					arrowId = android.R.drawable.arrow_down_float;
				}
				showAbilities.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
				abilitiesPanel.startAnimation(animation);
			}
		});
	}
	
	private void initAttributes()
	{
		TableLayout attributesTable = (TableLayout) ((ViewStub) findViewById(R.id.attributes_import)).inflate();
		String lastHeader = null;
		Attribute lastAttr = null;
		for (int i = 0; i < attributesTable.getChildCount(); i++ )
		{
			TableRow row = (TableRow) attributesTable.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++ )
			{
				View item = row.getChildAt(j);
				if (item instanceof Space)
				{
					TextView header = (TextView) row.getChildAt(j + 1);
					lastHeader = header.getText().toString();
					break;
				}
				else if (item instanceof GridLayout)
				{
					GridLayout attribute = (GridLayout) item;
					for (int k = 0; k < attribute.getChildCount(); k++ )
					{
						View button = attribute.getChildAt(k);
						if (button instanceof ImageButton)
						{
							ImageButton changeButton = (ImageButton) button;
							if (changeButton.getContentDescription().equals("Sub"))
							{
								lastAttr.setSub(changeButton);
							}
							else if (changeButton.getContentDescription().equals("Add"))
							{
								lastAttr.setAdd(changeButton);
								mAttributeListener.addAttribute(lastHeader, lastAttr);
							}
						}
						else if (button instanceof RadioButton)
						{
							lastAttr.setRadio(k - 1, (RadioButton) button);
						}
					}
				}
				else if (item instanceof TextView)
				{
					TextView name = (TextView) item;
					lastAttr = new Attribute(name.getText().toString());
				}
			}
		}
	}
	
	private void initAbilities()
	{
		((ViewStub) findViewById(R.id.abilities_import)).setVisibility(View.VISIBLE);
		// TODO Do the same stuff as in
	}
	
	private void loadChars()
	{
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		if (prefs != null && prefs.contains(KEY_CHARS))
		{
			for (String character : prefs.getStringSet(KEY_CHARS, new HashSet<String>(0)))
			{
				System.out.println(character);
			}
		}
	}
}
