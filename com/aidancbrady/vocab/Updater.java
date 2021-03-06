package com.aidancbrady.vocab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.panels.OptionsPanel;

public class Updater 
{
	public static class UpdateThread extends Thread
	{
		public static File appDir = new File(getLocalDirectory());
		
		public OptionsPanel panel;
		
		public UpdateThread(OptionsPanel p)
		{
			panel = p;
			setDaemon(false);
		}
		
		@Override
		public void run()
		{
			if(Utilities.dataLoaded)
			{
				panel.progressLabel.setVisible(true);
				panel.progressLabel.setText("Preparing for download...");
				
				panel.updateButton.setEnabled(false);
				
				if(findAndDelete())
				{
					File download = new File(appDir, Utilities.updateName);
					
					try {
						download.createNewFile();
						
						FileOutputStream output = new FileOutputStream(download);
						InputStream stream = createURL().openStream();
						
						int bytesDownloaded = 0;
						int lastBytesDownloaded = 0;
						byte[] buffer = new byte[2048];
						
						while((lastBytesDownloaded = stream.read(buffer)) > 0)
						{
							output.write(buffer, 0, lastBytesDownloaded);
							buffer = new byte[2048];
							bytesDownloaded += lastBytesDownloaded;
							panel.progressLabel.setText(bytesDownloaded/1000 + " KB downloaded...");
						}
						
						output.close();
						stream.close();
						
						panel.updated = true;
						
						JOptionPane.showMessageDialog(panel, "Successfully updated to the latest version, v" + Utilities.latestVersion + ". Restart for changes to take effect.");
					} catch(Exception e) {
						JOptionPane.showMessageDialog(panel, "An error occurred trying to update: " + e.getLocalizedMessage());
						download.delete();
					}
				}
				else {
					JOptionPane.showMessageDialog(panel, "Unable to locate application file, make sure it is titled correctly.", "Update Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(panel, "Initial data was not retrieved from server, try restarting.");
			}
			
			if(!panel.updated)
			{
				panel.updateButton.setEnabled(true);
			}
			
			panel.progressLabel.setVisible(false);
		}
		
		public boolean findAndDelete()
		{
			for(File file : appDir.listFiles())
			{
				if(file.getName().equals("VocabCrack.jar") || file.getName().equals("VocabCrack.app"))
				{
					file.delete();
					
					return true;
				}
			}
			
			return false;
		}
		
		private static URL createURL()
		{
			try {
				return new URL(Utilities.downloadUrl);
			} catch(Exception e) {}

			return null;
		}
		
		private static String getLocalDirectory()
		{
			return System.getProperty("user.dir");
		}
	}
}
