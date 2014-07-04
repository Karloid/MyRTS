package com.krld.myrts.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.krld.myrts.model.Direction;
import com.krld.myrts.model.Unit;
import com.krld.myrts.model.UnitType;

import java.util.ArrayList;
import java.util.List;

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
    private Texture undeadSoldierDown;
    private Texture undeadSoldierUp;
    private Texture undeadSoldierLeft;
    private Texture undeadSoldierRight;
    private Texture undeadSoldierCorpse;
    private Texture trooperUp;
    private List<Texture> bulletFlashes;
    public Texture sword;

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

        undeadSoldierDown = new Texture(Gdx.files.internal("undead_soldier.png"));
        undeadSoldierUp = new Texture(Gdx.files.internal("undead_soldier_up.png"));
        undeadSoldierLeft = new Texture(Gdx.files.internal("undead_soldier_left.png"));
        undeadSoldierRight = new Texture(Gdx.files.internal("undead_soldier_right.png"));
        undeadSoldierCorpse = new Texture(Gdx.files.internal("undead_soldier_dead.png"));

        sword = new Texture(Gdx.files.internal("sword.png"));
        bulletFlashes = new ArrayList<Texture>();
        bulletFlashes.add(new Texture(Gdx.files.internal("bullet_flash.png")));
        bulletFlashes.add(new Texture(Gdx.files.internal("bullet_flash_2.png")));
        bulletFlashes.add(new Texture(Gdx.files.internal("bullet_flash_3.png")));
        bulletFlashes.add(new Texture(Gdx.files.internal("bullet_flash_4.png")));
        bulletFlashes.add(new Texture(Gdx.files.internal("bullet_flash_5.png")));

        trooperUp = new Texture(Gdx.files.internal("trooper.png"));
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

        if (unit.getType().equals(UnitType.UNDEAD_SOLDIER)) {
            if (unit.getDirection() == null || unit.getDirection().equals(Direction.DOWN) || unit.getDirection() == Direction.SELF) {
                texture = undeadSoldierDown;
            } else if (unit.getDirection().equals(Direction.UP)) {
                texture = undeadSoldierUp;
            } else if (unit.getDirection().equals(Direction.LEFT)) {
                texture = undeadSoldierLeft;
            } else if (unit.getDirection().equals(Direction.RIGHT)) {
                texture = undeadSoldierRight;
            }
        }

        if (unit.getType().equals(UnitType.TROOPER)) {
            if (unit.getDirection() == null || unit.getDirection().equals(Direction.DOWN) || unit.getDirection() == Direction.SELF) {
                texture = trooperUp;
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
        if (type == UnitType.TROOPER) {
            return soldierCorpseTexture;
        }
        if (type == UnitType.UNDEAD_SOLDIER) {
            return undeadSoldierCorpse;
        }
        return null;
    }

    public Texture getRandomBulletFlash() {
        int index = (int) (Math.random() * bulletFlashes.size());
        return bulletFlashes.get(index);
    }
}
