package ai.algorithm;

import static ai.util.NetworkUtil.*;

import _59frames.Ds._59utils.math.Silvester;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    private int[] chromosomeGenes;

    public NeuralNetwork(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = this.NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = this.NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = this.NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];

        this.output = new double[NETWORK_SIZE][];
        this.weights = new int[NETWORK_SIZE][][];
        this.bias = new int[NETWORK_SIZE][];

        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[this.NETWORK_LAYER_SIZES[i]];

            this.bias[i] = createArrayWithZero(this.NETWORK_LAYER_SIZES[i]);

            if (i > 0)
                weights[i] = createArrayWithZero(this.NETWORK_LAYER_SIZES[i], this.NETWORK_LAYER_SIZES[i - 1]);
        }
    }

    public NeuralNetwork withWeightsAndBias(int[] genes) {
        if (genes.length == calcChromosomeGeneLength(this)) {
            this.chromosomeGenes = genes;
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
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];

                output[layer][neuron] = Silvester.hyperbolicTangent(sum);
            }
        }

        return output[NETWORK_SIZE - 1];
    }

    @SuppressWarnings("all")
    public String toJSON() {
        JSONObject root = new JSONObject();

        JSONObject config = new JSONObject();

        config.put("input_size", INPUT_SIZE);
        config.put("network_size", NETWORK_SIZE);
        config.put("output_size", OUTPUT_SIZE);

        JSONArray layerSizes = new JSONArray();

        for (int i : NETWORK_LAYER_SIZES) {
            layerSizes.add(i);
        }

        JSONArray genes = new JSONArray();

        for (int i : chromosomeGenes) {
            genes.add(i);
        }

        root.put("config", config);
        root.put("layerSizes", layerSizes);
        root.put("chromosomeGenes", genes);

        return root.toJSONString();
    }

}
