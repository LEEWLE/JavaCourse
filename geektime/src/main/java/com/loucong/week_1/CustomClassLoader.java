package com.loucong.week_1;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * @author loucong 2021/9/16 23:26
 */

// 作业2
public class CustomClassLoader extends ClassLoader {

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        String encodeClassFile = readFile();
        if (encodeClassFile == "") {
            return super.findClass(name);
        }
        byte[] decodeClassFile = Base64.getDecoder().decode(encodeClassFile);
        if (decodeClassFile == null || decodeClassFile.length == 0) {
            return super.findClass(name);
        }
        return defineClass(name, decodeClassFile, 0, decodeClassFile.length);
    }

    public static void main(String[] args) {
        CustomClassLoader customClassLoader = new CustomClassLoader();
        try {
            //这里为什么直接是 Hello - 由于该类中没有声明 package，所以表示在 src 目录下
            Class<?> helloClass = customClassLoader.findClass("Hello");

            Method hello = helloClass.getMethod("hello");
            hello.invoke(helloClass.newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public String readFile() {
        InputStream fis = null;
        ByteArrayOutputStream  bos = null;
        try {
            fis = new FileInputStream(new File("Hello.xlass"));
            bos = new ByteArrayOutputStream ();
            byte[] buf = new byte[1024];
            int length = 0;
            while((length = fis.read(buf)) != -1){
                for (int i = 0; i < buf.length; i++) {
                    buf[i] = (byte) (255 - buf[i]);
                }
                bos.write(buf, 0, length);
            }
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
