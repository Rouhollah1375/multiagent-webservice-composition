package message;

import com.google.gson.Gson;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import task.ComplexTask;
import task.SimpleTask;

public class UserRequestMessage {
    public String userId;
    public String taskName;

    public UserRequestMessage(String userId, String taskName) {
        this.userId = userId;
        this.taskName = taskName;
    }

    public UserRequestMessage() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ACLMessage getAclMessage(String receiver) {
        String messageContent = new Gson().toJson(this, UserRequestMessage.class);

        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID(receiver, AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent(messageContent);

        return message;
    }

}
