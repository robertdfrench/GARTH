/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.examples;

import com.korovasoft.garth.KSFitnessFunction;
import com.korovasoft.garth.KSOrganism;


public class AdditiveFitnessFunction extends KSFitnessFunction {

	protected double calculateFitness(KSOrganism organism) {
		double fitness = 0.0;
		for (int i = 0; i < organism.genomeLength; i++) {
			fitness += Math.sqrt(organism.genome[i]) + Math.sin(organism.genome[i]);
		}
		return fitness;
	}
}
