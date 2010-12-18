/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.distributed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class KSGarthConfig {
	/**
	 * Name of the database within the specific server
	 */
	public String databaseName;
	
	/**
	 * User with CREATE/DROP/ALTER/INSERT/SELECT privileges
	 * for all tables prefixed with the databaseTablePrefix
	 */
	public String databaseUser;
	
	/**
	 * Authentication string for above user
	 */
	public String databasePassword;
	
	/**
	 * Address or domain name of machine on which the 
	 * database server resides
	 */
	public String databaseHost;
	
	/**
	 * TCP/IP port on which to connect to said
	 * database server
	 */
	public int databasePort;
	
	/**
	 * In case we must share a database with other applications,
	 * all tables will be prefixed with databaseTablePrefix to
	 * prevent naming collisions
	 */
	public String databaseTablePrefix;
	
	/**
	 * Number of organisms that should be involved in the experiment
	 */
	public int populationSize;
	
	/**
	 * Number of genes each organism will carry. Note that genes are
	 * defined to be double precision numbers on the closed interval
	 * [0,1]. It is the responsibility of the fitness function to
	 * transform the gene into an appropriate value prior to evaluation
	 */
	public int genomeLength;
	
	/**
	 * Number of KSDistGAWorker processes to start. This is currently
	 * borked.
	 */
	public int numWorkers;
	
	/**
	 * Ignore this. The Statistics sampling is about to be reworked, so
	 * it may become super important or it may get removed
	 */
	public int numInsertsBeforeCallingGC;
	
	/**
	 * How many milliseconds to sleep between sampling intervals
	 */
	public int statisticsSamplerWaitPeriod;

	/**
	 * Fitness function to be used in calculations
	 */
	public String fitnessFunction;
	
	/**
	 * Number of organisms that will be checked out from the
	 * repository at a time. Should properly divide populationSize
	 */
	public int checkoutSize;
	
	/**
	 * create a new KSDistGAConfig with the hard-coded values
	 */
	public KSGarthConfig() {
		// TODO Read config info from an XML file
		databaseName = "distributed_ga";
		databaseUser = "root";
		databasePassword = "";
		databaseHost = "localhost";
		databasePort = 3306;
		databaseTablePrefix = "ksdga_";
		populationSize = 1000;
		genomeLength = 100;
		checkoutSize = 50;
		numWorkers = 6;
		numInsertsBeforeCallingGC = 10000;
		statisticsSamplerWaitPeriod = 2000;
		fitnessFunction = "com.korovasoft.garth.examples.InverseAmplitudeRipples";
		assert(populationSize % checkoutSize == 0);
		assert(checkoutSize % 2 == 0);
	}

	/**
	 * Reads in config options from filename
	 * @param filename
	 */
	public KSGarthConfig(String filename) {
		HashMap<String,String> internalDictionary = new HashMap<String,String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				String[] pair = str.split(" = ", 2);
				internalDictionary.put(pair[0], pair[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't read config file, bailing");
		}
		databaseName = internalDictionary.get("databaseName");
		databaseUser = internalDictionary.get("databaseUser");
		databasePassword = internalDictionary.get("databasePassword");
		databaseHost = internalDictionary.get("databaseHost");
		databasePort = (new Integer(internalDictionary.get("databasePort"))).intValue();
		databaseTablePrefix = internalDictionary.get("databaseTablePrefix");
		populationSize = (new Integer(internalDictionary.get("populationSize"))).intValue();
		genomeLength = (new Integer(internalDictionary.get("genomeLength"))).intValue();
		checkoutSize = (new Integer(internalDictionary.get("checkoutSize"))).intValue();
		numWorkers = (new Integer(internalDictionary.get("numWorkers"))).intValue();
		numInsertsBeforeCallingGC = (new Integer(internalDictionary.get("numInsertsBeforeGC"))).intValue();
		statisticsSamplerWaitPeriod = (new Integer(internalDictionary.get("statisticsSamplerWaitPeriod"))).intValue();
		fitnessFunction = internalDictionary.get("fitnessFunction");
		assert(populationSize % checkoutSize == 0);
		assert(checkoutSize % 2 == 0);
	}
}
