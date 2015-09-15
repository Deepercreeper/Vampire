package com.deepercreeper.vampireapp.util.view;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * An expander handles the expandable container view.
 * 
 * @author Vincent
 */
public class Expander implements ResizeListener, AnimatorUpdateListener
{
	private static final String TAG = "Expander";
	
	private final ValueAnimator mAnimator = new ValueAnimator();
	
	private final ResizeListener mResizeParent;
	
	private final View mParent;
	
	private final int mButtonId;
	
	private final int mContainerId;
	
	private Button mButton;
	
	private LinearLayout mContainer;
	
	private boolean mInitialized = false;
	
	private boolean mOpen;
	
	private boolean mLastOpen;
	
	private Expander(final int aButtonId, final int aContainerId, final View aContainer)
	{
		this(aButtonId, aContainerId, aContainer, null);
	}
	
	private Expander(final int aButtonId, final int aContainerId, final View aParent, final ResizeListener aResizeParent)
	{
		mButtonId = aButtonId;
		mContainerId = aContainerId;
		mResizeParent = aResizeParent;
		mParent = aParent;
		mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		mAnimator.setDuration(300);
		mAnimator.addUpdateListener(this);
	}
	
	/**
	 * Closes the expander.
	 */
	public void close()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
		mOpen = false;
		resize();
	}
	
	/**
	 * @return the currently set expand button.
	 */
	public Button getButton()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return null;
		}
		return mButton;
	}
	
	/**
	 * @return the currently set expandable panel.
	 */
	public LinearLayout getContainer()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return null;
		}
		return mContainer;
	}
	
	/**
	 * @return the parent expander if existing. <code>null</code> otherwise.
	 */
	public Expander getParent()
	{
		if (mResizeParent instanceof Expander)
		{
			return (Expander) mResizeParent;
		}
		return null;
	}
	
	/**
	 * Initializes the views by inflating them.
	 */
	public void init()
	{
		mButton = (Button) mParent.findViewById(mButtonId);
		mContainer = (LinearLayout) mParent.findViewById(mContainerId);
		
		mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
		mButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				toggleOpen();
			}
		});
		
		mInitialized = true;
		
		close();
	}
	
	/**
	 * @return whether this expander has been initialized yet.
	 */
	public boolean isInitialized()
	{
		return mInitialized;
	}
	
	/**
	 * @return whether this expander is open.
	 */
	public boolean isOpen()
	{
		return mOpen;
	}
	
	@Override
	public void onAnimationUpdate(final ValueAnimator aAnimation)
	{
		final LayoutParams params = getContainer().getLayoutParams();
		float alpha = aAnimation.getAnimatedFraction();
		if (alpha == 1.0f)
		{
			mLastOpen = isOpen();
		}
		if ( !isOpen())
		{
			alpha = 1 - alpha;
		}
		params.height = (Integer) aAnimation.getAnimatedValue();
		
		if (mLastOpen != mOpen)
		{
			final Drawable drawable = ViewUtil.rotateDrawable(getContainer().getResources().getDrawable(R.drawable.ic_down), alpha * 180);
			getButton().setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
			getContainer().setAlpha(alpha);
		}
		
		getContainer().requestLayout();
	}
	
	@Override
	public void resize()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
		ViewUtil.resizeRecursive(this);
	}
	
	/**
	 * @return the expander animator.
	 */
	public ValueAnimator getAnimator()
	{
		return mAnimator;
	}
	
	/**
	 * Toggles whether the expander is open.
	 */
	public void toggleOpen()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
		mOpen = !mOpen;
		resize();
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aButtonId
	 *            The expand button id.
	 * @param aContainerId
	 *            The expandable container id.
	 * @param aParent
	 *            The parent container.
	 * @return the created expander.
	 */
	public static Expander handle(final int aButtonId, final int aContainerId, final View aParent)
	{
		return new Expander(aButtonId, aContainerId, aParent);
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aButtonId
	 *            The expand button id.
	 * @param aContainerId
	 *            The expandable container id.
	 * @param aParent
	 *            The parent container.
	 * @param aResizeParent
	 *            The resize parent.
	 * @return the created expander.
	 */
	public static Expander handle(final int aButtonId, final int aContainerId, final View aParent, final ResizeListener aResizeParent)
	{
		return new Expander(aButtonId, aContainerId, aParent, aResizeParent);
	}
}
