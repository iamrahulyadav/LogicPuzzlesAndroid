package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.data.FourMeNotDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class FourMeNotMainActivity extends GameMainActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState> {
    @Bean
    protected FourMeNotDocument document;
    public FourMeNotDocument doc() {return document;}

    @Click
    void btnOptions() {
        FourMeNotOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        FourMeNotGameActivity_.intent(this).start();
    }
}
