package group.pub.cpu.executor;

import group.pub.cpu.process.Process;
import group.pub.cpu.process.ProcessControlBlock;
import group.pub.cpu.queue.PCBQueue;
import group.pub.cpu.timer.Timer;
import group.model.ProcessScheduleFrame.FrameController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

/**
 * @author Yanion_gwgzh
 */
public class Executor {
    //private static Executor EXECUTOR;
    private PCBQueue queue;
    private FrameController controller;
    private Timer timer;
    private ProcessControlBlock currentProcess;
    private final Object LOCK = new Object();
    private boolean pause = false;

    public Executor(FrameController controller) {
        this.controller = controller;
        controller.setExecutor(this);
        this.queue = new PCBQueue(controller.getNewComparator());
        queue.setController(controller);
        this.timer = new Timer(controller.getTimerMills(),controller.checkTimer());
        timer.start();
    }


    //大家调用--------------------------------------------
    public boolean getPauseStatus() {
        return pause;
    }
    public void notifyMe() {
        synchronized (controller) {
            controller.notifyAll();
            synchronized (Executor.class){
                Executor.class.notifyAll();
            }
        }
    }
    private void setPause(boolean b) {
        this.pause = b;
        if(!b){
            notifyMe();
        }
    }
    public void execute() {
        //实现循环执行CPU
        while (true) {
            //对于中断信号的检测
            try {
                //如果队列不为空获取进程执行
                while (queue.isEmpty()) {
                    synchronized (queue) {
                        try {
                            controller.setCPUText("等待任务中...");
                            queue.wait();
                            controller.setCPUText("发现任务");
                        } catch (InterruptedException e) {
                            throw new InterruptedException(e.getMessage());
                        }
                    }
                }
                //等待队列
                currentProcess = queue.getNext();
                Process process = currentProcess.getProcess();
                if (controller != null) {
                    controller.setCPUText("<html>PCB 编号: " + currentProcess.getName() + "<br>PCB 剩余执行时间: " + currentProcess.getRunningTime()
                            + "<br>PCB 优先级: " + currentProcess.getPriority() + "<br>PCB 入队列时间: "
                            + LocalDateTime.ofInstant(Instant.ofEpochMilli(currentProcess.getAddTime()), ZoneId.systemDefault()) + "</html>");
                }
                //这个是对于整个执行器的占有
                synchronized (Executor.class) {
                    //选择执行进程
                    process.setLOCK(LOCK);
                    if (process.getState() == Thread.State.NEW) {
                        process.setDaemon(true);
                        process.start();
                    } else {
                        //设置interrupt标志,并唤醒因为interrupt和LOCK占有的线程
                        synchronized (LOCK) {
                            process.isYou();
                            LOCK.notifyAll();
                        }
                    }
                    //等待进程通知
                    Executor.class.wait();
                }
                //判断是进程打断还是时间片打断
                if (!timer.isPauseTurn() || process.isAlive()) {
                    System.out.println("认为被时间片打断");
                    //打断当前正在执行的process
                    process.interrupt();
                    currentProcess.setRunningTime(currentProcess.getRunningTime() - timer.getMills());
                    if (currentProcess.getRunningTime() > 0) {
                        System.out.println(currentProcess.getRunningTime() + "现在剩余执行时间是;");
                        queue.add(currentProcess);
                    }
                }
                controller.setCPUText("");
                //判断是否暂停
                while (pause) {
                    //进入暂停流程
                    System.out.println("进入暂停循环...");
                    synchronized (controller) {
                        controller.setPauseIcon(pause);
                        controller.setCPUText("正在暂停--------");
                        controller.wait();
                    }
                }//对于没有下一个进程,等待队列通知
                controller.setPauseIcon(false);
            } catch (Exception ignored) {
            }
        }
    }

    //controller 调用--------------------------------------

    /**
     * controller 调用 添加新进程唯一接口
     * @param runningTime
     * @param priority
     */
    public void addPCB(int runningTime, int priority){
        ProcessControlBlock pcb = new ProcessControlBlock(new Process(runningTime),priority);
        //queue 主导 直接与controller工作
        queue.add(pcb);
    }

    /**
     * controller调用 暂停处理机
     * @param pause
     */
    public void pauseExecutor(boolean pause){
        setPause(pause);
    }

    /**
     * controller调用 用于改变定时器属性
     * @param isCycle
     * @param mills
     */
    public void changeTimerConfig(boolean isCycle, int mills){
        timer.setMills(mills);
        timer.setPauseTurn(!isCycle);
    }

    /**
     * controller 调用 用于修改队列规则
     * @param comparator
     */
    public void changeQueueComparator(Comparator<ProcessControlBlock> comparator){
        queue.changComparator(comparator);
    }
    //PCB queue 接口---------------------------------------


}
