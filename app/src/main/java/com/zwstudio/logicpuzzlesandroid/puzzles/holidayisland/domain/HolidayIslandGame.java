package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class HolidayIslandGame extends CellsGame<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public HolidayIslandGame(List<String> layout, GameInterface<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch != ' ') pos2hint.put(p, ch - '0');
            }
        }

        HolidayIslandGameState state = new HolidayIslandGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(HolidayIslandGameMove move, F2<HolidayIslandGameState, HolidayIslandGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        HolidayIslandGameState state = cloner.deepClone(state());
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

    public boolean switchObject(HolidayIslandGameMove move) {
        return changeObject(move, HolidayIslandGameState::switchObject);
    }

    public boolean setObject(HolidayIslandGameMove move) {
        return changeObject(move, HolidayIslandGameState::setObject);
    }

    public HolidayIslandObject getObject(Position p) {
        return state().get(p);
    }

    public HolidayIslandObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
