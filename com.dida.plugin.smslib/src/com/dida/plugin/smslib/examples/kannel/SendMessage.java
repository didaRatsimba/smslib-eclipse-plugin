// SendMessage.java - Sample application.
//
// Gateway used: Kannel (http://www.kannel.org)
// Please look the KannelHTTPGateway documentation for details.

package com.dida.plugin.smslib.examples.kannel;

import com.dida.plugin.smslib.org.smslib.AGateway;
import com.dida.plugin.smslib.org.smslib.AGateway.GatewayStatuses;
import com.dida.plugin.smslib.org.smslib.IGatewayStatusNotification;
import com.dida.plugin.smslib.org.smslib.Library;
import com.dida.plugin.smslib.org.smslib.OutboundMessage;
import com.dida.plugin.smslib.org.smslib.Service;
import com.dida.plugin.smslib.org.smslib.http.KannelHTTPGateway;

/**
 * @author Bassam Al-Sarori
 */
public class SendMessage
{
	public void doIt() throws Exception
	{
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		OutboundMessage msg;
		System.out.println("Example: Send message through Kannel HTTP Interface.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("Version: " + Library.getLibraryVersion());
		KannelHTTPGateway gateway = new KannelHTTPGateway("mysmsc", "http://localhost:13013/cgi-bin/sendsms", "simple", "elpmis");
		// Uncomment in order gateway to start and stop SMSC automatically on Kannel
		//gateway.setAutoStartSmsc(true);
		//gateway.setAutoStopSmsc(true);
		// Set Kannel's Admin URL and password to be used starting, stopping and checking SMSC status   
		gateway.setAdminUrl("http://localhost:13000");
		gateway.setAdminPassword("bar");
		gateway.setOutbound(true);
		Service.getInstance().addGateway(gateway);
		Service.getInstance().setGatewayStatusNotification(statusNotification);
		Service.getInstance().startService();
		// Send a message.
		msg = new OutboundMessage("+967712831950", "Hello from SMSLib (Kannel handler)");
		//msg.setEncoding(MessageEncodings.ENCUCS2);
		Service.getInstance().sendMessage(msg);
		System.out.println(msg);
		System.out.println("Now Sleeping - Hit <enter> to terminate.");
		System.in.read();
		Service.getInstance().stopService();
	}


	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		@Override
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}
}
