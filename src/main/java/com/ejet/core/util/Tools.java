package com.ejet.core.util;

public class Tools {
	
	/**
     * 16进制字符数组转成String
     * @param data 16进制字符数组
     * @return String 转换后的字符串
     */
    public static String hexStr(byte[] data){
    	StringBuffer logmes = new StringBuffer();		
    	
    	for(int i = 0;i < data.length; i ++){
    		
    		String temp = Integer.toHexString(data[i] & 0xff);
    		
    		if(temp.length() == 1){
    			logmes.append("0");
    		}
    		logmes.append(temp);
    	}
    	
    	return logmes.toString();
    }
    
    /**
     * 记录日志（16进制）
     * @param data 16进制的字符数组
     * @return String 转换后的字符串
     */
    public static String hexlog(byte[] data){
        /**
         * 记录日志
         */
    	StringBuffer logmes = new StringBuffer();		
    	
    	for(int i = 0;i < data.length; i ++){
    		
    		String temp = Integer.toHexString(data[i] & 0xff);
    		
    		if(temp.length() == 1){
    			logmes.append("0");
    		}
    		logmes.append(temp)
    			  .append(" ");
    	}
    	
    	return logmes.toString();
    }
    
    /**
     * @函数功能: 压缩的BCD码转为10进制串(阿拉伯数据)
     * @输入参数: bytes 压缩的BCD码
     * @输出结果: String 10进制 
     */
    public static String bcd2Dec(byte[] bytes){
         StringBuffer temp = new StringBuffer(bytes.length*2);
            
         for(int i = 0; i < bytes.length; i ++){
              temp.append((byte)((bytes[i]& 0xf0)>>>4));  
              temp.append((byte)(bytes[i]& 0x0f));
         }
         return temp.toString();
    }
    
    
    /**
     * @函数功能: 10进制串(阿拉伯数据)转为压缩的BCD码
     * @输入参数: dec 10进制
     * @输出结果: byte[] 压缩的BCD码
     */
    public static byte[] dec2Bcd(String dec){
    	//转换后的长度
    	int declen = dec.length() / 2;
    	if(dec.length() % 2 == 1){
    		declen ++;
    		dec = "0" + dec;
    	}
    	byte[] bcd = new byte[declen];
    	//赋值
    	for(int i = 0 ; i < declen; i ++){
    		int f = Integer.parseInt(dec.substring(2 * i, 2 * i + 1));
    		int s = Integer.parseInt(dec.substring(2 * i + 1,2 * i + 2));
    		bcd[i] = (byte)(f * 16 + s);
    	}
    	return bcd;
    }
    

}
