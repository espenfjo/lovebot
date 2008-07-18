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
import main.*;
public class FoodTip implements ModIface {
	String name = "FoodTip";
	String trigger = "!middagstips";
	HttpURLConnection web;
	public void startPlug(String string,Server server, Channel fromChannel) {
		try {
			URL uri = new URL("http://middagstips.mega.coop.no/middagstips/");

			 web = (HttpURLConnection) uri.openConnection();

			web.setRequestMethod("GET");
			web.connect();
			int code = web.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(web.getInputStream()));
				String httpString;
				String food = null;
				while ((httpString = in.readLine()) != null)
					if (httpString.indexOf("mtRecipeTitle") >= 0){
						food = httpString.split("<div class=\"mtRecipeTitle\">")[1].split("</div>")[0];
						Actions.say("Dagens middagstips: "+ food.replace('\n', ' ')+ " - Oppskrift: http://middagstips.mega.coop.no/middagstips/",fromChannel, server);
					}
			}
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
		web.disconnect();
		
	}

}
