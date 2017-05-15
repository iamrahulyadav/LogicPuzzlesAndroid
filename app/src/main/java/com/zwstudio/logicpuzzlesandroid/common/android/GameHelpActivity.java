
package com.zwstudio.logicpuzzlesandroid.common.android;

import android.widget.ListView;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class GameHelpActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM>, GM, GS extends GameState>
        extends BaseActivity {
    public abstract GD doc();

    @ViewById
    protected ListView lvHelp;

    protected void init() {
    }
}
