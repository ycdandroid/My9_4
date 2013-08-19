package com.example.my9_4;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView {

	final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
	SceneRenderer mRenderer;
	float mPreviousX;
	float mPreviousY;
	
	int textureId;
	
	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e){
		float x = e.getX();
		float y = e.getY();
		
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;//计算触控笔Y位移
            float dx = x - mPreviousX;//计算触控笔X位移
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//设置沿z轴旋转角度
            requestRender();//重绘画面
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
	
	class SceneRenderer implements GLSurfaceView.Renderer{

		float xAngle;
		float yAngle;
		@Override
		public void onDrawFrame(GL10 arg0) {
			
		}

		@Override
		public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public int initTexture(int drawableId){
		
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int textureId = textures[0];
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		
		InputStream is = this.getResources().openRawResource(drawableId);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		GLUtils.texImage2D(
				GLES20.GL_TEXTURE_2D, 
				0, 
				GLUtils.getInternalFormat(bitmap),
				bitmap, GLUtils.getType(bitmap),
				0);
		bitmap.recycle();
		
		return textureId;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
