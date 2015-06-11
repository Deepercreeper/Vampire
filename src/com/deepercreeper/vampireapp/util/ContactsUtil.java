package com.deepercreeper.vampireapp.util;

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
	private ContactsUtil()
	{}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @param aPhoneNumber
	 *            The phone number.
	 * @return The display name of the given number or the given number if no contact with this number exists.
	 */
	public static String getContactName(final Context aContext, final String aPhoneNumber)
	{
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
	 * @return The current phone number of this device.
	 */
	public static String getPhoneNumber(final Context aContext)
	{
		final TelephonyManager telManager = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telManager.getLine1Number();
	}
}
