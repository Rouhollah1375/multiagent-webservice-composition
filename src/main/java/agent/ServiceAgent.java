package agent;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import message.CompositorRequestMessage;
import message.CompositorResponseMessage;
import message.PingRequestMessage;
import message.PingResponseMessage;
import task.TaskKnowledgeBase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ServiceAgent extends Agent {

    public String serviceAgentId;
    public TaskKnowledgeBase.services taskService;   // the task this agent is capable of

    public void setup() {
        this.serviceAgentId = getLocalName();

        Object[] args = getArguments();
        int enumIndex = ((int) (((String) args[0]).charAt(0))) - 65;
//        System.out.println("index of enum is " + Integer.toString(enumIndex));
        this.taskService = TaskKnowledgeBase.services.values()[enumIndex];
        TaskKnowledgeBase.registerServiceAgent(this.serviceAgentId, this.taskService);

        addBehaviour(new HandleRequest());
    }

    public String doTheService(ArrayList<String> args) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
        String serviceIndex = String.valueOf(this.taskService.ordinal() + 1);
        Class[] cArg = new Class[1];
        cArg[0] = TaskKnowledgeBase.services.class;
        Object instance = Class.forName("service.Service" + serviceIndex)
                .getDeclaredConstructor(cArg).newInstance(this.taskService);
        return (String) TaskKnowledgeBase.serviceDictionary.get(this.taskService).invoke(instance,(Object) args);
    }

    class HandleRequest extends CyclicBehaviour {

        public void action() {
            ACLMessage message = blockingReceive();

            // ping request
            if (message.getPerformative() == ACLMessage.CFP) {
                ACLMessage res = new PingResponseMessage(message.getSender().getLocalName(),
                        serviceAgentId,
                        getCurQueueSize()).getAclMessage();
                send(res);
            }

            else {
                CompositorRequestMessage compositorReq = new Gson().fromJson(message.getContent(), CompositorRequestMessage.class);
                ArrayList<String> taskArgs = compositorReq.arguments;
                String response = null;
                try {
                    response = doTheService(taskArgs);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                send(new CompositorResponseMessage(compositorReq.compositorId, response).getAclMessage());
            }

        }
    }
}
