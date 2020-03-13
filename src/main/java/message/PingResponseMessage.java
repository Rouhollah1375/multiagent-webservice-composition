package message;

import com.google.gson.Gson;
import jade.core.AID;
import jade.lang.acl.ACLMessage;


public class PingResponseMessage {
    public String serviceAgentId;
    public String compositorAgentId;
    public Integer response;

    public PingResponseMessage(String serviceAgentId, String compositorAgentId, Integer response) {
        this.serviceAgentId = serviceAgentId;
        this.compositorAgentId = compositorAgentId;
        this.response = response;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
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
        String messageContent = new Gson().toJson(this, PingResponseMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID(this.serviceAgentId, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
