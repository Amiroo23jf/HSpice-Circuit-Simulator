package ui.mainPage;

import extra.Errors;
import extra.NumberConverter;
import base.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GetResultsPanel extends JPanel implements ActionListener {
    public static final int HEIGHT = 100;
    Dimension dimension = new Dimension(MainScreen.resultTextDimension.width/3,HEIGHT/3);
    JPanel labelPanel;
    JPanel inputsPanel;
    JPanel resultPanel;
    JLabel firstNodeLabel = new JLabel("Positive");
    JLabel secondNodeLabel = new JLabel("Negative");
    JLabel timeLabel = new JLabel("Time");
    JTextField firstNode ;
    JTextField secondNode;
    JTextField time;
    JLabel voltageLabel = new JLabel("Voltage :");
    JTextField voltage;
    JButton calculateButton = new JButton("Calculate");
    public GetResultsPanel(){
        this.setPreferredSize(new Dimension(MainScreen.resultTextDimension.width,HEIGHT));
        this.setLayout(new BorderLayout());
        createLabelPanel();
        createInputsPanel();
        createResultsPanel();
        this.add(labelPanel,BorderLayout.NORTH);
        this.add(inputsPanel,BorderLayout.CENTER);
        this.add(resultPanel,BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createLineBorder(Color.black,4));
        this.setVisible(true);


    }

    private void createResultsPanel() {
        voltageLabel.setPreferredSize(dimension);
        voltageLabel.setHorizontalAlignment(0);
        voltage = new JTextField();
        voltage.setPreferredSize(dimension);
        voltage.setEnabled(false);
        calculateButton.setPreferredSize(dimension);
        calculateButton.addActionListener(this);
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(voltageLabel,BorderLayout.WEST);
        resultPanel.add(voltage,BorderLayout.CENTER);
        resultPanel.add(calculateButton,BorderLayout.EAST);


    }

    private void createInputsPanel() {
        firstNode = new JTextField();
        secondNode = new JTextField();
        time = new JTextField();
        firstNode.setPreferredSize(dimension);
        secondNode.setPreferredSize(dimension);
        time.setPreferredSize(dimension);
        inputsPanel = new JPanel();
        inputsPanel.setPreferredSize(new Dimension(MainScreen.resultTextDimension.width,HEIGHT/3));
        inputsPanel.setLayout(new BorderLayout());
        inputsPanel.add(firstNode,BorderLayout.WEST);
        inputsPanel.add(secondNode,BorderLayout.CENTER);
        inputsPanel.add(time,BorderLayout.EAST);

    }

    private void createLabelPanel() {
        firstNodeLabel.setPreferredSize(dimension);
        secondNodeLabel.setPreferredSize(dimension);
        timeLabel.setPreferredSize(dimension);
        firstNodeLabel.setHorizontalAlignment(0);
        secondNodeLabel.setHorizontalAlignment(0);
        timeLabel.setHorizontalAlignment(0);
        labelPanel = new JPanel();
        labelPanel.setPreferredSize(new Dimension(MainScreen.resultTextDimension.width,HEIGHT/3));
        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(firstNodeLabel,BorderLayout.WEST);
        labelPanel.add(secondNodeLabel,BorderLayout.CENTER);
        labelPanel.add(timeLabel,BorderLayout.EAST);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton ){
            if(Database.getInstance().isCircuitSolved()) {
                String pNode = firstNode.getText();
                String nNode = secondNode.getText();
                double calcTime;
                if(time.getText().equals("")){
                    Errors.WrongTimeInputForResults();
                    return;
                }
                try {
                    calcTime = NumberConverter.convert(time.getText());
                } catch (Exception ex) {
                    Errors.WrongTimeInputForResults();
                    return;
                }
                if (Database.getInstance().isNodeFound(pNode) && Database.getInstance().isNodeFound(nNode)) {
                    double pVoltage = Database.getInstance().findNodeVoltage(pNode, calcTime);
                    double nVoltage = Database.getInstance().findNodeVoltage(nNode, calcTime);
                    double tVoltage = Math.round((pVoltage - nVoltage) * 1000) / 1000.0;
                    Database.getInstance().log("Voltage You Are Looking For is : " + (pVoltage - nVoltage));
                    voltage.setText(String.valueOf(tVoltage));

                } else {
                    Errors.nodeNotFound();
                }
            }
            else{
                Errors.circuitNotSolved();
            }
        }
    }
}
