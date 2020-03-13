package agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import message.UserRequestMessage;
import task.TaskKnowledgeBase;

import java.util.OptionalDouble;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class UserAgent extends Agent {

    public String userId;
    public String dispatcherId;

    public String taskName;
    public Integer pauseTime;
    public Float avgResponseTime;
    public Integer requestCount;

    public ArrayList<Float> responseTimes;

    public void setup() {

        Object[] args = getArguments();
        this.taskName = (String) args[0];
        this.pauseTime = Integer.valueOf((String) args[1]);

        this.avgResponseTime = 0F;
        this.requestCount = 0;
        this.responseTimes = new ArrayList<>();
        this.userId = getLocalName();
//        this.dispatcherId = TaskKnowledgeBase.dispatcherId;
        this.dispatcherId = "Dispatcher";
        addBehaviour(new SendTaskRequestOnce());
    }

    class SendTaskRequestOnce extends CyclicBehaviour {

        @Override
        public void action() {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(userId + "::sending request");
//            String dispatcherId = dispatcherId;

            try {
                TimeUnit.MILLISECONDS.sleep(pauseTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // sending the request to dispatcher agent
            UserRequestMessage urm = new UserRequestMessage(userId, taskName);
            ACLMessage request = urm.getAclMessage(dispatcherId);
            send(request);
            Long startTime = System.nanoTime();
            // wait for dispatcher to send back a response
            ACLMessage reply = blockingReceive();
            Long finishTime = System.nanoTime();

            Float delta = (float) (finishTime - startTime) / 1000000;       // in ms
            responseTimes.add(delta);
//            avgResponseTime = (avgResponseTime * requestCount + delta) / requestCount + 1;
            avgResponseTime += delta;
            requestCount++;

            // do some stuff. maybe report something
//            if (requestCount > 0 && requestCount % 100 == 0) {
//                System.out.println("Average response time: " + avgResponseTime.toString());
//            }
            if (requestCount > 0) {
                Float mean = mean(responseTimes);
                double variance = variance(responseTimes, mean);
                System.out.println("Average response time: " + mean.toString() + " / std = " + Math.sqrt(variance));
            }
        }
    }


    public static Float mean(ArrayList<Float> list) {
        Float result = 0F;
        for(Float value: list) {
            result += value;
        }
        return result / list.size();
    }

    public static Float variance(ArrayList<Float> list, Float average) {
        Float sumDiffsSquared = 0F;
        Float avg = average;
        for (Float value : list)
        {
            Float diff = value - avg;
            diff *= diff;
            sumDiffsSquared += diff;
        }
        return sumDiffsSquared  / (list.size()-1);
    }

}
