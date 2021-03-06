package com.zwstudio.logicpuzzlesandroid.puzzles.walls.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.data.WallsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain.WallsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class WallsOptionsActivity extends GameOptionsActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState> {
    @Bean
    protected WallsDocument document;
    public WallsDocument doc() {return document;}
}
