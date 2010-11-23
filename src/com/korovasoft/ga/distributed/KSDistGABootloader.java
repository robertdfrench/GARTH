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
		KSOrganism[] newPair = new KSOrganism[2];
		System.out.println("Generating Initial Population");
		for (int i = 0; i < conf.populationSize / 2; i++) {
			for (int j = 0; j < 2; j++) {
				newPair[j] = new KSOrganism(conf.genomeLength, true);
				// Evaluate before insert so that we can gather initial statistics
				ff.evaluate(newPair[j]); 
			}
			try {
				dbh.putPair(newPair);
			} catch (SQLException e) {
				System.err.println("MySQL connection died while building population.");
				System.exit(1);
			}
		}
		System.out.println("Database is loaded, start workers.");
		
		while(true) {
			try {
				dbh.calculateAndLogStatistics();
				Thread.sleep(conf.statisticsSamplerWaitPeriod);
			} catch (SQLException e) {
				System.err.println("MySQL connection died while doing statistical sampling.");
				System.exit(1);
			}
		}
	}

}
