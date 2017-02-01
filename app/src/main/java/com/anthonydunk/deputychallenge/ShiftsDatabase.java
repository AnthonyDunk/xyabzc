package com.anthonydunk.deputychallenge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.Environment;

public class ShiftsDatabase {

	final String m_sDatabaseName	= "ShiftsDatabase.sql";

	private SQLiteDatabase m_db;
	private boolean m_bInitialised;
	
	
	public ShiftsDatabase(Activity act)
	{
		m_bInitialised = false;
		try {
			   m_db = act.openOrCreateDatabase(m_sDatabaseName, Activity.MODE_PRIVATE, null);

			   // Create shifts table
			   m_db.execSQL("CREATE TABLE IF NOT EXISTS Shifts (_id INTEGER PRIMARY KEY, "+
					   "id INTEGER, start TEXT, end TEXT, startLatitude TEXT, startLongitude TEXT, "+
			   			"endLatitude TEXT, endLongitude TEXT, image TEXT);");

			   m_bInitialised = true;
		}
		catch (Exception e)
		{
			m_bInitialised = false; // An error occurred
		}
	}
	
	public boolean Initialised() {return m_bInitialised;}
	

	public int [] GetShiftIDs ()
	{
		int [] shiftIDs = null;
		try {			 			   
		   //retrieve data from database
		   Cursor c = m_db.rawQuery("SELECT * FROM Shifts ORDER BY id" , null);

		   int nColumnIndex = c.getColumnIndex("id");

		   // Check if our result was valid.
		   int nCount = c.getCount();
		   int nItem = 0;
		   if (nCount>0)
		   {
			   shiftIDs = new int[nCount];
			   c.moveToFirst();
			   if (c != null)
			   {
				    do {
				     int shiftID = c.getInt(nColumnIndex);
				     shiftIDs[nItem++] = shiftID;
				    } while(c.moveToNext());
			   }
		   }
		}
		catch (Exception e)
		{
			shiftIDs = null; // Could not read database
		}
		
		return shiftIDs;
	}

	public ShiftDetails GetShiftDetails(int shiftID)
	{
		ShiftDetails details = null;
		int nID = -1;
		
		try {			 			   
		   Cursor c = m_db.rawQuery("SELECT * FROM Shifts WHERE id=\""+Integer.toString(shiftID)+"\"", null);
		   int nColumnIndex = c.getColumnIndex("id");

		   // Check if our result was valid.
		   int nCount = c.getCount();
		   if (nCount>0)
		   {
			   c.moveToFirst();
			   if (c != null) {
				   details = new ShiftDetails();
				   details.id = c.getInt(c.getColumnIndex("id"));
				   details.start = c.getString(c.getColumnIndex("start"));
				   details.end = c.getString(c.getColumnIndex("end"));
				   details.startLatitude = c.getString(c.getColumnIndex("startLatitude"));
				   details.startLongitude = c.getString(c.getColumnIndex("startLongitude"));
				   details.endLatitude = c.getString(c.getColumnIndex("endLatitude"));
				   details.endLongitude = c.getString(c.getColumnIndex("endLongitude"));
				   details.image = c.getString(c.getColumnIndex("image"));
			   }
		   }
		}
		catch (Exception e)
		{
			details=null;
		}
		
		return details;
	}

	
	
	public boolean AddShift(ShiftDetails details)
	{
		boolean bOk = false;
		try {
			   m_db.execSQL("INSERT INTO Shifts (id,start,end,startLatitude,startLongitude,endLatitude,endLongitude,image) "+
					   "VALUES (" +
					   Integer.toString(details.id)+
					   ",\""+details.start+"\"" +
					   ",\""+details.end+"\"" +
					   ",\""+details.startLatitude+"\"" +
					   ",\""+details.startLongitude+"\"" +
					   ",\""+details.endLatitude+"\"" +
					   ",\""+details.endLongitude+"\"" +
					   ",\""+details.image+"\"" +
					   ");");
			   bOk = true;
		}
		catch (Exception e)	{}
		return bOk;
	}
	

	public boolean DeleteAllShifts()
	{
		boolean bOk = false;
		try {			 	
		   // Delete all list items
		   m_db.execSQL("DELETE FROM Shifts;");

		   bOk = true;
		}
		catch (Exception e)
		{
		}
		return bOk;
	}

	
	
	

}
