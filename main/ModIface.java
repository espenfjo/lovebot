/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

public interface ModIface {
	
	
	public void startPlug(String inputLine,Server server, Channel channel);
	public String getTrigger();
	public void setTrigger(String string);
	public String getPlugName();
	public void setPlugName(String string);
	public void finalize() throws Throwable;
}
