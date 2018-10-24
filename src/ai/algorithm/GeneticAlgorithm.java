package ai.algorithm;

import ai.model.Chromosome;
import ai.model.Generation;
import ai.model.Population;
import java.io.Serializable;

import static ai.util.NetworkUtil.randomValue;

public class GeneticAlgorithm
        implements Serializable, Cloneable {
    public final int POPULATION_SIZE;
    public final int CHROMOSOME_GENE_LENGTH;
    public final double MUTATION_RATE;
    public final int NUMB_OF_ELITE_CHROMOSOMES;
    public final int TOURNAMENT_SELECTION_SIZE;
    public final NeuralNetwork NETWORK;

    private GeneticAlgorithm(int POPULATION_SIZE,
                             int CHROMOSOME_GENE_LENGTH,
                             double MUTATION_RATE,
                             int NUMB_OF_ELITE_CHROMOSOMES,
                             int TOURNAMENT_SELECTION_SIZE,
                             NeuralNetwork NETWORK) {
        this.POPULATION_SIZE = POPULATION_SIZE;
        this.CHROMOSOME_GENE_LENGTH = CHROMOSOME_GENE_LENGTH;
        this.MUTATION_RATE = MUTATION_RATE;
        this.NUMB_OF_ELITE_CHROMOSOMES = NUMB_OF_ELITE_CHROMOSOMES;
        this.TOURNAMENT_SELECTION_SIZE = TOURNAMENT_SELECTION_SIZE;
        this.NETWORK = NETWORK;
    }

    public static GeneticAlgorithm fromConfiguration(AlgorithmConfiguration algorithmConfiguration){
        return new GeneticAlgorithm(algorithmConfiguration.POPULATION_SIZE, algorithmConfiguration.CHROMOSOME_GENE_LENGTH, algorithmConfiguration.MUTATION_RATE, algorithmConfiguration.NUMBER_OF_ELITE_CHROMOSOMES, algorithmConfiguration.TOURNAMENT_SELECTION_SIZE, algorithmConfiguration.NETWORK);
    }

    public Population createPopulation(){
        return new Population(POPULATION_SIZE, NETWORK.NETWORK_LAYER_SIZES).initializePopulation(CHROMOSOME_GENE_LENGTH);
    }

    public Generation evolve(Generation generation){
        return Generation.create(mutatePopulation(crossoverPopulation(generation.population())));
    }

    public Generation createGeneration(){
        return Generation.create(createPopulation());
    }

    private Population crossoverPopulation(Population population){
        Population crossoverPopulation = new Population(POPULATION_SIZE, NETWORK.NETWORK_LAYER_SIZES);
        for (int i = 0; i < NUMB_OF_ELITE_CHROMOSOMES; i++) {
            crossoverPopulation.chromosomes()[i] = population.chromosomes()[i];
        }
        for (int i = NUMB_OF_ELITE_CHROMOSOMES; i < population.chromosomes().length; i++) {
            Chromosome chromosome1 = selectTournamentPopulation(population).chromosomes()[0];
            Chromosome chromosome2 = selectTournamentPopulation(population).chromosomes()[0];
            crossoverPopulation.chromosomes()[i] = crossoverChromosome(chromosome1, chromosome2);
        }
        crossoverPopulation.sortChromosomesByFitness();
        return crossoverPopulation;
    }

    private Population mutatePopulation(Population population){
        Population mutatePopulation = new Population(POPULATION_SIZE, NETWORK.NETWORK_LAYER_SIZES);
        for (int i = 0; i < NUMB_OF_ELITE_CHROMOSOMES; i++) {
            mutatePopulation.chromosomes()[i] = population.chromosomes()[i];
        }

        for (int i = NUMB_OF_ELITE_CHROMOSOMES; i < population.chromosomes().length; i++) {
            mutatePopulation.chromosomes()[i] = mutateChromosome(population.chromosomes()[i]);
        }

        mutatePopulation.sortChromosomesByFitness();
        return mutatePopulation;
    }

    private Chromosome crossoverChromosome(Chromosome chromosome1, Chromosome chromosome2){
        Chromosome crossoverChromosome = new Chromosome(CHROMOSOME_GENE_LENGTH, NETWORK.NETWORK_LAYER_SIZES);
        for (int i = 0; i < chromosome1.genes().length; i++) {
            crossoverChromosome.genes()[i] = Math.random() < 0.5
                    ? chromosome1.genes()[i]
                    : chromosome2.genes()[i];
        }
        crossoverChromosome.generateNeuralNetwork();
        return crossoverChromosome;
    }

    private Chromosome mutateChromosome(Chromosome chromosome){
        Chromosome mutateChromosome = new Chromosome(CHROMOSOME_GENE_LENGTH, NETWORK.NETWORK_LAYER_SIZES);

        for (int i = 0; i < chromosome.genes().length; i++) {
            if (Math.random() < MUTATION_RATE){
                mutateChromosome.genes()[i] = randomValue(Chromosome.MIN, Chromosome.MAX);
            } else mutateChromosome.genes()[i] = chromosome.genes()[i];
        }

        mutateChromosome.generateNeuralNetwork();
        return mutateChromosome;
    }

    private Population selectTournamentPopulation(Population population){
        Population tournamentPopulation = new Population(TOURNAMENT_SELECTION_SIZE, NETWORK.NETWORK_LAYER_SIZES);

        for (int i = 0; i < TOURNAMENT_SELECTION_SIZE; i++) {
            tournamentPopulation.chromosomes()[i] = population.chromosomes()[(int)(Math.random()*population.chromosomes().length)];
        }

        tournamentPopulation.sortChromosomesByFitness();
        return tournamentPopulation;
    }
}