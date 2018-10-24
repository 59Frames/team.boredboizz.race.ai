import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import entities.EntityType;

public class RaceAIFactory implements EntityFactory {
    @Spawns("wall")
    public Entity newWall(SpawnData data){
        return Entities.builder()
                .type(EntityType.WALL)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("target")
    public Entity newTarget(SpawnData data){
        return Entities.builder()
                .type(EntityType.TARGET)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("start")
    public Entity newStart(SpawnData data){
        return Entities.builder()
                .type(EntityType.START)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .build();
    }
}
