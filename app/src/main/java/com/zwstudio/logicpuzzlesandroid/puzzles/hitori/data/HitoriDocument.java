package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class HitoriDocument extends GameDocument<HitoriGame, HitoriGameMove> {

    public void init() {
        super.init("Hitori.xml");
        selectedLevelID = gameProgress().levelID;
    }

    public HitoriGameProgress gameProgress() {
        try {
            HitoriGameProgress rec = app.daoHitoriGameProgress.queryBuilder()
                    .queryForFirst();
            if (rec == null) {
                rec = new HitoriGameProgress();
                app.daoHitoriGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HitoriLevelProgress levelProgress() {
        try {
            HitoriLevelProgress rec = app.daoHitoriLevelProgress.queryBuilder()
                    .where().eq("levelID", selectedLevelID).queryForFirst();
            if (rec == null) {
                rec = new HitoriLevelProgress();
                rec.levelID = selectedLevelID;
                app.daoHitoriLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<HitoriMoveProgress> moveProgress() {
        try {
            QueryBuilder<HitoriMoveProgress, Integer> qb = app.daoHitoriMoveProgress.queryBuilder();
            qb.where().eq("levelID", selectedLevelID);
            qb.orderBy("moveIndex", true);
            List<HitoriMoveProgress> rec = qb.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void levelUpdated(HitoriGame game) {
        try {
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoHitoriLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveAdded(HitoriGame game, HitoriGameMove move) {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = app.daoHitoriMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            deleteBuilder.delete();
            HitoriMoveProgress rec = new HitoriMoveProgress();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            rec.row = move.p.row;
            rec.col = move.p.col;
            rec.obj = move.obj.ordinal();
            app.daoHitoriMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        try {
            HitoriGameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoHitoriGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<HitoriMoveProgress, Integer> deleteBuilder = app.daoHitoriMoveProgress.deleteBuilder();
            deleteBuilder.where().eq("levelID", selectedLevelID);
            deleteBuilder.delete();
            HitoriLevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoHitoriLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
