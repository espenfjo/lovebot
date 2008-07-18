/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

import java.util.*;
import main.*;
public class Uptime {

	/**
	 * @param args
	 */
	public static void printUptime(Server server, Channel channel){
		Date uptime = new Date();
		long startTime = Lovebot.startTime;
		long runTime = uptime.getTime() - startTime;
		int timeInSeconds = (int) runTime / 1000;
		int hours, minutes, seconds;
		hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		seconds = timeInSeconds;
		Actions.say(hours + " Time(r) " + minutes + " minutt(er) " + seconds + " sekund(er)", channel, server);	
	}
}
