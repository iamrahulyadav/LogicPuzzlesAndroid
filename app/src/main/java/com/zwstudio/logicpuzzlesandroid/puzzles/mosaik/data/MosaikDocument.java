package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain.MosaikObject;

import org.androidannotations.annotations.EBean;

@EBean
public class MosaikDocument extends GameDocument<MosaikGame, MosaikGameMove> {
    protected void saveMove(MosaikGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public MosaikGameMove loadMove(MoveProgress rec) {
        return new MosaikGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = MosaikObject.values()[rec.intValue1];
        }};
    }
}
