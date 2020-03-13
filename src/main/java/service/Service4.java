package service;

import task.TaskKnowledgeBase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Service4 extends Service {
    public Service4(TaskKnowledgeBase.services taskService) {
        this.taskService = taskService;
    }

    public String compute(Object args) {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return taskService + " is done, baby!";
    }
}
