package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;

import static fj.data.List.iterableList;

public class SentinelsGameState extends CellsGameState<SentinelsGame, SentinelsGameMove, SentinelsGameState> {
    public SentinelsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public SentinelsGameState(SentinelsGame game) {
        super(game);
        objArray = new SentinelsObject[rows() * cols()];
        Arrays.fill(objArray, new SentinelsEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new SentinelsHintObject());
        updateIsSolved();
    }

    public SentinelsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SentinelsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SentinelsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, SentinelsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(SentinelsGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SentinelsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<SentinelsObject, SentinelsObject> f = obj -> {
            if (obj instanceof SentinelsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new SentinelsMarkerObject() : new SentinelsTowerObject();
            if (obj instanceof SentinelsTowerObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new SentinelsMarkerObject() : new SentinelsEmptyObject();
            if (obj instanceof SentinelsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new SentinelsTowerObject() : new SentinelsEmptyObject();
            return obj;
        };
        SentinelsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Sentinels

        Summary
        This time it's one Garden and many Towers

        Description
        1. On the Board there are a few sentinels. These sentinels are marked with
           a number.
        2. The number tells you how many tiles that Sentinel can control (see) from
           there vertically and horizontally. This includes the tile where he is
           located.
        3. You must put Towers on the Boards in accordance with these hints, keeping
           in mind that a Tower blocks the Sentinel View.
        4. The restrictions are that there must be a single continuous Garden, and
           two Towers can't touch horizontally or vertically.
        5. Towers can't go over numbered squares. But numbered squares don't block
           Sentinel View.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                SentinelsObject o = get(r, c);
                if (o instanceof SentinelsTowerObject)
                    ((SentinelsTowerObject) o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof SentinelsForbiddenObject)
                        set(r, c, new SentinelsEmptyObject());
                    Position p = new Position(r, c);
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        // 4. two Towers can't touch horizontally or vertically.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    for (Position os : SentinelsGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof SentinelsTowerObject)
                            return true;
                    }
                    return false;
                };
                SentinelsObject o = get(r, c);
                if (o instanceof SentinelsTowerObject) {
                    SentinelsTowerObject o2 = (SentinelsTowerObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof SentinelsEmptyObject || o instanceof SentinelsMarkerObject) &&
                        allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new SentinelsForbiddenObject());
            }
        // 2. The number tells you how many tiles that Sentinel can control (see) from
        // there vertically and horizontally. This includes the tile where he is
        // located.
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = SentinelsGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    SentinelsObject o2 = get(p2);
                    if (o2 instanceof SentinelsTowerObject) continue next;
                    if (o2 instanceof SentinelsEmptyObject)
                        rng.add(p2.plus());
                    nums[i]++;
                }
            }
            int n1 = nums[0] + nums[1] + nums[2] + nums[3] + 1;
            pos2state.put(p, n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2)
                isSolved = false;
            else
                for (Position p2 : rng)
                    set(p2, new SentinelsForbiddenObject());
        }
        if (!isSolved) return;
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : SentinelsGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        // 4. There must be a single continuous Garden
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
