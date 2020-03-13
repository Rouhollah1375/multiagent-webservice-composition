package task;

import java.util.ArrayList;

public class SimpleTask {

    public TaskKnowledgeBase.services taskService;
    public String userAgentId;
    public String compositorAgentId;
    public String serviceAgentId;

    public String result;   // stringified json
    public boolean done;
    public ArrayList<SimpleTask> children;
    public ArrayList<SimpleTask> parents;
    public boolean allDependenciesDone;

    public SimpleTask(String userId,
                      TaskKnowledgeBase.services taskService,
                      ArrayList<SimpleTask> parents,
                      ArrayList<SimpleTask> children) {

        this.userAgentId = userId;
        this.taskService = taskService;
        this.done = false;
        this.allDependenciesDone = false;

        this.parents = parents;
        this.children = children;
    }

    public SimpleTask() {

    }

    public void setCompositorAgentId(String compositorAgentId) {
        this.compositorAgentId = compositorAgentId;
    }

    public void setServiceAgentId(String serviceAgentId) {
        this.serviceAgentId = serviceAgentId;
    }

    public void addChild(SimpleTask child) {
        if (this.children != null) {
            this.children.add(child);
        } else {
            this.children = new ArrayList<SimpleTask>();
            this.children.add(child);
        }
    }

    public void addParent(SimpleTask parent) {
        if (this.parents != null) {
            this.parents.add(parent);
        } else {
            this.parents = new ArrayList<SimpleTask>();
            this.parents.add(parent);
        }
    }


    public boolean areParentsDone() {
        if (this.parents == null) {
            return true;
        }

        boolean result = true;
        for (SimpleTask par: this.parents) {
            result = result && par.done;
        }
        this.done = result;
        return result;
    }

    public TaskKnowledgeBase.services getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskKnowledgeBase.services taskService) {
        this.taskService = taskService;
    }

    public String getUserAgentId() {
        return userAgentId;
    }

    public void setUserAgentId(String userAgentId) {
        this.userAgentId = userAgentId;
    }

    public String getCompositorAgentId() {
        return compositorAgentId;
    }

    public String getServiceAgentId() {
        return serviceAgentId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ArrayList<SimpleTask> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SimpleTask> children) {
        this.children = children;
    }

    public ArrayList<SimpleTask> getParents() {
        return parents;
    }

    public void setParents(ArrayList<SimpleTask> parents) {
        this.parents = parents;
    }

    public boolean isAllDependenciesDone() {
        return allDependenciesDone;
    }

    public void setAllDependenciesDone(boolean allDependenciesDone) {
        this.allDependenciesDone = allDependenciesDone;
    }
}
