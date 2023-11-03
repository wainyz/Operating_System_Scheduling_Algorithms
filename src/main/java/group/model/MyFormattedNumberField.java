package group.model;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class MyFormattedNumberField extends JFormattedTextField {
    public MyFormattedNumberField(){
        super(NumberFormat.getNumberInstance());
    }
}
