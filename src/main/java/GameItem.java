import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class GameItem {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Texture texture;
    private final int points;

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
    }
}
