import ai.algorithm.NeuralNetwork;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class GameSetup {
    private GameSetup() {
    }

    public static NeuralNetwork getNeuralNetwork() {
        Object[] options = {"New", "Load"};

        int selected = JOptionPane.showOptionDialog(null,
                "Choose an option",
                "New",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        System.out.println(selected);

        switch (selected) {
            case 0:
                return new NeuralNetwork(5, 4, 1);

            case 1:
                File f = getFile();
                if (f != null) {
                    try {
                        JSONObject root = (JSONObject) new JSONParser().parse(new FileReader(f));
                        return NeuralNetwork.fromJSON(root);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                } else
                    return new NeuralNetwork(5, 4, 1);

            default:
                return new NeuralNetwork(5, 4, 1);
        }
    }

    private static File getFile() {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("assets"));
        fileChooser.setDialogTitle("Choose Network");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON", "json");
        fileChooser.setFileFilter(filter);

        fileChooser.showOpenDialog(null);

        return fileChooser.getSelectedFile();
    }
}
