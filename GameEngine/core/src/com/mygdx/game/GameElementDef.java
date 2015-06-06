package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Flávio on 19/05/2015.
 */
public abstract class GameElementDef {

    public abstract BodyDef getBodyDef();

    public abstract FixtureDef getFixtureDef();
}
