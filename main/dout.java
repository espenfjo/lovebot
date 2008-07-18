/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class dout {
	static BufferedWriter debugOut;
	
	public static void println(String inputLine){
		try {
		debugOut = new BufferedWriter(new PrintWriter(System.out));
		
			debugOut.write(inputLine + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static  void print(String inputLine){
		try {
		debugOut = new BufferedWriter(new PrintWriter(System.out));
		
			debugOut.write(inputLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
