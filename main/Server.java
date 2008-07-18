/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.Security;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.SSLSocket;

public class Server implements Runnable {

	ArrayList<ModIface> plugins = new ArrayList<ModIface>();
	public PrintWriter writer;
	public String fromChannel;
	public BufferedReader reader;
	String server;
	public String myNick = Lovebot.myNick;
	public String msg;
	public String sender;
	public String senderHost;
	public Channel currentChannel;
	Boolean useSsl = false;
	public String inputLine;
	Calendar cl = Calendar.getInstance();
	int today = cl.get(Calendar.DAY_OF_WEEK);
	Date tid = new Date();
	public Channel[] chans = new Channel[Lovebot.chanlist.length-1];

	public Server(String server, String nick) {
		this.server = server;
		this.myNick = nick;
	}

	public void start() throws InterruptedException {
		(new Thread(new Server(server, myNick))).start();
	}

	public void run() {
		String user = Lovebot.user;


		int serverNum = Lovebot.serverNum;
		if(Lovebot.sslList[serverNum] == 1)
			useSsl = true;

		//Annotation ann = new Annotation();
		//TeamTimer tt = new TeamTimer();
		int port = 6667;
		//UserControl users = null;
		System.out.println("Connecting to server: " + serverNum + " - " + server);
		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			if(useSsl){
				TrustingSSLSocketFactory factory = new TrustingSSLSocketFactory();
				SSLSocket sock = (SSLSocket)factory.createSocket(server, port);
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream(), Charset.defaultCharset()));

			}
			else{
				Socket sock = new Socket(server, port);
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream(), Charset.defaultCharset()));

			}

			//	writer = new PrintWriter(sock.getOutputStream());
			//reader = new BufferedReader(new InputStreamReader(sock.getInputStream(), Charset.defaultCharset()));

			//writer.println("PASS xxx@wewrTH324gbfs");
			//writer.flush();
			writer.println("USER " + user + " 8 * :A simple IRC-bot written in Java");
			writer.flush();
			writer.println("nick " + myNick);
			writer.flush();
			Channel[] chans = new Channel[Lovebot.chanlist.length-1];
			int d =0;
			for (String module : Lovebot.moduleList) {
				
				if(module!=null){
					ClassLoader cl = new ModuleLoader();

					String classfile = "modules."+module ;
					Class _class = cl.loadClass(classfile);
					Object obj = _class.newInstance();
					ModIface _if = (ModIface)obj;
					plugins.add(_if);
					System.out.println("Loading module :" + module);
				}
				System.out.println(d);
				d++;
			}


			while ((inputLine = reader.readLine()) != null) {

				System.out.println(server + " "+ inputLine);
				Thread.sleep(1);

				if (inputLine.indexOf("376 " + myNick) >= 0 || inputLine.indexOf("422 " + myNick) >= 0) { // End of MOTD
					System.out.println("End of motd");
					int i = 0;
					for (String chan : Lovebot.chanlist[serverNum]) {
						if (chan != null) {
							chans[i] = new Channel(this, chan,Lovebot.chanKeyList[serverNum][i]);
							//System.out.println(Lovebot.fishList[serverNum][i]);

							if(Lovebot.fishList[serverNum][i] != null){
								System.out.println("Activating fish");
								chans[i].setKey(Lovebot.fishList[serverNum][i]);
								chans[i].setFish(true);
								chans[i].fisk = new Blowfish(chans[i].getKey());
							}

							chans[i].joinChan();

							while ((inputLine = reader.readLine()) != null) {
								System.out.println(inputLine);
								//if (inputLine.indexOf("353") >= 0) {
								//chans[i].users.storeNames();
								//}
								//else if ....
								if (inputLine.indexOf("366") >= 0 || inputLine.indexOf("474") >= 0 || inputLine.indexOf("475") >= 0) {
									break;
								}
							}
							i++;
						}

					}
				}

				if (inputLine.indexOf("PING :") >= 0) {
					System.out.println(inputLine.split(":")[1]);
					writer.println("PONG :" + inputLine.split(":")[1]);
					writer.flush();
					System.out.println("PONG :" + inputLine.split(":")[1]);
				}
				else if (inputLine.indexOf("366") >= 0|| inputLine.indexOf("474") >= 0 || inputLine.indexOf("475") >= 0) {
					break;
				}

			}

			Date tid = new Date();
			while ((inputLine = reader.readLine()) != null) {
				int chanIterator = 0;
				int hour = tid.getHours();
				System.out.println(server + " " + inputLine);

				if (inputLine.startsWith("PING")) {

					writer.println("PONG " + inputLine.split("PING")[1]);
					writer.flush();
					System.out.println("PONG " + inputLine.split("PING")[1]);
					continue;
				}

				else if (inputLine.indexOf("#")> 2){
					fromChannel = Information.fromChan(inputLine);


					while(chans.length > chanIterator){
						if(chans[chanIterator] != null)
							if(chans[chanIterator].getChan().matches(fromChannel)){
								currentChannel = chans[chanIterator];
								//users = chans[chanIterator].getUsers();
								break;
							}
						chanIterator++;
					}
					if(currentChannel.isFish){
						inputLine = inputLine.split(":")[0] + ":" +inputLine.split(":")[1]+ ":"+ msg;
					}
				}
				msg = Information.msg(inputLine,currentChannel);
				System.out.println(msg);
				sender = Information.sender(inputLine);


				if(inputLine.indexOf("!load") >= 0){

					try{
						String module = msg.split(" ")[1];

						ClassLoader cl = new ModuleLoader();

						String classfile = "modules."+module ;
						Class _class = cl.loadClass(classfile);
						Object obj = _class.newInstance();
						ModIface _if = (ModIface)obj;


						System.out.println("Loading module :" + module);	
						//Class c  = Class.forName("modules." + module);
						//ModIface _if = (ModIface) c.newInstance();
						plugins.add(_if);

					}catch (Exception e) {
						e.printStackTrace();

					}

				}

				if(inputLine.indexOf("!unload") >= 0){


					String module = msg.split(" ")[1];
					for(int i = 0; i < plugins.size();i++){
						if(module.indexOf(plugins.get(i).getPlugName()) >=0){
							plugins.set(i, null);
							plugins.remove(i);


						}
					}
				}
				if(plugins != null)
					for(int i = 0; i < plugins.size();i++){
						System.out.println(plugins.get(i).getPlugName());
						if(inputLine.indexOf(plugins.get(i).getTrigger()) >=0){
							plugins.get(i).startPlug(inputLine,this,currentChannel);
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
