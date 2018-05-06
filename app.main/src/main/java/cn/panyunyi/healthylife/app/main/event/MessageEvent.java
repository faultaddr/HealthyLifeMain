package cn.panyunyi.healthylife.app.main.event;

public class MessageEvent {

    private String messageType;
    private String messageContent;

    public MessageEvent(String message, String content) {
        this.messageType = message;
        this.messageContent = content;
    }


    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }


}