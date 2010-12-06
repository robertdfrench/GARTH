/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.garth;


/**
 * Abstract class from which FitnessFunctions must inherit
 * Any caching / reuse optimizations go here
 * @author robertdfrench
 *
 */
public interface KSFitnessFunction {
	
	public void evaluate(KSOrganism organism);

}
