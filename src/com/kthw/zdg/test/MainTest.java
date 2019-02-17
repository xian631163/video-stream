package com.kthw.zdg.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.kthw.zdg.ballcamera.BallCameraInfo;
import com.kthw.zdg.ballcamera.KeepAliveHandler;
import com.kthw.zdg.ballcamera.TaskDao;
import com.kthw.zdg.ballcamera.TaskDaoImpl;
import com.kthw.zdg.common.ExecUtil;
import com.kthw.zdg.videomanager.CommandManager;
import com.kthw.zdg.videomanager.CommandManagerImpl;

public class MainTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO Auto-generated method stub

        BallCameraInfo ballCameraInfo=new BallCameraInfo(3, "192.168.2.11","rtmp://localhost:1935/live/test2");
        TaskDao taskDao=new TaskDaoImpl(1);
        CommandManager commandManager=new CommandManagerImpl( taskDao); 
        boolean lastcon=false;
        boolean cunncon=false;
        boolean noStart=true;
        while(true){
            lastcon=cunncon;
            cunncon= ExecUtil.ping("192.168.2.11");
            if (cunncon&&noStart){
                commandManager.start(String.valueOf(3), ballCameraInfo.getCommandString(), "error", ballCameraInfo.getBallCameraIp());
                noStart=false;
                continue;
            } 
            if (cunncon!=lastcon){
                commandManager.stop(String.valueOf(3));
                noStart=true;               
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        }  
     
        
   }
}
