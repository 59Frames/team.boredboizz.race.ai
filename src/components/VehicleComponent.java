package components;

import ai.model.Chromosome;
import colr.extensions.schemes.MaterialColors;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.physics.RaycastResult;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import mathematika.core.Mathematika;

import static java.lang.Math.*;
import static com.almasb.fxgl.app.FXGL.*;

public class VehicleComponent
        extends SensorComponent {

    private Chromosome chromosome;
    private double forwardSpeed;
    private double[] inputs;
    private boolean hasWon;
    private Line[] rayCasts;
    private long startTime;
    private long endTime;
    private long survivedTime;

    public VehicleComponent(Chromosome chromosome) {
        this.forwardSpeed = 1;
        this.chromosome = chromosome;
        this.chromosome.isAlive = true;
        this.hasWon = false;
        this.inputs = new double[chromosome.getNetwork().NETWORK_LAYER_SIZES[0]];
        this.rayCasts = new Line[chromosome.getNetwork().NETWORK_LAYER_SIZES[0]];

        for (int i = 0; i < rayCasts.length; i++) {
            rayCasts[i] = new Line();
            rayCasts[i].setStroke(MaterialColors.TRANSPARENT.toJFXColor());
            rayCasts[i].setStrokeWidth(1);
            getGameScene().addGameView(new EntityView(rayCasts[i]));
        }
    }

    @Override
    public void onAdded() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onUpdate(double tpf) {
        forwardSpeed = tpf * 512;

        for (int i = 0; i < DIRECTIONS.length; i++) {
            final int index = i;
            Point2D pos = calculateEndPoint(DIRECTIONS[i]);
            rayCasts[index].setStartY(entity.getY());
            rayCasts[index].setStartX(entity.getX());
            rayCasts[index].setEndX(pos.getX());
            rayCasts[index].setEndY(pos.getY());

            RaycastResult result = getPhysicsWorld().raycast(new Point2D(entity.getX(), entity.getY()), new Point2D(pos.getX(), pos.getY()));
            result.getPoint().ifPresent(p -> {
                rayCasts[index].setEndX(p.getX());
                rayCasts[index].setEndY(p.getY());
                inputs[index] = distanceBetweenPoints(entity.getPosition(), p);
            });
        }

        this.move();

        this.rotate(chromosome.feedForward(inputs)[0]*12);
    }

    private double distanceBetweenPoints(Point2D position, Point2D p) {
        return position.distance(p);
    }

    public void move() {
        if (this.chromosome.isAlive && !hasWon){
            double x = entity.getX();
            double y = entity.getY();
            double radAngle = Math.toRadians(entity.getRotation());

            x += forwardSpeed * cos(radAngle);
            y += forwardSpeed * sin(radAngle);

            entity.setX(x);
            entity.setY(y);
        }
    }

    private void end(){
        this.endTime = System.currentTimeMillis();
        this.survivedTime = endTime - startTime;
    }

    public void kill(){
        this.chromosome.isAlive = false;
        end();
        setChromosomeFitness();
    }

    public void won(){
        this.hasWon = true;
        this.chromosome.isAlive = false;
        end();
        setChromosomeFitness();
    }

    private void setChromosomeFitness() {
        double fitness;

        fitness = hasWon
                ? ((Double.MAX_VALUE - survivedTime))
                : survivedTime;

        this.chromosome.fitness(fitness);
    }

    public void rotate(double angle) {
        if (this.chromosome.isAlive && !hasWon)
            entity.rotateBy(angle);
    }
}
