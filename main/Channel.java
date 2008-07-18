/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

public class Channel {
	Server server;
	String chan;
	Blowfish fisk = null;
	boolean isFish = false;
	String key = "";
	public Channel(Server server, String chan,String key){
		this.chan = chan;
		this.server = server;
		this.key = key;
	}
	
    void joinChan()
	{
		server.writer.println("JOIN " + chan + " " + key);
		server.writer.flush();
	}
	void partChan()
	{
		server.writer.println("PART " + chan);
		server.writer.flush();
	}
	public String getChan() {
		return chan.toLowerCase();
	}

	public Blowfish getFisk() {
		return fisk;
	}

	public void setFisk(Blowfish fisk) {
		this.fisk = fisk;
	}

	public boolean isFish() {
		return isFish;
	}

	public void setFish(boolean isFish) {
		this.isFish = isFish;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	


}
