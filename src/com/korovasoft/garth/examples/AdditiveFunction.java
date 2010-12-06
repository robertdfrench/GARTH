/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth.examples;

import com.korovasoft.garth.KSFunction;


public class AdditiveFunction implements KSFunction {

	public double execute(double[] arguments) {
		double fitness = 0.0;
		for (int i = 0; i < arguments.length; i++) {
			fitness += Math.sqrt(arguments[i]) + Math.sin(arguments[i]);
		}
		return fitness;
	}
}
