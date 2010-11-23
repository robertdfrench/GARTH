package com.korovasoft.ga.distributed;

import java.util.Random;

public class KSOrganism {
	public double[] genome;
	public int genomeLength;
	public double fitnessScore;
	
	/**
	 * Special value used to denote that we have not yet calculated
	 * the fitness for an organism
	 */
	public static final double UNEVALUATED_FITNESS = -1;
	
	private static Random randomNumberGenerator = new Random(System.currentTimeMillis());
	private static double fitness_normalizer = Double.POSITIVE_INFINITY;
	private static final double MAX_DELTA = 0.5;
	

	public static void setFitnessNormalizer(double newFN) {
		fitness_normalizer = newFN;
	}
	/**
	 * Blank constructor
	 * Constructs a new organism whose genome contents are not
	 * guaranteed to be anything in particular. 
	 * @param genomeLength
	 */
	public KSOrganism(int genomeLength) {
		// New Blank Organism
		allocateAndInitialize(genomeLength);
	}
	
	/**
	 * Clone Constructor
	 * Makes a mutant clone of parent by randomly selecting one gene
	 * and replacing it with a random double.
	 * @param parent
	 */
	public KSOrganism(KSOrganism parent) {
		allocateAndInitialize(parent.genomeLength);
		System.arraycopy(parent.genome, 0, genome, 0, parent.genomeLength); 
		int mutantGeneIndex = randomNumberGenerator.nextInt(parent.genomeLength);
		this.genome[mutantGeneIndex] = calculateMutantGene(parent.genome[mutantGeneIndex], parent.fitnessScore);
	}
	
	/**
	 * It's the job of the fitness function to map genes in [0,1] to the appropriate alphabet for each gene
	 * TODO: Document the above somewhere more prominent
	 * @param gene_value
	 * @param fitness_value
	 * @return
	 */
	private double calculateMutantGene(double gene_value, double fitness_value) {
		double delta = (fitness_value/fitness_normalizer)*MAX_DELTA + Double.MIN_VALUE;
		double nextRandom = randomNumberGenerator.nextDouble();
		double shiftedRandom = (nextRandom - 0.5);
		double scaledRandom = shiftedRandom*2*delta; 
		double delta0 = scaledRandom; // delta0 is an element of [-delta,delta]
		
		double candidateGene = gene_value + delta0;
		if (candidateGene < 0) {
			return 0;
		} else if (candidateGene > 1) {
			return 1;
		} else { 
			return candidateGene;
		}
	}
	
	/**
	 * Origin / Random Constructor
	 * @param genomeLength The number of genes
	 * @param isRandom 	True  - All genes are Random doubles
	 * 					False - All genes are 0.0 
	 */
	public KSOrganism(int genomeLength, boolean isRandom) {
		allocateAndInitialize(genomeLength);
		for (int i = 0; i < genomeLength; i++) {
			this.genome[i] = (isRandom) ? randomNumberGenerator.nextDouble() : 0;
		}
	}
	
	/**
	 * Mallocs the genome, sets genomeLength, adds fitness score
	 * @param genomeLength
	 */
	private void allocateAndInitialize(int genomeLength) {
		this.genome = new double[genomeLength];
		this.genomeLength = genomeLength;
		this.fitnessScore = KSOrganism.UNEVALUATED_FITNESS;
	}
}
