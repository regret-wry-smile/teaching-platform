package com.ejet.core.util;

import com.zkxltech.config.ConfigConstant;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 临时授权文件
 *
 * @author ShenYijie
 *
 */
public class TempAuth {

    public static final byte[] key = {0x61, 0x64, 0x6d, 0x69, 0x6e, 0x40, 0x7a, 0x6b, 0x78, 0x6c, 0x74, 0x65, 0x63, 0x68, 0x2e, 0x63, 0x6f, 0x6d, 0x24, 0x31, 0x32, 0x33};

    /**
     * 验证是否通过
     *
     * @return
     */
    public static boolean isAuth() throws Exception {
        //首先读取授权信息
        if (!isValid()) {
            return false;
            //throw new RuntimeException("授权2018年8月12日，已经过期!");
        }
        return true;
    }

    public static boolean isValid() throws Exception {
        //String webUrl3 = "http://www.taobao.com";// 淘宝
        String webUrl = "http://www.baidu.com";// 百度
        Calendar calendar = Calendar.getInstance();
        //到期时间2019.11.05
//        calendar.set(2019, 10, 5);
//        Date validDate = calendar.getTime();
        Date validDate =  decrypt();
        Date currentDate = getNetworkTime(webUrl);
        if (validDate.after(currentDate)) {
            return true;
        }
        return false;
    }

    /**
     * 获取网络时间
     *
     * @param webUrl
     * @return
     * @throws IOException
     */
    public static Date getNetworkTime(String webUrl) {
        URL url;
        Date date = new Date();
        try {
            url = new URL(webUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.connect();
            long dateL = conn.getDate();
            date = new Date(dateL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 加密验证码
     * JDK实现Base64编码
     * @return xuyuanjie
     */
    public static String encryption() throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        String src = "";               //需要加密的原始字符串
        String encode = encoder.encode(src.getBytes());//编码
        return encode;
    }
    /**
     * 解密验证码
     *
     * @return xuyuanjie
     */
    public static Date decrypt() throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        String encode = ConfigConstant.projectConf.getJurisdiction();
        String str = new String(decoder.decodeBuffer(encode));//解码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date decode = sdf.parse(str);
        return decode;
    }

}
//
//	public static void main(String[] args) throws Exception {
//		String webUrl3 = "http://www.taobao.com";// 淘宝
//		String webUrl2 = "http://www.baidu.com";// 百度
////		System.out.println(getNetworkTime(webUrl3));
////		System.out.println(getNetworkTime(webUrl2));
//		String kk = "admin@zkxltech.com$123";
//		String ll = Tools.hexlog(kk.getBytes());
//		System.out.println(ll);
//		System.out.println(isAuth());
//
//
//
//	}
//

