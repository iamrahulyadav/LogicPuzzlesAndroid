package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data.TatamiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class TatamiGameActivity extends GameGameActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState> {
    @Bean
    protected TatamiDocument document;
    public TatamiDocument doc() {return document;}

    protected TatamiGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new TatamiGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new TatamiGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                TatamiGameMove move = doc().loadMove(rec);
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Click
    protected void btnHelp() {
        TatamiHelpActivity_.intent(this).start();
    }
}