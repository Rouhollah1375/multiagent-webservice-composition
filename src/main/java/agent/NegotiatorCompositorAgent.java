package agent;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import message.*;
import task.ComplexTask;
import task.SimpleTask;
import task.TaskKnowledgeBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class NegotiatorCompositorAgent extends Agent {

    public String compositorId;

    public void setup() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.compositorId = getLocalName();
        TaskKnowledgeBase.registerCompositorAgent(this.compositorId);

        addBehaviour(new HandleRequest());
    }

    // The compositor agent computes one complex behaviour at a time and does not consider
    // other incoming requests until the ongoing complex task is done.
    class HandleRequest extends CyclicBehaviour {

        public void action() {
            ACLMessage message = blockingReceive();
            UserRequestMessage req = new Gson().fromJson(message.getContent(), UserRequestMessage.class);

            String dispatcherId = TaskKnowledgeBase.dispatcherId;
            ComplexTask cTask = TaskKnowledgeBase.getComplexTask(req.taskName, req.userId, compositorId, dispatcherId);

            // traversing the cTask graph and compute the nodes of it.
            SimpleTask task;
            String finalResponse = null;
            while((task = cTask.findNextPossibleTask()) != null) {
                //TODO: find a service agent capable of accomplishing this task.
                ArrayList<String> eligibleServiceAgents = TaskKnowledgeBase.serviceAgents.get(task.taskService);
//                System.out.println("----" + eligibleServiceAgents);
                ArrayList<Integer> loads = new ArrayList<>();
//                loads.add(getQueueSize());
                for (String id: eligibleServiceAgents) {
//                    System.out.println("sending ping to " + id);
                    //send message to the worker
                    ACLMessage pingReq = new PingRequestMessage(id, compositorId).getAclMessage();
                    send(pingReq);
                    // wait for the response
                    ACLMessage res = blockingReceive();
                    PingResponseMessage pingRes = new Gson().fromJson(res.getContent(), PingResponseMessage.class);
                    loads.add(pingRes.response);
                }

                for (int i = 0; i < loads.size(); i++) {
                    if (loads.get(i) == null) {
                        loads.set(i, 0);
                    }
                }

                int minIndex = loads.indexOf(Collections.min(loads));
                String bestServiceAgent = eligibleServiceAgents.get(minIndex);

                // Outputs of parent nodes are input of children nodes
                ArrayList<String> taskArgs = new ArrayList<>();
                if (task.parents != null && task.parents.size() > 0) {
                    for (SimpleTask parent: task.parents) {
                        taskArgs.add(parent.result);
                    }
                }

                // sending this simple task to the selected service agent and wait for the response
                ACLMessage compositorReq = new CompositorRequestMessage(compositorId,
                                                                        bestServiceAgent,
                                                                        task.taskService,
                                                                        taskArgs).getAclMessage();
                send(compositorReq);

                // The result returned from the service agent is inside a message with
                // INFORM as the value of its performative verb, rather than REQUEST as in
                // the messages come from dispatcher
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage serviceRes = blockingReceive(mt);
//                System.out.println("compositor:: finally i got here");

                CompositorResponseMessage crm = new Gson().fromJson(serviceRes.getContent(), CompositorResponseMessage.class);
                // storing the response in the proper place and continue
                task.result = crm.response;
                finalResponse = crm.response;
                task.done = true;
            }
            // At this point of code, the complex task is done.
            // So you should return the results back to the dispatcher
            UserResponseMessage res = new UserResponseMessage(req.userId, finalResponse, compositorId);
            send(res.getAclMessage(dispatcherId));
        }
    }

}
