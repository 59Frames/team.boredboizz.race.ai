package components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

class SensorComponent extends Component {
    final Direction[] DIRECTIONS;

    SensorComponent() {
        this.DIRECTIONS = new Direction[5];

        DIRECTIONS[0] = Direction.LEFT_SIDE;
        DIRECTIONS[1] = Direction.LEFT_FRONT;
        DIRECTIONS[2] = Direction.FRONT;
        DIRECTIONS[3] = Direction.RIGHT_FRONT;
        DIRECTIONS[4] = Direction.RIGHT_SIDE;
    }

    Point2D calculateEndPoint(Direction direction) {
        double x = entity.getX();
        double y = entity.getY();
        double radAngle = Math.toRadians(entity.getRotation()+direction.angleModifier);
        x += 4096 * cos(radAngle);
        y += 4096 * sin(radAngle);
        return new Point2D(x, y);
    }

    public enum Direction {
        FRONT(0),
        LEFT_SIDE(-90),
        RIGHT_SIDE(90),
        LEFT_FRONT(-45),
        RIGHT_FRONT(45);

        private final int angleModifier;

        Direction(int val){
            this.angleModifier = val;
        }
    }
}
