package com.tite.system.comfc.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 图片处理工具类
 * @author yangh 2012-04-20
 * @version 6.2.0
 * */
public class ImageUtils {
	/**
	 * 获取图片流的字节数组
	 * param input 图片输入流
	 * param size 字节数组大小
	 * @return 返回图片流的字节数组
	 * */
	public static byte[] getByteArrayFromInputStream(InputStream input, int size) {
        byte[] content = null;
        ByteArrayOutputStream out = null;
        try {
            byte[] bytes = new byte[size];
            int len;
            out = new ByteArrayOutputStream();
            while ((len = input.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            input.close();
            content = out.toByteArray();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (out != null){
            	try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }  
        }
        return content;

    }
}
