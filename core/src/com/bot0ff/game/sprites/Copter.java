package com.bot0ff.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Copter {
    private static final int MOVEMENT = 100;
    private static final int GRAVITY = -15;

    private Vector3 position;
    private Vector3 velosity;
    private Circle bounds;

    private Sound jumpSound;
    private Texture texture;
    private Sprite copter;

    private long idJumpSound;

    public Copter(int x, int y){
        position = new Vector3(x, y, 0);
        velosity = new Vector3(0, 0, 0);
        texture = new Texture("copter.png");
        copter = new Sprite(texture);
        copter.setSize(100, 100);
        bounds = new Circle(position.x, position.y , copter.getWidth() / 2.5f);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
    }

    public void update(float dt){
        if (position.y > 0)
            velosity.add(0, GRAVITY, 0);
        velosity.scl(dt);
        position.add(MOVEMENT * dt, velosity.y, 0);
        if (position.y < 0)
            position.y = 0;

        velosity.scl(1 / dt);
        bounds.setPosition(position.x, position.y);
    }

    public void playJumpSound(){
        idJumpSound = jumpSound.play();
        jumpSound.setVolume(idJumpSound, 0.1f);
    }

    public void jump(){
        velosity.y = 300;
        playJumpSound();
    }

    public Vector3 getPosition() {
        return position;
    }

    public Sprite getCopter() {
        return copter;
    }

    public Circle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        jumpSound.dispose();
    }
}
