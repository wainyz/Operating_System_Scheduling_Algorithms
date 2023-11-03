package group.pub.cpu.process;

import group.pub.cpu.executor.Executor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Yanion_gwgzh
 */
@Getter
@Setter
public class Process extends Thread {
    private int runningTime;
    private Object LOCK;
    private boolean isMe = false;

    public void isYou() {
        isMe = true;
    }

    private boolean isMe() {
        return isMe;
    }

    public Process(final int keepingTime) {
    runningTime = keepingTime;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            if (runningTime <= 0) {
                return;
            }
            try {
                synchronized (LOCK) {
                    System.out.println("正在执行任务时间是" + runningTime + "...");
                    LOCK.wait(runningTime);
                    synchronized (Executor.class) {
                        //通知执行器
                        Executor.class.notifyAll();
                        System.out.println("交还处理机");
                        return;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("进程 中断");
                //因为LOCK占有和中断标志而止步不前
                while (!isMe) {
                    synchronized (LOCK) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException ignored) {

                        }
                    }
                }
                isMe = false;
            }
        }
    }

}
