package cn.panyunyi.healthylife.app.main.event;

public class MessageEvent {

    private int messageType;
    private String messageContent;

    public MessageEvent(int message) {
        this.messageType = message;
    }

    public int getMessage() {
        return messageType;
    }

    public void setMessage(int message) {
        this.messageType = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }


}