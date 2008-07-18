/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;

import java.util.Date;

import main.Actions;
import main.Channel;
import main.ModIface;
import main.Server;

public class Time implements ModIface{
	String name = "Time";
	String trigger = "!time";
	
	public String getPlugName() {
		// TODO Auto-generated method stub
		return name;
	}


	public String getTrigger() {
		// TODO Auto-generated method stub
		return trigger;
	}

	
	public void setPlugName(String string) {
		// TODO Auto-generated method stub
		this.name = string;
	}

	
	public void setTrigger(String string) {
		// TODO Auto-generated method stub
		this.trigger = string;
	}

	
	public void startPlug(String inputLine, Server server, Channel channel) {
		// TODO Auto-generated method stub
		Date date = new Date();
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		String[] months = {"January", "February", "Mars", "April", "May", "June","July","August","September","October","November","December"};
		Actions.say("lala " + days[date.getDay()] + " " + date.getDate() + " " + months[date.getMonth()] + " "+date.getYear() + " " + date.getTime(), channel,server);
		
	}
	public void finalize() throws Throwable{
		super.finalize();
	
		
	}

}
