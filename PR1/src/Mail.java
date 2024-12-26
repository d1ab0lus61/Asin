class Mail {
    private final String senderName;
    private final String recipientName;
    private final String content;

    public Mail(String senderName, String recipientName, String content) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getContent() {
        return content;
    }
}