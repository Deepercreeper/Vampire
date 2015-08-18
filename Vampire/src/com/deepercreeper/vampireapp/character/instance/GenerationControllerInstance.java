package com.deepercreeper.vampireapp.character.instance;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.change.GenerationChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerInstance implements Viewable, Saveable
{
	private static final int MAX_LEVEL_POINTS = 7;
	
	private static final int MIN_GENERATION = 3;
	
	private final CharacterInstance mChar;
	
	private final LinearLayout mContainer;
	
	private final boolean mHost;
	
	private final MessageListener mChangeListener;
	
	private final ImageButton mIncreaseButton;
	
	private final TextView mGenerationText;
	
	private int mGeneration;
	
	/**
	 * Creates a generation controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 * @param aChangeListener
	 *            A listener for generation changes.
	 */
	public GenerationControllerInstance(final Element aElement, final CharacterInstance aChar, final boolean aHost,
			final MessageListener aChangeListener)
	{
		mChar = aChar;
		mChangeListener = aChangeListener;
		mGeneration = Integer.parseInt(aElement.getAttribute("generation"));
		mHost = aHost;
		final int id = mHost ? R.layout.host_generation : R.layout.client_generation;
		mContainer = (LinearLayout) View.inflate(mChar.getContext(), id, null);
		
		mGenerationText = (TextView) getContainer().findViewById(mHost ? R.id.h_generation_label : R.id.c_generation_label);
		mIncreaseButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_increase_generation_button) : null;
		
		if (mHost)
		{
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
				}
			});
		}
	}
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aGeneration
	 *            The character creation.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            whether this is a host side controller.
	 * @param aChangeListener
	 *            A listener for generation changes.
	 */
	public GenerationControllerInstance(final int aGeneration, final CharacterInstance aChar, final boolean aHost,
			final MessageListener aChangeListener)
	{
		mChar = aChar;
		mGeneration = aGeneration;
		mChangeListener = aChangeListener;
		mHost = aHost;
		final int id = mHost ? R.layout.host_generation : R.layout.client_generation;
		mContainer = (LinearLayout) View.inflate(mChar.getContext(), id, null);
		
		mGenerationText = (TextView) getContainer().findViewById(mHost ? R.id.h_generation_label : R.id.c_generation_label);
		mIncreaseButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_increase_generation_button) : null;
		
		if (mHost)
		{
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
				}
			});
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("generation");
		element.setAttribute("generation", "" + mGeneration);
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * Lets the character increase its generation. (The generation value will decrease)
	 */
	public void increase()
	{
		// TODO Maybe add a button to request generation increase
		if (mGeneration > MIN_GENERATION)
		{
			mGeneration-- ;
		}
		mChangeListener.sendChange(new GenerationChange(mGeneration));
		// TODO Maybe update the whole character because of blood pool changes
		updateUI();
	}
	
	/**
	 * @return whether the character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration > MAX_LEVEL_POINTS;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Sets the generation of the character.
	 * 
	 * @param aGeneration
	 *            The new generation.
	 */
	public void updateGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
		mChar.updateUI();
	}
	
	/**
	 * Updates the generation value.
	 */
	@Override
	public void updateUI()
	{
		mGenerationText.setText("" + mGeneration);
	}
}
