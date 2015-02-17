package com.deepercreeper.vampireapp.items;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;

public class ItemProviderImpl implements ItemProvider
{
	private final ClanController		mClans;
	
	private final DescriptionController	mDescriptions;
	
	private final NatureController		mNatures;
	
	private final List<ItemController>	mControllers;
	
	private final int[]					mDefaultHealth;
	
	public ItemProviderImpl(final Context aContext)
	{
		LanguageUtil.init(aContext);
		mClans = DataUtil.createClans(aContext);
		mControllers = DataUtil.createItems(aContext);
		mNatures = new NatureController(aContext.getResources());
		mDescriptions = new DescriptionController(aContext.getResources());
		mDefaultHealth = DataUtil.loadDefaultHealth(aContext);
	}
	
	@Override
	public ClanController getClans()
	{
		return mClans;
	}
	
	@Override
	public List<ItemController> getControllers()
	{
		return mControllers;
	}
	
	@Override
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	@Override
	public NatureController getNatures()
	{
		return mNatures;
	}
	
	@Override
	public int[] getDefaultHealth()
	{
		return mDefaultHealth;
	}
	
	@Override
	public ItemController getController(final String aName)
	{
		for (final ItemController controller : getControllers())
		{
			if (controller.getName().equals(aName))
			{
				return controller;
			}
		}
		return null;
	}
}
