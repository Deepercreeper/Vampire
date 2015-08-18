package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.inventory.Weapon;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import com.deepercreeper.vampireapp.util.view.listeners.InventoryItemCreationListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * A dialog used for creating weapon items.
 * 
 * @author Vincent
 */
public class CreateWeaponItemDialog extends DefaultDialog<InventoryItemCreationListener, LinearLayout>
{
	private final ItemFinder mItems;
	
	private EditText mName;
	
	private EditText mWeight;
	
	private EditText mQuantity;
	
	private EditText mDamage;
	
	private EditText mDifficulty;
	
	private ArrayAdapter<Item> mAdditionalDamage;
	
	private EditText mStash;
	
	private EditText mDistance;
	
	private EditText mReloadTime;
	
	private EditText mMagazine;
	
	private EditText mAmmo;
	
	private Button mOK;
	
	private boolean mHasAdditionalDamage = true;
	
	private boolean mDistanceWeapon = true;
	
	private CreateWeaponItemDialog(final String aTitle, final Context aContext, final InventoryItemCreationListener aListener, ItemFinder aItems)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_weapon_item, LinearLayout.class);
		mItems = aItems;
		
		mAdditionalDamage = new ArrayAdapter<Item>(getContext(), android.R.layout.simple_spinner_dropdown_item, aItems.getItemsList());
	}
	
	@Override
	public Dialog createDialog(final Builder aBuilder)
	{
		CheckBox distanceWeapon = (CheckBox) getContainer().findViewById(R.id.dialog_weapon_distance_box);
		CheckBox hasAdditionalDamage = (CheckBox) getContainer().findViewById(R.id.dialog_weapon_additional_damage_box);
		mOK = (Button) getContainer().findViewById(R.id.dialog_weapon_ok_button);
		mName = (EditText) getContainer().findViewById(R.id.dialog_weapon_name_text);
		mWeight = (EditText) getContainer().findViewById(R.id.dialog_weapon_weight_text);
		mQuantity = (EditText) getContainer().findViewById(R.id.dialog_weapon_quantity_text);
		mDamage = (EditText) getContainer().findViewById(R.id.dialog_weapon_damage_text);
		mDifficulty = (EditText) getContainer().findViewById(R.id.dialog_weapon_difficulty_text);
		final Spinner additionalDamage = (Spinner) getContainer().findViewById(R.id.dialog_weapon_additional_damage_spinner);
		mStash = (EditText) getContainer().findViewById(R.id.dialog_weapon_stash_text);
		mDistance = (EditText) getContainer().findViewById(R.id.dialog_weapon_distance_text);
		mReloadTime = (EditText) getContainer().findViewById(R.id.dialog_weapon_reload_time_text);
		mMagazine = (EditText) getContainer().findViewById(R.id.dialog_weapon_magazine_text);
		mAmmo = (EditText) getContainer().findViewById(R.id.dialog_weapon_ammo_text);
		
		additionalDamage.setAdapter(mAdditionalDamage);
		hasAdditionalDamage.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton aButtonView, boolean aIsChecked)
			{
				mHasAdditionalDamage = aIsChecked;
				ViewUtil.setEnabled(additionalDamage, mHasAdditionalDamage);
			}
		});
		distanceWeapon.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton aButtonView, boolean aIsChecked)
			{
				mDistanceWeapon = aIsChecked;
				ViewUtil.setEnabled(mDistance, mDistanceWeapon);
				ViewUtil.setEnabled(mReloadTime, mDistanceWeapon);
				ViewUtil.setEnabled(mMagazine, mDistanceWeapon);
				ViewUtil.setEnabled(mAmmo, mDistanceWeapon);
				updateOKButton();
			}
		});
		final TextWatcher listener = new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				updateOKButton();
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{}
		};
		mQuantity.setText("" + 1);
		mName.addTextChangedListener(listener);
		mWeight.addTextChangedListener(listener);
		mQuantity.addTextChangedListener(listener);
		mDamage.addTextChangedListener(listener);
		mDifficulty.addTextChangedListener(listener);
		mStash.addTextChangedListener(listener);
		mDistance.addTextChangedListener(listener);
		mReloadTime.addTextChangedListener(listener);
		mMagazine.addTextChangedListener(listener);
		mAmmo.addTextChangedListener(listener);
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Weapon weapon;
				String additionalDamageName = null;
				if (mHasAdditionalDamage)
				{
					additionalDamageName = mAdditionalDamage.getItem(additionalDamage.getSelectedItemPosition()).getName();
				}
				if (mDistanceWeapon)
				{
					weapon = new Weapon(mName.getText().toString().trim(), Integer.parseInt(mWeight.getText().toString()),
							Integer.parseInt(mQuantity.getText().toString()), Integer.parseInt(mDifficulty.getText().toString()),
							Integer.parseInt(mDamage.getText().toString()), additionalDamageName, mStash.getText().toString().trim(),
							Integer.parseInt(mDistance.getText().toString()), Integer.parseInt(mReloadTime.getText().toString()),
							Integer.parseInt(mMagazine.getText().toString()), mAmmo.getText().toString().trim(), mItems, getContext(), null);
				}
				else
				{
					weapon = new Weapon(mName.getText().toString().trim(), Integer.parseInt(mWeight.getText().toString()),
							Integer.parseInt(mQuantity.getText().toString()), Integer.parseInt(mDifficulty.getText().toString()),
							Integer.parseInt(mDamage.getText().toString()), additionalDamageName, mStash.getText().toString().trim(), mItems,
							getContext(), null);
				}
				getListener().itemCreated(weapon);
				dismiss();
			}
		});
		
		updateOKButton();
		
		return aBuilder.create();
	}
	
	private void updateOKButton()
	{
		boolean enabled = true;
		enabled &= isNameOk(mName);
		enabled &= isNumberOk(mWeight, 0);
		enabled &= isNumberOk(mQuantity, 1);
		enabled &= isNumberOk(mDamage, 0);
		enabled &= isNumberOk(mDifficulty, 1);
		enabled &= isNameOk(mStash);
		if (mDistanceWeapon)
		{
			enabled &= isNumberOk(mDistance, 0);
			enabled &= isNumberOk(mMagazine, 1);
			enabled &= isNumberOk(mReloadTime, 0);
			enabled &= isNameOk(mAmmo);
		}
		ViewUtil.setEnabled(mOK, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(CreateWeaponItemDialog.class);
	}
	
	/**
	 * Shows a create weapon item dialog.
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
	public static void showCreateWeaponItemDialog(final String aTitle, final Context aContext, final InventoryItemCreationListener aListener,
			ItemFinder aItems)
	{
		if (isDialogOpen())
		{
			return;
		}
		new CreateWeaponItemDialog(aTitle, aContext, aListener, aItems).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
