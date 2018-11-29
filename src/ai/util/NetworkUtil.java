package ai.util;

import _59frames.Ds._59utils.math.Silvester;
import ai.algorithm.NeuralNetwork;

import java.io.Serializable;

public class NetworkUtil
        implements Serializable, Cloneable {
    public static int[] createArrayWithRandomValues(int size) {
        if(size < 1){
            return null;
        }
        int[] arr = new int[size];
        for(int i = 0; i < size; i++){
            arr[i] = randomValue(-10, 10);
        }
        return arr;
    }

    public static int[][] createArrayWithRandomValues(int sizeX, int sizeY){
        if(sizeX < 1 || sizeY < 1){
            return null;
        }
        int[][] arr = new int[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++){
            arr[i] = createArrayWithRandomValues(sizeY);
        }
        return arr;
    }

    public static int randomValue(double lower_bound, double upper_bound){
        return Silvester.randomInt(lower_bound, upper_bound);
    }



    public static int calcChromosomeGeneLength(NeuralNetwork network) {
        int geneLength = 0;

        int cur;
        int prev;
        for (int i = network.NETWORK_LAYER_SIZES.length-1; i >= 0; i--) {
            cur = i;
            prev = i-1;
            if (prev >= 0)
                geneLength += (network.NETWORK_LAYER_SIZES[cur] * network.NETWORK_LAYER_SIZES[prev])+network.NETWORK_LAYER_SIZES[cur];
        }

        return geneLength;
    }
}
