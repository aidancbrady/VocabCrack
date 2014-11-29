package com.aidancbrady.vocab.file;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;

public class WordListHandler 
{
	public static File dataFile = new File(WordDataHandler.dataDir, "ListData.txt");
	public static File defaultList = new File(WordDataHandler.dataDir, "DefaultList.txt");
	
	public static Character splitter = new Character('>');
	
	public static void init()
	{
		try {
			WordDataHandler.dataDir.mkdirs();
			
			if(!defaultList.exists())
			{
				InputStream stream = WordListHandler.class.getClassLoader().getResourceAsStream("resources" + File.separator + "external" + File.separator + "DefaultList.txt");
				FileOutputStream output = new FileOutputStream(defaultList);
				
				byte[] buffer = new byte[2048];
				
				int read = stream.read(buffer);
				
				while(read != -1)
				{
					output.write(buffer, 0, read);
					read = stream.read(buffer);
				}
				
				output.close();
			}
		} catch(Exception e) {}
		
		loadListData();
	}
	
	public static boolean loadList(String url, Container panel)
	{
		VocabCrack.instance().loadedList.clear();
		
		if(url.equals("DefaultURL"))
		{
			return loadDefaultList(panel);
		}
		else {
			return loadCustomList(url, panel);
		}
	}
	
	public static boolean loadCustomList(String u, Container panel)
	{
		System.out.println("Loading '" + u + "' word list...");
		
		String url = convertURL(u, true);
		List<String> text = Utilities.getHTML(url);
		
		if(text.size() == 1 && text.get(0).equals("null"))
		{
			JOptionPane.showMessageDialog(panel, "Unable to reach word list URL.");
			return false;
		}
		
		boolean failed = false;
		
		for(String s : text)
		{
			String[] split = s.trim().split(splitter.toString());
			
			if(split.length != 2)
			{
				JOptionPane.showMessageDialog(panel, "Invalid word list format.");
				failed = true;
				
				break;
			}
			
			VocabCrack.instance().loadedList.add(s.trim());
		}
		
		if(!failed && VocabCrack.instance().loadedList.size() >= 10)
		{
			return true;
		}
		else {
			JOptionPane.showMessageDialog(panel, "Word list must contain at least ten terms.");
		}
		
		return false;
	}
	
	public static String getURL(String name)
	{
		if(VocabCrack.instance().listURLs.get(name.trim()) == null)
		{
			return null;
		}
		
		return VocabCrack.instance().listURLs.get(name.trim()).replace("|", ":");
	}
	
	public static String convertURL(String url, boolean fromCompiled)
	{
		return fromCompiled ? url.replace("|", ":") : url.replace(":", "|");
	}
	
	public static boolean loadDefaultList(Container panel)
	{
		System.out.println("Loading default word list...");
		
		try {
			if(defaultList.exists())
			{
				BufferedReader reader = new BufferedReader(new FileReader(defaultList));
				boolean failed = false;
				
				VocabCrack.instance().loadedList.clear();
				
				String readingLine;
				
				while((readingLine = reader.readLine()) != null)
				{
					String[] split = readingLine.trim().split(splitter.toString());
					
					if(split.length != 2)
					{
						JOptionPane.showMessageDialog(panel, "Invalid word list format.");
						failed = true;
						
						break;
					}
					
					VocabCrack.instance().loadedList.add(readingLine.trim());
				}
				
				reader.close();
				
				if(!failed && VocabCrack.instance().loadedList.size() >= 10)
				{
					return true;
				}
				else {
					JOptionPane.showMessageDialog(panel, "Word list must contain at least ten terms.");
				}
			}
			else {
				JOptionPane.showMessageDialog(panel, "Unable to load default word list.");
			}
		} catch(Exception e) {
			System.err.println("An error occured while loading from data file:");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void loadListData()
	{
		System.out.println("Loading word list data...");
		
		try {
			if(dataFile.exists())
			{
				BufferedReader reader = new BufferedReader(new FileReader(dataFile));
				
				VocabCrack.instance().listURLs.clear();
				
				String readingLine;
				
				while((readingLine = reader.readLine()) != null)
				{
					String[] split = readingLine.trim().split(":");
					
					if(!split[0].equals("Default") && !split[1].equals("DefaultURL"))
					{
						VocabCrack.instance().listURLs.put(split[0], split[1]);
					}
				}
				
				reader.close();
			}
		} catch(Exception e) {
			System.err.println("An error occured while loading from data file:");
			e.printStackTrace();
		}
		
		VocabCrack.instance().listURLs.put("Default", "DefaultURL");
	}
	
	public static void saveListData()
	{
		System.out.println("Saving word data...");
		
		try {
			if(dataFile.exists())
			{
				dataFile.delete();
			}
			
			dataFile.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			
			for(Map.Entry<String, String> entry : VocabCrack.instance().listURLs.entrySet())
			{
				if(!entry.getKey().equals("Default") && !entry.getValue().equals("DefaultURL"))
				{
					writer.write(entry.getKey() + ":" + entry.getValue());
					writer.newLine();
				}
			}
			
			writer.flush();
			writer.close();
		} catch(Exception e) {
			System.err.println("An error occured while saving to data file:");
			e.printStackTrace();
		}
	}
	
	public static Vector<String> listNames()
	{
		return new Vector<String>(VocabCrack.instance().listURLs.keySet());
	}
	
	public static void addList(String name, String url)
	{
		VocabCrack.instance().listURLs.put(name, convertURL(url, false));
		saveListData();
	}
	
	public static void deleteList(String name)
	{
		VocabCrack.instance().listURLs.remove(name);
		saveListData();
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
