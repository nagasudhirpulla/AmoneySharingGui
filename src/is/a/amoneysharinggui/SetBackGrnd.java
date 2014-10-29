package is.a.amoneysharinggui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SetBackGrnd implements OnTouchListener {
	
	final private View frlyt;
	final private boolean dir; 
	final private boolean left; 
	final private float curwid; 
	final private float arstrt; 
	final private int color;
	final private int color2;
	SetBackGrnd(View fr,boolean di, boolean le, float curw, float arst, int co, int co2){
		frlyt = fr;
		dir = di;
		left = le;
		curwid = curw;
		arstrt = arst;
		color = co;
		color2 = co2;
		drawoncanvas(false);
		frlyt.setOnTouchListener(this);
	}

	private void drawoncanvas(final boolean touched)
	{
		frlyt.setBackgroundDrawable(new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				//do the drawing
				float[] hsv = new float[3];
				PointF pt;
				Paint paint = new Paint();
				paint.setAntiAlias(true); // enable anti aliasing
				if(touched)
				{
					paint.setColor(color2); // set default color to white
					Color.colorToHSV (color2, hsv);
				}
				else
				{
					paint.setColor(color); // set default color to white
					Color.colorToHSV (color, hsv);
				}
				paint.setDither(true); // enable dithering
				paint.setStyle(Paint.Style.FILL); // set to STOKE
				paint.setStrokeJoin(Paint.Join.ROUND); // set the join to round you want
				paint.setStrokeCap(Paint.Cap.ROUND);  // set the paint cap to round too
				float p1x,p2x,p3x,p4x,p1y,p2y,p3y,p4y;
				Path path = new Path();
				RectF rec = new RectF(0,0,frlyt.getWidth(),frlyt.getHeight());
				//float recw = rec.width();
				float rech = rec.height();		
				PointF[] _points;
				if(dir)
				{
					_points = new PointF[]
							{
							new PointF(rec.left+rech*curwid,rec.top),
							new PointF(rec.right*arstrt-rech*curwid,rec.top),
							new PointF(rec.right*arstrt+rech*curwid,rec.top+rech*curwid),
							new PointF(rec.right,rec.top+rech*(0.50f-0.25f*curwid)),
							new PointF(rec.right,rec.top+rech*(0.50f+0.25f*curwid)),
							new PointF(rec.right*arstrt+rech*curwid,rec.bottom-rech*curwid),
							new PointF(rec.right*arstrt-rech*curwid,rec.bottom),
							new PointF(rec.left+rech*curwid,rec.bottom),
							new PointF(rec.left,rec.bottom-rech*curwid),
							new PointF(rec.left,rec.top+rech*curwid)				
							};

					path.moveTo(_points[0].x, _points[0].y);
					for(int i=0;i<5;i++)
					{
						p1x = _points[2*i].x;
						p1y = _points[2*i].y;
						p2x = _points[2*i+1].x;
						p2y = _points[2*i+1].y;
						path.lineTo(p2x, p2y);
						p3x = _points[(2*i+2)%10].x;
						p3y = _points[(2*i+2)%10].y;
						p4x = _points[(2*i+3)%10].x;
						p4y = _points[(2*i+3)%10].y;
						pt = findinter(_points[2*i],_points[2*i+1],_points[(2*i+2)%10],_points[(2*i+3)%10]);
						path.cubicTo(p2x, p2y, pt.x, pt.y, p3x, p3y);
					}
					if(left == true)
					{
						canvas.save();
						canvas.rotate(180, frlyt.getWidth()/2, frlyt.getHeight()/2);
						canvas.drawPath(path, paint);
						//now set the gradient shader
						Shader shader = new LinearGradient(0, 0, 0, rec.bottom, Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.2f}), Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.1f}), TileMode.CLAMP);
						paint.setShader(shader);
						canvas.drawPath(path, paint);
						//draw the gloss
						shader = new LinearGradient(0, 0, 0, rec.bottom,
					            new int[] {0x00ffffff, 0x00ffffff, 0x11ffffff, 0x22ffffff, 0x44ffffff }, //substitute the correct colors for these
					            new float[] {
								0, 0.50f, 0.50f, 0.55f, 1 },
					            Shader.TileMode.CLAMP);
						paint.setShader(shader);
						//scale the path to show the border
						canvas.scale(0.99f, 0.97f,rec.width()*0.5f,rec.height()*0.5f);
						canvas.drawPath(path, paint);
						////////////
						canvas.restore();
					}
					else
					{
						canvas.drawPath(path, paint);
						//now set the gradient shader
						Shader shader = new LinearGradient(0, 0, 0, rec.bottom, Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.2f}), Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.1f}), TileMode.CLAMP);
						paint.setShader(shader);
						canvas.drawPath(path, paint);
						//draw the gloss
						shader = new LinearGradient(0, 0, 0, rec.bottom,
					            new int[] { 
					                0x44ffffff, 
					                0x22ffffff, 
					                0x11ffffff,
					                0x00ffffff,
					                0x00ffffff }, //substitute the correct colors for these
					            new float[] {
					                0, 0.450f, 0.50f, 0.5f, 1 },
					            Shader.TileMode.CLAMP);
						paint.setShader(shader);
						//scale the path to show the border
						canvas.save();
						canvas.scale(0.99f, 0.97f,rec.width()*0.5f,rec.height()*0.5f);
						canvas.drawPath(path, paint);
						canvas.restore();
						////////////
					}
				}
				else
				{
					_points = new PointF[]
							{
							new PointF(rec.left+rech*curwid,rec.top),
							new PointF(rec.right-rech*curwid,rec.top),
							new PointF(rec.right,rec.top+rech*curwid),
							new PointF(rec.right,rec.bottom-rech*curwid),
							new PointF(rec.right-rech*curwid,rec.bottom),
							new PointF(rec.left+rech*curwid,rec.bottom),
							new PointF(rec.left,rec.bottom-rech*curwid),
							new PointF(rec.left,rec.top+rech*curwid)				
							};
					path.moveTo(_points[0].x, _points[0].y);
					for(int i=0;i<4;i++)
					{
						p2x = _points[2*i+1].x;
						p2y = _points[2*i+1].y;
						path.lineTo(p2x, p2y);
						p3x = _points[(2*i+2)%8].x;
						p3y = _points[(2*i+2)%8].y;
						if(i%2==0)
							path.cubicTo(p2x, p2y, p3x, p2y, p3x, p3y);
						else
							path.cubicTo(p2x, p2y, p2x, p3y, p3x, p3y);
					}			
					canvas.drawPath(path, paint);
					
					//now set the gradient shader
					Shader shader = new LinearGradient(0, 0, 0, rec.bottom, Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.2f}), Color.HSVToColor(255,new float[]{hsv[0],hsv[1],hsv[2]+0.1f}), TileMode.CLAMP);
					paint.setShader(shader);
					canvas.drawPath(path, paint);
					//draw the gloss
					shader = new LinearGradient(0, 0, 0, rec.bottom,
				            new int[] { 
				                0x44ffffff, 
				                0x22ffffff, 
				                0x11ffffff,
				                0x00ffffff,
				                0x00ffffff }, //substitute the correct colors for these
				            new float[] {
				                0, 0.450f, 0.50f, 0.5f, 1 },
				            Shader.TileMode.CLAMP);
					paint.setShader(shader);
					//scale the path to show the border
					canvas.save();
					canvas.scale(0.99f, 0.97f,rec.width()*0.5f,rec.height()*0.5f);
					canvas.drawPath(path, paint);
					canvas.restore();
					////////////					
				}
			}
			@Override
			public int getOpacity() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public void setAlpha(int alpha) {
				// TODO Auto-generated method stub

			}
			public void setColorFilter(ColorFilter cf) {
				// TODO Auto-generated method stub

			}			
		}
				);
	}

	private PointF findinter(PointF pointF, PointF pointF2, PointF pointF3,
			PointF pointF4) {
		float a1,b1,c1,a2,b2,c2;
		if(pointF.x == pointF2.x)
		{
			a1 = 1;
			b1 = 0;
			c1 = pointF.x;
		}
		else if(pointF.y == pointF2.y)
		{
			a1 = 0;
			b1 = 1;
			c1 = pointF.y;
		}
		else 
		{
			a1 = (pointF2.y - pointF.y)/(pointF2.x - pointF.x);
			b1 = -1;
			c1 = a1*pointF.x+b1*pointF.y;
		}
		if(pointF3.x == pointF4.x)
		{
			a2 = 1;
			b2 = 0;
			c2 = pointF3.x;
		}
		else if(pointF3.y == pointF4.y)
		{
			a2 = 0;
			b2 = 1;
			c2 = pointF3.y;
		}
		else 
		{
			a2 = (pointF4.y - pointF3.y)/(pointF4.x - pointF3.x);
			b2 = -1;
			c2 = a2*pointF3.x+b2*pointF3.y;
		}
		return new PointF((c1*b2-c2*b1)/(a1*b2-a2*b1),(c1*a2-c2*a1)/(b1*a2-b2*a1));
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			drawoncanvas(true);
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			drawoncanvas(false);
			
		}
		return false;
	}

}
