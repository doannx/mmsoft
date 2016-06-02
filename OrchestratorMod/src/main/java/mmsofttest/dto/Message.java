package mmsofttest.dto;

/**
 * Implementation class for the Message data transfer object.
 * 
 * @author NX.Doan, @version 1.0
 * @date 20160602
 */
public class Message{
    /**
     * Message title.
     */
    private String messageTitle;
    /**
     * Message content.
     */
    private String messageContent;

    /**
     * Constructor.
     * @param messageTitle
     * @param messageContent
     */
    public Message(String messageTitle, String messageContent) {
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
