package com.dida.plugin.smslib.org.ajwcc.pduUtils.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dida.plugin.smslib.org.ajwcc.pduUtils.gsm3040.Pdu;
import com.dida.plugin.smslib.org.ajwcc.pduUtils.gsm3040.PduParser;
import com.dida.plugin.smslib.org.ajwcc.pduUtils.gsm3040.PduUtils;
import com.dida.plugin.smslib.org.smslib.Message;
import com.dida.plugin.smslib.org.smslib.Message.MessageEncodings;
import com.dida.plugin.smslib.org.smslib.OutboundBinaryMessage;
import com.dida.plugin.smslib.org.smslib.OutboundMessage;

public class MessageGeneratorTester
{

	public static void main(String[] args) throws Exception
	{
		String smscNo = "";
		int testCount = 0;
		int passCount = 0;

		// load a file for testing
		TestFileReader tfr = new TestFileReader();
		tfr.setSkipBlanksAndComment(false);

		tfr.init("java/org/ajwcc/pduUtils/testData/testMessages.txt");
		String currentLine;

		Message message = null;

		ArrayList<String> expectedPdus = new ArrayList<String>();

		main: while ((currentLine = tfr.next()) != null)
		{
			if (currentLine.trim().equals(""))
			{
				System.out.println(currentLine);
			}
			else if (currentLine.trim().startsWith("#"))
			{
				System.out.println(currentLine);
			}
			else
			{
				String[] data = new String[2];
				int colon = currentLine.indexOf(':');
				data[0] = currentLine.substring(0, colon);
				data[1] = currentLine.substring(colon+1);

				if (data[0].equals("messageType"))
				{
					message = (Message) Class.forName(data[1]).newInstance();
					testCount++;
				}
				else if (data[0].equals("messageEnd"))
				{
					if (message!=null)
					{
						// display pdus
						List<String> pdus = ((OutboundMessage) message).getPdus(smscNo, 123 );

						StringBuffer sb = new StringBuffer();
						for( String pduString  : pdus )
						{
							Pdu pdu = new PduParser().parsePdu(pduString);

							System.out.println(pdu);

							if (message instanceof OutboundBinaryMessage)
							{
								sb.append(PduUtils.bytesToPdu(pdu.getUserDataAsBytes()));
							}
							else if (message instanceof OutboundMessage)
							{
								sb.append(pdu.getDecodedText());
							}
						}
						System.out.println("EXPECTED PDUs   : "+expectedPdus.size());
						System.out.println("GENERATED PDUs  : "+pdus.size());

						if (message instanceof OutboundBinaryMessage)
						{
							OutboundBinaryMessage bin = (OutboundBinaryMessage) message;
							System.out.println("EXPECTED BYTES : "+PduUtils.bytesToPdu(bin.getDataBytes()));
							System.out.println("GENERATED BYTES: "+sb.toString());     

							boolean match = sb.toString().equals(PduUtils.bytesToPdu(bin.getDataBytes()));
							System.out.println("MATCH: "+match);                               
							if (match) {
								passCount++;
							}
						}
						else if (message instanceof OutboundMessage)
						{
							System.out.println("EXPECTED TEXT : "+message.getText());
							System.out.println("GENERATED TEXT: "+sb.toString());   

							boolean match = sb.toString().equals(message.getText());
							System.out.println("MATCH: "+match);   
							if (match) {
								passCount++;
							}
						}                            
					}

					System.out.println();
					expectedPdus.clear();
					message = null;
					smscNo = "";
				}
				else if (data[0].equals("encoding"))
				{
					if (data[1].equals("7"))
					{
						((OutboundMessage) message).setEncoding(MessageEncodings.ENC7BIT);
					}
					else if (data[1].equals("8"))
					{
						((OutboundMessage) message).setEncoding(MessageEncodings.ENC8BIT);                    
					}
					else if (data[1].equals("ucs2"))
					{
						((OutboundMessage) message).setEncoding(MessageEncodings.ENCUCS2);                    
					}
				}
				else if (data[0].equals("expectedPdu"))
				{
					expectedPdus.add(data[1]);
				}
				else if (data[0].equals("smscNumber"))
				{
					smscNo = data[1];
				}
				else if (data[0].equals("dataBytes"))
				{
					((OutboundBinaryMessage) message).setDataBytes(PduUtils.pduToBytes(data[1]));
				}
				else
				{
					// reflection
					// retrieve method getter for data[0]
					// get return type
					Class<?> currentClass = message.getClass();
					while(currentClass!=Object.class)
					{
						try
						{
							Method getter = message.getClass().getMethod("get"+Character.toUpperCase(data[0].charAt(0))+data[0].substring(1));
							Class<?> returnType = getter.getReturnType();

							// retrieve setter for data[0]
							// invoke with the data[1] based on type
							Method setter = currentClass.getDeclaredMethod("set"+Character.toUpperCase(data[0].charAt(0))+data[0].substring(1), returnType);
							setter.setAccessible(true);
							setter.invoke(message, convertStringToArgument(returnType, data[1]));                
							continue main;
						}
						catch(Exception e)
						{
							currentClass = currentClass.getSuperclass();
						}
					}
				}
			}
		}
		tfr.close();
		System.out.println();
		System.out.println("Total Tests: "+testCount);
		System.out.println("Passed: "+passCount);
	}

	public static Object convertStringToArgument(Class<?> c, String data)
	{
		if ((c==Boolean.class) || (c==Boolean.TYPE))
		{
			return Boolean.valueOf(data);
		}
		else if ((c==Integer.class) || (c==Integer.TYPE))
		{
			return Integer.valueOf(data);            
		}
		else if ((c==Long.class) || (c==Long.TYPE))
		{
			return Long.valueOf(data);            
		}
		else if ((c==Integer.class) || (c==Integer.TYPE))
		{
			return Integer.valueOf(data);            
		}
		else if ((c==Short.class) || (c==Short.TYPE))
		{
			return Short.valueOf(data);            
		}
		else if (c==String.class)
		{
			return data;            
		}

		return null;
	}
}