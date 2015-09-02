package com.deepercreeper.vampireapp.util.view.dialogs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * An abstract dialog, that implements the default methods.
 * 
 * @author Vincent
 * @param <L>
 *            The dialog listener type.
 * @param <C>
 *            The dialog view type.
 */
public abstract class DefaultDialog <L, C extends View> extends DialogFragment
{
	private static final Map<String, Boolean> sDialogsOpen = new HashMap<String, Boolean>();
	
	private final String mTitle;
	
	private final Context mContext;
	
	private final L mListener;
	
	private final C mContainer;
	
	protected DefaultDialog(final String aTitle, final Context aContext, final L aListener, final int aViewId, final Class<C> aContainerClass)
	{
		sDialogsOpen.put(getClass().getName(), true);
		mTitle = aTitle;
		mContext = aContext;
		mListener = aListener;
		mContainer = aContainerClass.cast(View.inflate(mContext, aViewId, null));
	}
	
	@Override
	public final void onCancel(final DialogInterface aDialog)
	{
		cancel();
		super.onCancel(aDialog);
	}
	
	@Override
	public final Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		return createDialog(new AlertDialog.Builder(mContext).setTitle(mTitle).setView(mContainer));
	}
	
	@Override
	public final void onDestroy()
	{
		super.onDestroy();
		sDialogsOpen.put(getClass().getName(), false);
	}
	
	@Override
	public final void onDetach()
	{
		super.onDetach();
		// TODO Remove when not necessary anymore
		try
		{
			final Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		}
		catch (final NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected void cancel()
	{
		// Do nothing
	}
	
	protected abstract Dialog createDialog(AlertDialog.Builder aBuilder);
	
	protected final C getContainer()
	{
		return mContainer;
	}
	
	protected final Context getContext()
	{
		return mContext;
	}
	
	protected final L getListener()
	{
		return mListener;
	}
	
	protected boolean isNameOk(final EditText aText)
	{
		return !aText.getText().toString().trim().isEmpty();
	}
	
	protected boolean isNumberOk(final EditText aText, final int aMin)
	{
		try
		{
			return Integer.parseInt(aText.getText().toString()) >= aMin;
		}
		catch (final NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * @return whether any dialog fragment is active and open.
	 */
	protected static boolean isDialogOpen(final Class<?> aClass)
	{
		if ( !sDialogsOpen.containsKey(aClass.getName()))
		{
			return false;
		}
		return sDialogsOpen.get(aClass.getName());
	}
}
