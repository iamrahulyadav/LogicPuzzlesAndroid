package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class MiniLitsTreeObject extends MiniLitsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
