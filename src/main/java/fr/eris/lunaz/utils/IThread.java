package fr.eris.lunaz.utils;

public abstract class IThread {
    protected Thread thread;
    protected int idleCounter;
    protected boolean running = false;

    private final String threadName;

    protected IThread(String threadName) {
        this.threadName = threadName;
    }

    protected abstract void threadRun();
    protected void onThreadStart() {}
    protected void onThreadStop() {}

    private void run() {
        try {
            while (true) {
                if(!Thread.interrupted())
                    Thread.sleep(10L);
                if (!running) {
                    idleCounter++;
                    if(idleCounter >= 10) {
                        stopThread(); // prevent from infinite getThread() (if your not happy override it)
                        break;
                    }
                    continue;
                }
                idleCounter = 0;
                threadRun();
            }
        } catch (InterruptedException ignored) {}
    }

    public void startThread() {
        if(thread == null) {
            running = true;
            thread = new Thread(this::run, threadName);
            onThreadStart();
            thread.start();
        }
    }

    public void stopThread() {
        if(thread != null) {
            running = false;
            onThreadStop();
            thread.interrupt();
            thread = null;
        }
    }

    public void sleep(long millis) {
        try {
            thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
