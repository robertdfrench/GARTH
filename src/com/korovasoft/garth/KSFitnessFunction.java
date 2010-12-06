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
public abstract class KSFitnessFunction {
	
	/**
	 * Detects if the organism has already been evaluated,
	 * If so, returns this score, if not, calls calculateFitness()
	 * and stores the result in the organism
	 * @param organism
	 * @return fitness score
	 */
	public void evaluate(KSOrganism organism) {
		// If the fitnessScore is not positive infinity, then we've already evaluated it
		if (organism.fitnessScore == KSOrganism.UNEVALUATED_FITNESS) {
			organism.fitnessScore = calculateFitness(organism);
		}
	}
	
	/**
	 * Evaluates the fitness of the organisms. Each fitness function must override
	 * this method. Scores should be returned according to the following rubrik:<br/>
	 * &bull; All fitness scores are non-negative<br/>
	 * &bull; Organisms with better fitness have lower scores<br/>
	 * &bull; A score of zero is reserved for perfect organisms</br>
	 * @param organism
	 * @return double fitness score
	 */
	protected abstract double calculateFitness(KSOrganism organism);
}
