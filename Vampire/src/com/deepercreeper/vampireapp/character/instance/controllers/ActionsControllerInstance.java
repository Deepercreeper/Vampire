package com.deepercreeper.vampireapp.character.instance.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstanceImpl;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.Expander;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * A controller that handles the usage of actions.
 * 
 * @author Vincent
 */
public class ActionsControllerInstance implements Viewable
{
	private final LinearLayout mContainer;
	
	private final MessageListener mMessageListener;
	
	private final Context mContext;
	
	private final CharacterInstance mChar;
	
	private final Expander mExpander;
	
	private final List<ActionInstance> mActions = new ArrayList<ActionInstance>();
	
	/**
	 * Creates a new actions controller.
	 * 
	 * @param aChar
	 *            The parent character.
	 * @param aDefaultActions
	 *            A set of all default actions.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 */
	public ActionsControllerInstance(final CharacterInstance aChar, final Set<Action> aDefaultActions, final Context aContext,
			final MessageListener aMessageListener)
	{
		mContext = aContext;
		mChar = aChar;
		mMessageListener = aMessageListener;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.client_actions, null);
		mExpander = Expander.handle(R.id.c_actions_button, R.id.c_actions_panel, getContainer());
		
		mExpander.init();
		
		for (final ItemControllerInstance controller : mChar.getControllers())
		{
			mActions.addAll(controller.getActions());
		}
		for (final Action defaultAction : aDefaultActions)
		{
			final ActionInstance action = new ActionInstanceImpl(defaultAction, mContext, mChar, mMessageListener, null);
			action.initDices();
			mActions.add(action);
		}
		
		// MARK Actions
		
		sortActions();
		updateUI();
	}
	
	/**
	 * Adds all given actions and updates everything.
	 * 
	 * @param aActions
	 *            A set of action instances.
	 */
	public void addActions(final Set<ActionInstance> aActions)
	{
		mActions.addAll(aActions);
		sortActions();
		updateUI();
	}
	
	/**
	 * @param aName
	 *            The action name.
	 * @return the action instance whose action name is equals to the given name.
	 */
	public ActionInstance getAction(final String aName)
	{
		for (final ActionInstance action : mActions)
		{
			if (action.getAction().getName().equals(aName))
			{
				return action;
			}
		}
		return null;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes all given actions and updates everything.
	 * 
	 * @param aActions
	 *            A set of action instances.
	 */
	public void removeActions(final Set<ActionInstance> aActions)
	{
		for (final ActionInstance action : aActions)
		{
			action.release();
		}
		mActions.removeAll(aActions);
		sortActions();
		updateUI();
	}
	
	@Override
	public void updateUI()
	{
		for (final ActionInstance action : mActions)
		{
			action.updateUI();
		}
		final boolean hasActions = !mActions.isEmpty();
		if ( !hasActions)
		{
			mExpander.close();
		}
		else
		{
			mExpander.resize();
		}
		ViewUtil.setEnabled(mExpander.getButton(), hasActions);
	}
	
	private void sortActions()
	{
		for (final ActionInstance action : mActions)
		{
			action.release();
		}
		Collections.sort(mActions);
		for (final ActionInstance action : mActions)
		{
			action.updateUI();
			mExpander.getContainer().addView(action.getContainer());
		}
	}
}
