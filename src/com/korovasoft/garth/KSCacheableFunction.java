package com.korovasoft.garth;

public abstract class KSCacheableFunction implements KSFunction {

	public static final int defaultCacheSize = 10000;
	private KSForgetfulHash cache;
	
	public KSCacheableFunction(int argumentLength) {
		this(defaultCacheSize, argumentLength);
	}
	
	public KSCacheableFunction(int cacheSize, int argumentLength) {
		cache = new KSForgetfulHash(cacheSize, argumentLength);
	}
	
	public double execute(double[] argument) {
		double returnValue = cache.get(argument);
		
		if (returnValue == Double.NaN) {
			returnValue = calculate(argument);
			cache.put(argument, returnValue);
		}
		
		return returnValue;
	}
	
	protected abstract double calculate(double[] argument);
}
