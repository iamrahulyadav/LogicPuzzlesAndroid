package com.zwstudio.logicpuzzlesandroid.home.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class HomeGameProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String gameName = "LightenUp";
    @DatabaseField
    public String gameTitle = "Lighten Up";
    @DatabaseField
    public boolean playMusic = true;
    @DatabaseField
    public boolean playSound = true;
}
