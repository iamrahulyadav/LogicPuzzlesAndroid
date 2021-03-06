package com.zwstudio.logicpuzzlesandroid.home.android;

import android.content.Intent;
import android.media.AudioManager;
import android.widget.Button;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_home_main)
public class HomeMainActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}
    static final int CHOOSE_GAME_REQUEST = 123;

    @ViewById
    protected Button btnResumeGame;

    @AfterViews
    protected void init() {
        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    protected void onResume() {
        super.onResume();
        btnResumeGame.setText("Resume Game " + doc().gameProgress().gameTitle);
    }

    @Click
    protected void btnResumeGame() {
        resumeGame(doc().gameProgress().gameName, doc().gameProgress().gameTitle, true);
    }

    @Click
    protected void btnChooseGame() {
        HomeChooseGameActivity_.intent(this).startForResult(CHOOSE_GAME_REQUEST);
    }

    @Click
    protected void btnOptions() {
        HomeOptionsActivity_.intent(this).start();
    }

    private void resumeGame(String gameName, String gameTitle, boolean toResume) {
        doc().resumeGame(gameName, gameTitle);
        Class<?> cls = null;
        try {
            cls = Class.forName(String.format("com.zwstudio.logicpuzzlesandroid.puzzles.%s.android.%sMainActivity_",
                    gameName.toLowerCase(), gameName));
            Intent intent = new Intent(this, cls);
            intent.putExtra("toResume", toResume);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnActivityResult(CHOOSE_GAME_REQUEST)
    void onResult(int resultCode) {
        if (resultCode == RESULT_OK)
            btnResumeGame();
    }
}
