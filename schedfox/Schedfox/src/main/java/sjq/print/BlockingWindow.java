
/*
 * BlockingWindow.java
 *
 * Created on Janueary 15, 2007, 8:42 PM
 */

package sjq.print;

import java.awt.*;
import javax.swing.*;
import rmischedule.main.Main_Window;

public class BlockingWindow extends JFrame {
  private JProgressBar progress;
  private JLabel label1;
  private JPanel topPanel;


  public BlockingWindow(JPanel  superior) {
      

Main_Window.parentOfApplication.setEnabled(false);    
    setTitle("Printing");
    setSize(50, 100);
    setBackground(Color.gray);
    this.setLocation(superior.getLocationOnScreen());
    this.setResizable(false);
    this.setEnabled(false);
    this.setFocusable(false);
    this.setAlwaysOnTop(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);


    topPanel = new JPanel();
    boolean runing = true;
    topPanel.setPreferredSize(new Dimension(350, 95));
    getContentPane().add(topPanel);

    // Create a label and progress bar
    label1 = new JLabel("Printing...");
    label1.setPreferredSize(new Dimension(280, 28));
    topPanel.add(label1);
    
    progress = new JProgressBar();
    progress.setPreferredSize(new Dimension(300, 20));
    progress.setIndeterminate(true);
    progress.setBounds(90, 35, 260, 20);
    topPanel.add(progress);

    label1 = new JLabel("Please wait...");
    label1.setPreferredSize(new Dimension(280, 28));
    topPanel.add(label1);
  }

  public void setIndeterminate(boolean val){
    progress.setIndeterminate(val);
  }

  public void setLabelText(String label){
    label1.setText(label);
  }

  public void setProgressStatus(int minimum,int maximum,int value){
    progress.setMinimum(minimum);
    progress.setMaximum(maximum);
    progress.setValue(value);
  }

  public void mandar() {
    Rectangle progressRect = progress.getBounds();
    progressRect.x = 0;
    progressRect.y = 0;
    progress.paintImmediately(progressRect);
  }

}