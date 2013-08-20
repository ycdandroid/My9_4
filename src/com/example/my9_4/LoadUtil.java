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
	 * 获得向量的叉积 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return float[]{a,b,c}
	 */
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2){
		//求出两个矢量叉积矢量在XYZ轴的分量ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	/**
	 * 向量规格化
	 * @param vector
	 * @return
	 */
	public static float[] vectorNormal(float[] vector)
	{
		//求向量的模
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	/**
	 * 从obj文件中读取顶点信息
	 * @param fname 文件名 
	 * @param r 资源对象
	 * @param view 视图对象
	 * @return
	 */
	public static LoadedObjectVertexNormalTexture loadFromFile(String fname, Resources r, View view ){
		LoadedObjectVertexNormalTexture lo = null;
		ArrayList<Float> alv = new ArrayList<Float>(); //原始顶点列表
		ArrayList<Float> alvResult = new ArrayList<Float>();//结果顶点列表
		ArrayList<Integer> alFaceIndex = new ArrayList<Integer>(); //顶点组装面索引列表,每个顶点的编号
		
		//平均前各个索引对应的点的法向量集合Map
    	//此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
		HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();
		
    	ArrayList<Float> alt = new ArrayList<Float>();  
    	//纹理坐标结果列表
    	ArrayList<Float> altResult = new ArrayList<Float>(); 
		
		try {
			InputStream in = r.getAssets().open(fname);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String temps = null;
			
			while((temps = br.readLine()) != null){
				String[] tempsa = temps.split("[ ]+");
				if (tempsa[0].trim().equals("v")) {//原始顶点坐标
					alv.add(Float.parseFloat(tempsa[1]));
					alv.add(Float.parseFloat(tempsa[2]));
					alv.add(Float.parseFloat(tempsa[3]));
				}else if (tempsa[0].trim().equals("vt")) {//原始纹理坐标
					alt.add(Float.parseFloat(tempsa[1]) / 2.0f);
					alt.add(Float.parseFloat(tempsa[2]) / 2.0f);
					
				}else if (tempsa[0].trim().equals("f")) {
					int[] index = new int[3];
					index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;//因为行号是从1开始的，因此要减去1
					float x0 = alv.get(3*index[0]); //因为每一行x,y,z坐标，alv各添加1次
					float y0 = alv.get(3*index[0] + 1);
					float z0 = alv.get(3*index[0] + 2);
					alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);
					
					index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;//因为行号是从1开始的，因此要减去1
					float x1 = alv.get(3*index[1]); //因为每一行x,y,z坐标，alv各添加1次
					float y1 = alv.get(3*index[1] + 1);
					float z1 = alv.get(3*index[1] + 2);
					alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
					
					index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;//因为行号是从1开始的，因此要减去1
					float x2 = alv.get(3*index[2]); //因为每一行x,y,z坐标，alv各添加1次
					float y2 = alv.get(3*index[2] + 1);
					float z2 = alv.get(3*index[2] + 2);
					alvResult.add(x2);
		      		alvResult.add(y2);
		      		alvResult.add(z2);
		      		
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		//通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
		      	    //求0号点到1号点的向量
		      		float vxa=x1-x0;
		      		float vya=y1-y0;
		      		float vza=z1-z0;
		      	    //求0号点到2号点的向量
		      		float vxb=x2-x0;
		      		float vyb=y2-y0;
		      		float vzb=z2-z0;
		      		//通过求两个向量的叉积计算法向量
		      		float[] vNormal=vectorNormal(getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));
		      		
		      		for (int tempIndex : index) {//把每个顶点的法向量put到hmn中
						HashSet<Normal> hsn = hmn.get(tempIndex);
						if(hsn == null){
							hsn = new HashSet<Normal>();
						}
						hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
						hmn.put(tempIndex, hsn);
					}
		      		
		      		//加载纹理坐标
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
			
			//生成顶点坐标数组
			int size = alvResult.size();
			float[] vXYZ = new float[size];
			for (int i = 0; i < size; i++) {
				vXYZ[i] = alvResult.get(i);
			}
			
			//生成法向量数组
			float[] nXYZ = new float[alFaceIndex.size() * 3];
			int c = 0;
			for (Integer i : alFaceIndex) {
				HashSet<Normal> sn = hmn.get(i);
				float[] t = Normal.getAverage(sn);
				nXYZ[c++] = t[0];
				nXYZ[c++] = t[1];
				nXYZ[c++] = t[2];
			}
			
			//生成纹理坐标数组
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