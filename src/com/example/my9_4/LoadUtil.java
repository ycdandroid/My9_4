package com.example.my9_4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;



import android.content.res.Resources;
import android.util.Log;
import android.view.View;

public class LoadUtil {
	
	/**
	 * ��������Ĳ�� 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return float[]{a,b,c}
	 */
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2){
		//�������ʸ�����ʸ����XYZ��ķ���ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	/**
	 * �������
	 * @param vector
	 * @return
	 */
	public static float[] vectorNormal(float[] vector)
	{
		//��������ģ
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	/**
	 * ��obj�ļ��ж�ȡ������Ϣ
	 * @param fname �ļ��� 
	 * @param r ��Դ����
	 * @param view ��ͼ����
	 * @return
	 */
	public static LoadedObjectVertexNormalTexture loadFromFile(String fname, Resources r, View view ){
		LoadedObjectVertexNormalTexture lo = null;
		ArrayList<Float> alv = new ArrayList<Float>(); //ԭʼ�����б�
		ArrayList<Float> alvResult = new ArrayList<Float>();//��������б�
		ArrayList<Integer> alFaceIndex = new ArrayList<Integer>(); //������װ�������б�,ÿ������ı��
		
		//ƽ��ǰ����������Ӧ�ĵ�ķ���������Map
    	//��HashMap��keyΪ��������� valueΪ�����ڵĸ�����ķ������ļ���
		HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();
		
    	ArrayList<Float> alt = new ArrayList<Float>();  
    	//�����������б�
    	ArrayList<Float> altResult = new ArrayList<Float>(); 
		
		try {
			InputStream in = r.getAssets().open(fname);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String temps = null;
			
			while((temps = br.readLine()) != null){
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {//ԭʼ��������
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				}else if (tempsa[0].trim().equals("vt")) {//ԭʼ��������
					alt.add(Float.parseFloat(tempsa[1]) / 2.0f);
					alt.add(Float.parseFloat(tempsa[2]) / 2.0f);
					
				}else if (tempsa[0].trim().equals("f")) {
					int[] index = new int[3];
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;//��Ϊ�к��Ǵ�1��ʼ�ģ����Ҫ��ȥ1
					float x0 = alv.get(3*index[0]); //��Ϊÿһ��x,y,z���꣬alv�����1��
					float y0 = alv.get(3*index[0] + 1);
					float z0 = alv.get(3*index[0] + 2);
					alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);
					
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;//��Ϊ�к��Ǵ�1��ʼ�ģ����Ҫ��ȥ1
					float x1 = alv.get(3*index[1]); //��Ϊÿһ��x,y,z���꣬alv�����1��
					float y1 = alv.get(3*index[1] + 1);
					float z1 = alv.get(3*index[1] + 2);
					alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
					
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;//��Ϊ�к��Ǵ�1��ʼ�ģ����Ҫ��ȥ1
					float x2 = alv.get(3*index[2]); //��Ϊÿһ��x,y,z���꣬alv�����1��
					float y2 = alv.get(3*index[2] + 1);
					float z2 = alv.get(3*index[2] + 2);
					alvResult.add(x2);
		      		alvResult.add(y2);
		      		alvResult.add(z2);
		      		
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		//ͨ��������������������0-1��0-2�����õ�����ķ�����
		      	    //��0�ŵ㵽1�ŵ������
		      		float vxa=x1-x0;
		      		float vya=y1-y0;
		      		float vza=z1-z0;
		      	    //��0�ŵ㵽2�ŵ������
		      		float vxb=x2-x0;
		      		float vyb=y2-y0;
		      		float vzb=z2-z0;
		      		//ͨ�������������Ĳ�����㷨����
		      		float[] vNormal=vectorNormal(getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));
		      		
		      		for (int tempIndex : index) {//��ÿ������ķ�����put��hmn��
						HashSet<Normal> hsn = hmn.get(tempIndex);
						if(hsn == null){
							hsn = new HashSet<Normal>();
						}
						hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
						hmn.put(tempIndex, hsn);
					}
		      		
		      		//������������
		      		int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1; 
		      		altResult.add(alt.get(2*indexTex));
		      		altResult.add(alt.get(2*indexTex + 1));
		      		
		      		indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1; 
		      		altResult.add(alt.get(2*indexTex));
		      		altResult.add(alt.get(2*indexTex + 1));
		      		
		      		indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1; 
		      		altResult.add(alt.get(2*indexTex));
		      		altResult.add(alt.get(2*indexTex + 1));
				}
			}
			
			//���ɶ�����������
			int size = alvResult.size();
			float[] vXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				vXYZ[i] = alvResult.get(i);
			}
			
			//���ɷ���������
			float[] nXYZ = new float[alFaceIndex.size() * 3];
			int c = 0;
			for (Integer i : alFaceIndex) {
				HashSet<Normal> sn = hmn.get(i);
				float[] t = Normal.getAverage(sn);
				nXYZ[c++] = t[0];
				nXYZ[c++] = t[1];
				nXYZ[c++] = t[2];
			}
			
			//����������������
			int tSize = altResult.size();
			float[] tST =  new float[tSize];
			for (int i = 0; i < tSize; i++) {
				tST[i] = altResult.get(i);
			}
			
			lo = new LoadedObjectVertexNormalTexture(view, vXYZ, nXYZ, tST);
			
		} catch (Exception e) {
			Log.d("loatutil", "loadFromFile"+fname+"error");
			e.printStackTrace();
		}
		
    	
    	
		return lo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}