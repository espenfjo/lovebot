/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;

import java.util.Date;
import main.*;
public class Nach implements ModIface{
	
	String name = "Nach";
	String trigger = "";
	@SuppressWarnings("deprecation")
	public static void runPlug(String inputLine, Server server, Channel channel)
	{
		Date time = new Date();
		int hour = time.getHours();
		int day = time.getDay();
		
		if(inputLine.toLowerCase().indexOf(":nach?") >= 0){
		if((hour < 3 || hour > 22) && (day == 6 || day==7))
			Actions.say("Aaaaaaalt for tidlig å komme på nach nå da!", channel, server);
		else if((hour < 1 || hour > 22) && (day == 5 || day==3))
			Actions.say("Aaaaaaalt for tidlig å komme på nach nå da!", channel, server);
		else if(hour >  22 || hour < 11)
			Actions.say("Im all in! Gogogogo!", channel, server);
		else
			Actions.say("Næææh... Ikke nå...", channel, server);	
	}
	}

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
		this.name= string;
	}

	public void setTrigger(String string) {
		// TODO Auto-generated method stub
		this.trigger = string;
	}

	public void startPlug(String inputLine, Server server, Channel channel) {
		// TODO Auto-generated method stub
		
	}
	public void finalize() throws Throwable{
		super.finalize();
	
		
	}

}
