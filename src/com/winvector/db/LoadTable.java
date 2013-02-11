package com.winvector.db;

import java.io.File;
import java.net.URI;
import java.util.Date;

import com.winvector.db.DBUtil.DBHandle;
import com.winvector.util.BurstMap;
import com.winvector.util.TrivialReader;


public class LoadTable {
	
	public static void main(final String[] args) throws Exception {
		final Date now = new Date();
		System.out.println("start LoadTable\t" + now);
		System.out.println("\tfor details see: http://www.win-vector.com/blog/2011/01/sql-screwdriver/");
		System.out.println("\tfor latest version see: https://github.com/WinVector/SQLScrewdriver");
		if(args.length!=4) {
			throw new Exception("use: LoadTable dbProbsURI sepChar inputURI tableName");
		}
		final URI propsURI = new URI(args[0]);
		final char sep = args[1].charAt(0);
		final URI inURI = new URI(args[2]);
		final String tableName = args[3];
		System.out.println("\tcwd: " + (new File(".")).getAbsolutePath());
		System.out.println("\tDBProperties XML:\t" + propsURI.toString());
		System.out.println("\tsep: " + sep);
		System.out.println("\tSource URI:\t" + inURI);
		System.out.println("\ttableName:\t" + tableName);
		final DBHandle handle = DBUtil.buildConnection(propsURI,false);
		System.out.println("\tdb:\t" + handle);
		
		final Iterable<BurstMap> source = new TrivialReader(inURI,sep,null,true,null, false);
		String sourceName = inURI.toString();
		final TableControl tableControl = new TableControl(tableName);
		tableControl.scanForDefs(sourceName,source, null);
		tableControl.buildSQLStatements();
		tableControl.createTable(handle);
		final long nInserted = tableControl.loadData(sourceName,now,source, null, handle);
		System.out.println("\tdone, wrote " + nInserted + " rows\t" + new Date());
		handle.conn.close();
		System.out.println("done LoadTable\t" + new Date());
	}
}
