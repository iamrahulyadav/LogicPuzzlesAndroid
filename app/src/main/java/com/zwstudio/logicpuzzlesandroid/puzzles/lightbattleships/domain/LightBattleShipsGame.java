package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class LightBattleShipsGame extends CellsGame<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(-1, 1),
            new Position(0, 1),
            new Position(1, 1),
            new Position(1, 0),
            new Position(1, -1),
            new Position(0, -1),
            new Position(-1, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();
    public Map<Position, LightBattleShipsObject> pos2obj = new HashMap<>();

    public LightBattleShipsGame(List<String> layout, GameInterface<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                switch(ch) {
                case '^':
                    pos2obj.put(p, new LightBattleShipsBattleShipTopObject()); break;
                case 'v':
                    pos2obj.put(p, new LightBattleShipsBattleShipBottomObject()); break;
                case '<':
                    pos2obj.put(p, new LightBattleShipsBattleShipLeftObject()); break;
                case '>':
                    pos2obj.put(p, new LightBattleShipsBattleShipRightObject()); break;
                case '+':
                    pos2obj.put(p, new LightBattleShipsBattleShipMiddleObject()); break;
                case 'o':
                    pos2obj.put(p, new LightBattleShipsBattleShipUnitObject()); break;
                case '.':
                    pos2obj.put(p, new LightBattleShipsMarkerObject()); break;
                default:
                    if (ch >= '0' && ch <= '9') {
                        int n = ch - '0';
                        pos2hint.put(new Position(r, c), n);
                    }
                    break;
                }
            }
        }

        LightBattleShipsGameState state = new LightBattleShipsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LightBattleShipsGameMove move, F2<LightBattleShipsGameState, LightBattleShipsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LightBattleShipsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LightBattleShipsGameMove move) {
        return changeObject(move, LightBattleShipsGameState::switchObject);
    }

    public boolean setObject(LightBattleShipsGameMove move) {
        return changeObject(move, LightBattleShipsGameState::setObject);
    }

    public LightBattleShipsObject getObject(Position p) {
        return state().get(p);
    }

    public LightBattleShipsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
