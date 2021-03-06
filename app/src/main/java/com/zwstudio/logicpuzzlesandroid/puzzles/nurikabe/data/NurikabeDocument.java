package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeObject;

import org.androidannotations.annotations.EBean;

@EBean
public class NurikabeDocument extends GameDocument<NurikabeGame, NurikabeGameMove> {
    protected void saveMove(NurikabeGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public NurikabeGameMove loadMove(MoveProgress rec) {
        return new NurikabeGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = NurikabeObject.objTypeFromString(rec.strValue1);
        }};
    }
}
