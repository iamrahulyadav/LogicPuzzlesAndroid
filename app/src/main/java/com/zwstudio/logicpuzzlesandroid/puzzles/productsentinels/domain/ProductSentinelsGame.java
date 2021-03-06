package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class ProductSentinelsGame extends CellsGame<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public ProductSentinelsGame(List<String> layout, GameInterface<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 2, c * 2 + 2);
                if (s.equals("  ")) continue;
                int n = Integer.parseInt(s.trim());
                pos2hint.put(p, n);
            }
        }
        ProductSentinelsGameState state = new ProductSentinelsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(ProductSentinelsGameMove move, F2<ProductSentinelsGameState, ProductSentinelsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        ProductSentinelsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(ProductSentinelsGameMove move) {
        return changeObject(move, ProductSentinelsGameState::switchObject);
    }

    public boolean setObject(ProductSentinelsGameMove move) {
        return changeObject(move, ProductSentinelsGameState::setObject);
    }

    public ProductSentinelsObject getObject(Position p) {
        return state().get(p);
    }

    public ProductSentinelsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
