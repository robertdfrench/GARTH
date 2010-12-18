package com.korovasoft.garth;


/**
 * A Shared Non-blocking forgetful hash
 * Allocated N elements on startup, and overwrites
 * rather than chaining.
 * Neither reads nor writes are guaranteed to succeed, but if they
 * do they will be atomic. 
 * Expects (double[], double) as (key, value)
 * @author robertdfrench
 *
 */
public class KSForgetfulHash {
	
	public static final double UNDEFINED = Double.POSITIVE_INFINITY;
	/**
	 * A single element in the ForgetfulHash
	 * All concurrency protection is handled at the element level
	 * @author robertdfrench
	 *
	 */
	private class KSForgetfulHashElement {
		
		/**
		 * No value has yet been written. Purposefully different from
		 * KSForgetfulHash.CACHE_MISS to support the bad ass non-blockingness
		 */
		private static final double EMPTY = Double.NEGATIVE_INFINITY;

		/**
		 * The actual value to be retrieved
		 */
		private double cachedValue;
		
		/**
		 * The key to compare
		 */
		private double[] key;
		
		/**
		 * in case a write interleaves a read and does not kill it,
		 * read will not succeed unless versionIDs match from beginning to end
		 */
		private int versionID;

		/**
		 * Creates a new Hash Element of length keyLength
		 * @param keyLength
		 */
		public KSForgetfulHashElement(int keyLength) {
			versionID = 1;
			cachedValue = EMPTY;
			key = new double[keyLength];
		}
		
		/**
		 * Returns KSForgetfulHash.UNDEFINED if the element is currently being overwritten,
		 * Otherwise returns the stored value.
		 * Also fails if versionID changes while the read is taken place
		 * (in case the write and read interleave).
		 * Fails early if cachedValue = Double.NaN (currently being overwritten)
		 * @param key
		 * @return
		 */
		public double read(double key[]) {
			if (this.accessIsDangerous()) return KSForgetfulHash.UNDEFINED;
			int versionID = this.versionID;
			int readUntil = (key.length < this.key.length) ? key.length : this.key.length;
			boolean keysMatch = true;
			for (int i = 0; i < readUntil && keysMatch; i++) {
				keysMatch = (key[i] == this.key[i]);
			}
			return (keysMatch && versionID == this.versionID) ? this.cachedValue : KSForgetfulHash.UNDEFINED;
		}
		
		/**
		 * Writes <i>totally</i> have precedence over reads
		 * @param key
		 * @param value
		 * @return
		 */
		public boolean write(double key[], double value) {
			if (this.accessIsDangerous()) return false;
			int versionID = disableAccess();
			int writeUntil = (key.length < this.key.length) ? key.length : this.key.length;
			System.arraycopy(key, 0, this.key, 0, writeUntil);
			enableAccess(versionID + 1, value);
			return true;
		}
		
		/**
		 * Allows readers or writers to access the cache again
		 * @param versionID
		 * @param value
		 */
		private void enableAccess(int versionID, double value) {
			this.versionID = versionID;
			this.cachedValue = value;
		}
		
		/**
		 * Dissuades readers and writers from accessing the cache
		 * @return
		 */
		private int disableAccess() {
			int versionID = this.versionID;
			this.versionID = 0;
			this.cachedValue = KSForgetfulHash.UNDEFINED;
			return versionID;
		}
		
		/**
		 * Determines if cache access is a Bad Idea
		 * @return
		 */
		private boolean accessIsDangerous() {
			return (this.cachedValue == KSForgetfulHash.UNDEFINED || this.versionID == 0);
		}
	}
	
	/**
	 * This is where we keep all the good shit
	 */
	private KSForgetfulHashElement[] hashTable;
	
	/**
	 * Builds a new ForgetfulHash
	 * @param numElements <- With this many elements,
	 * @param maxKeyLength <- Each having this long of a key.
	 * <br/><b>;-)</b>
	 */
	public KSForgetfulHash(int numElements, int maxKeyLength) {
		hashTable = new KSForgetfulHashElement[numElements];
		for (int i = 0; i < numElements; i++) {
			hashTable[i] = new KSForgetfulHashElement(maxKeyLength);
		}
	}
	
	/**
	 * Just like Map.put(), except it works explicitly on (double[], double)
	 * as the (key, value) pair.
	 * Returns false if the write fails (prolly because it got trampled on
	 * by a concurrent write).
	 * Even if put() returns true, your value could disappear as soon as 
	 * another put() operation gets called.
	 * @param key <- an array of doubles
	 * @param value <- a single double, Animal Style, with Animal Style fries and a Dr. Pepper
	 * @return
	 */
	public boolean put(double[] key, double value) {
		return hashTable[keyToTableAddress(key)].write(key, value);
	}
	
	/**
	 * Gets the value for this key from the Hash.
	 * Returns Double.NaN if something screws up
	 * @param key
	 * @return
	 */
	public double get(double[] key) {
		return hashTable[keyToTableAddress(key)].read(key);
	}
	
	/**
	 * Turns the key into an appropriate table location.
	 * @param key
	 * @return
	 */
	private int keyToTableAddress(double[] key) {
		return key.hashCode() % hashTable.length;
	}
	
}
