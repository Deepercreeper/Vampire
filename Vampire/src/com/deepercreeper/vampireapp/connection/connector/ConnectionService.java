package com.deepercreeper.vampireapp.connection.connector;

import com.deepercreeper.vampireapp.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * A service that is able to handle all connection actions.
 * 
 * @author vrl
 */
public class ConnectionService extends Service
{
	/**
	 * A binder that is able to provide the connector.
	 * 
	 * @author vrl
	 */
	public class ConnectorBinder extends Binder
	{
		/**
		 * @return the connector.
		 */
		public Connector getConnector()
		{
			return mConnector;
		}
	}
	
	private static final String TAG = "ConnectionService";
	
	private final IBinder mBinder = new ConnectorBinder();
	
	private boolean mInitializing;
	
	private Connector mConnector;
	
	@Override
	public IBinder onBind(final Intent intent)
	{
		while (mInitializing)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (final InterruptedException e)
			{
				Log.e(TAG, "Interrupted provider thread.");
			}
		}
		return mBinder;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		mInitializing = true;
		
		Log.i(TAG, "Starting connection service.");
		
		mConnector = new ConnectorImpl();
		
		Log.i(TAG, "Started connection service.");
		
		mInitializing = false;
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId)
	{
		return Service.START_NOT_STICKY;
	}
}
