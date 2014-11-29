package com.aidancbrady.vocab.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.panels.NewGamePanel;

public class WordListHandler 
{
	public static File wordListDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "VocabCrack" + File.separator + "Word Lists");
	
	public static Character splitter = new Character('>');
	
	public static void init()
	{
		try {
			wordListDir.mkdirs();
			
			File defFile = getListFile("Default");
			
			if(!defFile.exists())
			{
				InputStream stream = WordListHandler.class.getClassLoader().getResourceAsStream("resources" + File.separator + "external" + File.separator + "Default.txt");
				FileOutputStream output = new FileOutputStream(defFile);
				
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
	}
	
	public static boolean loadWordList(String identifier, NewGamePanel panel)
	{
		System.out.println("Loading word list '" + identifier + "'");
		
		try {
			File file = getListFile(identifier);
			
			if(file.exists())
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
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
				JOptionPane.showMessageDialog(panel, "File does not exist.");
			}
		} catch(Exception e) {
			System.err.println("An error occured while loading from data file:");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static Vector<String> listIdentifiers()
	{
		Vector<String> identifiers = new Vector<String>();
		
		for(File file : wordListDir.listFiles())
		{
			if(file.getName().endsWith(".txt"))
			{
				identifiers.add(file.getName().replace(".txt", ""));
			}
		}
		
		return identifiers;
	}
	
	public static void deleteList(String identifier)
	{
		getListFile(identifier).delete();
	}
	
	public static File getListFile(String identifier)
	{
		return new File(wordListDir, identifier + ".txt");
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
