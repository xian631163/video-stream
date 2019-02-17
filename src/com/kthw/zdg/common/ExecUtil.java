package com.kthw.zdg.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.kthw.zdg.ballcamera.CameraTasker;

/**
 * 命令行操作工具类
 * @author zsx
 * @version 2018年12月17日
 *
 */
public class ExecUtil {
    public final static String TYPE_INFO="info"; 
    public final static String TYPE_ERROR="error"; 

    
    
    /**
     * 组装 拉流ffmpeg 命令
     * @param cmd
     * @return
     * @throws IOException
     */
    public static String buidPushCmd(String ffmpegPath,String rtspSrc,String videoSize,String rtmpDest){
        StringBuilder comm = new StringBuilder();
            comm.append(ffmpegPath+"ffmpeg -rtsp_transport tcp -i "+rtspSrc+" -f flv  -r 25 -s "+videoSize+" -an "+rtmpDest);
       return comm.toString();
   }
    /**
     * 组装 录播ffmpeg 命令
     * @param cmd
     * @return
     * @throws IOException
     */
    public static String buidRecordCmd(String ffmpegPath,String rtmpDest,String pathFile){
        StringBuilder comm = new StringBuilder();
       comm.append(ffmpegPath+"ffmpeg  -i "+rtmpDest+" -vcodec copy "+pathFile);
       return comm.toString();
   }
    /**
     * 组装 拍照ffmpeg 命令
     * @param cmd
     * @return
     * @throws IOException
     */
    public static String buidCaptureCmd(String ffmpegPath,String rtmpDest,String pathFile){
        StringBuilder comm = new StringBuilder();
       comm.append(ffmpegPath+"ffmpeg -probesize 32768 -i "+rtmpDest+" -y -t 0.001 -ss 1 -f image2 -r 1 "+pathFile);
       return comm.toString();
   }
    
    /**
     * ping 函数
     * @param ip
     * @return
     * @throws InterruptedException 
     * @throws IOException
     */

    public static boolean ping(String ip)  {
        boolean res = false;
        InetAddress addr=null;
        try {
            addr = InetAddress.getByName(ip);
            if (addr.isReachable(5000)){
                res=true;
            } 
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return res;
      }

    /**
     * 执行命令行并获取进程
     * @param cmd
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    public static Process exec(String cmd) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmd);// 执行命令获取主进程
        return process;
    }
    
    /**
     * 销毁进程
     * @param process
     * @return
     */
    public static boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    /**
     * 销毁输出线程
     * @param outHandler
     * @return
     */
    public static boolean stop(OutHandler outHandler) {
        if (outHandler != null ) {
            outHandler.destroy();
            outHandler=null;
            return true;
        }
        return false;
    }
    
    /**
     * 销毁
     * @param process
     * @param outHandler
     * @throws InterruptedException 
     */
    public static boolean stop(CameraTasker tasker)  {
        boolean flag=false;
        if(tasker!=null) {
            stop(tasker.getProcess());
            stop(tasker.getOutHandler());
           
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            flag=true;
        }
        return flag;
    }

    /**
     * 创建拉流任务
     * @param id
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    public static CameraTasker createTasker(String id,String command,String type ,String ballIp) throws IOException, InterruptedException {
        // 执行本地命令获取任务主进程
        Process process=exec(command);
        // 创建输出线程
//        StreamGobbler sGobbler=new StreamGobbler(process.getInputStream(), TYPE_INFO);
        OutHandler outHandler=OutHandler.create(process.getErrorStream(), id, command, type);   
        CameraTasker tasker = new CameraTasker(id,command, process, outHandler,null,ballIp);
        return tasker;
    }

    
    
    /**
     * 中断故障缘故重启
     * @param tasker  视频推流任务
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    public static CameraTasker restart(CameraTasker tasker) throws IOException, InterruptedException {
        if(tasker!=null) {
            String id=tasker.getId();
            String command=tasker.getCommand();
            OutHandler outHandlerOld=tasker.getOutHandler();
            //安全销毁命令行进程和输出子线程
            stop(tasker.getOutHandler());
            stop(tasker);
            // 执行本地命令获取任务主进程
            Process process=exec(command);
            tasker.setProcess(process);

            OutHandler outHandler=OutHandler.create(process.getErrorStream(), id, outHandlerOld.getCmdId(), outHandlerOld.getType());
            tasker.setOutHandler(outHandler);    
        }
        return tasker;
    }


}
