package com.bot0ff.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bot0ff.game.FlaneGame;

import java.util.Random;

public class Cloud {

    public static final int CLOUD_WIDTH = 80;
    private static final int FLUCTUATION = 130;
    private static final int CLOUD_GAP = 120;
    private static final int LOWEST_OPENING = 130;

    private Texture texture;
    private Sprite cloudOne;
    private Sprite cloudTwo;
    private Sprite cloudThree;

    private Vector2 posCloudOne;
    private Vector2 posCloudTwo;
    private Vector2 posCloudThree;

    private Circle boundsCloudOne;
    private Circle boundsCloudTwo;
    private Circle boundsCloudThree;

    private Random random;

    public Cloud(float x){
        texture = new Texture("cloud.png");
        cloudOne = new Sprite(texture);
        cloudTwo = new Sprite(texture);
        cloudThree = new Sprite(texture);

        cloudOne.setSize(100, 100);
        cloudTwo.setSize(100, 100);
        cloudThree.setSize(100, 100);

        random = new Random();
        posCloudOne = new Vector2(x, random.nextInt(FLUCTUATION)+ LOWEST_OPENING);
        posCloudTwo = new Vector2(x + MathUtils.random(-60, 60), posCloudOne.y + CLOUD_GAP + cloudTwo.getHeight());
        posCloudThree = new Vector2(x + MathUtils.random(-60, 60), posCloudTwo.y + CLOUD_GAP + cloudThree.getHeight());

        boundsCloudOne = new Circle(posCloudOne.x, posCloudOne.y -10, cloudOne.getWidth() / 2.5f);
        boundsCloudTwo = new Circle(posCloudTwo.x, posCloudTwo.y - 10, cloudTwo.getWidth() / 2.5f);
        boundsCloudThree = new Circle(posCloudThree.x, posCloudThree.y - 10, cloudThree.getWidth() / 2.5f);
    }

    public boolean collides(Circle player){
        return player.overlaps(boundsCloudOne) || player.overlaps(boundsCloudTwo) || player.overlaps(boundsCloudThree);
    }

    public void reposition(float x){
        posCloudOne.set(x, random.nextInt(FLUCTUATION)+ LOWEST_OPENING);
        posCloudTwo.set(x + MathUtils.random(-60, 60), posCloudOne.y + CLOUD_GAP + cloudTwo.getHeight());
        posCloudThree.set(x + MathUtils.random(-60, 60), posCloudTwo.y + CLOUD_GAP + cloudThree.getHeight());

        boundsCloudOne.setPosition(posCloudOne.x, posCloudOne.y - 10);
        boundsCloudTwo.setPosition(posCloudTwo.x, posCloudTwo.y - 10);
        boundsCloudThree.setPosition(posCloudThree.x, posCloudThree.y - 10);
    }

    public Sprite getCloudOne() {
        return cloudOne;
    }

    public Sprite getCloudTwo() {
        return cloudTwo;
    }

    public Sprite getCloudThree() {
        return cloudThree;
    }

    public Vector2 getPosCloudOne() {
        return posCloudOne;
    }

    public Vector2 getPosCloudTwo() {
        return posCloudTwo;
    }

    public Vector2 getPosCloudThree() {
        return posCloudThree;
    }

    public void dispose() {
        texture.dispose();
    }
}
