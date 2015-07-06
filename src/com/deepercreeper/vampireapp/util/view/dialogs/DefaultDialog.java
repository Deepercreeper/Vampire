package com.deepercreeper.vampireapp.util.view.dialogs;

import java.lang.reflect.Field;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

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
	private static boolean	sDialogOpen	= false;
	
	private final String	mTitle;
	
	private final Context	mContext;
	
	private final L			mListener;
	
	private final C			mContainer;
	
	protected DefaultDialog(final String aTitle, final Context aContext, final L aListener, final int aViewId, final Class<C> aContainerClass)
	{
		sDialogOpen = true;
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
	public final void onDestroy()
	{
		super.onDestroy();
		sDialogOpen = false;
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
	
	@Override
	public final Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		return createDialog(new AlertDialog.Builder(mContext).setTitle(mTitle).setView(mContainer));
	}
	
	protected abstract Dialog createDialog(AlertDialog.Builder aBuilder);
	
	protected void cancel()
	{
		// Do nothing
	}
	
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
	
	/**
	 * @return whether any dialog fragment is active and open.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
}
