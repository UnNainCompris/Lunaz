package fr.eris.lunaz.utils;

public class AwaitTask extends IThread {

    private static int taskNumber = 1;
    private final GetValue<Boolean> conditionToExecute;
    private final Task todo;

    protected AwaitTask(GetValue<Boolean> conditionToExecute, Task todo) {
        super("AwaitTask - " + taskNumber);
        this.conditionToExecute = conditionToExecute;
        this.todo = todo;
        taskNumber++;
        startThread();
    }

    public static AwaitTask startTask(GetValue<Boolean> conditionToExecute, Task todo) {
        return new AwaitTask(conditionToExecute, todo);
    }

    @Override
    protected void threadRun() {
        if(!conditionToExecute.getValue()) return;
        todo.call(this);
        stopThread();
    }

    public interface Task {
        void call(AwaitTask task);
    }
}
