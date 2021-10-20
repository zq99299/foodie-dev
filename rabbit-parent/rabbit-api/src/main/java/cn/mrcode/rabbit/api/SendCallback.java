package cn.mrcode.rabbit.api;

/**
 * 消息确认回调
 *
 * @author mrcode
 * @date 2021/10/16 22:25
 */
public interface SendCallback {
    void onSuccess();

    void onFailure();
}
