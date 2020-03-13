package task;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskKnowledgeBase {

    public enum services {
        A, B, C, D, E, F, G, H
    }

    public static HashMap<services, ArrayList<String>> serviceAgents = new HashMap<>();
    public static ArrayList<String> compositorAgents = new ArrayList<>();
    public static String dispatcherId;

    public static void registerServiceAgent(String agentId, services taskService) {
        if (serviceAgents.containsKey(taskService)) {
            serviceAgents.get(taskService).add(agentId);
        } else {
//            System.out.println("hellelele");
            serviceAgents.put(taskService, new ArrayList<>());
            serviceAgents.get(taskService).add(agentId);
        }
    }

    public static void registerCompositorAgent(String id) {
        compositorAgents.add(id);
    }

    public static void registerDispatcherAgent(String id) {
        dispatcherId = id;
    }

    public static ComplexTask getComplexTask(String taskName, String userId, String compositorId, String dispatcherId) {
        ComplexTask result = null;
        switch (taskName) {
            case "task1" :
                result = generateTask1(userId, compositorId, dispatcherId);
                break;
            case "task2":
                result = generateTask2(userId, compositorId, dispatcherId);
                break;
        }
        return result;
    }


    public static HashMap<services, Method> serviceDictionary = new HashMap<>() {{
        Class[] cArg = new Class[1];
        cArg[0] = Object.class;
        try {
            put(services.A, service.Service1.class.getMethod("compute", cArg));
            put(services.B, service.Service2.class.getMethod("compute", cArg));
            put(services.C, service.Service3.class.getMethod("compute", cArg));
            put(services.D, service.Service4.class.getMethod("compute", cArg));
            put(services.E, service.Service5.class.getMethod("compute", cArg));
            put(services.F, service.Service6.class.getMethod("compute", cArg));
            put(services.G, service.Service7.class.getMethod("compute", cArg));
            put(services.H, service.Service8.class.getMethod("compute", cArg));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }};

    // A --> B, A --> C
    // B -> D
    // C --> E
    // D --> E
    // E --> F
    private static ComplexTask generateTask1(String userId, String compositorId, String dispatcherId) {
        SimpleTask a = new SimpleTask(userId, services.A, null, null);
        SimpleTask b = new SimpleTask(userId, services.B, null, null);
        SimpleTask c = new SimpleTask(userId, services.C, null, null);
        SimpleTask d = new SimpleTask(userId, services.D, null, null);
        SimpleTask e = new SimpleTask(userId, services.E, null, null);
        SimpleTask f = new SimpleTask(userId, services.F, null, null);

        a.addChild(b);
        a.addChild(c);
        b.addParent(a);
        c.addParent(a);

        b.addChild(d);
        d.addParent(b);

        c.addChild(e);
        e.addParent(c);

        d.addChild(e);
        e.addParent(d);

        e.addChild(f);
        f.addParent(e);

        return new ComplexTask(a, userId, compositorId, dispatcherId);
    }

    private static ComplexTask generateTask2(String userId, String compositorId, String dispatcherId) {
        SimpleTask a = new SimpleTask(userId, services.A, null, null);
        return new ComplexTask(a, userId, compositorId, dispatcherId);
    }


    }
