package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove;

/**
 * TODO: document your custom view class.
 */
public class ABCPathGameView extends CellsGameView {

    private ABCPathGameActivity activity() {return (ABCPathGameActivity)getContext();}
    private ABCPathGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public ABCPathGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public ABCPathGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ABCPathGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
            }
        if (isInEditMode()) return;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                char ch = game().getObject(r, c);
                if (ch == ' ') continue;
                HintState s = game().getState(r, c);
                textPaint.setColor(
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        game().get(r, c) == ' ' ? Color.GRAY : Color.WHITE
                );
                String text = String.valueOf(ch);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            ABCPathGameMove move = new ABCPathGameMove() {{
                p = new Position(row, col);
                obj = ' ';
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
