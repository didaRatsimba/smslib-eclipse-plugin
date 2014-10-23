// SendMessage.java - Sample application.
//
// SMPP Gateway used: JSMPP (http://code.google.com/p/jsmpp/)

package com.dida.plugin.smslib.examples.smpp.jsmpp;

import com.dida.plugin.smslib.org.smslib.AGateway;
import com.dida.plugin.smslib.org.smslib.AGateway.GatewayStatuses;
import com.dida.plugin.smslib.org.smslib.IGatewayStatusNotification;
import com.dida.plugin.smslib.org.smslib.IOutboundMessageNotification;
import com.dida.plugin.smslib.org.smslib.Library;
import com.dida.plugin.smslib.org.smslib.OutboundMessage;
import com.dida.plugin.smslib.org.smslib.Service;
import com.dida.plugin.smslib.org.smslib.smpp.BindAttributes;
import com.dida.plugin.smslib.org.smslib.smpp.BindAttributes.BindType;
import com.dida.plugin.smslib.org.smslib.smpp.jsmpp.JSMPPGateway;


/**
 * @author Bassam Al-Sarori
 */
public class SendMessage
{
	public void doIt() throws Exception
	{
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		OutboundNotification outboundNotification = new OutboundNotification();
		OutboundMessage msg;
		System.out.println("Example: Send messages through SMPP using JSMPP.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("Version: " + Library.getLibraryVersion());
		JSMPPGateway gateway = new JSMPPGateway("smppcon", "localhost", 2715, new BindAttributes("smppclient1", "password", "cp", BindType.TRANSMITTER));
		Service.getInstance().addGateway(gateway);
		Service.getInstance().setGatewayStatusNotification(statusNotification);
		Service.getInstance().setOutboundMessageNotification(outboundNotification);
		Service.getInstance().startService();
		// Send a message.
		msg = new OutboundMessage("+967712831950", "Hello from SMSLib and JSMPP");
		Service.getInstance().sendMessage(msg);
		System.out.println(msg);
		System.out.println("Now Sleeping - Hit <enter> to terminate.");
		System.in.read();
		Service.getInstance().stopService();
	}


	public class OutboundNotification implements IOutboundMessageNotification
	{
		@Override
		public void process(AGateway gateway, OutboundMessage msg)
		{
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
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
