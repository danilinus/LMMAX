package com.leroymerlin.lmmax;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import static com.leroymerlin.lmmax.Memory.otdel;

public class SeePhoto extends Activity {

    ImageView myImageView;

    float scl = 1;

    float px, py, oldx, oldy;

    int touchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    float dist0, distCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_photo);
        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), Function.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        Memory.getImageFromFile("MAPOTD"+otdel).continueWithTask(task -> {
            myImageView = (ImageView)findViewById(R.id.see_photo_img);
            distCurrent = 1;
            dist0 = 1; scl = myImageView.getScaleX();
            drawMatrix();
            findViewById(R.id.imgla).setOnTouchListener(MyOnTouchListener);
            touchState = IDLE;
            myImageView.setImageBitmap(task.getResult());
            return null;
        });
    }

    private void drawMatrix(){
        myImageView.setScaleX(scl * distCurrent/dist0);
        myImageView.setScaleY(myImageView.getScaleX());
    }

    View.OnTouchListener MyOnTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float distx, disty, x1 = 0, y1 = 0, x2 = 0, y2 = 0;
            switch(event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    oldx = myImageView.getX(); oldy = myImageView.getY();
                    px = event.getX(); py = event.getY();
                    touchState = TOUCH;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchState = PINCH;
                    distx = event.getX(0) - event.getX(1);
                    disty = event.getY(0) - event.getY(1);
                    dist0 = (float) Math.sqrt(distx * distx + disty * disty);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(touchState == PINCH){
                        x1 = event.getX(0); y1 = event.getY(0);
                        x2 = event.getX(1); y2 = event.getY(1);
                        distx = x1 - x2; disty = y1 - y2;
                        distCurrent = (float) Math.sqrt(distx * distx + disty * disty);
                        drawMatrix();
                    }else
                    {
                        myImageView.setX(event.getX() - px + oldx);
                        myImageView.setY(event.getY() - py + oldy);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    touchState = IDLE;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    scl = myImageView.getScaleX();
                    oldx = myImageView.getX(); oldy = myImageView.getY();
                    px = event.getX(); py = event.getY();
                    touchState = TOUCH;
                    break;
            }
            return true;
        }
    };
}
