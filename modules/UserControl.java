/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import main.*;
public class UserControl {
	final int OP = 2;
	final int VOICE = 1;
	final int NEUTRAL = 0;
	final int OP_VOICE = 3;
	Server server;
	String channel;
	HashMap<String, String[]> users = new HashMap<String, String[]>();
	
	public UserControl(Server server, String channel) {
		this.server = server;
		this.channel = channel;
	}

	public void issueNames(String channel) {
		server.writer.println("NAMES " + channel);
		server.writer.flush();
	}

	public void storeNames(String inputLine) throws IOException {
		String nick;
		
		String usable = inputLine.split(":")[2];
		String[] nicks = usable.split(" ");
		int i;
		int subString = 0;

		for (i = 0; i <= nicks.length - 1; i++) {
			
			//old nick, channel, state, host
			String[] userInfo = new String[4];
			String state = usable.split(" ")[i].substring(0, 1);
			
			if (state.matches("@"))
				userInfo[1] = String.valueOf(OP);
			else if( state.matches("\\+"))
				userInfo[1] = String.valueOf(VOICE);
			else
				userInfo[1] = String.valueOf(NEUTRAL);

			if (Integer.parseInt(userInfo[1]) == NEUTRAL)
				nick = usable.split(" ")[i].substring(subString);
			else
				nick = usable.split(" ")[i].substring(subString + 1);

			userInfo[0] = nick;
			userInfo[2] = inputLine.split(" ")[4];
			userInfo[3] = "host";
			System.out.println(nick);
			users.put(nick, userInfo);
		}
	}

	public void writeNames() {
		String[] getValue = new String[4];

		getValue = users.get("EspenHiST");
		for (String value : getValue) {
			System.out.println(value);
		}
	}
	
	public void changeMode(String inputLine){
			
			boolean giveMode = false;
			String[] rawNickPieces = inputLine.split(" ");
			String[] nicks = new String[rawNickPieces.length - 4];		
			
			for(int x = 4; rawNickPieces.length > x; x++){
				nicks[x-4] = rawNickPieces[x].trim();
			}
			
			for(int i = 0;nicks.length > i;i++){
			 String newMode = inputLine.split(" ")[3].substring(i+1,i+2);
			//System.out.println("new " + newMode);
			if(inputLine.split(" ")[3].substring(i,i+1).matches("\\+"))
			{
				//System.out.println("Give");
				giveMode = true;
			}
			else if(inputLine.split(" ")[3].substring(i,i+1).matches("\\-"))
			{
			//	System.out.println("Take");
				giveMode = false; 
			}
			
			//String nick = inputLine.split(" ")[4].trim();
			String[] userInfo = new String[4];
		   
			int setMode = NEUTRAL;
			userInfo = users.get(nicks[i]);
			int curMode = Integer.parseInt(userInfo[1]); 

			if(curMode <= OP_VOICE)
				setMode += curMode;
			else
				resetNames(userInfo[2]);
			
			if(newMode.matches("v") && giveMode && (curMode != VOICE || curMode != OP_VOICE))
				setMode += VOICE;
			else if(newMode.matches("o") && giveMode && (curMode != OP || curMode != OP_VOICE))
				setMode += OP;
			else if(newMode.matches("v") && !giveMode && (curMode == VOICE || curMode == OP_VOICE))
				setMode -= VOICE;
			else if(newMode.matches("o") && !giveMode && (curMode == OP || curMode == OP_VOICE))
				setMode -= OP;

			System.out.println("Setting " + setMode + " to " + nicks[i] + " from " + curMode);
			userInfo[1] = String.valueOf(setMode);	
			users.put(nicks[i], userInfo);
			}
	}
	
	//void resetNames(String channel) {
	void resetNames(String channel) {
		// TODO Auto-generated method stub
	//	issueNames(channel);
		Iterator<String> userIt = users.keySet().iterator();
		while(userIt.hasNext()) {
		    System.out.println(userIt.next());
		}
		
		
	}

	public void inOut(String inputLine){
			
		String action = inputLine.split(" ")[1];
		String nick = inputLine.split("!")[0].substring(1);
		if(action.matches("QUIT") || action.matches("PART"))
			users.remove(nick);
	
		if(action.matches("JOIN")){	
			String[] userInfo = new String[4];
			String state = "0";
			userInfo[0] = nick;
			userInfo[1] = state;
			userInfo[2] = inputLine.split(" ")[2];
			userInfo[3] = inputLine.split(" ")[0].split("!")[1];
			System.out.println(nick);
			users.put(nick, userInfo);
		}
	}
	boolean isOp(String nick){
		int mode = Integer.parseInt(users.get(nick)[1]);
		if (mode >=2)
			return true;
		return false;
	}
	
	public void nickChange(String inputLine){
    		
		String nick = inputLine.split("!")[0].substring(1);
		String newNick = inputLine.split(" ")[2].substring(1);
		String[] userInfo = users.get(nick);
		users.remove(nick);
		users.put(newNick, userInfo);
    }
}

