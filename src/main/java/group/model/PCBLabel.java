/*
 * Created by JFormDesigner on Wed Nov 01 23:39:57 CST 2023
 */

package group.model;

import java.awt.*;
import java.time.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;

import group.pub.cpu.process.Process;
import group.pub.cpu.process.ProcessControlBlock;

import javax.swing.*;

/**
 * @author Yanion_gwgzh
 */
public class PCBLabel extends JLabel {
    private ProcessControlBlock PCB;
    public ProcessControlBlock getPCB(){return PCB;}
    public PCBLabel(){
        this(new ProcessControlBlock(new Process(1)));
    }
    public PCBLabel(ProcessControlBlock PCB) {
        this.PCB = PCB;
        initComponents();
        //my code
        this.setText("<html>"
            +PCB.getName()+"<br>执行需要:"+PCB.getRunningTime()+" ms</html>");
        this.setText("<html>"
                +PCB.getName()+"<br>执行需要:"+PCB.getRunningTime()+"ms<br>优先级:"+ PCB.getPriority());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - wainyz
        setIcon(new FlatSVGIcon("group/assets/PCB_BOX.svg"));
        //---- this ----
        setBorder(new EtchedBorder());
        setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", this.getFont().getStyle(), this.getFont().getSize()));
        setMaximumSize(new Dimension(58, 90));
        setVerticalAlignment(SwingConstants.TOP);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - wainyz
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
