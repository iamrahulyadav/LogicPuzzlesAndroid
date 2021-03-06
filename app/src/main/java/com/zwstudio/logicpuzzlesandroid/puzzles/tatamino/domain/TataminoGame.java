package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class TataminoGame extends CellsGame<TataminoGame, TataminoGameMove, TataminoGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, 0),
    };
    public static int dirs[] = {1, 2, 1, 2};

    public List<List<Position>> areas = new ArrayList<>();
    public Map<Position, Integer> pos2area = new HashMap<>();
    public GridDots dots;

    public char[] objArray;
    public char get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public char get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, char obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public TataminoGame(List<String> layout, GameInterface<TataminoGame, TataminoGameMove, TataminoGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        dots = new GridDots(rows() + 1, cols() + 1);
        objArray = new char[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                set(r, c, ch);
            }
        }
        for (int r = 0; r < rows(); r++) {
            dots.set(r, 0, 2, GridLineObject.Line);
            dots.set(r, cols(), 2, GridLineObject.Line);
        }
        for (int c = 0; c < cols(); c++) {
            dots.set(0, c, 1, GridLineObject.Line);
            dots.set(rows(), c, 1, GridLineObject.Line);
        }
        TataminoGameState state = new TataminoGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(TataminoGameMove move, F2<TataminoGameState, TataminoGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        TataminoGameState state = cloner.deepClone(state());
        boolean changed = f.f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(TataminoGameMove move) {
        return changeObject(move, TataminoGameState::switchObject);
    }

    public boolean setObject(TataminoGameMove move) {
        return changeObject(move, TataminoGameState::setObject);
    }

    public char getObject(Position p) {
        return state().get(p);
    }

    public char getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getPosState(Position p) {
        return state().pos2state.get(p);
    }

    public GridDots getDots() {return state().dots;}
}
