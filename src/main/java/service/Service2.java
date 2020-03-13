package service;

import task.TaskKnowledgeBase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Service2 extends Service {
    public Service2(TaskKnowledgeBase.services taskService) {
        this.taskService = taskService;
    }

    public String compute(Object args) {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> stringArgs = (ArrayList<String>)args;
//        System.out.println(stringArgs);
        return taskService + " is done, baby!";
    }
}
