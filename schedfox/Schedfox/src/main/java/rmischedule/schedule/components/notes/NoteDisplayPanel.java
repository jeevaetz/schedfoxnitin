/*
 * NoteDisplayPanel.java
 *
 * Created on February 13, 2006, 8:54 AM
 */

package rmischedule.schedule.components.notes;
import rmischeduleserver.data_connection_types.*;
import rmischedule.main.*;
import javax.swing.*;

import com.creamtec.ajaxswing.AjaxSwingManager;

import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author  Ira Juneau
 */
public class NoteDisplayPanel extends JPanel {
    
    int lastNum;
    int currNote;
    
    public int moveFirst = 0;
    public int movePrev = 1;
    public int moveNext = 2;
    public int moveLast = 3;
    private long lastAction;
    
    private static long timeToDelayShow = 500;
    private static long idleTimeToHide = 20000;
    private showMeIfInsideThread showMeThread;
    private hideMeIfIdleThread hideMeThread;
    private TabbedNoteDisplay myTabbedNoteDisplay;
    
    
    public NoteDisplayPanel() {
        initComponents();
        init();
        myTabbedNoteDisplay = new TabbedNoteDisplay();
        notePanel.add(myTabbedNoteDisplay);
    }
    
    public void init() {
        currNote = 0;
        lastNum  = 0;
        firstNote.setIcon(Main_Window.First16x16);
        prevNote.setIcon(Main_Window.Prev16x16);
        nextNote.setIcon(Main_Window.Next16x16);
        lastNote.setIcon(Main_Window.Last16x16);

        myScrollPane.getViewport().setBackground(Color.WHITE);
        hideMeThread = new hideMeIfIdleThread();
        showMeThread = new showMeIfInsideThread();
        showMeThread.start();
        hideMeThread.start();
    }
    
    public void killThreads() {
        showMeThread.killme = true;
        hideMeThread.killme = true;   
    }
    
    public void loadData(ArrayList<NoteData> notes, myNoteIconLabel iconOfOrigin) {
        showMeThread.setInitialComponent(iconOfOrigin, notes);
    }
    
    private void load(ArrayList<NoteData> notes, myNoteIconLabel iconOfOrigin) {
        lastAction = System.currentTimeMillis();
        //notePanel.removeAll();
        myTabbedNoteDisplay.clearData();
        lastNum = notes.size() - 1;
        for (int i = 0; i < notes.size(); i++) {
            NoteData currNote = notes.get(i);
            SingleNoteDisplay nextNote = new SingleNoteDisplay(currNote);
            myTabbedNoteDisplay.addNote(nextNote);
        }
        notePanel.setBounds(0, 0, 240 * notes.size(), notePanel.getBounds().height);
        notePanel.setPreferredSize(new Dimension(240 * notes.size(), notePanel.getBounds().height));
        notePanel.setMinimumSize(new Dimension(240 * notes.size(), notePanel.getBounds().height));
        notePanel.setMaximumSize(new Dimension(50000, 195));
        notePanel.revalidate();
        revalidate();
        myScrollPane.revalidate();
        
        repaint();
    }
    
    private void resetTimer() {
        lastAction = System.currentTimeMillis();
    }

    /**
     * Used to give some sort of delay before showing notes...Thread need to be converted badly
     */
    private class showMeIfInsideThread extends Thread {
        private myNoteIconLabel myStartLabel;
        private ArrayList<NoteData> notes;
        private boolean runMe;
        public boolean killme = false;
        
        public showMeIfInsideThread() {
            runMe = false;
        }
        
        public void setInitialComponent(myNoteIconLabel myIconLabel, ArrayList<NoteData> notes) {
            myStartLabel = myIconLabel;
            this.notes = notes;
            runMe = true;
        }
        
        public void run() {
            while (!killme) {
                try {
                    sleep(500);
                    Point myPoint = AjaxSwingManager.isAjaxSwingRunning() ? new Point(0,0) : MouseInfo.getPointerInfo().getLocation();
                    Rectangle myRect = getBounds();
                    Point myLocation = new Point(0,0);
                    if (isVisible()) {
                    	try {
                    		myLocation = getLocationOnScreen();
                    	} catch (IllegalComponentStateException ex) {
							if (AjaxSwingManager.isAjaxSwingRunning()) {} else {throw ex;}
						}
                    }
                    myRect.y = myLocation.y;
                    myRect.x = myLocation.x;
                    if(AjaxSwingManager.isAjaxSwingRunning()) {
                    	if (myRect.contains(myPoint)) {
                    		resetTimer();
                    	}
                    }
                    if (runMe) {
                        runMe = false;
                        myNoteIconLabel intial = myStartLabel;
                        sleep(timeToDelayShow);
                        if (myStartLabel == intial && intial.isInsideThisLabel()) {
                            load(this.notes, myStartLabel);
                            setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * If I am idle (no mouse events for long enough hide this window...
     */
    private class hideMeIfIdleThread extends Thread {
        public boolean killme = false;
        
        public hideMeIfIdleThread() {
            
        }
        
        public void run() {
            while (!killme) {
                try {
                     sleep(500);
                     if (isVisible()) {
                         if (System.currentTimeMillis() - lastAction > idleTimeToHide) {
                             setVisible(false);
                         }
                     }
                } catch (Exception e) {}
            }
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        controlPanel = new javax.swing.JPanel();
        firstNote = new javax.swing.JLabel();
        prevNote = new javax.swing.JLabel();
        NoteList = new javax.swing.JLabel();
        nextNote = new javax.swing.JLabel();
        lastNote = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        myScrollPane = new javax.swing.JScrollPane();
        notePanel = new javax.swing.JPanel();

        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.X_AXIS));

        controlPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        controlPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        controlPanel.setMinimumSize(new java.awt.Dimension(10, 20));
        controlPanel.setOpaque(false);
        controlPanel.setPreferredSize(new java.awt.Dimension(100, 20));
        firstNote.setMaximumSize(new java.awt.Dimension(16, 16));
        firstNote.setMinimumSize(new java.awt.Dimension(16, 16));
        firstNote.setPreferredSize(new java.awt.Dimension(16, 16));
        firstNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveFirst(evt);
            }
        });

        controlPanel.add(firstNote);

        prevNote.setMaximumSize(new java.awt.Dimension(16, 16));
        prevNote.setMinimumSize(new java.awt.Dimension(16, 16));
        prevNote.setPreferredSize(new java.awt.Dimension(16, 16));
        prevNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                movePrev(evt);
            }
        });

        controlPanel.add(prevNote);

        NoteList.setForeground(new java.awt.Color(31, 33, 121));
        NoteList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NoteList.setMaximumSize(new java.awt.Dimension(3004, 14));
        controlPanel.add(NoteList);

        nextNote.setMaximumSize(new java.awt.Dimension(16, 16));
        nextNote.setMinimumSize(new java.awt.Dimension(16, 16));
        nextNote.setPreferredSize(new java.awt.Dimension(16, 16));
        nextNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveNext(evt);
            }
        });

        controlPanel.add(nextNote);

        lastNote.setMaximumSize(new java.awt.Dimension(16, 16));
        lastNote.setMinimumSize(new java.awt.Dimension(16, 16));
        lastNote.setPreferredSize(new java.awt.Dimension(16, 16));
        lastNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveLast(evt);
            }
        });

        controlPanel.add(lastNote);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(280, 200));
        setMinimumSize(new java.awt.Dimension(200, 200));
        setPreferredSize(new java.awt.Dimension(280, 200));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jPanel1.setBackground(new java.awt.Color(193, 208, 241));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 15));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 15));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 15));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setMaximumSize(new java.awt.Dimension(24, 24));
        jLabel1.setMinimumSize(new java.awt.Dimension(24, 24));
        jLabel1.setPreferredSize(new java.awt.Dimension(24, 24));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hideMe(evt);
            }
        });

        jPanel1.add(jLabel1);

        add(jPanel1);

        myScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        myScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        myScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        notePanel.setLayout(new java.awt.GridLayout());

        notePanel.setBackground(new java.awt.Color(255, 255, 255));
        notePanel.setMaximumSize(new java.awt.Dimension(280000, 18000));
        notePanel.setMinimumSize(new java.awt.Dimension(220, 180));
        notePanel.setOpaque(false);
        notePanel.setPreferredSize(new java.awt.Dimension(220, 180));
        notePanel.setRequestFocusEnabled(false);
        myScrollPane.setViewportView(notePanel);

        add(myScrollPane);

    }// </editor-fold>//GEN-END:initComponents

    private void hideMe(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideMe
        setVisible(false);
    }//GEN-LAST:event_hideMe

    private void movePrev(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_movePrev
     
    }//GEN-LAST:event_movePrev

    private void moveFirst(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveFirst
       
    }//GEN-LAST:event_moveFirst

    private void moveLast(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveLast
      
    }//GEN-LAST:event_moveLast

    private void moveNext(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveNext
        
    }//GEN-LAST:event_moveNext
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NoteList;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel firstNote;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lastNote;
    private javax.swing.JScrollPane myScrollPane;
    private javax.swing.JLabel nextNote;
    private javax.swing.JPanel notePanel;
    private javax.swing.JLabel prevNote;
    // End of variables declaration//GEN-END:variables
    
}
