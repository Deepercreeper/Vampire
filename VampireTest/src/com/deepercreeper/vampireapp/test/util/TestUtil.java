package com.deepercreeper.vampireapp.test.util;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class TestUtil
{
	private TestUtil()
	{}
	
	public static boolean waitForActivityDestroyed(Solo aSolo, final Activity aActivity, int aTimeout)
	{
		return aSolo.waitForCondition(new Condition()
		{
			@Override
			public boolean isSatisfied()
			{
				return aActivity.isDestroyed();
			}
		}, aTimeout);
	}
	
	public static boolean isServiceRunning(Class<?> serviceClass, Activity aActivity)
	{
		ActivityManager manager = (ActivityManager) aActivity.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if (serviceClass.getName().equals(service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}
}
