package message;

import com.google.gson.Gson;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

// this message is sent from composer to dispatcher and forwarded from dispatcher to user.
public class UserResponseMessage {
    public String response;
    public String userId;
    public String compositorId;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompositorId() {
        return compositorId;
    }

    public void setCompositorId(String compositorId) {
        this.compositorId = compositorId;
    }

    public UserResponseMessage(String userId, String response, String compositorId) {
        this.userId = userId;
        this.response = response;
        this.compositorId = compositorId;
    }

    public ACLMessage getAclMessage(String receiver) {
        String messageContent = new Gson().toJson(this, UserResponseMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
