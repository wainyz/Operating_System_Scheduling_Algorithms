package group.pub.cpu.timer;

import group.pub.cpu.executor.Executor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author Yanion_gwgzh
 */
@Getter
@Setter
public class Timer extends Thread {
    private boolean pauseTurn = true;
    private int mills;
    private Object LOCK;

    public Timer(int mills, Object LOCK) {
        this.mills = mills;
        this.LOCK = LOCK;
        setDaemon(true);
    }

    private Timer() {
    }
    public void notifyMe(){
        synchronized (this){
            notifyAll();
        }
    }
    public void setPauseTurn(boolean pauseTurn){
        if(pauseTurn){
            this.pauseTurn = true;
        }else{
            this.pauseTurn = false;
            notifyMe();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (pauseTurn) {
                    synchronized (this) {
                        this.wait();
                    }
                }
                TimeUnit.MILLISECONDS.sleep(mills);
                synchronized (Executor.class) {
                    //唤醒执行器
                    Executor.class.notifyAll();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
