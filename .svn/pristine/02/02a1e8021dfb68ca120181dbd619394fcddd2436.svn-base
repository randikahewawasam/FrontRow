package com.frontrow.ui;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class CachedFileProvider extends ContentProvider {
	
    private static final String CLASS_NAME = "CachedFileProvider";
    

    public static final String AUTHORITY = "com.frontrow.common.provider";
 
   
    private UriMatcher uriMatcher;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
	     uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	     
	        uriMatcher.addURI(AUTHORITY, "*", 1);
	 
	        return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	   @Override
	    public ParcelFileDescriptor openFile(Uri uri, String mode)
	            throws FileNotFoundException {
	 
	       
	 
	 
	        switch (uriMatcher.match(uri)) {
	 
	    
	        case 1:
	
	            String fileLocation = getContext().getCacheDir() + File.separator
	                    + uri.getLastPathSegment();
	 
	       
	            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(
	                    fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
	            return pfd;
	 
	           
	        default:
	     
	            throw new FileNotFoundException("Unsupported uri: "
	                    + uri.toString());
	        }
	    }

}
