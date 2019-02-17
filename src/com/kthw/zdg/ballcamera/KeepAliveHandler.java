package com.kthw.zdg.ballcamera;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.kthw.zdg.common.ExecUtil;
import com.kthw.zdg.common.OutHandler;


public class KeepAliveHandler extends Thread {
    static Logger logger = Logger.getLogger(OutHandler.class);
    /**待处理队*/
    private static Queue<String> queue=null;
    
    public int err_index=0;//错误计数
    
    public volatile int stop_index=0;//安全停止线程标记
    
    /** 任务持久化器*/
    private TaskDao taskDao = null;
    
    public KeepAliveHandler(TaskDao taskDao) {
        super();
        this.taskDao=taskDao;
        queue=new ConcurrentLinkedQueue<>();
    }
    @Override
    public void run() {
        for(;stop_index==0;) {
            if(queue.isEmpty()) {
                continue;
            }
            String id=null;
            CameraTasker task=null;
            try {
                while(queue.peek() != null) {
                    logger.info("准备重启任务"+queue);
                    id=queue.poll();
                  task=taskDao.get(id);
                    
                  if (ExecUtil.ping(task.getBallIp())){
                      ExecUtil.restart(task);  //网络正常的情况下重启任务
                  }else{
                      add(id);       //   如果处于断线状态，重新加入队列中
                  }
                   
                }
            }catch(IOException e) {
                logger.error( id+" 任务重启失败，详情："+task);
                //重启任务失败
                err_index++;
            }catch(Exception e) {
                
            }
        }
    }

       
    
    
    public TaskDao getTaskDao() {
        return taskDao;
    }


    @Override
    public void interrupt() {
        stop_index=1;
    }
    
    public static void add(String id ) {     //   当视频流断开，加入待处理队列
        if(queue.contains(id)){
            return;
        }else{      
           queue.offer(id);
           System.out.println(id+"加入保活队列");
           return;
        }   
    }
    

}
