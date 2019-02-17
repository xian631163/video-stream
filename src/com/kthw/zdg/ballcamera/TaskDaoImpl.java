package com.kthw.zdg.ballcamera;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 球机视频任务信息持久层实
 * 
 * @author zsx
 * @version 2018-12-17
 */
public class TaskDaoImpl implements TaskDao {

    private ConcurrentMap<String, CameraTasker> cameraTaskMap = null;   // 视频队列

    public TaskDaoImpl() {
        
        cameraTaskMap=new  ConcurrentHashMap<String, CameraTasker>();
    }
    public TaskDaoImpl(int size) {
        
        cameraTaskMap=new  ConcurrentHashMap<String, CameraTasker>(size);
    }

    @Override
    public CameraTasker get(String id) {
        // TODO Auto-generated method stub
        return cameraTaskMap.get(id);
    }

    @Override
    public Collection<CameraTasker> getAll() {
        // TODO Auto-generated method stub
        return cameraTaskMap.values();
    }

    @Override
    public int add(CameraTasker CommandTasker) {
        // TODO Auto-generated method stub
        String id = CommandTasker.getId();
        int flag = 0;
        if (id != null && !cameraTaskMap.containsKey(id)) {
            cameraTaskMap.put(CommandTasker.getId(), CommandTasker);
            if (cameraTaskMap.get(id) != null) {
                flag = 1;
            }
        }
        return flag;
    }

    @Override
    public int remove(String id) {
        if (cameraTaskMap.remove(id) != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int removeAll() {
        int size = cameraTaskMap.size();
        try {
            cameraTaskMap.clear();
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    @Override
    public boolean isHave(String id) {
        // TODO Auto-generated method stub
        return cameraTaskMap.containsKey(id);
    }

    public ConcurrentMap<String, CameraTasker> getCameraTaskMap() {
        return cameraTaskMap;
    }

    public void setCameraTaskMap(
            ConcurrentMap<String, CameraTasker> cameraTaskMap) {
        this.cameraTaskMap = cameraTaskMap;
    }

}
