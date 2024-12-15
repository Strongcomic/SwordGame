import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public final class GameItem {
    private final Vector2 position;
    private final Texture texture;
    private final int points;

    public GameItem(Vector2 position, Texture texture, int points) {
        this.position = position;
        this.texture = texture;
        this.points = points;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getPoints() {
        return points;
    }
}
