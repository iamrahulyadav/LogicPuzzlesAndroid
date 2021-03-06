package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class KakuroDocument extends GameDocument<KakuroGame, KakuroGameMove> {
    protected void saveMove(KakuroGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public KakuroGameMove loadMove(MoveProgress rec) {
        return new KakuroGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
