package com.example.ksketchandroid;

import android.graphics.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import java.util.Timer;

public class SketchModel extends Object {

	class Line implements Serializable{
		float x1,y1,x2,y2;
		public Line(float x1, float y1, float x2, float y2){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		public Line(){
			this.x1 = 0;
			this.y1 = 0;
			this.x2 = 0;
			this.y2 = 0;
		}
	}
	class Point implements Serializable{
		float x1,y1;
		public Point(float x1, float y1){
			this.x1 = x1;
			this.y1 = y1;
		}
		public Point(){
			this.x1 = 0;
			this.y1 = 0;
		}
	}
	
	public ArrayList<IView> views = new ArrayList<IView>();
	
	private int modes = 1;
	private boolean drag = false;
	public int pressedX, pressedY, draggedX, draggedY;
	
	private ArrayList<ArrayList<Line>> shapes = new ArrayList<ArrayList<Line>>();
	private ArrayList<HashMap<Integer, Point>> delta = new ArrayList<HashMap<Integer, Point>>();
	private ArrayList<Integer> colours = new ArrayList<Integer>();
	private ArrayList<Integer> strokes = new ArrayList<Integer>();
	private ArrayList<Boolean> selected = new ArrayList<Boolean>();
	
	private ArrayList<Point> coordChanges = new ArrayList<Point>();
	private ArrayList<Double> timeChanges = new ArrayList<Double>();
	
	private ArrayList<Line> lasso = new ArrayList<Line>();
	private ArrayList<Integer> six = new ArrayList<Integer>();
	
	private int currentColour = Color.BLACK;
	private Integer currentStroke = 1;
	
	private Point eraser = new Point();
	
	private boolean playing = false;
	private int time = 0;
	public static int timeMax = 5;
	public static int FPS = 40;
	public static int curMax = 0;
	public static boolean mouseUp = true;
	
	public static int bc = Color.WHITE;
	
	public Timer timer = new Timer();
	
	class MyTask extends TimerTask{
		public void run(){
			incrementTimer();
		}
	}
	
	MyTask myTask = new MyTask();
	// Override the default construtor, making it private.
	public SketchModel() {
	}
	
	
	public void saveGame(){
		try{
			FileOutputStream saveFile = new FileOutputStream("sketch.sav");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			
			save.writeObject(modes);
			save.writeObject(drag);
			save.writeObject(pressedX);
			save.writeObject(pressedY);
			save.writeObject(draggedX);
			save.writeObject(draggedY);
			
			
			save.writeObject(shapes.size());//
			for(ArrayList<Line> shape:shapes){
				save.writeObject(shape.size());//
				for(Line line:shape){
					save.writeObject(line.x1);
					save.writeObject(line.y1);
					save.writeObject(line.x2);
					save.writeObject(line.y2);
				}
			}//yes
			
			
			save.writeObject(delta.size());//
			for(HashMap<Integer, Point> hmap:delta){
				save.writeObject(hmap.size());//
				for (Map.Entry<Integer, Point> entry : hmap.entrySet()) {
					save.writeObject(entry.getKey());
					save.writeObject(entry.getValue().x1);
					save.writeObject(entry.getValue().y1);
				}
			}//yes
			
			
			save.writeObject(colours.size());//
			for(Integer c:colours){
				if(c==Color.BLACK)
					save.writeObject(0);
				else if(c==Color.RED)
					save.writeObject(1);
				else if(c==Color.YELLOW)
					save.writeObject(2);
				else if(c==Color.BLUE)
					save.writeObject(3);
			}
			
			
			//save.writeObject(strokes);
			save.writeObject(strokes.size());//
			for(Integer i:strokes){
				save.writeObject(i);
			}
			
			
			save.writeObject(selected);
			save.writeObject(coordChanges.size());//
			for (Point point:coordChanges) {
				save.writeObject(point.x1);
				save.writeObject(point.y1);
			}//yes
			save.writeObject(timeChanges);
			save.writeObject(lasso.size());//
			for(Line line:lasso){
				save.writeObject(line.x1);
				save.writeObject(line.y1);
				save.writeObject(line.x2);
				save.writeObject(line.y2);
			}
			save.writeObject(six);
			if(currentColour==Color.BLACK)
				save.writeObject(0);
			else if(currentColour==Color.RED)
				save.writeObject(1);
			else if(currentColour==Color.YELLOW)
				save.writeObject(2);
			else if(currentColour==Color.BLUE)
				save.writeObject(3);
			save.writeObject(currentStroke);
			save.writeObject(eraser.x1);//
			save.writeObject(eraser.y1);
			save.writeObject(playing);
			save.writeObject(time);
			save.writeObject(timeMax);
			save.writeObject(curMax);
			save.writeObject(mouseUp);
			System.out.println("si"+strokes.size());
			System.out.println(currentStroke);
			save.close();
		}catch(Exception e){}
	}
	public void loadGame(){
		try{
			FileInputStream saveFile = new FileInputStream("sketch.sav");
			ObjectInputStream save = new ObjectInputStream(saveFile);
			
			modes=(Integer)save.readObject();
			drag=(Boolean)save.readObject();
			pressedX=(Integer)save.readObject();
			pressedY=(Integer)save.readObject();
			draggedX=(Integer)save.readObject();
			draggedY=(Integer)save.readObject();
			
			
			shapes.clear();
			int count = (Integer)save.readObject();//#shapes
			for(int i=0;i<count;++i){//for shape
				int count2 = (Integer)save.readObject();//#lines
				ArrayList<Line> shapeArr = new ArrayList<Line>();
				for(int j=0;j<count2;++j){//for line
					shapeArr.add(new Line((Float)save.readObject(), (Float)save.readObject(), (Float)save.readObject(), (Float)save.readObject()));
				}
				shapes.add(shapeArr);
			}//yes
			
			
			delta.clear();
			count = (Integer)save.readObject();//#shapes
			for (int i=0;i<count;++i) {//for shape
				int count2 = (Integer)save.readObject();//#hmap entries
				HashMap<Integer, Point> deltaHash = new HashMap<Integer, Point>();
				for(int j=0;j<count2;++j){//for hmap
					deltaHash.put((Integer)save.readObject(), new Point((Float)save.readObject(), (Float)save.readObject()));
				}
				delta.add(deltaHash);
			}
			
			
			colours.clear();
			count = (Integer)save.readObject();//
			for(int i=0;i<count;++i){
				if((Integer)save.readObject()==0)
					colours.add(Color.BLACK);
				else if((Integer)save.readObject()==1)
					colours.add(Color.RED);
				else if((Integer)save.readObject()==2)
					colours.add(Color.YELLOW);
				else if((Integer)save.readObject()==3)
					colours.add(Color.BLUE);
			}
			
			
			//strokes=(ArrayList<Integer>)save.readObject();
			strokes.clear();
			count = (Integer)save.readObject();//#shapes
			for(int i=0;i<count;++i){
				strokes.add((Integer)save.readObject());
			}
			System.out.println("so"+strokes.size());
			
			
			selected=(ArrayList<Boolean>)save.readObject();
			coordChanges.clear();
			count = (Integer)save.readObject();//#shapes
			for (int i=0;i<count;++i) {
				coordChanges.add(new Point((Float)save.readObject(), (Float)save.readObject()));
			}
			timeChanges=(ArrayList<Double>)save.readObject();
			lasso.clear();
			count = (Integer)save.readObject();//#shapes
			for(int i=0;i<count;++i){
				lasso.add(new Line((Float)save.readObject(), (Float)save.readObject(), (Float)save.readObject(), (Float)save.readObject()));
			}
			six=(ArrayList<Integer>)save.readObject();
			
			if((Integer)save.readObject() == 0)
				currentColour = Color.BLACK;
			else if((Integer)save.readObject() == 1)
				currentColour = Color.RED;
			else if((Integer)save.readObject() == 2)
				currentColour = Color.YELLOW;
			else if((Integer)save.readObject() == 3)
				currentColour = Color.BLUE;
			currentStroke=(Integer)save.readObject();
			eraser = new Point((Float)save.readObject(), (Float)save.readObject());
			
			playing=(Boolean)save.readObject();
			time=(Integer)save.readObject();
			timeMax=(Integer)save.readObject();
			curMax=(Integer)save.readObject();
			mouseUp=(Boolean)save.readObject();
			System.out.println(shapes.toString());
			save.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		this.updateAllViews();
		
	}
	
	
	
	public int toPrevFrame(int time){
		return time - time%(1000/FPS);
	}
	
	public ArrayList<Integer> getSIX(){
		return six;
	}
	public void setSIX(ArrayList<Integer> six){
		this.six = six;
		this.updateAllViews();
	}
	
	public boolean getDrag(){
		return drag;
	}
	public void setDrag(boolean drag){
		this.drag = drag;
		if(drag){
			startTimer();
		}else{
			stopTimer();
		}
		this.updateAllViews();
	}
	
	public ArrayList<HashMap<Integer, Point>> getDelta(){
		return delta;
	}
	public void setDelta(ArrayList<HashMap<Integer, Point>> delta){
		this.delta = delta;
		this.updateAllViews();
	}
	
	public void startTimer(){
		timer.cancel();
		timer=new Timer();
		timer.scheduleAtFixedRate(myTask, 0, 1);
		playing = true;
		this.updateAllViews();
	}
	public void stopTimer(){
		timer.cancel();
		playing = false;
		this.updateAllViews();
	}
	
	public void incrementTimer(){
		
		if(time%(1000/FPS) == 0){
			if(drag){
				for(Integer i:six){
					if(delta.get(i).get(toPrevFrame(time))!=null){
						delta.get(i).put(time, new Point(draggedX-pressedX+draggedX-delta.get(i).get(toPrevFrame(time)).x1, draggedY-pressedY+draggedX-delta.get(i).get(toPrevFrame(time)).y1));
						//delta.get(i).put(time, new Point(draggedX-pressedX, draggedY-pressedY));
					}else{
						delta.get(i).put(time, new Point(draggedX-pressedX, draggedY-pressedY));
					}
					
				}
			}
			if(time>=timeMax*1000){
				timeMax+=1;
				System.out.println("doubled");
			}
			if(time>=curMax && mouseUp){
				System.out.println("time>=curMax:"+curMax);
				stopTimer();
			}
			this.updateAllViews();
			//System.out.println("update@"+time);
		}
		time++;
	}
	
	public ArrayList<Double> getTimeChanges(){
		return timeChanges;
	}
	public void setTimeChanges(ArrayList<Double> timeChanges){
		this.timeChanges = timeChanges;
		this.updateAllViews();
	}
	
	public ArrayList<Point> getCoordChanges(){
		return coordChanges;
	}
	public void setCoordChanges(ArrayList<Point> coordChanges){
		this.coordChanges = coordChanges;
		this.updateAllViews();
	}
	
	public int getTimeMax(){
		return timeMax;
	}
	public void setTimeMax(int timeMax){
		this.timeMax = timeMax;
		this.updateAllViews();
	}
	
	public int getTime(){
		return time;
	}
	public void setTime(int time){
		this.time = time;
		this.updateAllViews();
	}
	
	public boolean getPlaying(){
		return playing;
	}
	public void setPlaying(boolean playing){
		this.playing = playing;
		this.updateAllViews();
	}
	
	public void resetSelected(){
		for(int i = 0; i < selected.size(); ++i){
			selected.set(i, false);
		}
		this.updateAllViews();
	}
	
	public Point getEraser(){
		return eraser;
	}
	public void setEraser(Point eraser){
		this.eraser = eraser;
		this.updateAllViews();
	}
	
	public ArrayList<Boolean> getSelected(){
		return selected;
	}
	public void setSelected(ArrayList<Boolean> selected){
		this.selected = selected;
		this.updateAllViews();
	}
	
	public Integer getCurrentStroke(){
		return currentStroke;
	}
	public void setCurrentStroke(Integer currentStroke){
		this.currentStroke = currentStroke;
		this.updateAllViews();
	}
	
	public int getCurrentColour(){
		return currentColour;
	}
	public void setCurrentColour(int currentColour){
		this.currentColour = currentColour;
		this.updateAllViews();
	}
	
	public ArrayList<Integer> getStrokes(){
		return strokes;
	}
	public void setStrokes(ArrayList<Integer> strokeSizes){
		this.strokes = strokeSizes;
		this.updateAllViews();
	}
	
	public ArrayList<Integer> getColours(){
		return colours;
	}
	public void setColours(ArrayList<Integer> colours){
		this.colours = colours;
		this.updateAllViews();
	}
	
	public ArrayList<Line> getLasso(){
		return lasso;
	}
	public void setLasso(ArrayList<Line> lasso){
		this.lasso = lasso;
		this.updateAllViews();
	}
	
	public ArrayList<ArrayList<Line>> getShapes(){
		return shapes;
	}
	public void setShapes(ArrayList<ArrayList<Line>> shapes){
		this.shapes = shapes;
		this.updateAllViews();
	}
	
	public int getMode(){
		return this.modes;
	}
	public void setMode(int mode){
		this.modes = mode;
		this.updateAllViews();
	}
	
	/** Add a new view of this triangle. */
	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}

	/** Remove a view from this triangle. */
	public void removeView(IView view) {
		this.views.remove(view);
	}

	/** Update all the views that are viewing this triangle. */
	public void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}
}
