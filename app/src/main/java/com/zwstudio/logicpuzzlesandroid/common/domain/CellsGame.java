package com.zwstudio.logicpuzzlesandroid.common.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;

public class CellsGame<G extends Game<G, GM, GS>, GM, GS extends GameState> extends Game<G, GM, GS> {

    public Position size;
    public int rows() {return size.row;}
    public int cols() {return size.col;}
    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < size.row && col < size.col;
    }
    public boolean isValid(Position p) {
        return isValid(p.row, p.col);
    }

    public CellsGame(GameInterface<G, GM, GS> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
    }
}
