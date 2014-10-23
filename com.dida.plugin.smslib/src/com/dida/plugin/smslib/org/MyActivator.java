/**
 * 
 */
package com.dida.plugin.smslib.org;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.dida.plugin.smslib.examples.modem.ReadMessages;

/**
 * @author Dida
 *
 */
public class MyActivator implements BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("here");
		ReadMessages read = new ReadMessages();
		read.doIt();

	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
