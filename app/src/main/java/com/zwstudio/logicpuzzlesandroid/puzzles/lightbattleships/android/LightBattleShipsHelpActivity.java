package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data.LightBattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class LightBattleShipsHelpActivity extends GameHelpActivity<LightBattleShipsGame, LightBattleShipsDocument, LightBattleShipsGameMove, LightBattleShipsGameState> {
    @Bean
    protected LightBattleShipsDocument document;
    public LightBattleShipsDocument doc() {return document;}
}
