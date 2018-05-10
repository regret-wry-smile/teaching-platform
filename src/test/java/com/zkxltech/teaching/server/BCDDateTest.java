package com.zkxltech.teaching.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BCDDateTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date time = new Date();
		List<Byte> list = new ArrayList<Byte>();
        //SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = dateFormater.format(time);
        System.out.println(timeStr);
        for (int i = 0; i < timeStr.length();i++) {
            if (i%2==1){
            	String num = String.format("%s%s",timeStr.charAt(i-1),timeStr.charAt(i));
                list.add(Byte.valueOf(num, 16));
            }
            if (i+1 == timeStr.length()){
                list.add(Byte.valueOf(String.format("%s%s","0",timeStr.charAt(i)),16));
            }
        }
        System.out.println(list.toString());
		
		int num = 20;
		toBCD(num);
		num = 600;
		toBCD(num);
		int i = Integer.parseInt("99", 16);
		System.out.println("i:" + i);
		//System.out.println(Byte.valueOf("99", 16));
	}
	
	
	/**
	 * 十进制转bcd码
	 * 
	 * @param num
	 */
	public static void toBCD(int num) {
		String numStr = Integer.toString(num);
		Integer bcdObj = Integer.parseInt(numStr, 16);
		int bcdValue = bcdObj.intValue();
		String bcdReverseStr = Integer.toHexString(bcdValue);
		
		
		
		int numReverse = Integer.parseInt(bcdReverseStr);
		System.out.println("Num:" + num + ", numStr:" + numStr + 
				", bcdObj:" + bcdObj + ", bcdValue:" + bcdValue + 
				", bcdReverseStr:" + bcdReverseStr + ", numReverse:" + numReverse );
	}
	
	

}
