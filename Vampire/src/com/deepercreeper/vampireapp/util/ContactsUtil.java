package com.deepercreeper.vampireapp.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;

/**
 * A utility used to find out, what name some phone number has.
 * 
 * @author Vincent
 */
public class ContactsUtil
{
	/**
	 * When a device has no phone number this string is inserted.
	 */
	public static final String	EMPTY_NUMBER	= "<empty>";
	
	private ContactsUtil()
	{}
	
	/**
	 * @param aAddress
	 *            The device address.
	 * @return the Bluetooth name of the given address if bonded or the address itself otherwise.
	 */
	public static String getBluetoothName(String aAddress)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null)
		{
			return aAddress;
		}
		
		for (BluetoothDevice device : bluetoothAdapter.getBondedDevices())
		{
			if (device.getAddress().equals(aAddress))
			{
				if (device.getName() != null)
				{
					return device.getName();
				}
			}
		}
		return aAddress;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @param aPhoneNumber
	 *            The phone number.
	 * @param aAddress
	 *            The device address.
	 * @return The display name of the given number or the given number if no contact with this number exists.
	 */
	public static String getContactOrBluetoothName(final Context aContext, final String aPhoneNumber, String aAddress)
	{
		if (aPhoneNumber.equals(EMPTY_NUMBER))
		{
			return getBluetoothName(aAddress);
		}
		
		final ContentResolver cr = aContext.getContentResolver();
		final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(aPhoneNumber));
		final Cursor cursor = cr.query(uri, new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null)
		{
			return aPhoneNumber;
		}
		String contactName = aPhoneNumber;
		if (cursor.moveToFirst())
		{
			contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		
		return contactName;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return The current phone number of this device or an empty string if no phone number was detected.
	 */
	public static String getPhoneNumber(final Context aContext)
	{
		final TelephonyManager telManager = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = telManager.getLine1Number();
		if (phoneNumber == null)
		{
			return EMPTY_NUMBER;
		}
		return phoneNumber;
	}
}
