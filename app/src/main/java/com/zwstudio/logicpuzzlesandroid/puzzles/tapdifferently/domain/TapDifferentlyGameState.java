package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;
import fj.F2;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;
import static fj.data.Stream.range;

public class TapDifferentlyGameState extends CellsGameState<TapDifferentlyGame, TapDifferentlyGameMove, TapDifferentlyGameState> {
    public TapDifferentlyObject[] objArray;

    public TapDifferentlyGameState(TapDifferentlyGame game) {
        super(game);
        objArray = new TapDifferentlyObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TapDifferentlyEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new TapDifferentlyHintObject());
    }

    public TapDifferentlyObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TapDifferentlyObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TapDifferentlyObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TapDifferentlyObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TapDifferentlyGameMove move) {
        Position p = move.p;
        TapDifferentlyObject objOld = get(p);
        TapDifferentlyObject objNew = move.obj;
        if (objOld instanceof TapDifferentlyHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TapDifferentlyGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TapDifferentlyObject, TapDifferentlyObject> f = obj -> {
            if (obj instanceof TapDifferentlyEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapDifferentlyMarkerObject() : new TapDifferentlyWallObject();
            if (obj instanceof TapDifferentlyWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TapDifferentlyMarkerObject() : new TapDifferentlyEmptyObject();
            if (obj instanceof TapDifferentlyMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapDifferentlyWallObject() : new TapDifferentlyEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap Differently

        Summary
        Tapa Landscaper

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. Each row must have a different number of filled cells.
        3. Each column must have a different number of filled cells.
    */
    private void updateIsSolved() {
        isSolved = true;
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        F<List<Integer>, List<Integer>> computeHint = filled -> {
            List<Integer> hint = new ArrayList<>();
            if (filled.isEmpty())
                hint.add(0);
            else {
                for (int j = 0; j < filled.size(); j++)
                    if (j == 0 || filled.get(j) - filled.get(j - 1) != 1)
                        hint.add(1);
                    else
                        hint.set(hint.size() - 1, hint.get(hint.size() - 1) + 1);
                if (filled.size() > 1 && hint.size() > 1 && filled.get(filled.size() - 1) - filled.get(0) == 7) {
                    hint.set(0, hint.get(0) + hint.get(hint.size() - 1));
                    hint.remove(hint.size() - 1);
                }
                Collections.sort(hint);
                // hint.sort(Comparator.naturalOrder());
            }
            return hint;
        };
        F2<List<Integer>, List<Integer>, Boolean> isCompatible = (computedHint, givenHint) -> {
            if (computedHint.equals(givenHint)) return true;
            if (computedHint.size() != givenHint.size()) return false;
            Set<Integer> h1 = new HashSet<>(computedHint);
            Set<Integer> h2 = new HashSet<>(givenHint);
            h2.remove(-1);
            return h1.containsAll(h2);
        };
        fromMap(game.pos2hint).foreachDoEffect(kv -> {
            Position p = kv._1();
            List<Integer> arr2 = kv._2();
            List<Integer> filled = range(0, 8).filter(i -> {
                Position p2 = p.add(TapDifferentlyGame.offset[i]);
                return isValid(p2) && get(p2) instanceof TapDifferentlyWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            TapDifferentlyHintObject o = new TapDifferentlyHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(TapDifferentlyGame.offset2).forall(os -> {
                    TapDifferentlyObject o = get(p.add(os));
                    return o instanceof TapDifferentlyWallObject;
                })) {
                    isSolved = false; return;
                }
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) instanceof TapDifferentlyWallObject) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : TapaGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        // The goal is to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
        // 2. Each row must have a different number of filled cells.
        Set<Integer> nums = new HashSet<>();
        for (int r = 0; r < rows(); r++) {
            int n = 0;
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof TapDifferentlyWallObject)
                    n++;
            nums.add(n);
        }
        if (nums.size() != rows()) {isSolved = false; return;}
        // 3. Each column must have a different number of filled cells.
        nums.clear();
        for (int c = 0; c < cols(); c++) {
            int n = 0;
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof TapDifferentlyWallObject)
                    n++;
            nums.add(n);
        }
        if (nums.size() != cols()) isSolved = false;
    }
}
