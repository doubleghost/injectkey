package com.doubleghost.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class FileUtils {
	
	public static void copyAssetDirToFiles(Context context, String dirname)
			throws IOException {
		File dir = new File(context.getFilesDir() + "/" + dirname);
		dir.mkdir();
		
		AssetManager assetManager = context.getAssets();
		String[] children = assetManager.list(dirname);
		for (String child : children) {
			child = dirname + '/' + child;
			String[] grandChildren = assetManager.list(child);
			if (0 == grandChildren.length)
				copyAssetFileToFilesC(context, child);
			else
				copyAssetDirToFiles(context, child);
		}
	}
	
	public static void copyAssetFileToFilesC(Context context, String filename)
			throws IOException {
		InputStream is = context.getAssets().open(filename);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();
		
		File of = new File(context.getFilesDir() + "/"+ filename);
		of.createNewFile();
		FileOutputStream os = new FileOutputStream(of); 
		os.write(buffer);
		os.close();
	}
	public static void copyAssetFileToFilesDir(Context context,String filename)
			throws IOException {
//		File dir = new File(context.getFilesDir() + "/" + dirname);
//		if(!dir.exists())
//			dir.mkdir();
		Log.d("FileUtils","context filedir:"+context.getFilesDir());
		File of = new File(context.getFilesDir() + "/" + filename);
	    
		//if(!of.exists())
		{
			InputStream is = context.getAssets().open(filename);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			
			of.createNewFile();
			FileOutputStream os = new FileOutputStream(of);
			os.write(buffer);
			os.close();
		}
	}
}