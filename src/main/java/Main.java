import task.ComplexTask;
import task.SimpleTask;
import task.TaskKnowledgeBase;

public class Main {

    public static void main(String[] args) {
        ComplexTask c = TaskKnowledgeBase.getComplexTask("task1","rooholah", "asdf", "asdf");
        SimpleTask r;
        while((r = c.findNextPossibleTask()) != null) {
            r.done = true;
        }
    }
}
