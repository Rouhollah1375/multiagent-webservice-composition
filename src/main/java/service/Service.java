package service;

import task.TaskKnowledgeBase;

import java.util.ArrayList;

public abstract class Service {

    public TaskKnowledgeBase.services taskService;
    public ArrayList<String> serviceArguments;

    public abstract String compute(Object args);

}
