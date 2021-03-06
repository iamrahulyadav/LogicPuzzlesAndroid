package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.data.HolidayIslandDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class HolidayIslandHelpActivity extends GameHelpActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState> {
    @Bean
    protected HolidayIslandDocument document;
    public HolidayIslandDocument doc() {return document;}
}
