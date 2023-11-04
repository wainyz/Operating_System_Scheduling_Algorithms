package group.pub.cpu.queue;

import group.model.ProcessScheduleFrame;
import group.pub.cpu.process.ProcessControlBlock;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Yanion_gwgzh
 */
public class PCBQueue {
    private ProcessScheduleFrame.FrameController controller;
    private Object LOCK;
    private final ArrayList<ProcessControlBlock> array;
    private Comparator<ProcessControlBlock> comparator;

    public PCBQueue(Comparator<ProcessControlBlock> controlBlockComparator) {
        array = new ArrayList<>();
        this.comparator = controlBlockComparator;
    }

    /**
     * executor调用 取出PCB,并移除
     * @return
     */
    public ProcessControlBlock getNext() {
        //为了与queuePanel同步,这里设计为取出和
        ProcessControlBlock result = array.get(0);
        array.remove(0);
        controller.remove(0);
        controller.updateUI();
        System.out.println(result.getName() + " 出列!!!,当前剩余: " + array.size());
        return result;
    }

    /**
     * executor调用 用于向队列中添加项
     * @param processControlBlock
     */
    public void add(ProcessControlBlock processControlBlock) {
        processControlBlock.setAddTime(System.currentTimeMillis());
        int index = sort(processControlBlock);
        array.add(index, processControlBlock);
        controller.addPCBLabel(index, processControlBlock);
        controller.updateUI();
        System.out.println("当前剩余:" + array.size());
        synchronized (this) {
            this.notifyAll();
        }
    }

    private int sort(ProcessControlBlock newElem) {
        int length = array.size();
        //排序时占用自己
        for (int index = length - 1; index > -1; index--) {
            if (comparator.compare(newElem, array.get(index)) >= 0) {
                System.out.println("对于新入队 元素 :"+newElem+",打算加入下标:"+(index+1));
                return index + 1;
            }
        }
        return 0;
    }

    public void setController(ProcessScheduleFrame.FrameController controller) {
        this.controller = controller;
    }

    public void changComparator(Comparator<ProcessControlBlock> comparator){
        System.out.println("应用比较规则:"+comparator.toString());
        this.comparator = comparator;
        //暂时选择清除所有数据
//        controller.clearQueue();
//        array.clear();

    }
    public boolean isEmpty() {
        return array.isEmpty();
    }

}
