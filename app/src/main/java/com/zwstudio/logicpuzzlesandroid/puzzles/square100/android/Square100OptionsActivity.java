package com.zwstudio.logicpuzzlesandroid.puzzles.square100.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.data.Square100Document;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Game;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100GameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class Square100OptionsActivity extends GameOptionsActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState> {
    @Bean
    protected Square100Document document;
    public Square100Document doc() {return document;}
}
