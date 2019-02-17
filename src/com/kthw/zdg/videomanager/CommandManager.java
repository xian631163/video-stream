package com.kthw.zdg.videomanager;


import java.io.IOException;
import java.util.Collection;

import com.kthw.zdg.ballcamera.CameraTasker;
import com.kthw.zdg.ballcamera.TaskDao;


/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author zsx
 * @version 2018-12-17
 */
public interface CommandManager {
	

	/**
	 * 注入自己实现的持久层
	 * 
	 * @param taskDao
	 */
	public void setTaskDao(TaskDao taskDao);

	/**
	 * 注入ffmpeg命令处理
	 * 
	 * @param taskHandler
	 */
	public void setCameraTasker(CameraTasker taskHandler);


	/**
	 * 通过命令发布任务（默认命令前不加FFmpeg路径�?
	 * 
	 * @param id - 任务标识
	 * @param command - FFmpeg命令
	 * @return
	 * @throws IOException 
	 */
	public String start(String id, String command,String type,String ballIp) throws IOException, InterruptedException;
	/**
	 * 通过命令发布任务
	 * @param id -  任务标识
	 * @param commond - FFmpeg命令
	 * @param hasPath - 命令中是否包含FFmpeg执行文件的绝对路
	 * @return
	 */
	public String start(String id,String commond,boolean hasPath);



	/**
	 * 停止任务
	 * 
	 * @param id
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean stop(String id) throws InterruptedException;

	/**
	 * 停止全部任务
	 * 
	 * @return
	 */
	public int stopAll();

	/**
	 * 通过id查询任务信息
	 * 
	 * @param id
	 */
	public CameraTasker query(String id);

	/**
	 * 查询全部任务信息
	 * 
	 */
	public Collection<CameraTasker> queryAll();
	
	/**
	 * �?毁一些后台资源和保活线程
	 */
	public void destory();
	
}
