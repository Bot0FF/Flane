package com.bot0ff.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class GoldCloud {
    private static final float SPEED = -0.5f;

    private Vector2 position;
    private Vector2 velosity;
    private Circle boundsGoldCloud;
    private Texture texture;
    private Sprite goldCloud;

    public GoldCloud(int x, int y){
        position = new Vector2(x, y);
        velosity = new Vector2(0, 0);
        texture = new Texture("goldcloud.png");
        goldCloud = new Sprite(texture);
        goldCloud.setSize(100, 80);
        boundsGoldCloud = new Circle(position.x, position.y , goldCloud.getWidth() / 2.3f);
    }

    public void update(float dt){
        velosity.add(SPEED * dt, 0);
        velosity.scl(dt);
        velosity.scl(1 / dt);
        position.add(velosity.x, 0);
        boundsGoldCloud.setPosition(position.x, position.y);
    }

    public void reposition(float x, float y){
        position.set(x, y);
        velosity.set(0, 0);

        boundsGoldCloud.setPosition(position.x, position.y);
    }

    public boolean collides(Circle player){
        return player.overlaps(boundsGoldCloud);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Sprite getGoldCloud() {
        return goldCloud;
    }

    public void dispose() {
        texture.dispose();

    }
}
