package com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class Square100GameState extends CellsGameState<Square100Game, Square100GameMove, Square100GameState> {
    public Square100Object[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public Square100GameState(Square100Game game) {
        super(game);
        objArray = new Square100Object[rows() * cols()];
        Arrays.fill(objArray, Square100Object.Empty);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public Square100Object get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public Square100Object get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, Square100Object obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, Square100Object obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(Square100GameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(Square100GameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<Square100Object, Square100Object> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        Square100Object.Marker : Square100Object.Cloud;
            case Cloud:
                return markerOption == MarkerOptions.MarkerLast ?
                        Square100Object.Marker : Square100Object.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        Square100Object.Cloud : Square100Object.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Square100

        Summary
        Weather Radar Report

        Description
        1. You must find Square100 in the sky.
        2. The hints on the borders tell you how many tiles are covered by Square100
           in that row or column.
        3. Square100 only appear in rectangular or square areas. Furthermore, their
           width and height is always at least two tiles wide.
        4. Square100 can't touch between themselves, not even diagonally. 
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == Square100Object.Forbidden)
                    set(r, c, Square100Object.Empty);
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == Square100Object.Cloud)
                    n1++;
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == Square100Object.Cloud)
                    n1++;
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Square100Object o = get(r, c);
                if ((o == Square100Object.Empty || o == Square100Object.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    set(r, c, Square100Object.Forbidden);
            }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) != Square100Object.Cloud) continue;
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (Position p : pos2node.keySet())
            for (Position os : Square100Game.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            int r2 = 0, r1 = rows(), c2 = 0, c1 = cols();
            for (Node node : nodeList) {
                Position p = fromMap(pos2node).toStream().find(e -> e._2().equals(node)).some()._1();
                pos2node.remove(p);
                if (r2 < p.row) r2 = p.row;
                if (r1 > p.row) r1 = p.row;
                if (c2 < p.col) c2 = p.col;
                if (c1 > p.col) c1 = p.col;
            }
            int rs = r2 - r1 + 1, cs = c2 - c1 + 1;
            if (!(rs >= 2 && cs >= 2 && rs * cs == nodeList.size())) {
                isSolved = false;
                return;
            }
        }
    }
}
