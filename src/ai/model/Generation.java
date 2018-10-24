package ai.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Generation
        implements Serializable, Cloneable, Comparable<Generation> {
    private static int id_counter = 0;

    private final int id;
    private final Population population;

    private Generation(int id, Population population) {
        this.id = id;
        this.population = population;
    }

    public static Generation create(Population population){
        Generation g = new Generation(id_counter, population);
        id_counter++;
        return g;
    }

    public int id(){
        return id;
    }

    public Population population(){
        return population;
    }

    public double averageFitness(){
        return population.averageFitness();
    }

    @Override
    public String toString() {
        return "Generation #"+this.id+" | Fittest chromosome fitness: "+population.chromosomes()[0].fitness();
    }

    public boolean isAlive(){
        return this.population.isAlive();
    }

    @Override
    public int compareTo(@NotNull Generation otherGen) {
        int res = 0;
        double tAv = this.averageFitness();
        double oAv = otherGen.averageFitness();

        if (tAv > oAv) res = 1;
        else if (tAv < oAv) res = -1;

        return res;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
