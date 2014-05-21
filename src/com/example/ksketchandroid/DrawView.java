package com.example.ksketchandroid;

import com.example.ksketchandroid.SketchModel.Line;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View implements IView{
	
	SketchModel model;
	
	public DrawView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	/*public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // draw a solid blue circle
        paint.setColor(Color.BLUE);
        canvas.drawCircle(20, 20, 15, paint);
    }*/
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
		this.setBackgroundColor(model.bc);
		
		for(int i = 0; i < model.getShapes().size(); ++i){//for every shape
			paint.setStrokeWidth(model.getStrokes().get(i));
			if(model.getSelected().get(i) == false){
				paint.setColor(model.getColours().get(i));
			}else{
				paint.setColor(Color.GREEN);
				//System.out.println("brighter!");
			}
			int dx = 0, dy = 0;
			dx = (int)(model.getDelta().get(i).get(model.toPrevFrame(model.getTime())).x1);
			dy = (int)(model.getDelta().get(i).get(model.toPrevFrame(model.getTime())).y1);
			//System.out.println("dx:"+dx+" dy:"+dy);
			if(model.pressedX==-dx&&model.pressedY==-dy){
				dx=0;dy=0;
			}
			//g.translate(dx, dy);
			for(int j = 0; j < model.getShapes().get(i).size(); ++j){//for every line in shape
				canvas.drawLine((int)model.getShapes().get(i).get(j).x1+dx, (int)model.getShapes().get(i).get(j).y1+dy, (int)model.getShapes().get(i).get(j).x2+dx, (int)model.getShapes().get(i).get(j).y2+dy, paint);
			}
			//g.translate(-dx, -dy);
		}
		postInvalidate();
	}

	public SketchModel getModel() {
		return model;
	}

	public void setModel(SketchModel model) {
		this.model = model;
	}

	@Override
	public void updateView() {
		postInvalidate();
	}
	
	/*public void onCreate (Bundle savedInstanceState)
    {
     super.onCreate (savedInstanceState);
     setContentView (R.layout.main);
     ImageView iv = (ImageView) findViewById(R.id.ImageView01);
     iv.setOnTouchListener (this);

//this draws my lines, but I loose the pic above ^^^
     myPainter mp = new myPainter(this);        
     setContentView(mp);
    }*/
}
