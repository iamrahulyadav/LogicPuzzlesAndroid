package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class AbcHelpActivity extends GameHelpActivity<AbcGame, AbcDocument, AbcGameMove, AbcGameState> {
    @Bean
    protected AbcDocument document;
    public AbcDocument doc() {return document;}
}
