package com.deepercreeper.vampireapp.test;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.activities.MainActivity;
import com.robotium.solo.Solo;
import android.test.ActivityUnitTestCase;

public class ClientTest extends ActivityUnitTestCase<MainActivity>
{
	private Solo mSolo;
	
	public ClientTest()
	{
		super(MainActivity.class);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		mSolo = new Solo(getInstrumentation());
		
		getActivity();
	}
	
	@Override
	public void tearDown() throws Exception
	{
		mSolo.finishOpenedActivities();
	}
	
	@Override
	public void runTest()
	{
		assertTrue("Could not load activity!", mSolo.waitForActivity(MainActivity.class));
		assertNotNull(getActivity().findViewById(R.id.f_create_char_button));
		mSolo.drag(1, 0, 0, 0, 2);
		mSolo.waitForView(R.id.f_create_host_button);
		assertNotNull(getActivity().findViewById(R.id.f_create_host_button));
	}
}
