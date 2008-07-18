/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import sun.net.www.protocol.ftp.FtpURLConnection;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import main.Actions;
import main.Channel;
import main.Lovebot;
import main.ModIface;
import main.Server;

public class CurrencyConv implements ModIface, Runnable {
	public String trigger = "";
	String name = "CurrencyConv";
	FtpURLConnection web;
	String msg;
	static Server server;
	static Channel channel;
	static String[] input;

	Thread CC;
	public void startPlug(String inputLine, Server server, Channel channel) {
		this.msg = inputLine;
		CurrencyConv.server = server;
		CurrencyConv.channel = channel;
		this.msg = server.msg;
		if((msg.toLowerCase().indexOf("cur") >= 0 && msg.toLowerCase().indexOf(server.myNick) >= 0) || inputLine.indexOf("PRIVMSG " + server.myNick) >= 0){
			if (msg.startsWith(Lovebot.myNick + ":" )){ 
				input = msg.split(":")[1].split(" ");
				msg = input[1].replace("cur ", "");

				CC= new Thread(new CurrencyConv());
				CC.start();
			}
			else{ //From PMSG
				input = msg.split(" ");
				CC = new Thread(new CurrencyConv());
				CC.start();
			}

		}
	}
	public void run() {	
		try{
			URL uri = new URL("ftp://ftp.toll.no/omkurser.txt");

			web = (FtpURLConnection) uri.openConnection();
			web.connect();


			BufferedReader in = new BufferedReader(new InputStreamReader(web.getInputStream()));

			//	String line[];
			String rawInfo[] = new String[48];
			int i = 0;
			String inputLine;
			double conv_amount =0; //output
			double amount =0; //input
			double from_amount_size = 0; // 1 or 100?
			double to_amount_size =0; // 1 or 100?
			double to_value=0;
			double from_value=0;
			
			String[] raw_split;
			while (( inputLine = in.readLine()) != null){
				rawInfo[i] = inputLine;
				i++;
			}
			//usd;date,date,1,5.5,amerikanske penger
			for (String line : rawInfo) {
				if(line != null){
					raw_split = line.split(";");
					amount= Double.parseDouble(input[2]);
					if(raw_split[0].toLowerCase().matches(input[3].toLowerCase())){
						from_value = Double.parseDouble(raw_split[4]);
						from_amount_size = Double.parseDouble(raw_split[3]);
					}
				}
			}
			for (String line : rawInfo) {
				if(line != null){
					raw_split = line.split(";");
					if(raw_split[0].toLowerCase().matches(input[4].toLowerCase())){
						to_value = Double.parseDouble(raw_split[4]);
						to_amount_size = Double.parseDouble(raw_split[3]);
					}
				}
			}
			//if(to_amount_size == 100 && from_amount_size == 100){
				//conv_amount = amount/from_amount_size/from_value;
		//		conv_amount = conv_amount*to_amount_size*to_value;
		//		
//
	//		}
			if((from_amount_size == 100 && to_amount_size == 1 )|| (from_amount_size == 1 && to_amount_size == 100)){
				
				if(from_value!=100 && to_value==100){
						
					conv_amount = amount*from_value;
				}
					
				else if(from_value==100 && to_value!=100){
				
					conv_amount=amount/to_value;
				}
				else if(from_value != 100 && to_value!=100){
					
					if(from_amount_size == 1){
						conv_amount = amount*from_value*100/to_value;
					}
					else
						conv_amount = amount*from_value/100/to_value;
				}
				

			}
			else{
				System.out.println("USD, EURO eller annet dill :(");
					conv_amount = amount*from_value;
					conv_amount = conv_amount/to_value;
					
			}
			DecimalFormat decForm = new DecimalFormat("#0.000");
			Actions.say(input[2] + " " + input[3].toUpperCase() + " to " + input[4].toUpperCase() + " is " + decForm.format(conv_amount) + " " + input[4].toUpperCase(), channel, server);


			//break;
		} catch (Exception e) {
			e.printStackTrace();
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
	}
}
