package com.zwstudio.logicgamesandroid.clouds.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.clouds.data.CloudsDocument;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGame;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameMove;
import com.zwstudio.logicgamesandroid.clouds.domain.CloudsGameState;
import com.zwstudio.logicgamesandroid.common.android.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_clouds_main)
public class CloudsMainActivity extends MainActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState> {
    public CloudsDocument doc() {return app.cloudsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        CloudsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        CloudsGameActivity_.intent(this).start();
    }
}
