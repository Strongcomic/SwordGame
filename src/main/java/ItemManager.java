import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public final class ItemManager {
    private final float size;
    private final Array<GameItem> items = new Array<>();

    public ItemManager(float size) {
        this.size = size;
    }

    public void addItem(GameItem item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
    }

    public int collectItems(Player player) {
        int totalPoints = 0;
        for (int i = items.size - 1; i >= 0; i--) {
            var item = items.get(i);
            if (item.getPosition().dst(player.getTipPosition()) < size / 2f) {
                totalPoints += item.getPoints();
                items.removeIndex(i);
            }
        }
        return totalPoints;
    }

    public void draw(SpriteBatch batch) {
        for (var item : items) {
            batch.draw(item.getTexture(), item.getPosition().x, item.getPosition().y, size, size);
        }
    }
}
