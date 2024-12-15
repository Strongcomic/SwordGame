import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public final class Player {
    private final Texture texture;
    private final float size;
    private final Vector2 position;

    public Player(Texture texture, float size) {
        this.texture = texture;
        this.size = size;
        this.position = new Vector2();
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }

    public Vector2 getTipPosition() {
        return new Vector2(position.x + size / 2f, position.y + size);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, size, size);
    }
}
