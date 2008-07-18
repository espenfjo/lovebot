/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

/**
 * 
 */
package main;

import java.util.Date;

/**
 * @author Espen Fjellvr Olsen
 *
 */

public class Lovebot {

	public static String serverlist[] = new String[4];
	public static String chanlist[][] = new String[4][30];
	public static Server servers[] = new Server[4];
	public static String fishList[][] = new String[4][30];
	public static int sslList[] = new int[4];
	public static String chanKeyList[][] = new String[4][30];
	public static String myNick;
	public static int serverNum = 0;
	
	public static String user;
	public static String owner;
	static long startTime;
	public static String moduleList[] = new String[255];

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date uptime = new Date();
		startTime = uptime.getTime();

		XMLConfReader conf = new XMLConfReader();
		conf.read();
		System.out.println(myNick);
		
		try {
			int i = 0;
			for (String srv : serverlist) {
				for (String chan : chanlist[i]) {
					if (chan != null && srv != null)
						servers[i] = new Server(srv, myNick);
					
				}

				if (chanlist[i].length > 0 && srv != null) {
					serverNum = i;
					servers[i].start();
					Thread.sleep(5);
				}
				i++;
				System.out.println(srv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
