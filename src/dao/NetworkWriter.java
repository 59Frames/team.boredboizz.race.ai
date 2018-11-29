package dao;

import ai.algorithm.NeuralNetwork;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static _59frames.Ds._59utils.RandomString.random;
import static _59frames.Ds._59utils.Stringlify.stringlify;

public class NetworkWriter {

    public static void write(NeuralNetwork neuralNetwork) {
        try {
            JSONObject root = new JSONObject();

            root.put("network", neuralNetwork.toJSON());

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(stringlify("Network_{0}.json", random(12)))));
            String json = root.toJSONString();
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
