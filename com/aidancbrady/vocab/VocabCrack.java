package com.aidancbrady.vocab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.aidancbrady.vocab.frames.VocabFrame;

public class VocabCrack 
{
	private static VocabCrack instance = new VocabCrack();
	
	public ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
	
	public static String serverIP = "localhost";
	public static int serverPort = 26830;
	
	public static final String VERSION = "1.0.0";
	
	public VocabFrame frame;
	
	public List<String> loadedList = new ArrayList<String>();
	
	public Set<String> learnedWords = new HashSet<String>();
	
	public Map<String, String> listURLs = new HashMap<String, String>();
	
	public Account account = Account.DEFAULT;
	
	public static VocabCrack instance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		instance().init();
	}
	
	public void init()
	{
		initMacOSX();
		
        Runnable doSwingLater = new Runnable() {
            @Override
            public void run() 
            {
                frame = new VocabFrame();
            }
        };
         
        SwingUtilities.invokeLater(doSwingLater);
	}
	
	public void logout()
	{
		VocabCrack.instance().account = Account.DEFAULT;
		frame.openLogin();
	}
	
	private void initMacOSX()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "VocabCrack");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
	}
}
