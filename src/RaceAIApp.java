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
import components.SpacecraftComponent;
import entities.EntityType;
import javafx.scene.input.KeyCode;
import stringlify.core.Stringlify;

import java.util.Arrays;

import static com.almasb.fxgl.app.DSLKt.*;

public class RaceAIApp
        extends GameApplication {

    private GeneticAlgorithm _algorithm;
    private Generation currentGeneration;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(50*32);
        settings.setHeight(50*32);
        settings.setTitle("Race AI");
        settings.setVersion("v0.0.1-ALPHA");

        _algorithm = GeneticAlgorithm.fromConfiguration(new AlgorithmConfiguration.Builder()
                .populationSize(200)
                .numbOfEliteChromosomes(8)
                .tournamentSelectionSize(32)
                .mutationRate(0.12)
                .build());
        currentGeneration = _algorithm.createGeneration();
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new RaceAIFactory());

        spawnEntities();
    }

    private void spawnEntities() {
        getGameWorld().setLevelFromMap("Parkour_3.json");
        respawnSpacecrafts();
    }

    private void respawnSpacecrafts(){
        Entity start = getGameWorld().getEntitiesByType(EntityType.START).get(0);
        for (int i = 0; i < currentGeneration.population().chromosomes().length; i++) {
            Chromosome chromosome = currentGeneration.population().chromosomes()[i];
            Entities.builder()
                    .type(EntityType.SPACECRAFT)
                    .at((float)start.getX()+(start.getWidth()/2), (float)start.getY()+(start.getHeight()/2))
                    .rotate(-180)
                    .viewFromNodeWithBBox(texture("spacecraft.png", 32, 32))
                    .with(new SpacecraftComponent(chromosome))
                    .with(new CollidableComponent(true))
                    .with(new KeepOnScreenComponent(true, true))
                    .buildAndAttach(getGameWorld());
        }
    }

    private void clearEntities() {
        getGameWorld().getEntitiesByType(EntityType.SPACECRAFT).forEach(Entity::removeFromWorld);
        getGameScene().clearGameViews();
    }

    private int haveWon = 0;

    @Override
    protected void onUpdate(double tpf) {
        if (!currentGeneration.isAlive()){
            if (haveWon > 0)
                System.out.println(haveWon + " have made it to the target.");

            clearEntities();
            System.out.println(Stringlify.stringlify("Best Fitness of dead generation: {0}", currentGeneration.population().chromosomes()[0].fitness()));
            currentGeneration = _algorithm.evolve(currentGeneration);
            spawnEntities();
            System.out.println("Generation evolved... current: " + currentGeneration.id());
            haveWon = 0;
        }
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.SPACECRAFT, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity spacecraft, Entity wall) {
                SpacecraftComponent component = spacecraft.getComponent(SpacecraftComponent.class);
                component.kill();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.SPACECRAFT, EntityType.TARGET) {
            @Override
            protected void onCollisionBegin(Entity spacecraft, Entity target) {
                SpacecraftComponent component = spacecraft.getComponent(SpacecraftComponent.class);
                component.won();
                haveWon++;
            }
        });
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A, "left", () -> {
            getGameWorld().getEntitiesByType(EntityType.SPACECRAFT).forEach(spacecraft -> {
                SpacecraftComponent component = spacecraft.getComponent(SpacecraftComponent.class);
                component.rotate(-1);
            });
        });
        onKey(KeyCode.D, "right", () -> {
            getGameWorld().getEntitiesByType(EntityType.SPACECRAFT).forEach(spacecraft -> {
                SpacecraftComponent component = spacecraft.getComponent(SpacecraftComponent.class);
                component.rotate(1);
            });
        });
        onKey(KeyCode.W, "up", () -> {
            getGameWorld().getEntitiesByType(EntityType.SPACECRAFT).forEach(spacecraft -> {
                SpacecraftComponent component = spacecraft.getComponent(SpacecraftComponent.class);
                component.move();
            });
        });
    }
}
