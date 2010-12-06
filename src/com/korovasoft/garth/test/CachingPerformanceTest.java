package com.korovasoft.garth.test;


import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.korovasoft.garth.KSCacheableFunction;
import com.korovasoft.garth.KSFunction;
import com.korovasoft.garth.KSOrganism;
import com.korovasoft.garth.examples.AdditiveFunction;
import com.korovasoft.garth.examples.DistanceFunction;

public class CachingPerformanceTest {
	static final int populationSize = 20000;
	static final int genomeLength = 1000;
	static final int tableSize = 2500;
	static KSOrganism[] population;
	static Random rng;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rng = new Random(System.currentTimeMillis());
		
		// Initialize parent
		KSOrganism parent = new KSOrganism(genomeLength);
		for (int i = 0; i < parent.genomeLength; i++) {
			parent.genome[i] = rng.nextDouble();
		}
		
		// Initialize population
		population = new KSOrganism[populationSize];
		for (int i = 0; i < populationSize; i++) {
			population[i] = new KSOrganism(parent);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAdditiveFunction() {
		performanceTest(new AdditiveFunction());
	}
	
	@Test
	public void testDistanceFunction() {
		performanceTest(new DistanceFunction());
	}
	
	public void performanceTest(KSFunction f) {
		KSFunction caching = new KSCacheableFunction(genomeLength, f);
		KSFunction normal  = f;
		
		long cachingTime = getTimingForFunction(caching);
		long normalTime = getTimingForFunction(normal);
		
		assertTrue(cachingTime < normalTime);
	}
	
	public long getTimingForFunction(KSFunction f) {
		int numThreads = 4;
		int orgsPerThread = 5000;
		
		Thread[] threads = new Thread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new Thread(new ConcurrentWorker(i * orgsPerThread, (i + 1) * orgsPerThread - 1, f));
		}
		long t0 = System.currentTimeMillis();
		for(Thread t : threads) { t.start(); }
		for(Thread t : threads) { try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} }
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}
	
	public class ConcurrentWorker implements Runnable {

		private int lowerBound;
		private int upperBound;
		private KSFunction f;
		
		public ConcurrentWorker(int lowerBound, int upperBound, KSFunction f) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.f = f;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			for (int i = this.lowerBound; i <= this.upperBound; i++) {
				f.execute(population[i].genome);
			}
		}
		
	}

}
