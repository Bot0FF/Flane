package com.bot0ff.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bot0ff.game.FlaneGame;
import com.bot0ff.game.sprites.Copter;
import com.bot0ff.game.sprites.Cloud;
import com.bot0ff.game.sprites.GoldCloud;

public class PlayState extends State{

    private static int SCORE = 0;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;

    private static final int CLOUD_SPACING = 125;
    private static final int CLOUD_COUNT = 4;
    private static final int GROUND_Y_OFFSET = 0;

    private Copter copter;
    private GoldCloud goldCloud;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Sound collidesSound;
    private long idCollidesSound;
    private float timer;
    private static float rotation;

    private boolean isRotation = false;

    private Array<Cloud> clouds;

    private Preferences preferences;

    public PlayState(StateManager stateManager) {
        super(stateManager);

        copter = new Copter(90, 500);
        goldCloud = new GoldCloud(600, 270);
        camera.setToOrtho(false, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        bg = new Texture("background.png");
        ground = new Texture("ground.png");

        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);
        collidesSound = Gdx.audio.newSound(Gdx.files.internal("collides.mp3"));
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Chunq.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 50;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.SKY;
        font = fontGenerator.generateFont(fontParameter);

        preferences = Gdx.app.getPreferences("flane");

        clouds = new Array<>();

        for (int i = 0; i < CLOUD_COUNT; i++) {
            clouds.add(new Cloud((i + 2) * (CLOUD_SPACING + Cloud.CLOUD_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            copter.jump();
        }
    }

    public void setMaxScore(int score){
        preferences.putInteger("MAX_SCORE", score);
        preferences.flush();
    }

    public void collidesSound(){
        idCollidesSound = collidesSound.play();
        collidesSound.setVolume(idCollidesSound, 0.5f);
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        copter.update(dt);
        goldCloud.update(dt);

        camera.position.x = copter.getPosition().x + 150;

        if (!isRotation) {
            rotation += dt * 10;
            if(rotation > 20){
                isRotation = true;
            }
        } if(isRotation) {
            rotation -= dt * 10;
            if(rotation < -10){
                isRotation = false;
            }
        }

        timer += dt * 10;
        if(timer > 70) {
            repositionGoldCloud();
        }
        for (int i = 0; i < clouds.size; i++){
            Cloud cloud = clouds.get(i);

            if (camera.position.x - (camera.viewportWidth / 2 + 70) > cloud.getPosCloudOne().x + cloud.getCloudOne().getWidth()){
                cloud.reposition(cloud.getPosCloudOne().x + ((Cloud.CLOUD_WIDTH + CLOUD_SPACING) * CLOUD_COUNT));
            }

            if(goldCloud.collides(copter.getBounds())){
                repositionGoldCloud();
                timer = 0;
                SCORE++;
            }

            if(cloud.collides(copter.getBounds())){
                collidesSound();
                if(MenuState.MAX_SCORE < SCORE) setMaxScore(SCORE);
                SCORE = 0;
                stateManager.push(new GameOver(stateManager));
            }
            if(copter.getPosition().y < 10 || copter.getPosition().y > FlaneGame.HEIGHT - copter.getCopter().getHeight()){
                if(MenuState.MAX_SCORE < SCORE) setMaxScore(SCORE);
                SCORE = 0;
                stateManager.push(new GameOver(stateManager));
            }
        }
        camera.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0, FlaneGame.WIDTH, FlaneGame.HEIGHT);
        sb.draw(copter.getCopter(), copter.getPosition().x, copter.getPosition().y, copter.getCopter().getWidth() / 2, copter.getCopter().getHeight() / 2, copter.getCopter().getWidth(), copter.getCopter().getHeight(), 1f, 1f, rotation);
        for (Cloud cloud : clouds) {
            sb.draw(cloud.getCloudOne(), cloud.getPosCloudOne().x, cloud.getPosCloudOne().y, cloud.getCloudOne().getWidth(), cloud.getCloudOne().getHeight() - 20);
            sb.draw(cloud.getCloudTwo(), cloud.getPosCloudTwo().x, cloud.getPosCloudTwo().y, cloud.getCloudTwo().getWidth(), cloud.getCloudTwo().getHeight() - 20);
            sb.draw(cloud.getCloudThree(), cloud.getPosCloudThree().x, cloud.getPosCloudThree().y, cloud.getCloudThree().getWidth(), cloud.getCloudThree().getHeight() - 20);
        }
        sb.draw(goldCloud.getGoldCloud(), goldCloud.getPosition().x, goldCloud.getPosition().y, goldCloud.getGoldCloud().getWidth(), goldCloud.getGoldCloud().getHeight());
        this.font.draw(sb, "SCORE" + "  " + SCORE, camera.position.x - FlaneGame.WIDTH / 2 + 10, FlaneGame.HEIGHT - 10);
        sb.draw(ground, groundPos1.x, groundPos1.y, ground.getWidth(), ground.getHeight() - 20);
        sb.draw(ground, groundPos2.x, groundPos2.y, ground.getWidth(), ground.getHeight() - 20);
        sb.end();
    }

    public void repositionGoldCloud(){
         int t = MathUtils.random(0, 1);
         switch (t){
             case 0:
                 goldCloud.reposition(camera.position.x - (camera.viewportWidth / 2) + MathUtils.random(900, 1300), 270);
                 timer = 0;
                 break;
             case 1:
                 goldCloud.reposition(camera.position.x - (camera.viewportWidth / 2) + MathUtils.random(900, 1300), 520);
                 timer = 0;
                 break;
         }
    }

    private void updateGround(){
        if (camera.position.x - (camera.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (camera.position.x - (camera.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }

    @Override
    public void dispose() {
        bg.dispose();
        copter.dispose();
        for(Cloud cloud : clouds){
            cloud.dispose();
        }
        ground.dispose();
        font.dispose();
        goldCloud.dispose();
    }
}
