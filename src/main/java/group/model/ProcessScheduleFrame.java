/*
 * Created by JFormDesigner on Thu Nov 02 22:45:43 CST 2023
 */

package group.model;

import java.awt.*;
import java.awt.event.*;
import java.security.cert.TrustAnchor;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.formdev.flatlaf.extras.*;
import group.pub.cpu.executor.Executor;
import group.pub.cpu.process.ProcessControlBlock;

/**
 * @author Yanion_gwgzh
 */
public class ProcessScheduleFrame extends JFrame {
    public class FrameController{
        private Executor executor;
        //executor调用 -------------------------------------------
        public void setExecutor(Executor executor){
            this.executor = executor;
        }
        /**
         * executor调用 用于初始化Timer
         * @return
         */
        public int getTimerMills() {
            return getCheckTime();
        }

        /**
         * executor 用于初始化timer
         * @return
         */
        public boolean checkTimer(){
            return checkBoxTime();
        }



        /**
         * 用于改变现在的cpu中的图片
         * @param pause
         */
        public void setPauseIcon(boolean pause){
            if(pause){
                CPULabel.setIcon(new FlatSVGIcon("group/assets/pause_cpu.svg"));
            }else{
                CPULabel.setIcon(new FlatSVGIcon("group/assets/cpu.svg"));
            }
            CPULabel.updateUI();
        }

        /**
         * executor 以及process调用 用于改变CPULabel显示的文字
         * @param s
         */
        public void setCPUText(String s){
            cpuProcessLabel.setText(s);
        }

        /**
         * 调用 以用来更新图片
         */
        public void updateUI(){
            queuePanel.updateUI();
        }

        /**
         * queue调用 以用来同步UI队列
         */
        public void clearQueue(){
            queuePanel.removeAll();
            queuePanel.updateUI();
        }

        /**
         * queue调用 用于向可视化队列中添加项,按照下标
         * @param index
         * @param processControlBlock
         */
        public void addPCBLabel(int index, ProcessControlBlock processControlBlock) {
            queuePanel.add(new PCBLabel(processControlBlock), index);
        }

        /**
         * queue调用 用于删除可视化队列中的一项
         * @param index
         */
        public void remove(int index){
            queuePanel.remove(index);
        }

        //外部类调用 用于作为外部类的处理程序 ----------------------------------------------------

        /**
         * 外部类调用 作为改变复选框的处理程序
         * @param flag
         * @param mills
         */
        private void setTimeCycle(boolean flag, int mills){
            executor.changeTimerConfig(flag,mills);
        }



        /**
         * 外部类调用 作为开始按钮和CPULabel的处理程序
         */
        public void changCPUStatus(){
            executor.pauseExecutor(!executor.getPauseStatus());
            setCPUText("正在等待响应...");
        }

        /**
         * 外部类调用 用于作为添加按钮的处理程序
         */
        public void addPCB(){
            int runtime = Integer.parseInt(String.valueOf(runtimeInput.getValue()==null?"1":runtimeInput.getValue()));
            int priority = Integer.parseInt(String.valueOf(priorityInput.getValue()==null?"1":priorityInput.getValue()));
            executor.addPCB(runtime,priority);
        }

        /**
         * 外部类调用 作为改变单选按钮的处理程序
         * @param processControlBlockComparator
         */
        public void changeQueueComparator(Comparator<ProcessControlBlock> processControlBlockComparator){
            System.out.println("请求更改比较规则:"+processControlBlockComparator.toString());
            executor.changeQueueComparator(processControlBlockComparator);
        }
        //自己用 ------------------------------------------------------------
        /**
         * 自己调用 处理来自外部类的单选按钮信息
         * @return
         */
        private int checkWhatRadioSelected() {
            Enumeration<AbstractButton> components = radioGroup.getElements();
            Iterator<AbstractButton> abstractButtonIterator = components.asIterator();
            while (abstractButtonIterator.hasNext()) {
                AbstractButton next = abstractButtonIterator.next();
                if (next.isSelected()) {
                    System.out.println(next.getText()+"选中!");
                    return Integer.parseInt(next.getText().trim());
                }
            }
            return -1;
        }
        /**
         * 外部调 用于获取新选择的队列规则
         * @return
         */
        public Comparator<ProcessControlBlock> getNewComparator(){
            return ProcessControlBlock.PCB_Comparator.getById(checkWhatRadioSelected()).getProcessControlBlockComparator();
        }

    }
    private ProcessScheduleFrame.FrameController controller;
    public ProcessScheduleFrame.FrameController getController(){
        if(controller==null){
            controller = new ProcessScheduleFrame.FrameController();
        }
        return controller;
    }
    public static void main(String[] args) {
        ProcessScheduleFrame processScheduleFrame = new ProcessScheduleFrame();
        Executor executor = new Executor(processScheduleFrame.getController());
        Thread thread = new Thread(executor::execute);
        thread.setDaemon(true);
        thread.start();
        processScheduleFrame.setVisible(true);
    }
    public ProcessScheduleFrame() {
        initComponents();
        myInit();
    }
    private ButtonGroup radioGroup = new ButtonGroup();
    private void myInit(){
        cpuProcessLabel.setVerticalTextPosition(JLabel.CENTER);
        cpuProcessLabel.setHorizontalTextPosition(JLabel.CENTER);
        radioGroup.add(radioFCFS);
        radioGroup.add(radioShort);
        radioGroup.add(radioLong);
        radioGroup.add(radioPrority);
        getController();
    }
    private boolean checkBoxTime(){
        return checkBoxTime.isSelected();
    }
    private int getCheckTime(){
        try {
            Object value = timeInput.getValue();
            int i = Integer.parseInt(value==null?"100":value.toString());
            return i;
        }catch (Exception e){
            return 100;
        }

    }

    private void cpuIconMouseClicked(MouseEvent e) {
        getController().changCPUStatus();
    }

    private void pauseButtonMouseClicked(MouseEvent e) {
        cpuIconMouseClicked(e);
    }

    private void addButtonMouseClicked(MouseEvent e) {
        getController().addPCB();
    }

    private void radioItemStateChanged(ItemEvent e) {
        // TODO add your code here
        controller.changeQueueComparator(controller.getNewComparator());
    }

    private void checkBoxTimeStateChanged(ChangeEvent e) {
        getController().setTimeCycle(checkBoxTime(),getCheckTime());
    }

    private void radioClicked(ActionEvent e) {
        controller.changeQueueComparator(controller.getNewComparator());
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - wainyz
        panel1 = new JPanel();
        pasueButton = new JButton();
        addPanel = new JPanel();
        label2 = new JLabel();
        label3 = new JLabel();
        runtimeInput = new MyFormattedNumberField();
        priorityInput = new MyFormattedNumberField();
        addButton = new JButton();
        comparetorPanel = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        radioFCFS = new JRadioButton();
        radioShort = new JRadioButton();
        radioLong = new JRadioButton();
        radioPrority = new JRadioButton();
        checkBoxTime = new JCheckBox();
        timeInput = new MyFormattedNumberField();
        queuePanel = new JPanel();
        CPULabel = new JLabel();
        cpuProcessLabel = new JLabel();

        //======== this ========
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("\u64cd\u4f5c\u7a7a\u95f4"));
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "作者: wainyz", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );

            //---- pasueButton ----
            pasueButton.setText("\u5f00\u59cb");
            pasueButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pauseButtonMouseClicked(e);
                }
            });

            //======== addPanel ========
            {
                addPanel.setBorder(new TitledBorder("\u6dfb\u52a0\u8fdb\u7a0b"));

                //---- label2 ----
                label2.setText("\u8fd0\u884c\u65f6\u95f4");

                //---- label3 ----
                label3.setText("\u4f18\u5148\u7ea7");

                //---- priorityInput ----
                priorityInput.setText("1");

                //---- addButton ----
                addButton.setText("\u52a0\u5165");
                addButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        addButtonMouseClicked(e);
                    }
                });

                GroupLayout addPanelLayout = new GroupLayout(addPanel);
                addPanel.setLayout(addPanelLayout);
                addPanelLayout.setHorizontalGroup(
                    addPanelLayout.createParallelGroup()
                        .addGroup(addPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(addPanelLayout.createParallelGroup()
                                .addComponent(addButton, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                                .addGroup(addPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(addPanelLayout.createSequentialGroup()
                                        .addComponent(label3)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(priorityInput, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(addPanelLayout.createSequentialGroup()
                                        .addComponent(label2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(runtimeInput, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))))
                            .addGap(6, 8, Short.MAX_VALUE))
                );
                addPanelLayout.setVerticalGroup(
                    addPanelLayout.createParallelGroup()
                        .addGroup(addPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(addPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2)
                                .addComponent(runtimeInput, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                            .addGap(9, 9, 9)
                            .addGroup(addPanelLayout.createParallelGroup()
                                .addComponent(label3)
                                .addComponent(priorityInput, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(addButton, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                );
            }

            //======== comparetorPanel ========
            {
                comparetorPanel.setBorder(new TitledBorder("\u8c03\u5ea6\u7b56\u7565"));

                //---- label4 ----
                label4.setText("\u5148\u6765\u5148\u670d\u52a1");

                //---- label5 ----
                label5.setText("\u77ed\u4f5c\u4e1a\u4f18\u5148");

                //---- label6 ----
                label6.setText("\u957f\u4f5c\u4e1a\u4f18\u5148");

                //---- label7 ----
                label7.setText("\u6309\u7167\u4f18\u5148\u7ea7");

                //---- label8 ----
                label8.setText("\u5206\u65f6\u7cfb\u7edf");

                //---- radioFCFS ----
                radioFCFS.setText(" 0");
                radioFCFS.setSelected(true);
                radioFCFS.addActionListener(e -> radioClicked(e));

                //---- radioShort ----
                radioShort.setText(" 1");
                radioShort.addActionListener(e -> radioClicked(e));

                //---- radioLong ----
                radioLong.setText(" 2");
                radioLong.addActionListener(e -> radioClicked(e));

                //---- radioPrority ----
                radioPrority.setText(" 3");
                radioPrority.addActionListener(e -> radioClicked(e));

                //---- checkBoxTime ----
                checkBoxTime.setText(" ");
                checkBoxTime.addChangeListener(e -> checkBoxTimeStateChanged(e));

                //---- timeInput ----
                timeInput.setColumns(3);
                timeInput.setText("100");

                GroupLayout comparetorPanelLayout = new GroupLayout(comparetorPanel);
                comparetorPanel.setLayout(comparetorPanelLayout);
                comparetorPanelLayout.setHorizontalGroup(
                    comparetorPanelLayout.createParallelGroup()
                        .addGroup(comparetorPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(comparetorPanelLayout.createParallelGroup()
                                .addGroup(comparetorPanelLayout.createSequentialGroup()
                                    .addComponent(label8)
                                    .addGap(18, 18, 18)
                                    .addComponent(checkBoxTime)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(timeInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(comparetorPanelLayout.createSequentialGroup()
                                    .addComponent(label4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(radioFCFS))
                                .addGroup(comparetorPanelLayout.createSequentialGroup()
                                    .addComponent(label5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(radioShort))
                                .addGroup(comparetorPanelLayout.createSequentialGroup()
                                    .addComponent(label7)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(radioPrority))
                                .addGroup(comparetorPanelLayout.createSequentialGroup()
                                    .addComponent(label6)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(radioLong)))
                            .addGap(0, 8, Short.MAX_VALUE))
                );
                comparetorPanelLayout.setVerticalGroup(
                    comparetorPanelLayout.createParallelGroup()
                        .addGroup(comparetorPanelLayout.createSequentialGroup()
                            .addGroup(comparetorPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(radioFCFS)
                                .addComponent(label4))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(comparetorPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(radioShort)
                                .addComponent(label5))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(comparetorPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label6)
                                .addComponent(radioLong))
                            .addGap(9, 9, 9)
                            .addGroup(comparetorPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(radioPrority)
                                .addComponent(label7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(comparetorPanelLayout.createParallelGroup()
                                .addComponent(checkBoxTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(timeInput, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                );
            }

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(pasueButton, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(comparetorPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(18, Short.MAX_VALUE))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pasueButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comparetorPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(37, Short.MAX_VALUE))
            );
        }
        contentPane.add(panel1);
        panel1.setBounds(530, 20, 195, 425);

        //======== queuePanel ========
        {
            queuePanel.setBorder(new TitledBorder("\u5c31\u7eea\u961f\u5217"));
            queuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        }
        contentPane.add(queuePanel);
        queuePanel.setBounds(10, 15, 175, 490);

        //---- CPULabel ----
        CPULabel.setIcon(new FlatSVGIcon("group/assets/cpu.svg"));
        CPULabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cpuIconMouseClicked(e);
            }
        });
        contentPane.add(CPULabel);
        CPULabel.setBounds(250, 10, 200, 170);
        contentPane.add(cpuProcessLabel);
        cpuProcessLabel.setBounds(270, 175, 160, 105);

        contentPane.setPreferredSize(new Dimension(740, 445));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - wainyz
    private JPanel panel1;
    private JButton pasueButton;
    private JPanel addPanel;
    private JLabel label2;
    private JLabel label3;
    private MyFormattedNumberField runtimeInput;
    private MyFormattedNumberField priorityInput;
    private JButton addButton;
    private JPanel comparetorPanel;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JRadioButton radioFCFS;
    private JRadioButton radioShort;
    private JRadioButton radioLong;
    private JRadioButton radioPrority;
    private JCheckBox checkBoxTime;
    private MyFormattedNumberField timeInput;
    private JPanel queuePanel;
    private JLabel CPULabel;
    private JLabel cpuProcessLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
