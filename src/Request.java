public class Request {
    private String request;
    private String sender;
    private String receiver;

    public Request(String request, String sender, String receiver) {
        this.request = request;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void sendRequest(Request request) {
        System.out.println("Request sent from " + request.getSender() + " to " + request.getReceiver() + ": " + request.getRequest());
    }

    public void updateRequest(String newRequest) {
        this.request = newRequest;
        System.out.println("Request updated: " + this.request);
    }

    public void cancelRequest() {
        this.request = null;
        System.out.println("Request canceled by " + sender);
    }

    @Override
    public String toString() {
        return "Request [request=" + request + ", sender=" + sender + ", receiver=" + receiver + "]";
    }
}
