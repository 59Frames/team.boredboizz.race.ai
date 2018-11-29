package components;

import ai.model.Chromosome;
import colr.extensions.schemes.MaterialColors;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.physics.RaycastResult;
import dao.NetworkWriter;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import static java.lang.Math.*;
import static com.almasb.fxgl.app.FXGL.*;

public class VehicleComponent
        extends SensorComponent {

    private final int SPEED_MODIFIER = 128;
    private final double ANGLE_MODIFIER = 2;

    private Chromosome chromosome;
    private double forwardSpeed;
    private double[] inputs;
    private boolean hasWon;
    private Line[] rayCasts;
    private double theoreticalFitness = 0.0;

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
        this.theoreticalFitness = 0.0;
    }

    @Override
    public void onUpdate(double tpf) {
        if (this.isAlive()){
            forwardSpeed = tpf * SPEED_MODIFIER;

            for (int i = 0; i < DIRECTIONS.length; i++) {
                final int index = i;
                inputs[index] = 640;
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

                this.theoreticalFitness += (inputs[index] / 10);
            }

            this.move();

            this.rotate(chromosome.feedForward(inputs)[0]*ANGLE_MODIFIER);
        }
    }

    public void move() {
        double x = entity.getX();
        double y = entity.getY();
        double radAngle = Math.toRadians(entity.getRotation());

        x += forwardSpeed * cos(radAngle);
        y += forwardSpeed * sin(radAngle);

        entity.setX(x);
        entity.setY(y);
    }

    private boolean isAlive() {
        return (this.chromosome.isAlive && !hasWon);
    }

    private double distanceBetweenPoints(Point2D position, Point2D p) {
        return position.distance(p);
    }

    private void kill(){
        this.chromosome.isAlive = false;
        setChromosomeFitness();
    }

    public void lost(){
        this.hasWon = false;
        kill();
    }

    public void won(){
        this.hasWon = true;
        NetworkWriter.write(this.chromosome.getNetwork());
        kill();
    }

    private void setChromosomeFitness() {
        this.chromosome.fitness(theoreticalFitness);
    }

    public void rotate(double angle) {
        if (this.chromosome.isAlive && !hasWon)
            entity.rotateBy(angle);
    }
}
