/*                                                                                 */
/*  Copyright 2009 Espen Fjellvær Olsen <espen@mrfjo.org>.  All rights reserved.  */
/*  See license distributed with this file and                                     */
/*  available online at http://http://www.gnu.org/licenses/gpl-3.0.txt             */
/*                                                                                 */
/*                                                                                 */
/*                                                                                 */

package modules;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.*;
import main.*;
public class Annotation extends Thread implements ModIface {

	public static String httpRegex = ".*(?:http://(?:(?:(?:(?:(?:[a-zA-Z\\d](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?)\\.)*(?:[a-zA-Z](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?))|(?:(?:\\d+)(?:\\.(?:\\d+)){3}))(?::(?:\\d+))?)(?:/(?:(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*)(?:/(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*))*)(?:\\?(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;:@&=])*))?)?)|(?:ftp://(?:(?:(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;?&=])*)(?::(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[;?&=])*))?@)?(?:(?:(?:(?:(?:[a-zA-Z\\d](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?)\\.)*(?:[a-zA-Z](?:(?:[a-zA-Z\\d]|-)*[a-zA-Z\\d])?))|(?:(?:\\d+)(?:\\.(?:\\d+)){3}))(?::(?:\\d+))?))(?:/(?:(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[?:@&=])*)(?:/(?:(?:(?:[a-zA-Z\\d$\\-_.+!*'(),]|(?:%[a-fA-F\\d]{2}))|[?:@&=])*))*))).*";
	Information info = new Information();
	String trigger = "";
	Actions act = new Actions();
	String name = "Annotation";
	static String msg;
	Thread tGet;

	static Channel channel;

	static Server server;

	public String getUrlFromMsg(String inputLine) {
		String[] input = inputLine.split(" ");
		for (String text : input)
			if (text.matches(httpRegex)) {
				System.out.println(text);
				return text;
			}

		return "";
	}

	public void startPlug(String inputLine, Server server, Channel channel) {
		// TODO Auto-generated method stub
		if (getUrlFromMsg(server.msg).matches(httpRegex)
				&& server.sender != Lovebot.myNick
				&& server.sender.toLowerCase().indexOf("anna") < 0 && server.msg.indexOf("TOPIC") <0)
			getHeader(server, channel);

	}

	public void getHeader(Server server, Channel channel) {
		Annotation.msg = server.msg;
		Annotation.server = server;
		this.channel = channel;
		tGet = new Thread(new Annotation());
		tGet.start();
	}

	public void run() {
		String url = getUrlFromMsg(msg);
		try {
			URL uri = new URL(url);
			if (url.indexOf("tv2.no/nettavisen") >= 0)
				nettavisen(uri, url);
			if (url.indexOf("side2.no") >= 0)
				nettavisen(uri, url);
			else {

				HttpURLConnection web = (HttpURLConnection) uri
				.openConnection();

				web.setRequestMethod("GET");
				web.connect();
				int code = web.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {

					String titleSplitWhole = null;

					BufferedReader in = new BufferedReader(
							new InputStreamReader(web.getInputStream()));
					String httpString;
					String title = null;
					Pattern titleSplitPattern = Pattern
					.compile("<(title|TITLE)([^a-zA-Z0-9>]+[^>]+)?>");
					Pattern titleContentPattern = Pattern
					.compile(".*</(title|TITLE)([^a-zA-Z0-9>]+[^>]+)?>.*");
					int i = 0;
					while ((httpString = in.readLine()) != null) {
						if (httpString.length() > 0)
							i++;
						if (i == 100)
							break;

						if (Pattern.matches(
								".*<(title|TITLE)([^a-zA-Z0-9>]+[^>]+)?>.*",
								httpString)) {
							Matcher titleSplitMatcher = titleSplitPattern
							.matcher(httpString);
							while (titleSplitMatcher.find())
								titleSplitWhole = httpString
								.substring(titleSplitMatcher.end());
							// Gah, those damn line splitters :P
							if (titleSplitWhole.length() == 0)
								if ((httpString = in.readLine()).length() > 0)
									title = httpString;

						}
						if (Pattern.matches(
								".*</(title|TITLE)([^a-zA-Z0-9>]+[^>]+)?>.*",
								httpString)) {
							Matcher titleContentMatcher = titleContentPattern
							.matcher(titleSplitWhole);
							while (titleContentMatcher.find())
								title = titleSplitWhole.split("<")[0];
						}

						if (title != null) {
							if (url.length() >= 70) {
								URL tu = new URL(
										"http://tinyurl.com/api-create.php?url="
										+ url);
								HttpURLConnection tuweb = (HttpURLConnection) tu
								.openConnection();
								tuweb.setRequestMethod("GET");
								tuweb.connect();
								int tucode = web.getResponseCode();

								if (tucode == HttpURLConnection.HTTP_OK) {
									BufferedReader tuin = new BufferedReader(
											new InputStreamReader(tuweb
													.getInputStream()));
									String turl = tuin.readLine();
									System.out.println(turl);
									Actions.say("[" + title.replace('\n', ' ')
											+ "] - " + turl, channel, server);
								}
							} else
								Actions.say("[" + title.replace('\n', ' ')
										+ "]", channel, server);
							in.close();
							break;

						}
					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		// return null;
	}

	public void nettavisen(URL uri, String url) {
		try {
			HttpURLConnection web = (HttpURLConnection) uri.openConnection();
			web.setRequestMethod("GET");
			web.connect();

			int code = web.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {

				BufferedReader in = new BufferedReader(new InputStreamReader(
						web.getInputStream()));
				String httpString;
				String title = null;
				int i = 0;
				while ((httpString = in.readLine()) != null) {
					if (httpString.length() > 0)
						i++;

					if (httpString.indexOf("<h1 class=\"title\">") >= 0) {
						httpString = in.readLine();
						title = httpString.trim();

					}

					if (title != null) {
						if (url.length() >= 70) {
							URL tu = new URL(
									"http://tinyurl.com/api-create.php?url="
									+ url);
							HttpURLConnection tuweb = (HttpURLConnection) tu
							.openConnection();
							tuweb.setRequestMethod("GET");
							tuweb.connect();
							int tucode = web.getResponseCode();

							if (tucode == HttpURLConnection.HTTP_OK) {
								BufferedReader tuin = new BufferedReader(
										new InputStreamReader(tuweb
												.getInputStream()));
								String turl = tuin.readLine();
								System.out.println(turl);
								Actions.say("[" + title.replace('\n', ' ')
										+ "] - " + turl, channel, server);
							}
						}

						else

							Actions.say("[" + title.replace('\n', ' ') + "]",
									channel, server);

						in.close();
						break;

					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTrigger() {
		// TODO Auto-generated method stub
		return trigger;
	}

	public void setTrigger(String string) {
		// TODO Auto-generated method stub
		this.trigger = string;
	}

	public String getPlugName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setPlugName(String string) {
		// TODO Auto-generated method stub
		this.name = string;
		
		
	}
	public void finalize() throws Throwable{
		super.finalize();
		tGet.destroy();
		
	}
	
	
}
