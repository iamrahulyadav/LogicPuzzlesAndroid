package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data.RippleEffectDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class RippleEffectOptionsActivity extends GameOptionsActivity<RippleEffectGame, RippleEffectDocument, RippleEffectGameMove, RippleEffectGameState> {
    @Bean
    protected RippleEffectDocument document;
    public RippleEffectDocument doc() {return document;}
}
