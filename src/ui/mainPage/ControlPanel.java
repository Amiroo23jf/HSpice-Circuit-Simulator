package ui.mainPage;

import extra.Errors;
import base.Database;
import ui.circuitDrawer.CircuitDrawer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ControlPanel extends JPanel implements ActionListener {
    public static final int HEIGHT = 150;
    public static final int WIDTH = MainScreen.WIDTH;

    CircuitDrawer circuitDrawer;
    Dimension panelDimensions = new Dimension(ControlPanel.WIDTH,ControlPanel.HEIGHT/3);
    Dimension labelDimensions = new Dimension(100,50);
    Dimension buttonDimensions = new Dimension(100,50);
    JPanel srcPanel = new JPanel();
    JPanel savePanel = new JPanel();
    JPanel resultsPanel = new JPanel();
    JLabel srcFileLabel = new JLabel("File Path : ");
    JLabel saveFolderLabel = new JLabel("Result Folder : ");
    JButton loadButton = new JButton("Load File");
    JButton runButton = new JButton("Run");
    JButton plotButton = new JButton("Open Plots");
    JButton circuitButton = new JButton("Show Circuit");
    JTextField srcTextField = new JTextField();
    JTextField saveFolderTextField = new JTextField();
    JFileChooser fileChooser = new JFileChooser();
    public ControlPanel(){
        this.setBackground(MainScreen.BK_COLOR);
        this.setPreferredSize(new Dimension(ControlPanel.WIDTH,ControlPanel.HEIGHT));
        this.setLayout(new BorderLayout());
        this.createSrcPanel();
        this.add(srcPanel,BorderLayout.NORTH);
        this.createSavePanel();
        this.add(savePanel,BorderLayout.CENTER);
        this.createResultsPanel();
        this.add(resultsPanel,BorderLayout.SOUTH);
        this.setBorder(MainScreen.PADDING);
        this.setVisible(true);

    }

    private void createResultsPanel() {
        this.resultsPanel.setMaximumSize(panelDimensions);
        this.resultsPanel.setPreferredSize(panelDimensions);
        this.resultsPanel.setBackground(Color.LIGHT_GRAY);
        this.resultsPanel.setLayout(new BorderLayout());
        this.plotButton.setPreferredSize(buttonDimensions);
        this.plotButton.setMaximumSize(buttonDimensions);
        this.circuitButton.setPreferredSize(buttonDimensions);
        this.circuitButton.setMaximumSize(buttonDimensions);
        this.plotButton.addActionListener(this);
        this.circuitButton.addActionListener(this);
        this.resultsPanel.add(plotButton,BorderLayout.CENTER);
        this.resultsPanel.add(circuitButton,BorderLayout.EAST);
        this.resultsPanel.setVisible(true);

    }

    private void createSrcPanel(){
        this.srcPanel.setMaximumSize(panelDimensions);
        this.srcPanel.setPreferredSize(panelDimensions);
        this.srcPanel.setBackground(Color.LIGHT_GRAY);
        this.srcPanel.setLayout(new BorderLayout());
        this.srcFileLabel.setPreferredSize(labelDimensions);
        this.srcFileLabel.setHorizontalAlignment(0);
        this.srcTextField.setText("Please Click Load File To Select Your Input File...");
        this.srcTextField.setEnabled(false);
        this.srcTextField.setEditable(false);
        this.loadButton.setPreferredSize(buttonDimensions);
        this.loadButton.setMaximumSize(buttonDimensions);
        this.loadButton.addActionListener(this);
        this.srcPanel.add(srcFileLabel,BorderLayout.WEST);
        this.srcPanel.add(loadButton,BorderLayout.EAST);
        this.srcPanel.add(srcTextField,BorderLayout.CENTER);
        this.srcPanel.setVisible(true);
    }
    private void createSavePanel(){
        this.savePanel.setMaximumSize(panelDimensions);
        this.savePanel.setPreferredSize(panelDimensions);
        this.savePanel.setBackground(Color.LIGHT_GRAY);
        this.savePanel.setLayout(new BorderLayout());
        this.saveFolderLabel.setPreferredSize(labelDimensions);
        this.saveFolderLabel.setHorizontalAlignment(0);
        this.saveFolderTextField.setText("Please Enter The Results Folder's Name Here...");
        this.runButton.setPreferredSize(buttonDimensions);
        this.runButton.setMaximumSize(buttonDimensions);
        this.runButton.addActionListener(this);
        this.savePanel.add(saveFolderLabel,BorderLayout.WEST);
        this.savePanel.add(runButton,BorderLayout.EAST);
        this.savePanel.add(saveFolderTextField,BorderLayout.CENTER);
        this.savePanel.setVisible(true);

    }
    private void fillSrcTextField(){
        srcTextField.setText(Database.getInstance().getCodeSrc());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loadButton){
            if(!Database.getInstance().isFileLoaded()) {
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Only .txt Files","txt"));
                int returnVal = fileChooser.showDialog(this,"Load");
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    Database.getInstance().setCodeSrc(fileChooser.getSelectedFile().getAbsolutePath());
                    fillSrcTextField();
                    Database.getInstance().fillEditor();
                }
            }
            else{
                Errors.fileLoadedBefore();
            }
        }
        else if(e.getSource() == runButton){
            if(Database.getInstance().isFileLoaded() && !Database.getInstance().isFileRun()){
                if(Database.getInstance().setResultsFolder(saveFolderTextField.getText())) {
                    saveFolderTextField.setEditable(false);
                    runButton.setEnabled(false);
                    loadButton.setEnabled(false);
                    Database.getInstance().updateInputFile();
                    Database.getInstance().getMainScreen().editorPane.setEnabled(false);
                    Database.getInstance().getMainScreen().resetResultsText();
                    try {
                        Database.getInstance().runCode();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    Errors.enterSavingFolder();
                }
            }
            else{
                Errors.fileNotLoaded();
            }
        }
        else if(e.getSource() == circuitButton){
            if (true/*Database.getInstance().isCircuitSolved()*/){
                circuitDrawer = new CircuitDrawer();
            }
            else{
                Errors.circuitNotSolved();
            }
        }
    }
}
