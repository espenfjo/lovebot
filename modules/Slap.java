/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;
import java.util.Random;

import main.Actions;
import main.Channel;
import main.ModIface;
import main.Server;

public class Slap implements ModIface{
	public String trigger = "slap ";
	String name = "Slap";
	public void startPlug(String inputLine,Server server, Channel channel) {
		 Random randomGen = new Random(); String[] slap = {"with Dr. Phil", "with funky fever", "with the Holy Grail", "with an invalid argument", ":D"}; 
		Actions.say("slaps " + server.msg.split(" ")[2] + " " + slap[randomGen.nextInt(slap.length-1)], channel, server);
}
			

	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getPlugName() {
		// TODO Auto-generated method stub
		return name;
	}
	public void setPlugName(String string) {
		// TODO Auto-generated method stub
		this.name = string;
	}
	public void finalize() throws Throwable{
		super.finalize();
	}
	
}
