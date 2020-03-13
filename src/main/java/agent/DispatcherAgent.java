package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import message.UserRequestMessage;
import message.UserResponseMessage;
import task.TaskKnowledgeBase;

public class DispatcherAgent extends Agent {

    public String dispatcherId;
    public ArrayList<String> compositorAgents;
    public ACLMessage currentMessage;
    public HashMap<String, Integer> loadCounter;

    @Override
    public void setup() {
        this.dispatcherId = getLocalName();
        TaskKnowledgeBase.registerDispatcherAgent(this.dispatcherId);
        addBehaviour(new Dispatch());

        this.loadCounter = new HashMap<>();

        // wait for 5 seconds to make sure all service agents are setup.
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(String cAgent: TaskKnowledgeBase.compositorAgents) {
            this.loadCounter.put(cAgent, 0);
        }

        System.out.println(this.loadCounter);

    }

    private String findLeastLoadedCompositor(HashMap<String, Integer> map, ArrayList<String> keys) {
        String minKey = null;
        int minValue = Integer.MAX_VALUE;
        for(String key : keys) {
            int value = map.get(key);
            if(value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
        return minKey;
    }

    // This behaviour is comprised of two sub-behaviour. The first one checks whether a request is received
    // from the user. The second one checks if there are any results ready to be sent back to the user.
    class Dispatch extends CyclicBehaviour {
        public void action() {
            addBehaviour(new HandleIncomingMessage());
            addBehaviour(new HandleUserRequest());
            addBehaviour(new HandleUserResponse());
        }
    }

    class HandleIncomingMessage extends OneShotBehaviour {
        public void action() {
            ACLMessage message = blockingReceive();
            currentMessage = message;
        }
    }

    // poll whether there are any requests submitted by the user
    class HandleUserRequest extends OneShotBehaviour {
        public void action() {
            int perf = currentMessage.getPerformative();

            // request performative means this message has been sent by a user agent
            if (perf == ACLMessage.REQUEST) {
                UserRequestMessage urm = new Gson().fromJson(currentMessage.getContent(), UserRequestMessage.class);

                // finding the best compositor
                String bestCompositorId = findLeastLoadedCompositor(loadCounter, TaskKnowledgeBase.compositorAgents);
                // sending the complex task to the chosen compositor
                currentMessage.clearAllReceiver();
                currentMessage.addReceiver(new AID(bestCompositorId, AID.ISLOCALNAME));
                send(currentMessage);

                Integer load = loadCounter.get(bestCompositorId) - 1;
                loadCounter.put(bestCompositorId, load);
            }
        }
    }


    // poll whether there are any responses to be sent back to the user
    class HandleUserResponse extends OneShotBehaviour {
        public void action() {
            int perf = currentMessage.getPerformative();

            // request performative means this message has been sent by a composer agent
            // containing the result of a user's request
            if (perf == ACLMessage.INFORM) {
//                System.out.println("got a message from a compositor");
                UserResponseMessage urm = new Gson().fromJson(currentMessage.getContent(), UserResponseMessage.class);
                String userId = urm.userId;

                currentMessage.clearAllReceiver();
                currentMessage.addReceiver(new AID(userId, AID.ISLOCALNAME));
                send(currentMessage);

                Integer load = loadCounter.get(urm.compositorId) - 1;
                loadCounter.put(urm.compositorId, load);
            }
        }
    }
}

