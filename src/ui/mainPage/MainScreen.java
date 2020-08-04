package ui.mainPage;

import ui.circuitDrawer.CircuitDrawer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainScreen extends JFrame  {
    public static final int SCREEN_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    public static final Border PADDING = new EmptyBorder(5,5,5,5);
    public static final Color BK_COLOR = Color.LIGHT_GRAY;

        //Sizes
    public static final Dimension resultDimension = new Dimension(300,SCREEN_HEIGHT - ControlPanel.HEIGHT);
    public static final Dimension resultTextDimension = new Dimension(290,SCREEN_HEIGHT - ControlPanel.HEIGHT - 10);
    JTextArea resultsTextArea;

        //Labels
    JLabel resultsLabel = new JLabel("Results");
        //Panels
    ControlPanel controlPanel;
    JPanel resultPanel;
    JPanel showedPanel = new JPanel();
    JPanel editorPanel = new JPanel();
    GetResultsPanel getResultsPanel;
        //Panes
    JEditorPane editorPane = new JEditorPane();
    JScrollPane scrollPane ;

    //Creators
    public MainScreen(){
        this.setBounds((SCREEN_WIDTH - WIDTH)/2,(SCREEN_HEIGHT-HEIGHT)/2,WIDTH,HEIGHT);
        this.setMinimumSize(new Dimension(500,600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Circuit Simulator");
        this.setBackground(BK_COLOR);
        this.loadMainPanel(showedPanel);
        this.add(showedPanel);
        this.setVisible(true);

    }
    private void loadMainPanel(JPanel panel){
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        this.controlPanel = new ControlPanel();
        panel.add(controlPanel,BorderLayout.NORTH);
        this.createEditorPanel();
        panel.add(editorPanel,BorderLayout.CENTER);
        this.createResultPanel();
        panel.add(resultPanel,BorderLayout.EAST);

    }
    private void createResultPanel(){
        //Panel
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBorder(PADDING);
        resultPanel.setBackground(BK_COLOR);
        resultPanel.setPreferredSize(resultDimension);
        //Text Area
        resultsTextArea = new JTextArea("After solving the circuit,\nthe results will be displayed here...");
        //resultsTextArea.setPreferredSize(resultTextDimension);
        resultsTextArea.setBackground(new Color(220, 220, 220));
        //Scroll Pane
        scrollPane = new JScrollPane(resultsTextArea);
        scrollPane.setPreferredSize(resultTextDimension);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black,4,true));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        //Label
        resultsLabel.setHorizontalAlignment(0);
        resultsLabel.setPreferredSize(new Dimension(resultTextDimension.width,50));
        resultsLabel.setForeground(Color.red);
        resultsLabel.setFont(resultPanel.getFont().deriveFont(30.0f));
        //Get Results Panel
        createGetResultsPanel();
        //Add
        resultPanel.add(resultsLabel,BorderLayout.NORTH);
        resultPanel.add(scrollPane,BorderLayout.CENTER);
        resultPanel.add(getResultsPanel,BorderLayout.SOUTH);
        resultsTextArea.setEnabled(false);


    }
    private void createGetResultsPanel(){
        getResultsPanel = new GetResultsPanel();

    }
    private void createEditorPanel(){
        editorPane.setBorder(BorderFactory.createLineBorder(Color.black, 4,true));
        editorPanel.setBorder(PADDING);
        editorPanel.setLayout(new BorderLayout());
        editorPanel.setBackground(BK_COLOR);
        editorPanel.add(editorPane,BorderLayout.CENTER);
        editorPane.setText("After You Load Your Input, You Will Be Able To Edit It in Here!!!");
        editorPane.setEnabled(false);
    }

    //Editor
    public void fillEditorPane(List<String> inputList){
        String text = "";
        for(String input : inputList){
            text = text + input + "\n";
        }
        editorPane.setText(text.substring(0,text.length()-1));
        editorPane.setEnabled(true);
    }
    public String getEditorTextList() {
        return editorPane.getText();
    }


    //Result
    public void logResults(String text){
        resultsTextArea.append(text);
        resultsTextArea.setEnabled(true);
        resultsTextArea.setEditable(false);
    }
    public void resetResultsText() {
        resultsTextArea.setText("");
    }
}
