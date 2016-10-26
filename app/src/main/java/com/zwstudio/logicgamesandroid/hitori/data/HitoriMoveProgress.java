package com.zwstudio.logicgamesandroid.hitori.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class HitoriMoveProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
    @DatabaseField
    public int row;
    @DatabaseField
    public int col;
    @DatabaseField
    public int obj;
}
