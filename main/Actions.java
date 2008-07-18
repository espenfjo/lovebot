/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

public class Actions{

	/**
	 * @param args
	 */
	public static void say(String inputLine, Channel channel, Server server){
		//if(channel.isFish())
			//inputLine = channel.fisk.Encrypt(inputLine);
		System.out.println("Kanal: " + channel.getChan());
		server.writer.println("PRIVMSG " + channel.getChan() + " :" + inputLine);
		server.writer.flush();
	}
}
