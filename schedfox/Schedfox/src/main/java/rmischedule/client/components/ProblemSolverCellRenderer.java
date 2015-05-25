/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import schedfoxlib.model.Problemsolver;

/**
 *
 * @author user
 */
public class ProblemSolverCellRenderer extends JPanel implements ListCellRenderer {

    private JPanel datePanel = new JPanel();
    private JPanel textPanel = new JPanel();
    private JLabel dateLabel = new JLabel();
    private JTextArea descTextArea = new JTextArea();

    public ProblemSolverCellRenderer() {
        super();

        this.datePanel.setLayout(new BorderLayout());
        this.textPanel.setLayout(new BorderLayout());

        Border lineBorder = BorderFactory.createLineBorder(new Color(100, 125, 175));
        Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        this.setBorder(BorderFactory.createCompoundBorder(emptyBorder, lineBorder));

        this.datePanel.setBorder(emptyBorder);
        this.textPanel.setBorder(emptyBorder);
        this.datePanel.setMinimumSize(new Dimension(90, 19));
        this.datePanel.setPreferredSize(new Dimension(90, 19));
        this.datePanel.setMaximumSize(new Dimension(90, 32767));
        this.descTextArea.setLineWrap(true);
        this.descTextArea.setWrapStyleWord(true);
        this.descTextArea.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
        this.dateLabel.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
        this.dateLabel.setOpaque(true);
        this.dateLabel.setBackground(Color.WHITE);
        this.dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.datePanel.add(this.dateLabel, BorderLayout.CENTER);
        this.textPanel.add(this.descTextArea, BorderLayout.CENTER);
        this.add(this.datePanel);
        this.add(this.textPanel);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Problemsolver ps = (Problemsolver) value;

        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
        }
        if (ps.getPsDate() != null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            this.dateLabel.setText(myFormat.format(ps.getPsDate()));
        }
        this.descTextArea.setText(ps.getProblem());

        //Necessary little evil to get the word-wrap feature to work as expected on our text area,
        //and have variable height list cells
        this.setBounds(0, 0, list.getWidth(), 32767);
        this.descTextArea.invalidate();
        try {
            this.validateTree();
        } catch (Exception exe) {}
        this.setPreferredSize(new Dimension(list.getWidth(), this.descTextArea.getPreferredScrollableViewportSize().height + 8));

        this.setFont(list.getFont());
        return this;
    }

    public void setFont(Font f) {
        super.setFont(f);
        for (Component comp : this.getComponents()) {
            comp.setFont(f);
        }
    }

    public void setBackground(Color c) {
        super.setBackground(c);
        for (Component comp : this.getComponents()) {
            comp.setBackground(c);
        }
    }

    public void setForeground(Color c) {
        super.setForeground(c);
        for (Component comp : this.getComponents()) {
            comp.setForeground(c);
        }
    }
}
