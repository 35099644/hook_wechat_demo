package me.robin.xposed_wx_hook;

import java.io.*;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class FileReadUtils {
    public static String read(String path) throws IOException {
        File file = new File(path);
        FileInputStream inStream = new FileInputStream(file);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, length);
        }
        outStream.close();
        inStream.close();
        return outStream.toString();
    }
}
