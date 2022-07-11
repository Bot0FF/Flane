package com.bot0ff.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.bot0ff.game.FlaneGame;

public class MenuState extends State{

    public static int MAX_SCORE = 0;

    private Texture background;
    private Texture btnTexture;
    private Texture copterTexture;
    private Texture groundMenu;
    private Sprite copterMenu;
    private Sprite btnStart;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter, fpScore;
    private BitmapFont font, fontScore;
    private GlyphLayout glyphLayout, glScore;
    private static float copterMove;
    private boolean isCopterMove = false;

    private Preferences preferences;

    public MenuState(StateManager stateManager) {
        super(stateManager);
        camera.setToOrtho(false, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        background = new Texture("background.png");
        groundMenu = new Texture("ground.png");
        copterTexture = new Texture("copter.png");
        copterMenu = new Sprite(copterTexture);
        copterMenu.setSize(120, 120);
        btnTexture = new Texture("btnStart.png");
        btnStart = new Sprite(btnTexture);
        btnStart.setSize(100, 100);
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Chunq.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fpScore = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 120;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.SKY;
        fpScore.size = 65;
        fpScore.borderWidth = 5;
        fpScore.borderColor = Color.BLACK;
        fpScore.color = Color.SKY;
        font = fontGenerator.generateFont(fontParameter);
        fontScore = fontGenerator.generateFont(fpScore);
        glyphLayout = new GlyphLayout();
        glScore = new GlyphLayout();
        glyphLayout.setText(font, "FLANE");

        preferences = Gdx.app.getPreferences("flane");
        MAX_SCORE = preferences.getInteger("MAX_SCORE", 0);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            stateManager.set(new PlayState(stateManager));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        glScore.setText(fontScore, "MAX SCORE " + MAX_SCORE);

        if (!isCopterMove) {
            copterMove -= dt * 20;
            if(copterMove < -30){
                isCopterMove = true;
            }
        } if(isCopterMove) {
            copterMove += dt * 20;
            if(copterMove > 60){
                isCopterMove = false;
            }
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.setProjectionMatrix(camera.combined);
        sb.draw(background, 0, 0, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        sb.draw(groundMenu, 0, 0, groundMenu.getWidth(), groundMenu.getHeight() - 20);
        fontScore.draw(sb, glScore, 10, camera.position.y + FlaneGame.HEIGHT / 2 - 10);
        font.draw(sb, glyphLayout, (FlaneGame.WIDTH / 2) - (glyphLayout.width / 2), (FlaneGame.HEIGHT / 2) + 100);
        sb.draw(copterMenu, FlaneGame.WIDTH / 2 - (copterMenu.getWidth() / 2 + 10), (FlaneGame.HEIGHT / 2 + FlaneGame.HEIGHT / 6) + copterMove, copterMenu.getWidth(), copterMenu.getHeight());
        sb.draw(btnStart, FlaneGame.WIDTH / 2 - btnStart.getWidth() / 2, FlaneGame.HEIGHT / 2 - (btnStart.getHeight() + 50), btnStart.getWidth(), btnStart.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        btnTexture.dispose();
        copterTexture.dispose();
        groundMenu.dispose();
        fontScore.dispose();
        font.dispose();
    }
}
