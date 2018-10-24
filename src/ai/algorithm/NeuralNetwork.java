package ai.algorithm;

import static ai.util.NetworkUtil.*;
import mathematika.core.Mathematika;

import java.io.Serializable;
import java.util.Arrays;

public class NeuralNetwork
        implements Serializable, Cloneable {

    public final int[] NETWORK_LAYER_SIZES;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;
    
    private double[][] output;
    private int[][][] weights;
    private int[][] bias;

    public NeuralNetwork(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = this.NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = this.NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = this.NETWORK_LAYER_SIZES[NETWORK_SIZE-1];

        this.output = new double[NETWORK_SIZE][];
        this.weights = new int[NETWORK_SIZE][][];
        this.bias = new int[NETWORK_SIZE][];

        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[this.NETWORK_LAYER_SIZES[i]];

            this.bias[i] = createArrayWithZero(this.NETWORK_LAYER_SIZES[i]);

            if (i > 0)
                weights[i] = createArrayWithZero(this.NETWORK_LAYER_SIZES[i], this.NETWORK_LAYER_SIZES[i-1]);
        }
    }

    public NeuralNetwork withWeightsAndBias(int[] genes){

        if (genes.length == calcChromosomeGeneLength(this)){
            for (int layer = 1; layer < NETWORK_SIZE; layer++) {
                for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                    for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                        weights[layer][neuron][prevNeuron] = genes[0];
                        genes = Arrays.copyOfRange(genes, 1, genes.length);
                    }
                    bias[layer][neuron] = genes[0];
                    genes = Arrays.copyOfRange(genes, 1, genes.length);
                }
            }
        }

        return this;
    }

    public double[] feedForward(double... input) {
        if (input.length != this.INPUT_SIZE)
            return null;

        this.output[0] = input;
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = bias[layer][neuron];
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++)
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];

                output[layer][neuron] = Mathematika.hyperbolicTangent(sum);
            }
        }

        return output[NETWORK_SIZE-1];
    }
}
