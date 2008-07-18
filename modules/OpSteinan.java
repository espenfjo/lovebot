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

public class OpSteinan implements ModIface{
	public String trigger = "asl?";
	String name = "Asl";
	public void startPlug(String inputLine,Server server, Channel channel){
		Random randomGen = new Random();
		String[] asl = {"14 bærte bærum", "57 gutt brønnøysund", "20 gutt 25-14", "23 usikker steinan", "19 jente 25-14", "22 jente halden", "35 mann Trondheim", "16 jente risvollan", "16 jente nardo", "1 Fride 25-14"};
		Actions.say(asl[randomGen.nextInt(asl.length-1)], channel, server);
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
