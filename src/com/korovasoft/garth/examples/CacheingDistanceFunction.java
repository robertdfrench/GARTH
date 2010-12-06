package com.korovasoft.garth.examples;

import com.korovasoft.garth.KSCacheableFunction;

public class CacheingDistanceFunction extends KSCacheableFunction {

	public CacheingDistanceFunction(int argumentLength) {
		super(argumentLength);
		// TODO Auto-generated constructor stub
	}

	public double calculate(double[] arguments) {
		double distanceFromOrigin = 0.0;
		for(double coordinate : arguments) {
			distanceFromOrigin += Math.pow(coordinate, 2);
		}
		return Math.sqrt(distanceFromOrigin);
	}
}
