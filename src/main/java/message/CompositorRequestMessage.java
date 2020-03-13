package message;

import com.google.gson.Gson;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import task.TaskKnowledgeBase;

import java.util.ArrayList;

// this class is used when a compositor agent wants to send a simple task to a service agent
public class CompositorRequestMessage {
    public String compositorId;
    public String serviceAgentId;
    public TaskKnowledgeBase.services taskService;
    public ArrayList<String> arguments;

    public CompositorRequestMessage(String compositorId,
                                    String serviceAgentId,
                                    TaskKnowledgeBase.services taskService,
                                    ArrayList<String> arguments) {

        this.compositorId = compositorId;
        this.serviceAgentId = serviceAgentId;
        this.taskService = taskService;
        this.arguments = arguments;
    }

    public String getCompositorId() {
        return compositorId;
    }

    public void setCompositorId(String compositorId) {
        this.compositorId = compositorId;
    }

    public String getServiceAgentId() {
        return serviceAgentId;
    }

    public void setServiceAgentId(String serviceAgentId) {
        this.serviceAgentId = serviceAgentId;
    }

    public TaskKnowledgeBase.services getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskKnowledgeBase.services taskService) {
        this.taskService = taskService;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> argument) {
        this.arguments = argument;
    }


    public ACLMessage getAclMessage() {
        String messageContent = new Gson().toJson(this, CompositorRequestMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID(this.serviceAgentId, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
