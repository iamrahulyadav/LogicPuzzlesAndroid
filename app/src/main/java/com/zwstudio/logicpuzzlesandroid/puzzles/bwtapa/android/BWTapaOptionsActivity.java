package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.data.BWTapaDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class BWTapaOptionsActivity extends GameOptionsActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState> {
    @Bean
    protected BWTapaDocument document;
    public BWTapaDocument doc() {return document;}
}
