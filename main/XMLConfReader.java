/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package main;

/**
 * @author espen
 * 
 */

import java.io.File;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class XMLConfReader {

	public void read() {
		int servernum = 0;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("lovebot.xml"));
			doc.getDocumentElement().normalize();
			
			NodeList listOfServers = doc.getElementsByTagName("server");

			for (int i = 0; i < listOfServers.getLength(); i++) {
				Node firstServerNode = listOfServers.item(i);
				if (firstServerNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstServerElement = (Element) firstServerNode;
					NodeList firstHostList = firstServerElement.getElementsByTagName("host");
					Element firstHostElement = (Element) firstHostList.item(0);
					NodeList textFNList = firstHostElement.getChildNodes();
					Lovebot.serverlist[i] = ((Node) textFNList.item(0)).getNodeValue().trim();

					//chans
					NodeList firstChanList = firstServerElement.getElementsByTagName("channel");
					

					for (int x = 0; x < firstChanList.getLength(); x++) {
						Element firstChanElement = (Element) firstChanList.item(x);
						NodeList textChanList = firstChanElement.getChildNodes();
						Lovebot.chanlist[servernum][x] = ((Node) textChanList.item(0)).getNodeValue().trim();
						
					}
					NodeList firstFishList = firstServerElement.getElementsByTagName("fishKey");

					for (int x = 0; x < firstFishList.getLength(); x++) {
						Element firstFishElement = (Element) firstFishList.item(x);
						NodeList textFishList = firstFishElement.getChildNodes();
						Lovebot.fishList[servernum][x] = ((Node) textFishList.item(0)).getNodeValue().trim();
						
					}
					NodeList firstSslList = firstServerElement.getElementsByTagName("ssl");

					for (int x = 0; x < firstSslList.getLength(); x++) {
						Element firstSslElement = (Element) firstSslList.item(x);
						NodeList textSslList = firstSslElement.getChildNodes();
						Lovebot.sslList[servernum] = Integer.parseInt(((Node) textSslList.item(0)).getNodeValue().trim());
						
					}
					NodeList firstChanKeyList = firstServerElement.getElementsByTagName("chanKey");

					for (int x = 0; x < firstChanKeyList.getLength(); x++) {
						Element firstkeyElement = (Element) firstChanKeyList.item(x);
						NodeList textkeyList = firstkeyElement.getChildNodes();
						Lovebot.chanKeyList[servernum][x] = ((Node) textkeyList.item(0)).getNodeValue().trim();
						
					}
					servernum++;
				}
			}

			NodeList nick = doc.getElementsByTagName("nick");
			NodeList user = doc.getElementsByTagName("user");

			Element firstNickElement = (Element) nick.item(0);
			NodeList textNickList = firstNickElement.getChildNodes();
			Lovebot.myNick = ((Node) textNickList.item(0)).getNodeValue().trim();

			Element firstUserElement = (Element) user.item(0);
			NodeList textUserList = firstUserElement.getChildNodes();
			Lovebot.user = ((Node) textUserList.item(0)).getNodeValue().trim();
			
		
			NodeList listOfModules = doc.getElementsByTagName("modules");
			
			for (int i = 0; i < listOfModules.getLength(); i++) {
				Node firstModulesNode = listOfModules.item(i);
				if (firstModulesNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstModulesElement = (Element) firstModulesNode;
					
					//chans
					NodeList firstModuleList = firstModulesElement.getElementsByTagName("module");

					for (int x = 0; x < firstModuleList.getLength(); x++) {
						Element firstChanElement = (Element) firstModuleList.item(x);
						NodeList textChanList = firstChanElement.getChildNodes();
						Lovebot.moduleList[x] = ((Node) textChanList.item(0)).getNodeValue().trim();
						
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
