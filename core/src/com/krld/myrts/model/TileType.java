package com.krld.myrts.model;

import com.badlogic.gdx.graphics.Texture;

import java.util.Collection;

/**
 * Created by Andrey on 2/19/14.
 */
public class TileType {

    private final int id;
    private final String name;
    private final String textureName;
    private final Collection<String> tags;
    private Texture gdxTexture;

    public Texture getGdxTexture() {
        return gdxTexture;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTextureName() {
        return textureName;
    }

    public TileType(int id, String name, String textureName, Collection<String> tags) {
        this.id = id;
        this.name = name;
        this.textureName = textureName;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "[id: " + id + ", name: " + name + ", textureName: " + textureName + " ]";
    }

/*    public Bitmap getBitmap() {
        return bitmap;
    }*/

    public void setGdxTexture(Texture gdxTexture) {
        this.gdxTexture = gdxTexture;
    }
}
