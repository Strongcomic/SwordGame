import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class SwordGame extends ApplicationAdapter {
    private enum GameState { MENU, PLAYING, GAME_OVER }

    private static final float PLAYER_SIZE = 64f;
    private static final float ITEM_SIZE   = 32f;
    private static final long  ITEM_SPAWN_INTERVAL = 1_000_000_000L;
    private static final int   SCORE_TARGET  = 10;
    private static final float FALL_SPEED    = -200f;

    private SpriteBatch batch;
    private Texture     playerTexture;
    private Texture     itemTexture;
    private Texture     badlogicTexture;
    private Player      player;
    private ItemManager itemManager;
    private BitmapFont  font;

    private GameState   gameState = GameState.MENU;
    private long        lastItemSpawnTime;
    private int         score;

    @Override
    public void create() {
        batch           = new SpriteBatch();
        font            = new BitmapFont();
        playerTexture   = new Texture("player_sword.png");
        itemTexture     = new Texture("item.png");
        badlogicTexture = new Texture("badlogic.jpg");

        player = new Player(playerTexture, PLAYER_SIZE);
        player.setPosition(
            Gdx.graphics.getWidth() / 2f - PLAYER_SIZE / 2f,
            Gdx.graphics.getHeight() / 2f - PLAYER_SIZE / 2f
        );
        itemManager = new ItemManager(ITEM_SIZE);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        handleInput();
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        switch (gameState) {
            case MENU -> drawMenu();
            case PLAYING -> {
                player.draw(batch);
                itemManager.draw(batch);
                font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
            }
            case GAME_OVER -> drawGameOver();
        }
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            switch (gameState) {
                case MENU -> {
                    gameState = GameState.PLAYING;
                    spawnItem();
                }
                case PLAYING -> player.setPosition(
                    Gdx.input.getX() - PLAYER_SIZE / 2f,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - PLAYER_SIZE / 2f
                );
                case GAME_OVER -> resetGame();
            }
        }
    }

    private void update(float delta) {
        if (gameState == GameState.PLAYING) {
            if (TimeUtils.nanoTime() - lastItemSpawnTime > ITEM_SPAWN_INTERVAL) {
                spawnItem();
            }
            itemManager.updateAndClean(delta, Gdx.graphics.getHeight());
            score += itemManager.collectItems(player);

            if (score >= SCORE_TARGET) {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    private void spawnItem() {
        float x         = (float) Math.random() * (Gdx.graphics.getWidth()  - ITEM_SIZE);
        float y         = Gdx.graphics.getHeight();
        boolean badItem = Math.random() < 0.3;

        itemManager.addItem(
            new GameItem(
                new Vector2(x, y),
                new Vector2(0, FALL_SPEED),
                badItem ? badlogicTexture : itemTexture,
                badItem ? 2 : 1
            )
        );
        lastItemSpawnTime = TimeUtils.nanoTime();
    }

    private void drawMenu() {
        font.draw(batch,
            "Sword Game\nTouch to Start",
            Gdx.graphics.getWidth() / 2f - 50,
            Gdx.graphics.getHeight() / 2f
        );
    }

    private void drawGameOver() {
        font.draw(batch,
            "Game Over!\nScore: " + score + "\nTouch to Restart",
            Gdx.graphics.getWidth() / 2f - 50,
            Gdx.graphics.getHeight() / 2f
        );
    }

    private void resetGame() {
        score = 0;
        itemManager.clearItems();
        player.setPosition(
            Gdx.graphics.getWidth() / 2f - PLAYER_SIZE / 2f,
            Gdx.graphics.getHeight() / 2f - PLAYER_SIZE / 2f
        );
        gameState = GameState.MENU;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        playerTexture.dispose();
        itemTexture.dispose();
        badlogicTexture.dispose();
    }
}
