package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SkyscrapersMainActivity extends GameMainActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState> {
    @Bean
    protected SkyscrapersDocument document;
    public SkyscrapersDocument doc() {return document;}

    @Click
    void btnOptions() {
        SkyscrapersOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SkyscrapersGameActivity_.intent(this).start();
    }
}
