package com.deepercreeper.vampireapp.lists.controllers.instances;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerInstance implements Viewable, Saveable
{
	private static final int		MAX_LEVEL_POINTS	= 7;
	
	private static final int		MIN_GENERATION		= 3;
	
	private final CharacterInstance	mChar;
	
	private final LinearLayout		mContainer;
	
	private final boolean			mHost;
	
	private TextView				mGenerationText;
	
	private boolean					mInitialized		= false;
	
	private int						mGeneration;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aGeneration
	 *            The character creation.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            whether this is a host side controller.
	 */
	public GenerationControllerInstance(final int aGeneration, final CharacterInstance aChar, final boolean aHost)
	{
		mChar = aChar;
		mGeneration = aGeneration;
		mHost = aHost;
		final int id = mHost ? R.layout.host_generation : R.layout.client_generation;
		mContainer = (LinearLayout) View.inflate(mChar.getContext(), id, null);
		init();
	}
	
	/**
	 * Creates a generation controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public GenerationControllerInstance(final Element aElement, final CharacterInstance aChar, final boolean aHost)
	{
		mChar = aChar;
		mGeneration = Integer.parseInt(aElement.getAttribute("generation"));
		mHost = aHost;
		final int id = mHost ? R.layout.host_generation : R.layout.client_generation;
		mContainer = (LinearLayout) View.inflate(mChar.getContext(), id, null);
		init();
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mGenerationText = (TextView) mContainer.findViewById(R.id.generation_label);
			
			mInitialized = true;
		}
		
		updateValue();
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("generation");
		element.setAttribute("generation", "" + mGeneration);
		return element;
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
	 * Lets the character increase its generation. (The generation value will decrease)
	 */
	public void increase()
	{
		if (mGeneration > MIN_GENERATION)
		{
			mGeneration-- ;
		}
		updateValue();
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * Sets the generation of the character.
	 * 
	 * @param aGeneration
	 *            The new generation.
	 */
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
		updateValue();
		mChar.update();
	}
	
	/**
	 * @return whether the character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration > MAX_LEVEL_POINTS;
	}
	
	/**
	 * Updates the generation value.
	 */
	public void updateValue()
	{
		mGenerationText.setText("" + mGeneration);
	}
}
