package com.kthw.zdg.ballcamera;


/**
 * 球机信息类
 * 
 * @author zsx
 * @version 2018-12-17
 */
public class BallCameraInfo {
    boolean connRes = false;
    boolean ballConnRes = false;
    String robotIp;
    int robotId;
    String ballCameraIp;
    boolean flag = false;
    String ffmpegPath = "D:\\ZDG\\ffmpeg-20170801-1193301-win64-static\\bin\\";
    String rtspSrc = "";
    String rtmpDest = "";
    String videoSize = "860x484";
    String commandString="";
    
    
    
    public BallCameraInfo(){
        robotId=0;
        ballCameraIp="";
        rtspSrc="";
        rtmpDest="";
        commandString="";
    }
  
    public BallCameraInfo(int robotId,String ballCameraIp,String rtmpDest){
        this.robotId=robotId;
        this.ballCameraIp=ballCameraIp;
        this.rtspSrc="rtsp://"+ballCameraIp+":554/1/h264major";;
        this.rtmpDest=rtmpDest;
        commandString=buidCmdContent();
          
    }
    public BallCameraInfo(int robotId,String ballCameraIp,String rtspSrc,String rtmpDest){
        this.robotId=robotId;
        this.ballCameraIp=ballCameraIp;
        this.rtspSrc=rtspSrc;
        this.rtmpDest=rtmpDest;
        commandString=buidCmdContent();
          
    }
    
    public  String buidCmdContent(){
         StringBuilder comm = new StringBuilder();
         comm.append(ffmpegPath+"ffmpeg -rtsp_transport tcp -i "+rtspSrc+" -f flv  -r 25 -s "+videoSize+" -an "+rtmpDest);  //-rtsp_transport tcp
        return comm.toString();
    }
   
    public String getRtspSrc() {
        return rtspSrc;
    }
    public void setRtspSrc(String rtspSrc) {
        this.rtspSrc = rtspSrc;
    }

    public int getRobotId() {
        return robotId;
    }

    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }

    public String getBallCameraIp() {
        return ballCameraIp;
    }

    public void setBallCameraIp(String ballCameraIp) {
        this.ballCameraIp = ballCameraIp;
    }

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    public void setFfmpegPath(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

    public String getRtmpDest() {
        return rtmpDest;
    }

    public void setRtmpDest(String rtmpDest) {
        this.rtmpDest = rtmpDest;
    }
    
    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

}

