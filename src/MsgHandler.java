
/**
* Created by CHNhawk on 2017/5/16.
* ���칤�̴�ѧ ���������� ��͢��
* �Զ������Ϣ�ӿ�
*/
public interface MsgHandler
{
    /**
     * ��Ϣ
     * @param type ��Ϣ����
     * @param data ����
     */
    void onMessage(String type, Object data);

    /**
     * �¼�
     * @param event x
     */
    void onEvent(int event);
}
