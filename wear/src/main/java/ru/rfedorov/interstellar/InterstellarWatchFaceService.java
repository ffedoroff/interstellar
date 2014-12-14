package ru.rfedorov.interstellar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.view.SurfaceHolder;

public class InterstellarWatchFaceService extends CanvasWatchFaceService {


    @Override
    public Engine onCreateEngine() {
        /* provide your watch face implementation */
        return new Engine();
    }

    /* implement service callback methods */
    private class Engine extends CanvasWatchFaceService.Engine {
        private Bitmap mBackgroundBitmap;

        @Override
        public void onCreate(SurfaceHolder holder) {
            /* initialize your watch face */
            Resources resources = InterstellarWatchFaceService.this.getResources();
            Drawable backgroundDrawable = resources.getDrawable(R.drawable.preview_analog_circular);
            mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            /* get device features (burn-in, low-bit ambient) */
        }

        @Override
        public void onTimeTick() {
            /* the time changed */
            super.onTimeTick();

            invalidate();

        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            /* the wearable switched between modes */
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, new Paint());

            /* draw your watch face */
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            /* the watch face became visible or invisible */
        }
    }

}
