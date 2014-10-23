// SendMessage.java - Sample application.
//
// This application shows you the basic procedure for sending messages.
// You will find how to send synchronous and asynchronous messages.
//
// For asynchronous dispatch, the example application sets a callback
// notification, to see what's happened with messages.
//
// Bulk Operator used: BULKSMS (http://www.bulksms.com)
// Please look the BulkSmsHTTPGateway documentation for details.

package com.dida.plugin.smslib.examples.bulksms;

import com.dida.plugin.smslib.org.smslib.Library;
import com.dida.plugin.smslib.org.smslib.OutboundMessage;
import com.dida.plugin.smslib.org.smslib.Service;
import com.dida.plugin.smslib.org.smslib.http.BulkSmsHTTPGateway;

public class SendMessage
{
	public void doIt() throws Exception
	{
		try
		{
			OutboundMessage msg;
			System.out.println("Example: Send message from BulkSMS HTTP Interface.");
			System.out.println(Library.getLibraryDescription());
			System.out.println("Version: " + Library.getLibraryVersion());
			BulkSmsHTTPGateway gateway = new BulkSmsHTTPGateway("bulksms.http.1", "username", "password");
			gateway.setOutbound(true);
			Service.getInstance().addGateway(gateway);
			Service.getInstance().startService();
			// Query the service to find out our credit balance.
			System.out.println("Remaining credit: " + gateway.queryBalance());
			// Send a message synchronously.
			msg = new OutboundMessage("+30...", "Hello from SMSLib (BULKSMS handler)");
			Service.getInstance().sendMessage(msg);
			System.out.println(msg);
			System.out.println("Now Sleeping - Hit <enter> to terminate.");
			System.in.read();
			Service.getInstance().stopService();
		}
		catch (Exception e)
		{
			System.out.print(e);
			e.printStackTrace();
		}
	}

}
