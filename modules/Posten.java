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
public class Posten implements ModIface
{
	String name = "Posten";
	String trigger = "posten?";
	Information info = new Information();
	
	@SuppressWarnings("deprecation")
	public void startPlug(String inputLine,Server server, Channel channel)
	{
		Date tid = new Date();
		int day = tid.getDay();
		System.out.println(tid.getTime());
		String[] days = {"Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lrdag", "Sndag"};
		// set the days
		
		
		if(day == 4)
			Actions.say("I dag, Torsdag, har posten p Nardocenteret pent fra 0900 til 1800", channel, server);
		else if(day == 6)
			Actions.say("I dag, Lrdag, har posten p Nardocenteret pent fra 0900 til 1500", channel, server);
		else if(day == 7)
			Actions.say("I dag er posten stengt!", channel, server);
		else
			Actions.say("I dag, " + days[day] + ", har posten p Nardocenteret pent fra 0900 til 1700", channel, server);
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
	public void finalize() throws Throwable{
		super.finalize();
	}


}