package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class TentsTentObject extends TentsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tent";
    }
}
