package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data.RippleEffectDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class RippleEffectGameActivity extends GameGameActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState> {
    @Bean
    protected RippleEffectDocument document;
    public RippleEffectDocument doc() {return document;}

    protected RippleEffectGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new RippleEffectGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        GameLevel level = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(selectedLevelID)).orSome(0));
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new RippleEffectGame(level.layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                RippleEffectGameMove move = doc().loadMove(rec);
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (moveIndex >= 0 && moveIndex < game.moveCount())
                while (moveIndex != game.moveIndex())
                    game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Click
    protected void btnHelp() {
        RippleEffectHelpActivity_.intent(this).start();
    }
}
