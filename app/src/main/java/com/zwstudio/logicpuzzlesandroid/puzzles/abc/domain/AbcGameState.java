package com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class AbcGameState extends CellsGameState<AbcGame, AbcGameMove, AbcGameState> {
    private AbcObject[] objArray;
    public String[] row2hint;
    public String[] col2hint;

    public AbcGameState(AbcGame game) {
        super(game);
        objArray = new AbcObject[rows() * cols()];
        Arrays.fill(objArray, AbcObject.Normal);
        row2hint = new String[rows()];
        col2hint = new String[cols()];
        updateIsSolved();
    }

    public AbcObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public AbcObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, AbcObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, AbcObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        String chars;
        for (int r = 0; r < rows(); r++) {
            chars = row2hint[r] = "";
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == AbcObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(String.valueOf(ch))) {
                    isSolved = false;
                    row2hint[r] += ch;
                } else
                    chars += ch;
            }
        }
        for (int c = 0; c < cols(); c++) {
            chars = col2hint[c] = "";
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                if (get(p) == AbcObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(String.valueOf(ch))) {
                    isSolved = false;
                    col2hint[c] += ch;
                } else
                    chars += ch;
            }
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        List<Position> rngDarken = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == AbcObject.Darken)
                    rngDarken.add(p);
                else{
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2Node.put(p, node);
                }
            }
        for (Position p : rngDarken)
            for (Position os : AbcGame.offset) {
                Position p2 = p.add(os);
                if (rngDarken.contains(p2)) {
                    isSolved = false;
                    return;
                }
            }
        for (Position p : pos2Node.keySet()) {
            for (Position os : AbcGame.offset) {
                Position p2 = p.add(os);
                if (pos2Node.containsKey(p2))
                    g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(AbcGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(AbcMarkerOptions markerOption, AbcGameMove move) {
        F<AbcObject, AbcObject> f = obj -> {
            switch (obj) {
            case Normal:
                return markerOption == AbcMarkerOptions.MarkerBeforeDarken ?
                        AbcObject.Marker : AbcObject.Darken;
            case Darken:
                return markerOption == AbcMarkerOptions.MarkerAfterDarken ?
                        AbcObject.Marker : AbcObject.Normal;
            case Marker:
                return markerOption == AbcMarkerOptions.MarkerBeforeDarken ?
                        AbcObject.Darken : AbcObject.Normal;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }
}
