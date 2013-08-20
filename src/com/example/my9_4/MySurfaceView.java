package com.example.my9_4;

import java.io.Console;
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
import android.util.Log;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView {

	final float TOUCH_SCALE_FACTOR = 180.0f/320;//Ω«∂»Àı∑≈±»¿˝
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
			float dy = y - mPreviousY;//º∆À„¥•øÿ± YŒª“∆
            float dx = x - mPreviousX;//º∆À„¥•øÿ± XŒª“∆
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//…Ë÷√—ÿx÷·–˝◊™Ω«∂»
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//…Ë÷√—ÿz÷·–˝◊™Ω«∂»
            requestRender();//÷ÿªÊª≠√Ê
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
	
	class SceneRenderer implements GLSurfaceView.Renderer{

		float xAngle;
		float yAngle;
		
		LoadedObjectVertexNormalTexture lovo;
		
		@Override
		public void onDrawFrame(GL10 arg0) {
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			MatrixState.pushMatrix();
			MatrixState.translate(0, -2, -25);
			MatrixState.rotate(xAngle, 1, 0, 0);
			MatrixState.rotate(yAngle, 0, 1, 0);
			
			if (lovo != null) {
				lovo.drawSelf(textureId);
			}
			MatrixState.popMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 arg0, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			float ratio = (float)width/height;
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
			MatrixState.setCamera(0, 0, 0, 0, 0, -1, 0, 1, 0);
		}

		@Override
		public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
			GLES20.glClearColor(0.5f, 0, 0, 1);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			
			MatrixState.setInitStack();
			MatrixState.setLightLocation(40, 10, 20);
			lovo = LoadUtil.loadFromFile("ch_t.obj", MySurfaceView.this.getResources(), MySurfaceView.this);
			textureId = initTexture(R.drawable.ghxp);
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
