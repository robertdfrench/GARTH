package com.korovasoft.garth;


public class KSCacheableFunction implements KSFunction {

	private static final int defaultCacheSize = 10000;
	private static KSMultikeyHash<Class<? extends Object>,Integer,KSForgetfulHash> hashhash;
	private KSForgetfulHash cache;
	private KSFunction internalFunction;
	
	
	public KSCacheableFunction(int argumentLength, KSFunction internalFunction) {
		this.internalFunction = internalFunction;
		this.cache = getCacheForFunction(internalFunction, argumentLength);
	}
	
	private static KSForgetfulHash getCacheForFunction(KSFunction f, int argc) {
		Class<? extends Object> classKey = f.getClass();
		Integer argLengthKey = new Integer(argc);
		if (hashhash == null) {
			hashhash = new KSMultikeyHash<Class<? extends Object>,Integer,KSForgetfulHash>();
		}
		KSForgetfulHash cache = hashhash.get(classKey, argLengthKey);
		if (cache == null) {
			cache = new KSForgetfulHash(defaultCacheSize, argc);
			hashhash.put(classKey, argLengthKey, cache);
		}
		return cache;
	}
	
	public double execute(double[] argument) {
		double returnValue = cache.get(argument);
		if (returnValue == KSForgetfulHash.UNDEFINED) {
			returnValue = internalFunction.execute(argument);
			cache.put(argument, returnValue);
		}
		
		return returnValue;
	}
	

}
