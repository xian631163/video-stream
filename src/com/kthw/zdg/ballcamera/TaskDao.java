package com.kthw.zdg.ballcamera;

import java.util.Collection;


/**
 *球机视频任务信息持久层接口
 * @author zsx
 * @version 2018年12月17日
 */
public interface TaskDao {
    /**
     * 通过id查询任务信息
     * @param id - 任务ID
     * @return CommandTasker -任务实体
     */
    public CameraTasker get(String id);
    /**
     * 查询全部任务信息
     * @return Collection<CommandTasker>
     */
    public Collection<CameraTasker> getAll();
    /**
     * 增加任务信息
     * @param CommandTasker -任务信息实体
     * @return 增加数量�?<1-增加失败�?>=1-增加成功
     */
    public int add(CameraTasker CommandTasker);
    /**
     * 删除id对应的任务信�?
     * @param id
     * @return
     */
    public int remove(String id);
    /**
     * 删除全部任务信息
     * @return 
     */
    public int removeAll();
    /**
     * 是否存在某个ID
     * @param id - 任务ID
     * @return true:存在，false：不存在
     */
    public boolean isHave(String id);
}
