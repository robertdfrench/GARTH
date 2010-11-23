/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.ga.distributed;

import java.io.IOException;
import java.sql.SQLException;

public class KSDistGABootloader {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		KSDistGAConfig conf = new KSDistGAConfig();
		KSFitnessFunction ff = conf.fitnessFunction;
		KSDistGADatasource dbh = null;
		
		
		System.out.println("Creating Tables from Experiment definition");
		try {
			dbh = new KSDistGADatasource(conf);
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
