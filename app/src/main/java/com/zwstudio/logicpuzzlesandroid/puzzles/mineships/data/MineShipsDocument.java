package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsObject;

import org.androidannotations.annotations.EBean;

@EBean
public class MineShipsDocument extends GameDocument<MineShipsGame, MineShipsGameMove> {
    protected void saveMove(MineShipsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public MineShipsGameMove loadMove(MoveProgress rec) {
        return new MineShipsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = MineShipsObject.objFromString(rec.strValue1);
        }};
    }
}
