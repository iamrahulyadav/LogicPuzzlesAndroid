package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data.SentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class SentinelsHelpActivity extends GameHelpActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState> {
    @Bean
    protected SentinelsDocument document;
    public SentinelsDocument doc() {return document;}
}
