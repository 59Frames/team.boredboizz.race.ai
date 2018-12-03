import ai.model.Chromosome;
import ai.model.Generation;
import ai.algorithm.AlgorithmConfiguration;
import ai.algorithm.GeneticAlgorithm;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import components.VehicleComponent;
import dao.NetworkWriter;
import entities.EntityType;
import javafx.scene.input.KeyCode;

import static _59frames.Ds._59utils.Stringlify.stringlify;
import static com.almasb.fxgl.app.DSLKt.*;

public class RaceAIApp
        extends GameApplication {

    private static GeneticAlgorithm _algorithm;
    private static Generation currentGeneration;

    public static void main(String[] args) {
        setup();
        launch(args);
    }

    private static void setup() {
        _algorithm = GeneticAlgorithm.fromConfiguration(new AlgorithmConfiguration.Builder()
                .populationSize(300)
                .numbOfEliteChromosomes(10)
                .tournamentSelectionSize(50)
                .mutationRate(0.12)
                .network(GameSetup.getNeuralNetwork())
                .isBasedOnNetwork(false)
                .build());

        currentGeneration = _algorithm.createGeneration();
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(50*32);
        settings.setHeight(50*32);
        settings.setTitle("Race AI");
        settings.setVersion("v0.0.1-ALPHA");
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new RaceAIFactory());

        spawnEntities();
    }

    private void spawnEntities() {
        getGameWorld().setLevelFromMap("Parkour_4.json");
        respawnSpacecrafts();
    }

    private void respawnSpacecrafts(){
        Entity start = getGameWorld().getEntitiesByType(EntityType.START).get(0);
        for (int i = 0; i < currentGeneration.population().chromosomes().length; i++) {
            Chromosome chromosome = currentGeneration.population().chromosomes()[i];
            Entities.builder()
                    .type(EntityType.VEHICLE)
                    .at((float)start.getX()+(start.getWidth()/2), (float)start.getY()+(start.getHeight()/2))
                    .rotate(90)
                    .viewFromNodeWithBBox(texture("spacecraft.png", 32, 32))
                    .with(new VehicleComponent(chromosome))
                    .with(new CollidableComponent(true))
                    .with(new KeepOnScreenComponent(true, true))
                    .buildAndAttach(getGameWorld());
        }
    }

    private void clearEntities() {
        getGameWorld().getEntitiesByType(EntityType.VEHICLE).forEach(Entity::removeFromWorld);
        getGameScene().clearGameViews();
    }

    private int haveWon = 0;

    @Override
    protected void onUpdate(double tpf) {
        if (!currentGeneration.isAlive())
            reset();
    }

    private void reset() {
        if (haveWon > 0) {
            System.out.println(haveWon + " have made it to the target.");
        }

        clearEntities();
        currentGeneration.population().sortChromosomesByFitness();
        System.out.println(stringlify("Best Fitness of dead generation: {0}", currentGeneration.population().chromosomes()[0].fitness()));
        currentGeneration = _algorithm.evolve(currentGeneration);
        spawnEntities();
        System.out.println("Generation evolved... current: " + currentGeneration.id());
        haveWon = 0;
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.VEHICLE, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity spacecraft, Entity wall) {
                VehicleComponent component = spacecraft.getComponent(VehicleComponent.class);
                component.lost();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.VEHICLE, EntityType.TARGET) {
            @Override
            protected void onCollisionBegin(Entity spacecraft, Entity target) {
                VehicleComponent component = spacecraft.getComponent(VehicleComponent.class);
                component.won();
                haveWon++;
            }
        });
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.R, "reset", this::reset);
    }
}
