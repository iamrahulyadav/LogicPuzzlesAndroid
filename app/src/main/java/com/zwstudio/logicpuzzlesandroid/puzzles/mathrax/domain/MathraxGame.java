package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class MathraxGame extends CellsGame<MathraxGame, MathraxGameMove, MathraxGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(1, 1),
            new Position(1, 0),
            new Position(0, 1),
    };

    public int[] objArray;
    public Map<Position, MathraxHint> pos2hint = new HashMap<>();

    public int get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public int get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, int obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, int obj) {
        set(p.row, p.col, obj);
    }

    public MathraxGame(List<String> layout, GameInterface<MathraxGame, MathraxGameMove, MathraxGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() / 2 + 1, layout.get(0).length());
        objArray = new int[rows() * cols()];
        for (int r = 0, i = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                objArray[i++] = ch == ' ' ? 0 : ch - '0';
            }
        }
        for (int r = 0; r < rows() - 1; r++) {
            String str = layout.get(rows() + r);
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 3, c * 3 + 2);
                char ch = str.charAt(c * 3 + 2);
                if (ch == ' ') continue;
                pos2hint.put(p, new MathraxHint() {{
                    op = ch;
                    result = s.equals("  ") ? 0 : Integer.parseInt(s.trim());
                }});
            }
        }
        MathraxGameState state = new MathraxGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(MathraxGameMove move, F2<MathraxGameState, MathraxGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        MathraxGameState state = cloner.deepClone(state());
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

    public boolean switchObject(MathraxGameMove move) {
        return changeObject(move, MathraxGameState::switchObject);
    }

    public boolean setObject(MathraxGameMove move) {
        return changeObject(move, MathraxGameState::setObject);
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }

    public HintState getPosState(Position p) {
        return state().pos2state.get(p);
    }
}
