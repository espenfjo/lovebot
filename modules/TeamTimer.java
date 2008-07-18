/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;

import java.net.*;
import java.io.*;
import main.*;
public class TeamTimer implements Runnable, ModIface{
	Information info = new Information();
	static Server server;
	static Actions act = new Actions();
	static String msg;
	static String sender;
	static Channel channel;
	String name = "TeamTimer";
	String trigger = "";
	Thread TT;
	public void startPlug(String inputLine, Server server,Channel channel) {

		String[] input;
		TeamTimer.server = server;
		TeamTimer.channel = channel;
		TeamTimer.msg = server.msg;
		//this.sender = server.sender;
		System.out.println("msg: " + sender);
		if((server.msg.toLowerCase().indexOf("buss") >= 0 && msg.toLowerCase().indexOf(server.myNick) >= 0) || inputLine.indexOf("PRIVMSG " + server.myNick) >= 0)
		{
			if (msg.startsWith(Lovebot.myNick + ":" )){ 
				input = msg.split(":");
				System.out.println(input[1].replace("buss ", ""));
				msg = input[1].replace("buss ", "");

				TT= new Thread(new TeamTimer());
				TT.start();
			}
			else{ //From PMSG
				TT = new Thread(new TeamTimer());
				TT.start();
			}

		}

	}


	@SuppressWarnings("deprecation")

	public void run() {

		try {
			URL url;
			HttpURLConnection urlConn;
			DataOutputStream printout;
			BufferedReader input;
			url = new URL("http://team-trafikk.no/team_bussorakel.asp");
			String[] timeSplitArray = null;
			String[] titleContent = null;
			urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);

			//set request method
			urlConn.setRequestMethod("POST");

			//data-value pairs are separatesubstring(Main.myNick.length() + 2);d by & 
			String content = "quest=" + URLEncoder.encode(msg) + "&"
			+ "btnSubmit=" + URLEncoder.encode("Spr >>") + "&"
			+ "lang=" + URLEncoder.encode("nor");

			urlConn.setRequestProperty("Content-Length", content.length() + "");

			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());
			printout.writeBytes(content);

			printout.flush();
			printout.close();

			//Get response data.
			input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

			String str;
			while ((str = input.readLine()) != null){
				if(str.indexOf("span class=\"clsOrakelReply\">") >= 0){
					System.out.println("Skrive");
					timeSplitArray = str.split("span class=\"clsOrakelReply\">");
					while ((str = input.readLine()) != null){
						timeSplitArray[1] = timeSplitArray[1] + str;		
						if(str.indexOf("Tidene angir|</span>")>= 0)
							break;

						titleContent = timeSplitArray[1].split("Tidene angir|</span>");
					}
					Actions.say(titleContent[0].replace("<br>", "").replace("  ", " "),channel, server);
				}
			}
			input.close();
		}

		catch (MalformedURLException me) {
			System.err.println("MalformedURLException; " + me);
		} catch (IOException ioe) {
			System.err.println("IOException; " + ioe.getMessage());
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
		this.name = string;
	}

	public void setTrigger(String string) {
		// TODO Auto-generated method stub
		this.trigger = string;
	}
	public void finalize() throws Throwable{
		super.finalize();
		TT.destroy();

	}

}  
