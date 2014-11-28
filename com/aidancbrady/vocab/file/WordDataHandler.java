package com.aidancbrady.vocab.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aidancbrady.vocab.VocabCrack;

public class WordDataHandler 
{
	public static File dataDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "VocabCrack" + File.separator + "Data");
	public static File dataFile = new File(dataDir, "Data.txt");
	
	private static Random rand = new Random();
	
	public static void load()
	{
		System.out.println("Loading word data...");
		
		try {
			dataDir.mkdirs();
			
			if(!dataFile.exists())
			{
				return;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			
			VocabCrack.instance().learnedWords.clear();
			
			String readingLine;
			
			while((readingLine = reader.readLine()) != null)
			{
				String[] wordSplit = readingLine.trim().split(",");
				
				for(String word : wordSplit)
				{
					VocabCrack.instance().learnedWords.add(word.trim());
				}
			}
			
			reader.close();
		} catch(Exception e) {
			System.err.println("An error occured while loading from data file:");
			e.printStackTrace();
		}
	}
	
	public static void save()
	{
		System.out.println("Saving word data...");
		
		try {
			if(dataFile.exists())
			{
				dataFile.delete();
			}
			
			dataFile.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			
			StringBuilder str = new StringBuilder();
			
			for(String word : VocabCrack.instance().learnedWords)
			{
				str.append(word);
				str.append(",");
			}
			
			writer.flush();
			writer.close();
		} catch(Exception e) {
			System.err.println("An error occured while saving to data file:");
			e.printStackTrace();
		}
	}
	
	public static List<String> createWordSet()
	{
		List<String> list = new ArrayList<String>();
		
		while(list.size() < 10)
		{
			String word = VocabCrack.instance().loadedList.get(rand.nextInt(VocabCrack.instance().loadedList.size()));
			
			if(!list.contains(word) && !VocabCrack.instance().learnedWords.contains(word.split("|")[0]))
			{
				list.add(word);
			}
		}
		
		return list;
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
