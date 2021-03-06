package com.zwstudio.logicpuzzlesandroid.puzzles.snail.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class SnailDocument extends GameDocument<SnailGame, SnailGameMove> {
    protected void saveMove(SnailGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = String.valueOf(move.obj);
    }
    public SnailGameMove loadMove(MoveProgress rec) {
        return new SnailGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1.charAt(0);
        }};
    }
}
