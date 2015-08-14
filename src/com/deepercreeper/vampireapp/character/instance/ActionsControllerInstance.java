package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
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
	
	private final Context mContext;
	
	private final CharacterInstance mChar;
	
	private final Expander mExpander;
	
	private final List<ActionInstance> mActions = new ArrayList<ActionInstance>();
	
	/**
	 * Creates a new actions controller.
	 * 
	 * @param aChar
	 *            The parent character.
	 * @param aContext
	 *            The underlying context.
	 */
	public ActionsControllerInstance(final CharacterInstance aChar, final Context aContext)
	{
		mContext = aContext;
		mChar = aChar;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.client_actions, null);
		mExpander = Expander.handle(R.id.c_actions_button, R.id.c_actions_panel, getContainer());
		
		mExpander.init();
		
		init();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * Updates the expander button.
	 */
	public void update()
	{
		for (final ActionInstance action : mActions)
		{
			action.update();
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
		update();
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
		update();
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
			action.init();
			mExpander.getContainer().addView(action.getContainer());
		}
	}
	
	@Override
	public void init()
	{
		for (final ActionInstance action : mActions)
		{
			action.release();
		}
		mActions.clear();
		for (final ItemControllerInstance controller : mChar.getControllers())
		{
			mActions.addAll(controller.getActions());
		}
		// TODO Add other actions
		
		sortActions();
		update();
	}
}
