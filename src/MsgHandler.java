
/**
* Created by CHNhawk on 2017/5/16.
* 重庆工商大学 物联网工程 熊廷宇
* 自定义的消息接口
*/
public interface MsgHandler
{
    /**
     * 消息
     * @param type 消息类型
     * @param data 数据
     */
    void onMessage(String type, Object data);

    /**
     * 事件
     * @param event x
     */
    void onEvent(int event);
}
