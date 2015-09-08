package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.Message.Builder;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.Message.MessageType;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.ChooseDicesDialog;
import com.deepercreeper.vampireapp.util.view.listeners.DicesChooseListener;
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
	
	private final MessageListener mMessageListener;
	
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
	 * @param aMessageListener
	 *            The message listener.
	 * @param aParent
	 *            The parent item. May be {@code null}.
	 */
	public ActionInstanceImpl(final Action aAction, final Context aContext, final CharacterInstance aChar, final MessageListener aMessageListener,
			final ItemInstance aParent)
	{
		mAction = aAction;
		mParent = aParent;
		mContext = aContext;
		mChar = aChar;
		mMessageListener = aMessageListener;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_action, null);
		
		initDices();
		
		mUse = (Button) getContainer().findViewById(R.id.view_use_action_button);
		final TextView name = (TextView) getContainer().findViewById(R.id.view_action_name_label);
		
		mUse.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				use();
			}
		});
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
	public boolean canUse()
	{
		int level = -1;
		if (mParent != null)
		{
			level = mParent.getValue();
		}
		if (level >= 0 && level < mAction.getMinLevel() || !mChar.getMode().getMode().canUseAction())
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
	public void use()
	{
		if ( !canUse())
		{
			Log.w(TAG, "Tried to use a non usable action.");
			return;
		}
		final int defaultDices = getDefaultDices();
		if (hasCostDices())
		{
			final DicesChooseListener listener = new DicesChooseListener()
			{
				@Override
				public void choseDices(final Map<ItemInstance, Integer> aValues)
				{
					use(defaultDices, aValues);
				}
			};
			ChooseDicesDialog.showChooseDicesDialog(mCostDices, mContext.getString(R.string.choose_dices_amount), mContext, listener);
		}
		else
		{
			use(defaultDices, new HashMap<ItemInstance, Integer>());
		}
	}
	
	@Override
	public void use(final int aDefaultDices, final Map<ItemInstance, Integer> aDices)
	{
		final String[] arguments = new String[3 + 2 * aDices.size()];
		final boolean[] trans = new boolean[3 + 2 * aDices.size()];
		final String[] args = new String[3 + 2 * aDices.size()];
		int counter = 0;
		
		arguments[counter] = getAction().getName();
		args[counter] = getAction().getDisplayName();
		trans[counter++ ] = true;
		
		arguments[counter] = "" + aDefaultDices;
		args[counter] = "" + aDefaultDices;
		trans[counter++ ] = false;
		
		arguments[counter] = "" + getAction().getInstantSuccess();
		args[counter] = "" + getAction().getInstantSuccess();
		trans[counter++ ] = false;
		
		for (final ItemInstance item : aDices.keySet())
		{
			arguments[counter] = item.getName();
			args[counter] = item.getDisplayName();
			trans[counter++ ] = true;
			
			arguments[counter] = " = " + aDices.get(item);
			args[counter] = " = " + aDices.get(item);
			trans[counter++ ] = false;
		}
		
		mMessageListener.makeText(DataUtil.buildMessage(R.string.use_action, args, mContext), Toast.LENGTH_LONG);
		Builder builder = new Builder(R.string.uses_action, mContext);
		builder.setGroup(MessageGroup.ACTION).setSender(mChar.getName()).setArguments(arguments).setTranslated(trans);
		builder.setType(MessageType.YES_NO).setYesAction(ButtonAction.ACCEPT_ACTION).setNoAction(ButtonAction.DENY_ACTION);
		mMessageListener.sendMessage(builder.setSaveables(arguments).create());
	}
	
	@Override
	public void use(final String[] aArguments)
	{
		final int defaultDices = Integer.parseInt(aArguments[1]);
		final int instantSuccess = Integer.parseInt(aArguments[2]);
		int additionalDices = 0;
		final int difficulty = Integer.parseInt(aArguments[aArguments.length - 1]);
		
		for (int i = 3; i < aArguments.length - 1; i += 2)
		{
			final ItemInstance item = getCostDiceItem(aArguments[i]);
			int value = Integer.parseInt(aArguments[i + 1].substring(" = ".length()));
			additionalDices += value;
			while (value > 0)
			{
				item.decrease(false);
				value-- ;
			}
		}
		
		for (final ItemInstance cost : mCosts.keySet())
		{
			int amount = mCosts.get(cost);
			while (amount > 0)
			{
				cost.decrease(false);
				amount-- ;
			}
		}
		
		int success = instantSuccess;
		int specialSuccess = 0;
		final StringBuilder results = new StringBuilder();
		for (int i = 0; i < defaultDices + additionalDices; i++ )
		{
			final int result = (int) (Math.random() * 9 + 1);
			if (result >= difficulty)
			{
				if (result == 10)
				{
					specialSuccess++ ;
				}
				else
				{
					success++ ;
				}
			}
			
			results.append(" " + result);
		}
		
		final String[] args = new String[] { getAction().getDisplayName(), results.toString(), "" + success, "" + specialSuccess };
		mMessageListener.makeText(DataUtil.buildMessage(R.string.using_action, args, mContext), Toast.LENGTH_LONG);
		args[0] = getAction().getName();
		Builder builder = new Builder(R.string.used_action, mContext);
		builder.setGroup(MessageGroup.ACTION).setSender(mChar.getName()).setArguments(args).setTranslated(true, false, false, false);
		mMessageListener.sendMessage(builder.create());
	}
	
	private ItemInstance getCostDiceItem(final String aName)
	{
		for (final ItemInstance item : mCostDices)
		{
			if (item.getName().equals(aName))
			{
				return item;
			}
		}
		return null;
	}
	
	@Override
	public boolean hasCostDices()
	{
		return !mCostDices.isEmpty();
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
		ViewUtil.setEnabled(mUse, canUse());
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
