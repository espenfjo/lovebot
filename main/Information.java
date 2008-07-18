/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

import java.io.UnsupportedEncodingException;

public class Information {
	public static String sender (String inputLine){

		String sender;
		sender = inputLine.substring(1).split("!")[0].trim();
		if (sender.indexOf("@") >= 0 || sender.indexOf("+")>= 0)
			sender = sender.substring(1);
		return sender;
	}
	public static String senderHost (String inputLine){return inputLine.split("@")[1].split(" ")[0].trim();}
	public static String senderUser (String inputLine){return inputLine.split("!")[1].split("@")[0].trim();}

	public static String msg(String inputLine,Channel channel)
	{
		String[] rawPieces;
		String msg = null;
		try {

			rawPieces = inputLine.split(" ");

			msg = "";

			if (rawPieces.length >= 4)
			{
				if(channel.isFish()){
					msg = channel.fisk.Decrypt(rawPieces[4]);

					//msg = channel.fisk2.decryptString(rawPieces[4]).toString();

				}
				else{
					for (int i = 3; i < rawPieces.length; i++) {
						msg = msg + " " + rawPieces[i];

					}
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(msg.trim().startsWith(":"))
			return msg.trim().substring(1);
		else
			return msg.trim();

	}
	public static String fromChan (String inputLine){

		String fromChan = "";

		try {
			if(inputLine.split(" ")[2].startsWith("#") && inputLine.split(" ")[1].indexOf("TOPIC")<0)
				fromChan = inputLine.split(" ")[2];

			else if(inputLine.split(" ")[2].startsWith(":"))
				fromChan = inputLine.split(" ")[2].substring(1);
			else if (!inputLine.split(" ")[2].startsWith("#") && inputLine.indexOf("PRIVMSG") >= 0)
				fromChan = Information.sender(inputLine);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} 

		return fromChan.toLowerCase();
	}
}
