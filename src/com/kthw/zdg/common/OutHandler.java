package com.kthw.zdg.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.kthw.zdg.ballcamera.KeepAliveHandler;



/**
 * FFmpeg 推流进程的管理线程
 * 
 * @author zsx
 * @version 2018年12月16日
 */
public class OutHandler extends Thread  
{  
//    static Logger logger = Logger.getLogger(OutHandler.class);
    // 控制线程状态  
    volatile boolean desstatus = true;  
    /**字节流*/
    BufferedReader br = null;  
    /**读取的信息类型（采用 error ）*/
    String type = null;  
    /**命令行信息*/
    String cmdId = null;
    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    /**任务ID   格式：zdg_robotId 
     * 推流出错后，根据id 重启推流任务
     *   */
    private String id = null;
  
    public OutHandler(InputStream is, String id, String cmdId,String type)  
    {  
        try {
            br = new BufferedReader(new InputStreamReader(is,"GB2312"));            //设置编码格式，否则会有中文乱码
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        this.id=id;
        this.type = type;  
        this.cmdId = cmdId;
    }  
    
    /**
     * 创建输出线程（默认立即开启线程）
     * @return
     */
    public static OutHandler create(InputStream is, String id, String cmdId,String type) {
        return create(is, id, cmdId,type,true);
    }
    


    /**
     * 创建输出线程
     * @param start-是否立即开启线程
     * @return
     */
    public static OutHandler create(InputStream is, String id, String cmdId,String type,boolean start) {
        OutHandler out= new OutHandler(is, id, cmdId,type);
        if (start){
          out.start();
        }
        return out;
    }
  
    /** 
     * 重写线程销毁方法，安全的关闭线程 
     */  
    @Override  
    public void destroy()  
    {  
        desstatus = false;  
        cmdId=null;
        type=null;
        id=null;                      
        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  
  
    /** 
     * 执行输出线程 
     */  
    @Override  
    public void run()  
    {  
        String msg = null;  
        int errorIndex = 1;  
        int missIndex = 1;  
        int failIndex=1;
        boolean isBroken=false;

        try {  
            while (desstatus&&( msg = br.readLine())!=null ) { 
                System.out.println(id + "消息：" + msg);
                    if (msg.indexOf("fail") != -1) {
                        System.err.println(id + "任务可能发生故障：" + msg);
                        failIndex++;
                    }else if(msg.indexOf("miss")!= -1) {
//                    
                        missIndex++;
                    }else if(msg.indexOf("Unknown error")!= -1) {
                        System.err.println(id + "消息：" + msg);
                        isBroken=true;
                       
                    }
                    if (0==missIndex%10||0==errorIndex%5||0==failIndex%3){
                        isBroken=true;
                    }
                    
                    if (isBroken){
                       
                        KeepAliveHandler.add(id);    //   加入保活处理队列
                        isBroken=false;
                    }   
                }           
        
        } catch (IOException e) {  
            System.out.println("发生内部异常错误，自动关闭[" + this.getId() + "]线程");  
            destroy();  
        } finally {  
            if (this.isAlive()) {  
                destroy();  
            }  
        }  
    }  
  
}  