package com.deepercreeper.vampireapp.connection.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.Device;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * A controller for between device actions.
 * 
 * @author vrl
 */
public class DeviceController extends Thread
{
	private static final String TAG = "DeviceController";
	
	private static final UUID DEFAULT_UUID = UUID.fromString("c155da2d-302d-4f10-bc3b-fa277af3599d");
	
	private static final int TIMEOUT = 3000;
	
	private final Connector mConnector;
	
	private BluetoothServerSocket mInsecureServerSocket = null;
	
	private BluetoothServerSocket mSecureServerSocket = null;
	
	private boolean mRunning = true;
	
	private boolean mServerRunning = false;
	
	private SelectItemDialog<Device> mListeningDialog = null;
	
	private final List<Device> mListeningDevices = new ArrayList<Device>();
	
	private final List<ConnectedDevice> mConnectedDevices = new ArrayList<ConnectedDevice>();
	
	/**
	 * Creates a new device controller that runs inside of another thread.
	 * 
	 * @param aConnector
	 *            The connector.
	 */
	public DeviceController(Connector aConnector)
	{
		mConnector = aConnector;
		start();
	}
	
	/**
	 * Stops listening for device servers and ends this controller.
	 */
	public void stopRunning()
	{
		if (mListeningDialog != null)
		{
			mListeningDialog = null;
			mListeningDevices.clear();
		}
		mRunning = false;
	}
	
	/**
	 * @return the connected device that is a host or {@code null} if no host is connected.
	 */
	public ConnectedDevice getHost()
	{
		for (final ConnectedDevice device : mConnectedDevices)
		{
			if (device.isHost())
			{
				return device;
			}
		}
		return null;
	}
	
	/**
	 * @return the Bluetooth adapter from the connector.
	 */
	public BluetoothAdapter getBluetoothAdapter()
	{
		return mConnector.getBluetoothAdapter();
	}
	
	/**
	 * Starts a server that may be connected by any device.
	 */
	public void startServer()
	{
		mServerRunning = true;
		try
		{
			mInsecureServerSocket = getBluetoothAdapter().listenUsingInsecureRfcommWithServiceRecord(getBluetoothAdapter().getName(), DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		try
		{
			mSecureServerSocket = getBluetoothAdapter().listenUsingRfcommWithServiceRecord(getBluetoothAdapter().getName(), DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		if (mInsecureServerSocket != null)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					listenForDevices(false);
				}
			}.start();
		}
		if (mSecureServerSocket != null)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					listenForDevices(true);
				}
			}.start();
		}
	}
	
	private void listenForDevices(final boolean aSecure)
	{
		final BluetoothServerSocket server = aSecure ? mSecureServerSocket : mInsecureServerSocket;
		while (mServerRunning && server != null)
		{
			BluetoothSocket socket = null;
			ConnectedDevice connectedDevice = null;
			try
			{
				socket = server.accept(1000);
			}
			catch (final IOException e)
			{}
			if (socket != null)
			{
				try
				{
					connectedDevice = new ConnectedDevice(socket, mConnector, false);
					mConnectedDevices.add(connectedDevice);
					mConnector.connectedTo(connectedDevice);
				}
				catch (final IOException e)
				{}
				if (connectedDevice == null)
				{
					Log.i(TAG, "Connection listening failed.");
				}
			}
		}
		if (server != null)
		{
			try
			{
				server.close();
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not close insecure socket.");
			}
		}
		if (aSecure)
		{
			mSecureServerSocket = null;
		}
		else
		{
			mInsecureServerSocket = null;
		}
	}
	
	/**
	 * @return a list of all connected devices.
	 */
	public List<ConnectedDevice> getConnectedDevices()
	{
		return mConnectedDevices;
	}
	
	/**
	 * @return the Bluetooth receiver.
	 */
	public BluetoothReceiver getBluetoothReceiver()
	{
		return mConnector.getBluetoothReceiver();
	}
	
	/**
	 * The given device has exited.
	 * 
	 * @param aDevice
	 *            The once connected device.
	 */
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		mConnectedDevices.remove(aDevice);
	}
	
	/**
	 * Creates a dialog that searches for devices.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public void findServer(final Context aContext)
	{
		ItemSelectionListener<Device> listener = new ItemSelectionListener<Device>()
		{
			@Override
			public void select(Device aItem)
			{
				getBluetoothReceiver().removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				mListeningDialog = null;
				mListeningDevices.clear();
				connectTo(aItem);
			}
			
			@Override
			public void cancel()
			{
				getBluetoothReceiver().removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				mListeningDialog = null;
				mListeningDevices.clear();
			}
		};
		mListeningDialog = SelectItemDialog.createSelectionDialog(Collections.<Device> emptyList(), aContext.getString(R.string.choose_host),
				aContext, listener);
				
		for (final BluetoothDevice btDevice : getBluetoothAdapter().getBondedDevices())
		{
			Device device = new Device(btDevice, aContext);
			mListeningDevices.add(device);
			mListeningDialog.addOption(device, false);
		}
		
		getBluetoothReceiver().setDeviceListener(BluetoothDevice.ACTION_FOUND, new BluetoothListener()
		{
			@Override
			public void action(final BluetoothDevice aDevice)
			{
				getHandler().post(new Runnable()
				{
					@Override
					public void run()
					{
						if (aDevice.getName() == null)
						{
							Log.i(TAG, "Added no name device.");
						}
						Device device = new Device(aDevice, aContext);
						mListeningDevices.add(device);
						mListeningDialog.addOption(device, false);
					}
				});
			}
		});
		
		if ( !getBluetoothAdapter().startDiscovery())
		{
			if (getBluetoothAdapter().getBondedDevices().isEmpty())
			{
				mConnector.makeText(R.string.no_paired_device, Toast.LENGTH_SHORT);
				return;
			}
		}
		
		mListeningDialog.show(((Activity) aContext).getFragmentManager(), aContext.getString(R.string.choose_host));
	}
	
	/**
	 * @return the handler of the connector.
	 */
	public Handler getHandler()
	{
		return mConnector.getHandler();
	}
	
	@Override
	public void run()
	{
		while (mRunning)
		{
			int index = 0;
			while (mRunning && isListening())
			{
				if ( !getBluetoothAdapter().isDiscovering() && !mListeningDevices.isEmpty())
				{
					if (index >= mListeningDevices.size())
					{
						index = 0;
					}
					Device device = mListeningDevices.get(index);
					if (askForServer(device))
					{
						mListeningDevices.remove(device);
					}
					else
					{
						index++ ;
					}
				}
			}
		}
	}
	
	private void connectTo(Device aDevice)
	{
		final BluetoothDevice device = aDevice.getDevice();
		ConnectedDevice connectedDevice = null;
		BluetoothSocket socket = null;
		try
		{
			socket = device.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		if (socket == null)
		{
			try
			{
				socket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
			}
			catch (final IOException e)
			{}
		}
		if (socket != null)
		{
			try
			{
				socket.connect();
			}
			catch (final IOException e)
			{}
			if (socket.isConnected())
			{
				try
				{
					connectedDevice = new ConnectedDevice(socket, mConnector, true);
				}
				catch (final IOException e)
				{}
			}
		}
		if (connectedDevice == null)
		{
			mConnector.makeText("Could not connect to device " + aDevice.getDisplayName(), Toast.LENGTH_SHORT);
		}
		else
		{
			mConnectedDevices.add(connectedDevice);
			mConnector.connectedTo(connectedDevice);
		}
	}
	
	/**
	 * @return whether any user is trying to connect to a server.
	 */
	public boolean isListening()
	{
		return mListeningDialog != null;
	}
	
	private boolean askForServer(Device aDevice)
	{
		aDevice.setState(Device.State.SEARCHING);
		mListeningDialog.updateUI();
		final BluetoothDevice device = aDevice.getDevice();
		BluetoothSocket socket = null;
		
		try
		{
			socket = device.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		if (socket == null)
		{
			try
			{
				socket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
			}
			catch (final IOException e)
			{}
		}
		boolean connected = false;
		if (socket != null)
		{
			try
			{
				runConnectionThread(socket);
				try
				{
					Thread.sleep(TIMEOUT);
				}
				catch (final InterruptedException e)
				{}
				if ( !socket.isConnected())
				{
					socket.close();
				}
			}
			catch (final Exception e)
			{}
			if (socket.isConnected())
			{
				aDevice.setState(Device.State.AVAILABLE);
				mListeningDialog.setOptionEnabled(aDevice, true);
				connected = true;
				try
				{
					socket.close();
				}
				catch (final IOException e)
				{}
			}
		}
		if ( !connected)
		{
			aDevice.setState(Device.State.REFUSED);
			mListeningDialog.setOptionEnabled(aDevice, false);
		}
		return connected;
	}
	
	private void runConnectionThread(final BluetoothSocket aSocket)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				boolean exception = false;
				try
				{
					aSocket.connect();
				}
				catch (final Exception e)
				{
					exception = true;
				}
				if ( !exception)
				{
					DeviceController.this.interrupt();
				}
			}
		}.start();
	}
}
