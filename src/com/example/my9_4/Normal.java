package com.example.my9_4;

import java.util.Set;

/**
 * һ�������ʾһ��������
 * @author yinchuandong
 *
 */
public class Normal {
	public final static float DIFF = 0.0000001f;
	float nx;
	float ny;
	float nz;
	
	public Normal(float nx, float ny, float nz){
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Normal) {
			Normal tn=(Normal)o;
			   if(Math.abs(nx-tn.nx)<DIFF &&
				  Math.abs(ny-tn.ny)<DIFF &&
				  Math.abs(ny-tn.ny)<DIFF
	             )
			   {
				   return true;
			   }else{
				   return false;
			   }
			
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return 1;
	}
	
	/**
	 * ������ƽ��ֵ�Ĺ��߷���
	 * @param sn
	 * @return float[]
	 */
	public static float[] getAverage(Set<Normal> sn){
		float[] result = new float[3];
		for (Normal n : sn) {
			result[0] += n.nx;
			result[1] += n.ny;
			result[2] += n.nz;
		}
		return LoadUtil.vectorNormal(result);
	}

}
