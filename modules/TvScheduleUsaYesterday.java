/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;
import main.*;
import java.net.URL;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


public class TvScheduleUsaYesterday implements ModIface,Runnable{
static	Server server;
	static Channel channel;
	String name = "TvScheduleUsaYesterday";
	String trigger = "!yesterday";
	Thread TV;

	public void startPlug(String inputLine, Server server, Channel channel) {
		this.server = server;
		this.channel = channel;

		TV = new Thread(new TvScheduleUsaYesterday());
		TV.start();
	}

	public void run() {
		String finalShow = "";
		String url = "http://www.tvrage.com/myrss.php?tid=9629&hash=97397894944d12293c8a833af3bb37da&date=yesterday";
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(url);
			URL uri = new URL(url);
			doc.getDocumentElement().normalize();
			NodeList listOfItems = doc.getElementsByTagName("item");

			for (int i = 0; i < listOfItems.getLength(); i++) {
				Node firstItemNode = listOfItems.item(i);
				if (firstItemNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstItemElement = (Element) firstItemNode;
					//shows
					NodeList firstTitleList = firstItemElement.getElementsByTagName("title");

					for (int x = 0; x < firstTitleList.getLength(); x++) {
						Element firstChanElement = (Element) firstTitleList.item(x);
						NodeList textChanList = firstChanElement.getChildNodes();
						 String show = ((Node) textChanList.item(0)).getNodeValue().trim();

						if (show.indexOf("-")>=0)
						{
							finalShow = finalShow + show.replace("-", "") + " |";

						}
					}
				}
			}
			Actions.say("Natt til i dag kom følgende: " + finalShow, channel, server);
		} catch (Exception e) {
			// TODO: handle exception
		System.out.println((e.toString()));
		}
	}

	public String getPlugName() {
		return name;
	}

	public void setPlugName(String name) {
		this.name = name;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public void finalize() throws Throwable{
		super.finalize();
		TV.destroy();

	}

}
