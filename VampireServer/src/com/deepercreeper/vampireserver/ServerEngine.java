package com.deepercreeper.vampireserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerEngine implements HttpHandler
{
	private static final String IP = "134.255.218.105";
	
	private static final int PORT = 3114;
	
	private HttpServer mServer;
	
	public ServerEngine()
	{
		try
		{
			mServer = HttpServer.create(new InetSocketAddress(PORT), 0);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if (mServer != null)
		{
			mServer.createContext("/vampire", this);
			mServer.setExecutor(null);
			mServer.start();
		}
	}
	
	public void stop()
	{
		mServer.stop(0);
	}
	
	@Override
	public void handle(final HttpExchange aArg0) throws IOException
	{
		final String response = "This is the response";
		aArg0.sendResponseHeaders(200, response.length());
		final OutputStream os = aArg0.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
