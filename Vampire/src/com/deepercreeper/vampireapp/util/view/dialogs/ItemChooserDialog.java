package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.view.listeners.ItemChooseListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A dialog used to choose one item out of all given.
 * 
 * @author Vincent
 */
public class ItemChooserDialog extends DefaultDialog<ItemChooseListener, LinearLayout>
{
	private final ItemProvider mItems;
	
	private ItemChooserDialog(final ItemProvider aItems, final String aTitle, final Context aContext, final ItemChooseListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_item_chooser, LinearLayout.class);
		mItems = aItems;
	}
	
	@Override
	protected Dialog createDialog(final Builder aBuilder)
	{
		for (final ItemController controller : mItems.getControllers())
		{
			for (final ItemGroup group : controller.getGroupsList())
			{
				getContainer().addView(createGroup(group));
			}
		}
		return aBuilder.create();
	}
	
	private LinearLayout createGroup(final ItemGroup aGroup)
	{
		final LinearLayout groupView = (LinearLayout) View.inflate(getContext(), R.layout.dialog_item_group, null);
		((TextView) groupView.findViewById(R.id.dialog_group_name_label)).setText(aGroup.getDisplayName());
		for (final Item item : aGroup.getItemsList())
		{
			final LinearLayout itemView = (LinearLayout) View.inflate(getContext(), R.layout.dialog_item_view, null);
			((TextView) itemView.findViewById(R.id.dialog_item_name_label)).setText(item.getDisplayName());
			((ImageButton) itemView.findViewById(R.id.dialog_choose_item_button)).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					getListener().chose(item);
				}
			});
		}
		return groupView;
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(ItemChooserDialog.class);
	}
	
	/**
	 * Shows a item chooser dialog.
	 * 
	 * @param aItems
	 *            The item provider.
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The item listener.
	 */
	public static void showItemChooserDialog(final ItemProvider aItems, final String aTitle, final Context aContext,
			final ItemChooseListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new ItemChooserDialog(aItems, aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
