package com.bot0ff.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.bot0ff.game.FlaneGame;
import com.sun.org.apache.bcel.internal.Const;

public class GameOver extends State{

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private Texture background;

    public GameOver(StateManager stateManager) {
        super(stateManager);
        camera.setToOrtho(false, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        background = new Texture("background.png");

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Chunq.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 70;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.SKY;
        font = fontGenerator.generateFont(fontParameter);
        glyphLayout = new GlyphLayout();
        String gameOver = "GAME OVER";
        glyphLayout.setText(font, gameOver);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            stateManager.set(new MenuState(stateManager));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.setProjectionMatrix(camera.combined);
        sb.draw(background, 0, 0, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        font.draw(sb, glyphLayout, (FlaneGame.WIDTH / 2) - (glyphLayout.width / 2), (FlaneGame.HEIGHT / 2) - (glyphLayout.height / 2));
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
    }
}
