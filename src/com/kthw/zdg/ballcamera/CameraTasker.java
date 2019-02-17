package com.kthw.zdg.ballcamera;

import com.kthw.zdg.common.OutHandler;
import com.kthw.zdg.common.StreamGobbler;

/**
 * 球机视频任务管理类
 * 
 * @author zsx
 * @since jdk1.7
 * @version 2018-12-17
 */
public class CameraTasker {
    private  String id;// zdg_robotId
    private  String command;// ffmpeg 命令
    private Process process;//  推流进程
    private OutHandler outHandler;// 管理推流的线程
    private  StreamGobbler sGobbler;
    private String ballIp;

    public CameraTasker(String id,String command) {
        this.id = id;
        this.command=command;
    }

    public CameraTasker(String id,String command, Process process, OutHandler outHandler,StreamGobbler sGobbler,String ballIp) {
        this.id = id;
        this.command=command;
        this.process = process;
        this.outHandler = outHandler;
        this.sGobbler=sGobbler;
        this.ballIp=ballIp;
    }

    public StreamGobbler getsGobbler() {
        return sGobbler;
    }

    public void setsGobbler(StreamGobbler sGobbler) {
        this.sGobbler = sGobbler;
    }

    public String getBallIp() {
        return ballIp;
    }

    public void setBallIp(String ballIp) {
        this.ballIp = ballIp;
    }

    public String getId() {
        return id;
    }

    public Process getProcess() {
        return process;
    }

    public OutHandler getOutHandler() {
        return outHandler;
    }

    public String getCommand() {
        return command;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setOutHandler(OutHandler thread) {
        this.outHandler = thread;
    }

    @Override
    public String toString() {
        return "CommandTasker [id=" + id + ", command=" + command + ", process=" + process + ", outHandler=" + outHandler + "]";
    }

}
