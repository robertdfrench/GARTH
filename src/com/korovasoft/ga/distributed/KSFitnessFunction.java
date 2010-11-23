/**
* COPYRIGHT 2010 Korovasoft.
*
* See LICENSE file for details.
**/
package com.korovasoft.ga.distributed;

/**
 * Fuck this class
 * @author robertdfrench
 *
 */
public abstract class KSFitnessFunction {
	
	/**
	 * Just a shitty additive fitness score
	 * @param organism
	 * @return fitness score
	 */
	public void evaluate(KSOrganism organism) {
		// If the fitnessScore is not positive infinity, then we've already evaluated it
		if (organism.fitnessScore == KSOrganism.UNEVALUATED_FITNESS) {
			organism.fitnessScore = calculateFitness(organism);
		}
	}
	
	protected abstract double calculateFitness(KSOrganism organism);
}
