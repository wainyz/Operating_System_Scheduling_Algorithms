package group.pub.cpu.process;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Comparator;
import java.util.Objects;


/**
 * @author Yanion_gwgzh
 */
@Getter
@Setter
@EqualsAndHashCode
public class ProcessControlBlock {
    public enum PCB_Comparator{
        COMPARE_RUNNING_TIME(1,ProcessControlBlock.COMPARE_RUNNING_TIME),COMPARE_RUNNING_TIME_REVERSE(2,ProcessControlBlock.COMPARE_RUNNING_TIME_REVERSE),COMPARE_PRIORITY(3,ProcessControlBlock.COMPARE_PRIORITY),COMPARE_ADD_TIME(0,ProcessControlBlock.COMPARE_ADD_TIME);
        private int id;
        private int index;
        private Comparator<ProcessControlBlock> processControlBlockComparator;
        PCB_Comparator(int id, Comparator<ProcessControlBlock> comparator){
            this.id = id;
            this.processControlBlockComparator = comparator;
        }

        public int getId() {
            return id;
        }
        public int getIndex(){
            return index;
        }

        public Comparator<ProcessControlBlock> getProcessControlBlockComparator() {
            return processControlBlockComparator;
        }

        public static PCB_Comparator getById(int id){
            for(PCB_Comparator comparator:values()){
                if(comparator.getId()==id){
                    return comparator;
                }
            }
            return values()[0];
        }
    }
    public static Comparator<ProcessControlBlock> COMPARE_RUNNING_TIME;
    public static Comparator<ProcessControlBlock> COMPARE_RUNNING_TIME_REVERSE;
    public static Comparator<ProcessControlBlock> COMPARE_PRIORITY;
    public static Comparator<ProcessControlBlock> COMPARE_ADD_TIME;
    private static int numbers = 0;
    static{
        COMPARE_RUNNING_TIME = Comparator.comparingInt(ProcessControlBlock::getRunningTime);
        COMPARE_RUNNING_TIME_REVERSE = (p1,p2)->p2.getRunningTime()-p1.getRunningTime();
        COMPARE_PRIORITY = (p1,p2)->p2.getPriority()-p1.getPriority();
        COMPARE_ADD_TIME = Comparator.comparingLong(ProcessControlBlock::getAddTime);
    }
    private int runningTime;
    private Process process;
    private int priority=0;
    private Long addTime=0L;
    private final int name;
    public ProcessControlBlock(Process p){
        process = p;
        runningTime = p.getRunningTime();
        name = numbers++;
    }
    public ProcessControlBlock(Process p, int priority){
        process = p;
        runningTime = p.getRunningTime();
        name = numbers++;
        this.priority = priority;
    }
    public ProcessControlBlock(){
        process = new Process(1);
        name = numbers++;
    }
    public boolean setProcess(@NonNull Process p){
        this.process = p;
        runningTime = p.getRunningTime();
        return true;
    }
    public void setRunningTime(int time){
        this.runningTime = time;
        process.setRunningTime(time);
    }
}
