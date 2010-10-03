package de.bsd.zwitscher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import twitter4j.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class PicHelper {

	private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

	public Bitmap getUserPic(User user,Context context) {
		
		// TODO optimize see e.g. http://www.androidpeople.com/android-load-image-from-url-example/#comment-388

		URL imageUrl = user.getProfileImageURL();
		
		String username = user.getScreenName();
		System.out.println("Storing files to " + context.getFilesDir());
		boolean found = false;
		try {
			FileInputStream fis = context.openFileInput(username);
			found = true;
			fis.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
// TODO check if file is outdated		
//		if (imgFile.exists()) {
//			long lastModFile = imgFile.lastModified();
//			// check for new image once a day
//			if (lastModFile < System.currentTimeMillis() - ONE_DAY) {
//				long lastMod;
//				try {
//					lastMod = imageUrl.openConnection().getLastModified();
//					if (lastMod < lastModFile)
//						found = true;
//					
//				} catch (IOException e) {
//					; 
//				}
//			}
//			else
//				found = true;
//		} 
			
		if (!found) {
			try {
			BufferedInputStream in = new BufferedInputStream(imageUrl.openStream());
			BufferedOutputStream out = new BufferedOutputStream(context.openFileOutput(username,Context.MODE_WORLD_READABLE));
			int val;
			while ((val = in.read()) > -1)
				out.write(val);
			out.flush();
			out.close();
			in.close();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		Log.i("show image",imageUrl.toString());
		try {
			Bitmap bi = BitmapFactory.decodeStream(context.openFileInput(username));
			return bi;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
