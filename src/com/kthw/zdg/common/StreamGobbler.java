package com.kthw.zdg.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

    
/**
 * 为防止线程阻塞，导出输出流和错误流
 * 
 * @author zsx
 * @version 2018年12月23日
 */
public class StreamGobbler extends Thread {
    InputStream is;
    String type;
    OutputStream os;
    boolean stopFlag=true;

    StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while (stopFlag&&(line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + ">" + line);
                System.out.println(type + ">");
            }
            if (pw != null)
                pw.flush();
            br.close();
            isr.close();
            is.close();  
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @Override
    public void destroy(){
        stopFlag=false;
    }
}
