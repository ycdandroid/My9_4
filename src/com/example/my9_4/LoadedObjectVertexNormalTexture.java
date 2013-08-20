package com.example.my9_4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.R.integer;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.view.View;

public class LoadedObjectVertexNormalTexture {
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
	
	public LoadedObjectVertexNormalTexture(View view, float[] vertices, float[] normals, float[] texCoors){
		initVertexData(vertices, normals, texCoors);
		initShader(view);
	}
	
	
	public void initVertexData(float vertices[], float normals[], float[] texCoors){
		vCount = vertices.length / 3;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		
		ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		mNormalBuffer = nbb.asFloatBuffer();
		mNormalBuffer.put(normals);
		mNormalBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(vertices.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer = tbb.asFloatBuffer();
		mTexCoorBuffer.put(texCoors);
		mTexCoorBuffer.position(0);
		
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
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(),0);
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
		GLES20.glUniform3fv(muLightLocationHandle, 1, MatrixState.lightPositionFB);
		GLES20.glUniform3fv(muCameraHandle, 1, MatrixState.cameraFB);
	
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertexBuffer);
		GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3*4, mNormalBuffer);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2*4, mTexCoorBuffer);
		
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
