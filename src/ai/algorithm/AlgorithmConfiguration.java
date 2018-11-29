package ai.algorithm;

import java.io.Serializable;

import static ai.util.NetworkUtil.calcChromosomeGeneLength;

public class AlgorithmConfiguration
        implements Serializable, Cloneable {
    public final NeuralNetwork NETWORK;
    public final int POPULATION_SIZE;
    public final int NUMBER_OF_ELITE_CHROMOSOMES;
    public final int TOURNAMENT_SELECTION_SIZE;
    public final int CHROMOSOME_GENE_LENGTH;
    public final double MUTATION_RATE;
    public final boolean IS_BASED_ON_NETWORK;

    private AlgorithmConfiguration(NeuralNetwork NETWORK,
                                   int POPULATION_SIZE,
                                   int NUMBER_OF_ELITE_CHROMOSOMES,
                                   int TOURNAMENT_SELECTION_SIZE,
                                   double MUTATION_RATE, boolean is_based_on_network) {
        this.NETWORK = NETWORK;
        this.POPULATION_SIZE = POPULATION_SIZE;
        this.NUMBER_OF_ELITE_CHROMOSOMES = NUMBER_OF_ELITE_CHROMOSOMES;
        this.TOURNAMENT_SELECTION_SIZE = TOURNAMENT_SELECTION_SIZE;
        this.CHROMOSOME_GENE_LENGTH = calcChromosomeGeneLength(NETWORK);
        this.MUTATION_RATE = MUTATION_RATE;
        this.IS_BASED_ON_NETWORK = is_based_on_network;
    }

    public static class Builder {
        private NeuralNetwork network;
        private int populationSize;
        private int numbOfEliteChromosomes;
        private int tournamentSelectionSize;
        private double mutationRate;
        private boolean is_based_on_network;

        public Builder(){
            this.network = new NeuralNetwork(5,4,1);
            this.populationSize = 50;
            this.numbOfEliteChromosomes = 2;
            this.tournamentSelectionSize = 8;
            this.mutationRate = 0.32;
            this.is_based_on_network = false;
        }

        public Builder network(NeuralNetwork neuralNetwork){
            this.network = neuralNetwork;
            return this;
        }

        public Builder isBasedOnNetwork(boolean value) {
            this.is_based_on_network = value;
            return this;
        }

        public Builder populationSize(int size){
            this.populationSize = size;
            return this;
        }

        public Builder numbOfEliteChromosomes(int numb){
            this.numbOfEliteChromosomes = numb;
            return this;
        }

        public Builder tournamentSelectionSize(int size){
            this.tournamentSelectionSize = size;
            return this;
        }

        public Builder mutationRate(double rate){
            this.mutationRate = rate;
            return this;
        }

        public AlgorithmConfiguration build(){
            return new AlgorithmConfiguration(network, populationSize, numbOfEliteChromosomes, tournamentSelectionSize, mutationRate, is_based_on_network);
        }
    }
}
