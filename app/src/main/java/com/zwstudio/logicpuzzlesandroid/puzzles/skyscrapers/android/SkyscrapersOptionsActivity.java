package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class SkyscrapersOptionsActivity extends GameOptionsActivity<SkyscrapersGame, SkyscrapersDocument, SkyscrapersGameMove, SkyscrapersGameState> {
    @Bean
    protected SkyscrapersDocument document;
    public SkyscrapersDocument doc() {return document;}

    protected void onDefault() {}
}
