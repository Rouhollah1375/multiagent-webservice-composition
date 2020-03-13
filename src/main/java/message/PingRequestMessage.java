package message;

import com.google.gson.Gson;

import jade.core.AID;
import jade.lang.acl.ACLMessage;


public class PingRequestMessage {
    public String serviceAgentId;
    public String compositorAgentId;

    public PingRequestMessage(String serviceAgentId, String compositorAgentId) {
        this.serviceAgentId = serviceAgentId;
        this.compositorAgentId = compositorAgentId;
    }

    public String getServiceAgentId() {
        return serviceAgentId;
    }

    public void setServiceAgentId(String serviceAgentId) {
        this.serviceAgentId = serviceAgentId;
    }

    public String getCompositorAgentId() {
        return compositorAgentId;
    }

    public void setCompositorAgentId(String compositorAgentId) {
        this.compositorAgentId = compositorAgentId;
    }

    public ACLMessage getAclMessage() {
        String messageContent = new Gson().toJson(this, PingRequestMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.addReceiver(new AID(this.serviceAgentId, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
