package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain.MathraxGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class MathraxDocument extends GameDocument<MathraxGame, MathraxGameMove> {
    protected void saveMove(MathraxGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public MathraxGameMove loadMove(MoveProgress rec) {
        return new MathraxGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
