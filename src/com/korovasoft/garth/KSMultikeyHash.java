package com.korovasoft.garth;

import java.util.HashMap;

public class KSMultikeyHash<OuterKeyType, InnerKeyType, ValueType> {

	private HashMap<OuterKeyType,HashMap<InnerKeyType,ValueType>> outerHashMap;
	
	public KSMultikeyHash() {
		outerHashMap = new HashMap<OuterKeyType, HashMap<InnerKeyType,ValueType>>();
	}
	
	public void put(OuterKeyType outerKey, InnerKeyType innerKey, ValueType value) {
		if (!outerHashMap.containsKey(outerKey)) {
			outerHashMap.put(outerKey, new HashMap<InnerKeyType,ValueType>());
		}
		outerHashMap.get(outerKey).put(innerKey, value);
	}
	
	public ValueType get(OuterKeyType outerKey, InnerKeyType innerKey) {
		if (!outerHashMap.containsKey(outerKey)) {
			return null;
		}
		return outerHashMap.get(outerKey).get(innerKey);
	}

}