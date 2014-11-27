package com.aidancbrady.vocab;

import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public final class Utilities 
{
	public static String latestVersion;
	public static String versionNews;
	public static String news;
	public static String downloadUrl;
	public static String updateName;
	
	public static boolean dataLoaded;
	
	public static int getLabelWidth(JLabel l)
	{ 
		FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
		
		return (int)(l.getFont().getStringBounds(l.getText(), context).getWidth());
	}
	
	public static ImageIcon scaleImage(ImageIcon img, int width, int height)
	{
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_FAST));
	}
	
	public static void loadData()
	{
		new DownloadData().start();
		WordListHandler.init();
		
		System.out.println(System.getProperty("user.dir"));
	}
	
	public static void updateCheck()
	{
		if(dataLoaded)
		{
			if(!latestVersion.equals(VocabCrack.VERSION))
			{
				JOptionPane.showMessageDialog(VocabCrack.instance().frame, "A new version is available. Check the options menu after logging in for details.");
			}
		}
	}
	
	public static List<String> getHTML(String urlToRead)
	{
		List<String> result = new ArrayList<String>();

		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(urlToRead).openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";

			while((line = rd.readLine()) != null)
			{
				result.add(line.trim());
			}

			rd.close();
		} catch(Exception e) {
			result.clear();
			result.add("null");
			System.out.println("An error occured while connecting to URL '" + urlToRead + ".'");
		}

		return result;
	}

	public static class DownloadData extends Thread
	{
		public DownloadData()
		{
			setDaemon(true);
		}
		
		@Override
		public void run()
		{
			List<String> data = getHTML("https://dl.dropboxusercontent.com/u/90411166/Versions/VocabCrack.txt");
			
			if(!data.get(0).equals("null"))
			{
				VocabCrack.serverIP = data.get(0).trim();
				VocabCrack.serverPort = Integer.parseInt(data.get(1));
				
				latestVersion = data.get(2).trim();
				versionNews = data.get(3).trim();
				news = data.get(4).trim();
				downloadUrl = data.get(5).trim();
				updateName = data.get(6).trim();
				
				dataLoaded = true;
				
				updateCheck();
			}
			else {
				JOptionPane.showMessageDialog(VocabCrack.instance().frame, "Unable to retrieve data from server, things may not work properly.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
