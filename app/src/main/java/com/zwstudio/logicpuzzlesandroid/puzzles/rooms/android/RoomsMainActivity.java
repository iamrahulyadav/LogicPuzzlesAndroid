package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class RoomsMainActivity extends GameMainActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState> {
    @Bean
    protected RoomsDocument document;
    public RoomsDocument doc() {return document;}

    @Click
    void btnOptions() {
        RoomsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        RoomsGameActivity_.intent(this).start();
    }
}
