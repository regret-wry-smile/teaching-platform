package com.ejet.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.core.util.IOUtils;

import com.ejet.core.util.constant.Constant;
import com.zkxltech.domain.Result;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    
    public static Result postData(String url , String data) {
    	Result result = new Result();
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
          .url(url)
          .post(body)
          .addHeader("Content-Type", "application/x-www-form-urlencoded")
          .addHeader("Cache-Control", "no-cache")
//          .addHeader("Postman-Token", "679b42c0-ef0d-b63c-18ef-d51633bc9da2")
          .build();

        try {
            Response response = client.newCall(request).execute();
            InputStream input = response.body().byteStream() ;
//            System.out.println("http请求："+response.isSuccessful());
            result.setRet(response.isSuccessful() ? Constant.SUCCESS : Constant.ERROR);
            if (Constant.ERROR.equals(result.getRet())) {
				result.setMessage("服务器连接异常！");
			}
            result.setItem(IOUtils.toString(new InputStreamReader(input, "gbk") ));
            return result ;
        } catch (IOException e) {
        	 result.setRet(Constant.ERROR);
             result.setMessage("服务器连接异常！");
             return result ;
        }
    }
}
