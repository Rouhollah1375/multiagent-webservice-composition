package message;

import com.google.gson.Gson;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

// this message is sent from a service agent to a compositor agent
public class CompositorResponseMessage {

    public String compositorId;
    public String response;

    public CompositorResponseMessage(String compositorId, String response) {
        this.compositorId = compositorId;
        this.response = response;
    }

    public String getCompositorId() {
        return compositorId;
    }

    public void setCompositorId(String compositorId) {
        this.compositorId = compositorId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ACLMessage getAclMessage() {
        String messageContent = new Gson().toJson(this, CompositorResponseMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID(this.compositorId, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
