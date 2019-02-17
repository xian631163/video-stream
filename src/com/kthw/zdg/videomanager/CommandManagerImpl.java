package com.kthw.zdg.videomanager;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.kthw.zdg.ballcamera.CameraTasker;
import com.kthw.zdg.ballcamera.KeepAliveHandler;
import com.kthw.zdg.ballcamera.TaskDao;
import com.kthw.zdg.common.ExecUtil;



/**
 * FFmpeg命令操作管理器
 * 
 * @author zsx
 * @version 2018年12月19日
 * @param <TaskDao>
 */
public class CommandManagerImpl implements CommandManager {
    
//    static Logger logger = Logger.getLogger(CommandManagerImpl.class);
     
	/**
	 * 任务持久化器
	 */
	private TaskDao taskDao = null;
	/**
	 * 任务执行处理器
	 */
	@SuppressWarnings("unused")
    private CameraTasker cameraTasker = null;
	
	/**
	 * 保活处理器
	 */
	private KeepAliveHandler keepAliveHandler=null;

	/**
	 * 指定任务池大小的初始化，其他使用默认
	 * @param size
	 */
	public CommandManagerImpl(Integer size) {
		//init(size);
	}
	
	/**
	 * 初始化
	 * @param taskDao
	 * @param taskHandler
	 * @param commandAssembly
	 * @param ohm
	 * @param size
	 */
	public CommandManagerImpl(TaskDao taskDao) {
		this.taskDao = taskDao;
		init();
	}

	/**
	 * 初始化，如果几个处理器未注入，则使用默认处理器
	 * 
	 * @param size
	 */
	private void init() {
           keepAliveHandler = new KeepAliveHandler(taskDao);
           keepAliveHandler.start();         
        }
        
	@Override
    public void setTaskDao(TaskDao taskDao) {
        // TODO Auto-generated method stub
        this.taskDao=taskDao;
    }

    @Override
    public void setCameraTasker(CameraTasker taskHandler) {
        // TODO Auto-generated method stub
        this.cameraTasker=taskHandler;
    }

    @Override
    public String start(String id, String command,String type,String ballIp) throws IOException, InterruptedException {
        // TODO Auto-generated method stub
        if (id != null && command != null) {          
            CameraTasker tasker = ExecUtil.createTasker(id, command,type,ballIp);
            System.out.println(id + "推流成功！");
            if (tasker != null) {
                int ret = taskDao.add(tasker);
                if (ret > 0) {
                    return tasker.getId();
                } else {
                    // 持久化信息失败，停止处理
                    ExecUtil.stop(tasker);
//                     logger.error("持久化失败，停止任务！");
                     System.err.println("持久化失败，停止任务！");
                }
            }
        }
        return null;
    }

    @Override
    public String start(String id, String commond, boolean hasPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean stop(String id) {
        // TODO Auto-generated method stub
        boolean flag=false;
        CameraTasker tasker=null;
        if (id!=null){
             tasker=taskDao.get(id);
        }
        if (ExecUtil.stop(tasker)){
            taskDao.remove(id);
            flag=true;
        }
        return flag;
    }

    @Override
    public int stopAll() {
        Collection<CameraTasker> list = taskDao.getAll();
        Iterator<CameraTasker> iter = list.iterator();
        CameraTasker tasker = null;
        int index = 0;
        while (iter.hasNext()) {
            tasker = iter.next();
            if (ExecUtil.stop(tasker)) {
                taskDao.remove(tasker.getId());
                index++;
            }
        }
//       logger.info("停止了" + index + "个任务！");
       System.err.println("停止了" + index + "个任务！");
        return index;
    }

    @Override
    public CameraTasker query(String id) {
        // TODO Auto-generated method stub
        return taskDao.get(id);
    }

    @Override
    public Collection<CameraTasker> queryAll() {
        // TODO Auto-generated method stub
        return taskDao.getAll();
    }

    @Override
    public void destory() {
        // TODO Auto-generated method stub
        if(keepAliveHandler!=null) {
            //安全停止保活线程
            keepAliveHandler.interrupt();
        }
        
    }

    public void seCameraTasker(CameraTasker cameraTasker) {
		this.cameraTasker = cameraTasker;
	}




	

}
