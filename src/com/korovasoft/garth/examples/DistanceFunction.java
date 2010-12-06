package com.korovasoft.garth.examples;

import com.korovasoft.garth.KSFunction;

public class DistanceFunction implements KSFunction {

	public double execute(double[] arguments) {
		double distanceFromOrigin = 0.0;
		for(double coordinate : arguments) {
			distanceFromOrigin += Math.pow(coordinate, 2);
		}
		return Math.sqrt(distanceFromOrigin);
	}

}
