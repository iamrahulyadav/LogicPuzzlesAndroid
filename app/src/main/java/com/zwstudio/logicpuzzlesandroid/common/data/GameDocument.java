package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.home.android.HomeChooseGameActivity;
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.array;
import static fj.data.List.iterableList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@EBean(scope = EBean.Scope.Singleton)
public abstract class GameDocument<G extends Game, GM> implements GameDocumentInterface {
    public String gameID() {
        String name = getClass().getSimpleName();
        return name.substring(0, name.indexOf("Document"));
    }
    public String gameTitle() {
        String name = gameID();
        return defaultIfNull(HomeChooseGameActivity.name2title.get(name), name);
    }

    public List<GameLevel> levels = new ArrayList<>();
    public String selectedLevelID;
    public String selectedLevelIDSolution() {return selectedLevelID + " Solution";}
    public List<String> help = new ArrayList<>();
    @App
    public LogicPuzzlesApplication app;

    @AfterInject
    public void init() {
        String filename = gameID() + ".xml";
        try {
            InputStream in_s = app.getApplicationContext().getAssets().open("xml/" + filename);
            loadXml(in_s);
            in_s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedLevelID = gameProgress().levelID;
    }

    private void loadXml(InputStream in_s) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        F<List<String>, List<String>> getCdata = strs -> iterableList(strs).filter(s -> !StringUtils.isAllBlank(s)).toJavaList();
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
            case XmlPullParser.START_DOCUMENT:
                break;
            case XmlPullParser.START_TAG:
                String name = parser.getName();
                if (name.equals("level")){
                    String id = parser.getAttributeValue(null,"id");
                    Map<String, String> settings = new HashMap<>();
                    for (int i = 0; i < parser.getAttributeCount(); i++)
                        settings.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                    List<String> layout = array(parser.nextText().split("\n"))
                            .map(s -> s.replace("\r", ""))
                            .toJavaList();
                    layout = iterableList(getCdata.f(layout))
                            .map(s -> s.replace("`", ""))
                            .toJavaList();
                    GameLevel level = new GameLevel();
                    level.id = id;
                    level.layout = layout;
                    level.settings = settings;
                    levels.add(level);
                } else if (name.equals("help")){
                    help = array(parser.nextText().split("\n"))
                            .map(s -> s.replace("\r", ""))
                            .toJavaList();
                    help = getCdata.f(help);
                }
                break;
            case XmlPullParser.END_TAG:
                break;
            }
            eventType = parser.next();
        }

    }

    public GameProgress gameProgress() {
        try {
            GameProgress rec = app.daoGameProgress.queryBuilder()
                    .where().eq("gameID", gameID())
                    .queryForFirst();
            if (rec == null) {
                rec = new GameProgress();
                rec.gameID = gameID();
                rec.levelID = levels.get(0).id;
                app.daoGameProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LevelProgress levelProgress(String levelID) {
        try {
            LevelProgress rec = app.daoLevelProgress.queryBuilder()
                    .where().eq("gameID", gameID())
                    .and().eq("levelID", levelID).queryForFirst();
            if (rec == null) {
                rec = new LevelProgress();
                rec.gameID = gameID();
                rec.levelID = levelID;
                app.daoLevelProgress.create(rec);
            }
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public LevelProgress levelProgress() {
        return levelProgress(selectedLevelID);
    }
    public LevelProgress levelProgressSolution() {
        return levelProgress(selectedLevelIDSolution());
    }

    private List<MoveProgress> moveProgress(String levelID) {
        try {
            QueryBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.queryBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", levelID);
            builder.orderBy("moveIndex", true);
            List<MoveProgress> rec = builder.query();
            return rec;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<MoveProgress> moveProgress() {
        return moveProgress(selectedLevelID);
    }
    public List<MoveProgress> moveProgressSolution() {
        return moveProgress(selectedLevelIDSolution());
    }

    public void levelUpdated(Game game) {
        try {
            LevelProgress rec = levelProgress();
            rec.moveIndex = game.moveIndex();
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void gameSolved(Game game) {
        LevelProgress recLP = levelProgress(), recLPS = levelProgressSolution();
        if (recLPS.moveIndex == 0 || recLPS.moveIndex > recLP.moveIndex)
            saveSolution(game);
    }

    public void moveAdded(Game game, GM move) {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID)
                    .and().ge("moveIndex", game.moveIndex());
            builder.delete();
            MoveProgress rec = new MoveProgress();
            rec.gameID = gameID();
            rec.levelID = selectedLevelID;
            rec.moveIndex = game.moveIndex();
            saveMove(move, rec);
            app.daoMoveProgress.create(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void saveMove(GM move, MoveProgress rec);
    public abstract GM loadMove(MoveProgress rec);

    public void resumeGame() {
        try {
            GameProgress rec = gameProgress();
            rec.levelID = selectedLevelID;
            app.daoGameProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGame() {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelID);
            builder.delete();
            LevelProgress rec = levelProgress();
            rec.moveIndex = 0;
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void copyMoves(List<MoveProgress> moveProgressFrom, String levelIDTo) {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", levelIDTo);
            builder.delete();
            for (MoveProgress recMP : moveProgressFrom) {
                GM move = loadMove(recMP);
                MoveProgress recMPS = new MoveProgress();
                recMPS.gameID = gameID();
                recMPS.levelID = levelIDTo;
                recMPS.moveIndex = recMP.moveIndex;
                saveMove(move, recMPS);
                app.daoMoveProgress.create(recMPS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSolution(Game game) {
        copyMoves(moveProgress(), selectedLevelIDSolution());
        try {
            LevelProgress rec = levelProgressSolution();
            rec.moveIndex = game.moveIndex();
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadSolution() {
        List<MoveProgress> mps = moveProgressSolution();
        copyMoves(mps, selectedLevelID);
        try {
            LevelProgress rec = levelProgress();
            rec.moveIndex = mps.size();
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSolution() {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID())
                    .and().eq("levelID", selectedLevelIDSolution());
            builder.delete();
            LevelProgress rec = levelProgressSolution();
            rec.moveIndex = 0;
            app.daoLevelProgress.update(rec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAllLevels() {
        try {
            DeleteBuilder<MoveProgress, Integer> builder = app.daoMoveProgress.deleteBuilder();
            builder.where().eq("gameID", gameID());
            builder.delete();
            DeleteBuilder<LevelProgress, Integer> builder2 = app.daoLevelProgress.deleteBuilder();
            builder2.where().eq("gameID", gameID());
            builder2.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(GameProgress rec, int o) {
        rec.option1 = String.valueOf(o);
    }
    public boolean isAllowedObjectsOnly() {
        String o = gameProgress().option2;
        return Boolean.parseBoolean(o);
    }
    public void setAllowedObjectsOnly(GameProgress rec, boolean o) {
        rec.option2 = String.valueOf(o);
    }
}
