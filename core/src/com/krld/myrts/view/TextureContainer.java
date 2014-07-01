package com.krld.myrts.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.krld.myrts.model.Direction;
import com.krld.myrts.model.Unit;
import com.krld.myrts.model.UnitType;

/**
 * Created by Andrey on 7/1/2014.
 */
public class TextureContainer {
    public Texture defaultTexture;
    public Texture soldierDownTexture;
    public Texture soldierUpTexture;
    public Texture soldierleftTexture;
    public Texture soldierRightTexture;
    public Texture selectRect;
    public Texture destMoveTexture;
    public Texture stepsTexture;
    private Texture soldierCorpseTexture;

    public void initTextures() {
        defaultTexture = new Texture(Gdx.files.internal("unknow.png"));
        soldierDownTexture = new Texture(Gdx.files.internal("soldier2.png"));
        soldierUpTexture = new Texture(Gdx.files.internal("soldier2_up.png"));
        soldierleftTexture = new Texture(Gdx.files.internal("soldier2_left.png"));
        soldierRightTexture = new Texture(Gdx.files.internal("soldier2_right.png"));
        selectRect = new Texture(Gdx.files.internal("selectRect.png"));
        destMoveTexture = new Texture(Gdx.files.internal("destMovePoint.png"));
        stepsTexture = new Texture(Gdx.files.internal("steps.png"));
        soldierCorpseTexture = new Texture(Gdx.files.internal("soldier2_dead.png"));
    }

    public Texture getTextureForUnit(Unit unit) {
        Texture texture = null;
        if (unit.getType().equals(UnitType.SOLDIER)) {
            if (unit.getDirection() == null || unit.getDirection().equals(Direction.DOWN) || unit.getDirection() == Direction.SELF) {
                texture = soldierDownTexture;
            } else if (unit.getDirection().equals(Direction.UP)) {
                texture = soldierUpTexture;
            } else if (unit.getDirection().equals(Direction.LEFT)) {
                texture = soldierleftTexture;
            } else if (unit.getDirection().equals(Direction.RIGHT)) {
                texture = soldierRightTexture;
            }
        }
        return texture;
    }

    public Texture getTextureForCorpse(UnitType type) {
        if (type == UnitType.SOLDIER) {
            return soldierCorpseTexture;
        }
        return null;
    }
}
