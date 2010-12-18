/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.distributed;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import com.korovasoft.garth.KSFitnessFunction;
import com.korovasoft.garth.KSFitnessFunctionWrapper;
import com.korovasoft.garth.KSOrganism;

/**
 * KSDistGAWorker is responsible for grabbing organisms from the gene pool, evaluating
 * their fitness, performing appropriate mating rituals, killing any misfits, and inserting
 * any newly formed organisms back into the gene pool
 * @author robertdfrench
 *
 */
public class KSGarthWorker implements Runnable {
	/**
	 * reference to the gene_pool data source
	 */
	private KSGarthDatasource genePool;
	
	/**
	 * reference to the fitness function object
	 */
	private KSFitnessFunction ff;
	
	/**
	 * reference to the configuration parameters
	 */
	private KSGarthConfig conf;
	
	/**
	 * Standard constructor for KSDistGAWorker. Nothing fancy. Move along
	 * @param genePool
	 * @param ff
	 * @param conf
	 */
	public KSGarthWorker(KSGarthDatasource genePool, KSFitnessFunction ff, KSGarthConfig conf) {
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
		KSGarthConfig conf = new KSGarthConfig(System.getProperty("garthConfig"));
		KSFitnessFunction ff = null;
		try {
			ff = KSFitnessFunctionWrapper.getFitnessFunction(conf.fitnessFunction);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		KSGarthDatasource genePool;
		KSGarthWorker wkr;
		Thread[] threads = new Thread[conf.numWorkers];
		try {
			for (int i = 0; i < conf.numWorkers; i++) {
				genePool = new KSGarthDatasource(conf);
				wkr = new KSGarthWorker(genePool,ff,conf);
				threads[i] = new Thread(wkr);
				threads[i].start();
			}
			for (int i = 0; i < conf.numWorkers; i++) {
				threads[i].join();
			}
		} catch (SQLException e) {
			KSGarthBootloader.garth_bail(e,"MySQL is not started, worker exiting");
		} catch (InterruptedException e) {
			KSGarthBootloader.garth_bail(e,"Worker thread interrupted. Start Panicking.");
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
	public void run() {
		System.out.println("New worker started");
		try {
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
					System.gc(); // Suspicious that GC never realizes to collect KSOrganisms that have been overwritten
				}
			}
		} catch (SQLException e) {
			KSGarthBootloader.garth_bail(e, "Database croaked, I'm out");
		}
	}
}
