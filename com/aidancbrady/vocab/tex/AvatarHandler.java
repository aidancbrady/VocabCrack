package com.aidancbrady.vocab.tex;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import jgravatar.Gravatar;

import com.aidancbrady.vocab.Account;

public class AvatarHandler 
{
	public static File avatarDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "VocabServer" + File.separator + "Data");
	
	private static final Gravatar gravatar = new Gravatar();
	
	public static BufferedImage downloadGravatar(Account acct) throws Exception
	{
		gravatar.setSize(256);
		
		byte[] download = gravatar.download(acct.email);
		
		if(download != null)
		{
			return ImageIO.read(new ByteArrayInputStream(download));
		}
		
		return null;
	}
	
	private static File getAvatarFile(Account acct)
	{
		return new File(avatarDir, acct.username + ".jpg");
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
