package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data.PairakabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class PairakabeMainActivity extends GameMainActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState> {
    @Bean
    protected PairakabeDocument document;
    public PairakabeDocument doc() {return document;}

    @Click
    void btnOptions() {
        PairakabeOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        PairakabeGameActivity_.intent(this).start();
    }
}
