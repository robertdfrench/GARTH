/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.ga.distributed;

import java.sql.SQLException;

/**
 * KSDistGAWorker is responsible for grabbing organisms from the gene pool, evaluating
 * their fitness, performing appropriate mating rituals, killing any misfits, and inserting
 * any newly formed organisms back into the gene pool
 * @author robertdfrench
 *
 */
public class KSDistGAWorker {
	/**
	 * reference to the gene_pool data source
	 */
	private KSDistGADatasource genePool;
	
	/**
	 * reference to the fitness function object
	 */
	private KSFitnessFunction ff;
	
	/**
	 * reference to the configuration parameters
	 */
	private KSDistGAConfig conf;
	
	/**
	 * Standard constructor for KSDistGAWorker. Nothing fancy. Move along
	 * @param genePool
	 * @param ff
	 * @param conf
	 */
	public KSDistGAWorker(KSDistGADatasource genePool, KSFitnessFunction ff, KSDistGAConfig conf) {
		this.genePool = genePool;
		this.ff = ff;
		this.conf = conf;
	}
	
	/**
	 * Setup function for DistGAWorker
	 * See run() for all the interesting shiz
	 * @param args
	 */
	public static void main(String[] args) {
		KSDistGAConfig conf = new KSDistGAConfig();
		KSFitnessFunction ff = conf.fitnessFunction;
		KSDistGADatasource genePool;
		KSDistGAWorker wkr;
		try {
			genePool = new KSDistGADatasource(conf);
			wkr = new KSDistGAWorker(genePool,ff,conf);
			wkr.run();
		} catch (SQLException e) {
			System.err.println("MySQL is not started, worker exiting");
		}
	}
	
	/**
	 * This is the entry point for KSDistGAWorker
	 * Should eventually conform to IRunnable's run() specification
	 * so this can be a thread entry point
	 * TODO: Separate Datasource connections so that threads can
	 * access the datasource independently of one another
	 * @throws SQLException
	 */
	public void run() throws SQLException {
		System.out.println("New worker started");
		double poorestFitnessScore = genePool.getPoorestFitness();
		KSOrganism.setFitnessNormalizer(poorestFitnessScore);
		for (int i = 0; true; i++) {
			KSOrganism[] checkoutArray = genePool.takeArray();
			for (int j = 0; j < conf.checkoutSize; j++) {
				ff.evaluate(checkoutArray[j]);
			}
			for (int j = 0; j < conf.checkoutSize / 2; j++) {
				int overwriteIndex = (checkoutArray[2*j + 0].fitnessScore > checkoutArray[2*j + 1].fitnessScore) ? 0 : 1; // pick the loser
				int cloneIndex = 1 - overwriteIndex; // pick the winner
				checkoutArray[2*j + overwriteIndex] = new KSOrganism(checkoutArray[2*j + cloneIndex]); // overwrite the loser with clone of winner
			}
			genePool.putArray(checkoutArray);
			if (i == conf.numInsertsBeforeCallingGC) {
				i = 0;
			//TODO: Test this renormalizing routine 	
			//	KSOrganism.setFitnessNormalizer(poorestFitnessScore);
			//	poorestFitnessScore = genePool.getPoorestFitness();
				System.gc(); // Suspicious that GC never realizes to collect KSOrganisms that have been overwritten
			}
		}
	}
}
