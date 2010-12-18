/**
 * 
 */
package com.korovasoft.garth.test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.korovasoft.garth.KSForgetfulHash;
import com.korovasoft.garth.KSOrganism;

/**
 * @author robertdfrench
 *
 */
public class TestForgetfulHash extends TestCase {

	static final int populationSize = 20000;
	static final int genomeLength = 100;
	static final int tableSize = 2500;
	static KSOrganism[] population;
	static Random rng;
	KSForgetfulHash hash;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rng = new Random(System.currentTimeMillis());
		
		// Initialize parent
		KSOrganism parent = new KSOrganism(genomeLength);
		
		// Initialize population
		population = new KSOrganism[populationSize];
		for (int i = 0; i < populationSize; i++) {
			population[i] = new KSOrganism(parent);
			population[i].fitnessScore = rng.nextDouble();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		hash = new KSForgetfulHash(tableSize,genomeLength);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.korovasoft.garth.KSForgetfulHash#put(double[], double)}.
	 */
	@Test
	public final void testPut() {
		int num_failures = 0;
		for(KSOrganism org : population) {
			if(!hash.put(org.genome, org.fitnessScore)) {
				num_failures++;
			}
		}
		assertTrue(num_failures == 0);
	}

	/**
	 * Test method for {@link com.korovasoft.garth.KSForgetfulHash#get(double[])}.
	 */
	@Test
	public final void testGet() {
		for(KSOrganism org : population) {
			hash.put(org.genome, org.fitnessScore);
		}
		int num_nulls = 0;
		for(KSOrganism org : population) {
			if (org.fitnessScore != hash.get(org.genome)) {
				num_nulls++;
			}
		}
		// Assure that at least 98% of the hash is being used.
		// If not, we should doubt the hashing function.
		// I think...
		assertTrue(populationSize - num_nulls > tableSize * 0.98);
	}

	/**
	 * Make sure the reading and writing actually works like I think it does
	 */
	@Test
	public final void testConcurrency() {
		double[] threshholds = {0.7, 0.2, 0.1, 0.01};
		ConcurrentHashMap<Double,Integer> status = new ConcurrentHashMap<Double, Integer>();
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(double threshhold : threshholds) {
			Thread t = new Thread(new ConcurrencyTester(threshhold, status));
			t.start();
			threads.add(t);
		}
		try {
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Interrupted while waiting to join threads");
		}
		int totalMisses = 0;
		int totalAttempts = 0;
		for(Double key : status.keySet()) {
			totalMisses += status.get(key).intValue();
			totalAttempts += populationSize;
		}
		double missRatio = ((double) totalMisses) / ((double) totalAttempts);
		assertTrue(missRatio < 0.03);
	}
	
	public class ConcurrencyTester implements Runnable {

		private double threshhold;
		private ConcurrentHashMap<Double,Integer> status;
		private int numMisses;
		
		public ConcurrencyTester(double threshhold, ConcurrentHashMap<Double,Integer> status) {
			this.threshhold = threshhold;
			this.status = status;
			this.numMisses = 0;
		}
		
		public void run() {
			for (KSOrganism org : population) {
				if (rng.nextDouble() > this.threshhold) {
					if(!hash.put(org.genome, org.fitnessScore)) {
						this.numMisses++;
					}
				} else {
					if(hash.get(org.genome) == Double.NaN) {
						this.numMisses++;
					}
				}
				try {
					Thread.sleep(0, (int) Math.floor(rng.nextDouble() * 200000)); // sleep no more than 0.2 milliseconds
				} catch (InterruptedException e) {
					e.printStackTrace();
					fail("Worker thread interrupted while sleeping");
				}
			}
			status.put(new Double(this.threshhold), new Integer(this.numMisses));
		}
		
	}
}
