/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.distributed;

import java.io.IOException;
import java.sql.SQLException;

import com.korovasoft.garth.KSFitnessFunction;
import com.korovasoft.garth.KSOrganism;

public class KSGarthBootloader {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		KSGarthConfig conf = new KSGarthConfig();
		KSFitnessFunction ff = conf.fitnessFunction;
		KSGarthDatasource dbh = null;
		
		System.out.println("Creating Tables from Experiment definition");
		try {
			dbh = new KSGarthDatasource(conf);
			dbh.setupTables();
		} catch (SQLException e) {
			System.err.println("MySQL appears to be inaccessible");
			System.exit(1);
		}
		// Prime the data source with random organisms
		KSOrganism[] checkoutArray = new KSOrganism[conf.checkoutSize];
		System.out.println("Generating Initial Population");
		for (int i = 0; i < conf.populationSize / conf.checkoutSize; i++) {
			//TODO Enforce requirement that conf.checkoutSize will always divide conf.populationSize
			for (int j = 0; j < conf.checkoutSize; j++) {
				checkoutArray[j] = new KSOrganism(conf.genomeLength, true);
				// Evaluate before insert so that we can gather initial statistics
				ff.evaluate(checkoutArray[j]); 
			}
			try {
				dbh.putArray(checkoutArray);
			} catch (SQLException e) {
				garth_bail(e, "MySQL connection died while building population.");
			}
		}
		System.out.println("Database is loaded, start workers.");
		
		while(true) {
			try {
				dbh.calculateAndLogStatistics();
				Thread.sleep(conf.statisticsSamplerWaitPeriod);
			} catch (SQLException e) {
				garth_bail(e,"MySQL connection died while doing statistical sampling.");
			}
		}
	}

	private static void garth_bail(Exception e, String message) {
		System.out.println(message);
		e.printStackTrace();
		System.exit(1);
	}
}
