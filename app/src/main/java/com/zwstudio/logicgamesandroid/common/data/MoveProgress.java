package com.zwstudio.logicgamesandroid.common.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MoveProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
}