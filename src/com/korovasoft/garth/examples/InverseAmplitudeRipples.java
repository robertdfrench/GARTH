package com.korovasoft.garth.examples;

import com.korovasoft.garth.KSCacheableFunction;
import com.korovasoft.garth.KSFunction;

public class InverseAmplitudeRipples implements KSFunction {
	
	private KSFunction cachedDistanceFunction;
	
	public InverseAmplitudeRipples() {
		cachedDistanceFunction = new KSCacheableFunction(100, new DistanceFunction());
	}

	public double execute(double[] arguments) {
		KSFunction cdf = cachedDistanceFunction;
		return Math.sin(cdf.execute(arguments)) / (1 + Math.sqrt(cdf.execute(arguments)));
	}

}
