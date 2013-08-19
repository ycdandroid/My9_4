package com.example.my9_4;

import java.nio.FloatBuffer;

import android.R.integer;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.view.View;

public class LoadedObjectVertexOnly {
	int mProgram;
	int muMVPMatrixHandle;
	int muMMatrixHandle;
	int muLightLocationHandle;
	int muCameraHandle;
	
	int maPositionHandle;
	int maTexCoorHandle;
	int maNormalHandle;
	
	String mVertexShader;
	String mFragmentShader;
	
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer;
	FloatBuffer mNormalBuffer;
	
	int vCount;
	int mtextureId;
	
	public LoadedObjectVertexOnly(View view, float[] vertices, float[] normals, float[] texCoors){
		initVertexData(vertices, normals, texCoors);
		initShader(view);
	}
	
	
	public void initVertexData(float vertices[], float normals[], float[] texCoors){
		
		
		
	}
	
	public void initShader(View view){
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", view.getResources());
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", view.getResources());
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
		muCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		
	}
	
	public void drawSelf(int texId){
		
	}
	
	
	
	
	
	
	
	
	
	
}
