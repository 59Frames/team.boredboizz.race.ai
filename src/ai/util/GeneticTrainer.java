package ai.util;

import ai.model.Chromosome;
import ai.model.Generation;
import ai.algorithm.GeneticAlgorithm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static _59frames.Ds._59utils.Stringlify.stringlify;

public class GeneticTrainer
        implements Serializable, Cloneable {
    private final Logger LOGGER;

    private final int counter;
    private final GeneticAlgorithm algorithm;

    private boolean isEvolving;

    public GeneticTrainer(GeneticAlgorithm algorithm, int counter) {
        LOGGER = Logger.getLogger(this.getClass().getName());
        LOGGER.setLevel(Level.FINEST);
        this.counter = counter;
        this.algorithm = algorithm;
        this.isEvolving = false;
    }

    public Map<String, Object> startTraining(){
        isEvolving = true;
        Generation generation = algorithm.createGeneration();

        Generation[] generations = new Generation[this.counter];

        double bestAvFi = generation.averageFitness();
        int bestGenId = 0;

        int survivedRounds = 0;
        for (int i = 0; i < counter; i++) {
            generations[i] = generation;
            if (i != 0) generation = algorithm.evolve(generation);
            while (generation.isAlive()){
                survivedRounds++;
                for (Chromosome c : generation.population().chromosomes()){
                    if (c.isAlive)
                        c.isAlive = Math.random() > 0.001;
                }
            }

            double avFi;
            if ((avFi = generation.averageFitness()) > bestAvFi){
                bestAvFi = avFi;
                bestGenId = generation.id();
            }


            System.out.println(stringlify("Generation {0} survived {1} rounds. Average fitness: {2}", generation.id(), survivedRounds, avFi));
            survivedRounds = 0;
        }
        LOGGER.info(stringlify("Latest generation: {0}", generation));
        LOGGER.info(stringlify("Best generation: {0}. Average Fitness: {1}", bestGenId, bestAvFi));

        Map<String, Object> map = new HashMap<>();
        map.put("generations", generations);
        map.put("best", generations[bestGenId+1]);

        return map;
    }
}
