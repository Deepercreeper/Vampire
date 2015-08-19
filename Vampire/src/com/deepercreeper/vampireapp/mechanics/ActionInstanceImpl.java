package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An action implementation.
 * 
 * @author Vincent
 */
public class ActionInstanceImpl implements ActionInstance
{
	private static final String TAG = "ActionInstanceImpl";
	
	private final Action mAction;
	
	private final ItemInstance mParent;
	
	private final CharacterInstance mChar;
	
	private final List<ItemInstance> mDices = new ArrayList<ItemInstance>();
	
	private final Map<ItemInstance, Integer> mCosts = new HashMap<ItemInstance, Integer>();
	
	private final List<ItemInstance> mCostDices = new ArrayList<ItemInstance>();
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final Button mUse;
	
	/**
	 * Creates a new action.
	 * 
	 * @param aAction
	 *            The action type.
	 * @param aContext
	 *            The underlying context.
	 * @param aChar
	 *            The parent character.
	 * @param aParent
	 *            The parent item. May be {@code null}.
	 */
	public ActionInstanceImpl(final Action aAction, final Context aContext, final CharacterInstance aChar, final ItemInstance aParent)
	{
		mAction = aAction;
		mParent = aParent;
		mContext = aContext;
		mChar = aChar;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_action, null);
		
		initDices();
		
		mUse = (Button) getContainer().findViewById(R.id.view_use_action_button);
		final TextView name = (TextView) getContainer().findViewById(R.id.view_action_name_label);
		
		name.setText(getAction().getDisplayName());
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final StringBuilder costs = new StringBuilder();
				boolean first = true;
				for (final ItemInstance item : mDices)
				{
					if (first)
					{
						first = false;
					}
					else
					{
						costs.append(" + ");
					}
					costs.append(item.getItem().getDisplayName());
				}
				String parent = "";
				if (mParent != null)
				{
					parent = mParent.getItem().getDisplayName() + " " + getAction().getMinLevel() + ": ";
				}
				mChar.getMessageListener().makeText(parent + getAction().getDisplayName() + ": " + costs.toString() + " = " + getDefaultDices(),
						Toast.LENGTH_SHORT);
			}
		});
	}
	
	@Override
	public boolean canUse(final int aLevel)
	{
		if (aLevel >= 0 && aLevel < mAction.getMinLevel() || !mChar.getMode().getMode().canUseAction())
		{
			return false;
		}
		for (final ItemInstance cost : mCosts.keySet())
		{
			if (cost.getValue() < mCosts.get(cost))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int compareTo(final ActionInstance aAnother)
	{
		if (hasParent() && aAnother.hasParent())
		{
			if (getParent() == aAnother.getParent())
			{
				return getAction().getMinLevel() - aAnother.getAction().getMinLevel();
			}
			return getParent().compareTo(aAnother.getParent());
		}
		if (hasParent())
		{
			return 1;
		}
		if (aAnother.hasParent())
		{
			return -1;
		}
		return mAction.compareTo(aAnother.getAction());
	}
	
	@Override
	public boolean hasParent()
	{
		return mParent != null;
	}
	
	@Override
	public Action getAction()
	{
		return mAction;
	}
	
	@Override
	public View getContainer()
	{
		return mContainer;
	}
	
	@Override
	public int getDefaultDices()
	{
		int dices = mAction.getMinDices();
		for (final ItemInstance dice : mDices)
		{
			dices += dice.getValue();
		}
		return dices;
	}
	
	@Override
	public ItemInstance getParent()
	{
		return mParent;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{
		if (mParent != null)
		{
			ViewUtil.setEnabled(mUse, canUse(mParent.getValue()));
		}
		else
		{
			ViewUtil.setEnabled(mUse, canUse( -1));
		}
	}
	
	private void initDices()
	{
		for (final String dice : mAction.getDiceNames())
		{
			final ItemInstance item = mChar.findItemInstance(dice);
			if (item == null)
			{
				Log.w(TAG, "Could not find item: " + dice);
			}
			else
			{
				mDices.add(item);
			}
		}
		for (final String costDice : mAction.getCostDiceNames())
		{
			final ItemInstance item = mChar.findItemInstance(costDice);
			if (item == null)
			{
				Log.w(TAG, "Could not find item: " + costDice);
			}
			else
			{
				mCostDices.add(item);
			}
		}
		for (final String costName : mAction.getCostNames().keySet())
		{
			final ItemInstance item = mChar.findItemInstance(costName);
			if (item == null)
			{
				Log.w(TAG, "Could not find item: " + costName);
			}
			else
			{
				mCosts.put(item, mAction.getCostNames().get(costName));
			}
		}
	}
}
