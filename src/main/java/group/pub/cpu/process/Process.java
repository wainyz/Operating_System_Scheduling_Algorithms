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
                System.out.println("试图执行任务而请求获取LOCK占有...");
                synchronized (LOCK) {
                    System.out.println("申请到锁,正在执行任务时间是" + runningTime + "...");
                    LOCK.wait(runningTime);
                    System.out.println("任务执行完毕!试图获取执行器类对象...");
                    synchronized (Executor.class) {
                        //通知执行器
                        System.out.println("已拿到执行器类对象锁,试图通知类对象...");
                        Executor.class.notifyAll();
                        System.out.println("通知完毕,任务销毁,还处理机...");
                        this.setLOCK(null);
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
