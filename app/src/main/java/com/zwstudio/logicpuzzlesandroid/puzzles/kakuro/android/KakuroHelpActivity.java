package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data.KakuroDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class KakuroHelpActivity extends GameHelpActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState> {
    @Bean
    protected KakuroDocument document;
    public KakuroDocument doc() {return document;}
}
