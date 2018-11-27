package dao;

import ai.algorithm.NeuralNetwork;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static _59frames.Ds._59utils.RandomString.random;
import static _59frames.Ds._59utils.Stringlify.stringlify;

public class NetworkWriter {

    private static final ArrayList<NeuralNetwork> winners = new ArrayList<>();

    public static void add(NeuralNetwork neuralNetwork) {
        winners.add(neuralNetwork);
    }

    public static void write(String generationId) {
        try {
            JSONObject root = new JSONObject();
            JSONArray winnerNetworks = new JSONArray();

            for (NeuralNetwork neuralNetwork : winners) {
                winnerNetworks.add(neuralNetwork.toJSON());
            }

            winners.clear();

            root.put("winners", winnerNetworks);

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(stringlify("Generation_{0}_{1}.json", generationId, random(12)))));
            String json = root.toJSONString();
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
