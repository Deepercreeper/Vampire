package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.RestrictionInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance.RestrictionInstanceType;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.mechanics.TimeListener.Type;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import com.deepercreeper.vampireapp.util.view.listeners.RestrictionCreationListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * A dialog for restriction creation.
 * 
 * @author Vincent
 */
public class CreateRestrictionDialog extends DefaultDialog<RestrictionCreationListener, LinearLayout>
{
	private final ArrayAdapter<Nameable> mDurationAdapter;
	
	private final ArrayAdapter<Nameable> mTypeAdapter;
	
	private final ArrayAdapter<Item> mItemAdapter;
	
	private Spinner mItemSpinner;
	
	private RestrictionInstanceType mType = RestrictionInstanceType.ITEM_VALUE;
	
	private EditText mDurationValue;
	
	private EditText mMinimumValue;
	
	private EditText mMaximumValue;
	
	private EditText mIndexValue;
	
	private EditText mValue;
	
	private Button mOk;
	
	private boolean mForever = false;
	
	private CreateRestrictionDialog(final String aTitle, final Context aContext, final RestrictionCreationListener aListener, final ItemFinder aItems)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_restriction, LinearLayout.class);
		
		mDurationAdapter = new ArrayAdapter<Nameable>(getContext(), android.R.layout.simple_spinner_dropdown_item,
				TimeListener.Type.getTypesList(getContext()));
		mTypeAdapter = new ArrayAdapter<Nameable>(getContext(), android.R.layout.simple_spinner_dropdown_item,
				RestrictionInstanceType.getTypesList(getContext()));
		mItemAdapter = new ArrayAdapter<Item>(getContext(), android.R.layout.simple_spinner_dropdown_item, aItems.getItemsList());
	}
	
	@Override
	protected Dialog createDialog(final Builder aBuilder)
	{
		mOk = (Button) getContainer().findViewById(R.id.dialog_restriction_ok_button);
		mDurationValue = (EditText) getContainer().findViewById(R.id.dialog_restriction_duration_value_text);
		mMinimumValue = (EditText) getContainer().findViewById(R.id.dialog_restriction_minimum_text);
		mMaximumValue = (EditText) getContainer().findViewById(R.id.dialog_restriction_maximum_text);
		mIndexValue = (EditText) getContainer().findViewById(R.id.dialog_restriction_index_text);
		mValue = (EditText) getContainer().findViewById(R.id.dialog_restriction_value_text);
		mItemSpinner = (Spinner) getContainer().findViewById(R.id.dialog_restriction_item_spinner);
		final Spinner typeSpinner = (Spinner) getContainer().findViewById(R.id.dialog_restriction_type_spinner);
		final Spinner durationSpinner = (Spinner) getContainer().findViewById(R.id.dialog_restriction_duration_spinner);
		final CheckBox forever = (CheckBox) getContainer().findViewById(R.id.dialog_restriction_forever_box);
		
		typeSpinner.setAdapter(mTypeAdapter);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mType = RestrictionInstanceType.getTypeOf(mTypeAdapter.getItem(aPosition));
				updateOkButton();
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{}
		});
		mItemSpinner.setAdapter(mItemAdapter);
		durationSpinner.setAdapter(mDurationAdapter);
		forever.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton aButtonView, final boolean aIsChecked)
			{
				mForever = aIsChecked;
				ViewUtil.setEnabled(durationSpinner, !mForever);
				ViewUtil.setEnabled(mDurationValue, !mForever);
				updateOkButton();
			}
		});
		final TextWatcher listener = new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				updateOkButton();
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{}
		};
		mDurationValue.addTextChangedListener(listener);
		mMinimumValue.addTextChangedListener(listener);
		mMaximumValue.addTextChangedListener(listener);
		mIndexValue.addTextChangedListener(listener);
		mValue.addTextChangedListener(listener);
		mOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Duration duration = Duration.FOREVER;
				if ( !mForever)
				{
					duration = new Duration(Type.getTypeOf(mDurationAdapter.getItem(durationSpinner.getSelectedItemPosition())),
							Integer.parseInt(mDurationValue.getText().toString()));
				}
				String item = null;
				if (mType.hasItemName())
				{
					item = mItemAdapter.getItem(mItemSpinner.getSelectedItemPosition()).getName();
				}
				int minimum = Integer.MIN_VALUE;
				int maximum = Integer.MAX_VALUE;
				if (mType.hasRange())
				{
					if (mMinimumValue.getText().toString().isEmpty())
					{
						minimum = Integer.MIN_VALUE;
					}
					else
					{
						minimum = Integer.parseInt(mMinimumValue.getText().toString());
					}
					if (mMaximumValue.getText().toString().isEmpty())
					{
						maximum = Integer.MAX_VALUE;
					}
					else
					{
						maximum = Integer.parseInt(mMaximumValue.getText().toString());
					}
				}
				int index = 0;
				if (mType.hasIndex())
				{
					index = Integer.parseInt(mIndexValue.getText().toString());
				}
				int value = 0;
				if (mType.hasValue())
				{
					value = Integer.parseInt(mValue.getText().toString());
				}
				final RestrictionInstance restriction = new RestrictionInstanceImpl(mType, item, minimum, maximum, index, value, duration);
				getListener().restrictionCreated(restriction);
				dismiss();
			}
		});
		
		updateOkButton();
		
		return aBuilder.create();
	}
	
	private void updateOkButton()
	{
		ViewUtil.setEnabled(mItemSpinner, mType.hasItemName());
		ViewUtil.setEnabled(mMinimumValue, mType.hasRange());
		ViewUtil.setEnabled(mMaximumValue, mType.hasRange());
		ViewUtil.setEnabled(mIndexValue, mType.hasIndex());
		ViewUtil.setEnabled(mValue, mType.hasValue());
		
		boolean enabled = true;
		
		final boolean minOk = isNumberOk(mMinimumValue, Integer.MIN_VALUE);
		final boolean maxOk = isNumberOk(mMaximumValue, Integer.MIN_VALUE);
		final boolean minAny = mMinimumValue.getText().toString().isEmpty();
		final boolean maxAny = mMaximumValue.getText().toString().isEmpty();
		
		enabled &= !mType.hasRange() || (minOk || minAny) && (maxOk || maxAny);
		enabled &= !mType.hasRange() || minAny || maxAny
				|| minOk && maxOk && Integer.parseInt(mMinimumValue.getText().toString()) <= Integer.parseInt(mMaximumValue.getText().toString());
		enabled &= !mType.hasRange() || !minAny || !maxAny;
		enabled &= !mType.hasIndex() || isNumberOk(mIndexValue, 0);
		enabled &= !mType.hasValue() || isNumberOk(mValue, Integer.MIN_VALUE);
		enabled &= mForever || isNumberOk(mDurationValue, 1);
		ViewUtil.setEnabled(mOk, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(CreateRestrictionDialog.class);
	}
	
	/**
	 * Shows a create restriction dialog.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The dialog listener.
	 * @param aItems
	 *            An item finder.
	 */
	public static void showCreateRestrictionDialog(final String aTitle, final Context aContext, final RestrictionCreationListener aListener,
			final ItemFinder aItems)
	{
		if (isDialogOpen())
		{
			return;
		}
		new CreateRestrictionDialog(aTitle, aContext, aListener, aItems).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}