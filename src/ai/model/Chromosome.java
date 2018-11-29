package ai.model;

import ai.algorithm.NeuralNetwork;
import ai.util.NetworkUtil;

import java.io.Serializable;
import java.util.Arrays;

public class Chromosome
        implements Serializable, Cloneable {
    public static final int MAX = 10, MIN = -10;
    public boolean isAlive;
    private boolean shouldChangeFitness = true;
    private double fitness = 0;
    private int[] genes;
    private int[] networkLayerSizes;
    private NeuralNetwork network;

    public Chromosome(int length, int[] networkLayerSizes) {
        this.isAlive = true;
        this.genes = new int[length];
        this.networkLayerSizes = networkLayerSizes;
        this.network = new NeuralNetwork(networkLayerSizes);
    }

    public Chromosome initializeChromosome() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = NetworkUtil.randomValue(MIN, MAX);
        }
        network = new NeuralNetwork(networkLayerSizes).withWeightsAndBias(genes);
        return this;
    }

    public void generateNeuralNetwork() {
        network = new NeuralNetwork(networkLayerSizes).withWeightsAndBias(genes);
    }

    public double[] feedForward(double[] input){
        if (input.length != networkLayerSizes[0])
            return input;
        return this.network.feedForward(input);
    }

    public int[] genes() {
        shouldChangeFitness = true;
        return genes;
    }

    public double fitness() {
        return fitness;
    }

    public void fitness(double fitness) {
        this.fitness = fitness;
    }

    public NeuralNetwork getNetwork(){
        return this.network;
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.genes);
    }
}
