package ai.model;

import _59frames.Ds._59utils.math.Silvester;
import ai.algorithm.NeuralNetwork;

import java.io.Serializable;
import java.util.Arrays;

public class Population
        implements Serializable, Cloneable {
    private Chromosome[] chromosomes;
    private int[] networkLayerSizes;

    public Population(int length, int[] networkLayerSizes) {
        this.chromosomes = new Chromosome[length];
        this.networkLayerSizes = networkLayerSizes;
    }

    public Population initializePopulation(int chromosomeGeneLength) {
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome(chromosomeGeneLength, networkLayerSizes).initializeChromosome();
        }
        sortChromosomesByFitness();
        return this;
    }

    public Population withNetwork(NeuralNetwork network) {
        for (Chromosome chromosome : chromosomes) {
            chromosome.setNetwork(network);
        }
        sortChromosomesByFitness();
        return this;
    }

    public boolean isAlive(){
        for(Chromosome c : chromosomes){
            if (c.isAlive)
                return true;
        }
        return false;
    }

    public Chromosome[] chromosomes(){
        return chromosomes;
    }

    public void sortChromosomesByFitness(){
        Arrays.sort(chromosomes, ((t1, t2) -> {
            int flag = 0;

            if (t1.fitness() > t2.fitness()) flag = -1;
            else if (t1.fitness() < t2.fitness()) flag = 1;

            return flag;
        }));
    }

    public double averageFitness() {
        double[] arr = new double[chromosomes.length];
        for (int i = 0; i < chromosomes.length; i++) {
            arr[i] = chromosomes[i].fitness();
        }
        return Silvester.average(arr);
    }
}
