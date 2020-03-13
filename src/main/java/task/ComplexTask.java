package task;

import java.util.ArrayList;
import java.util.LinkedList;

// This class contains infrastructures to crate complex tasks out of simple tasks, keep track of
// which simple tasks are done and which can be done next.
public class ComplexTask {

    public SimpleTask root;

    public String userId;
    public String compositorId;
    public String dispatcherId;

    public SimpleTask getRoot() {
        return root;
    }

    public void setRoot(SimpleTask root) {
        this.root = root;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompositorId() {
        return compositorId;
    }

    public void setCompositorId(String compositorId) {
        this.compositorId = compositorId;
    }

    public String getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(String dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public ComplexTask(SimpleTask root, String userId, String compositorId, String dispatcherId) {
        this.root = root;
        this.root.parents = null;

        this.userId = userId;
        this.compositorId = compositorId;
        this.dispatcherId = dispatcherId;
    }

    // BFS transform on the dependency graph to find the next feasible task
    public SimpleTask findNextPossibleTask() {
        LinkedList q = new LinkedList<SimpleTask>();
        q.add(this.root);
        while(!q.isEmpty()) {
            SimpleTask current = (SimpleTask) q.poll();
            if (!current.done && current.areParentsDone()) {
                return current;
            } else if (current.done && current.children != null) {
                q.addAll(current.children);
            }
        }
        return null;    // it means the complex task is done or its completion is no more feasible
    }

}
