package org.nfmp.dev.gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger("com.org.nfmp.TestFrame");
    private static final String DEFAULT_TITLE = "Development Utility";
    private static final String lScript = "scripts/script.sh";
    private static final String lDefaultModules = "nms nms_nodes";

    private static final String[] lBranches = {"NFMP_MAIN", "NFMP_22_11_REL"};

    private String lBranch;
    private String lFolder;

    private JComboBox<String> lBranchComboBox;
    private JButton lFileChooserButton;
    private JTextField lFileChooserTextField;


    public TestFrame() throws HeadlessException {
        super();
        this.setComponents();
        this.setDefaults();

    }

    private void setComponents() {
        setBranchComboBox();
        setFileChooserButtonAndTextField();
        setCheckoutButton();

    }

    private void setCheckoutButton() {
        JButton lCheckout = new JButton("Checkout");

        lCheckout.addActionListener(e->{
            executeCheckoutScript();
        });

        this.add(lCheckout);

    }

    private void executeCheckoutScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(lScript, lFolder, lBranch, lDefaultModules);
            Process process = processBuilder.start();
            int exitStatus = process.waitFor();

            String line;
            LOGGER.info("***** Script execution Starts *****");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                LOGGER.info(line);
            }
            if (exitStatus != 0) {
                bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    LOGGER.log(Level.SEVERE, line);
                }
                throw new IllegalStateException("Script exited abnormally");
            }
            LOGGER.info("***** Script executed successfully *****");

        } catch (InterruptedException | IOException e) {
            LOGGER.log(Level.SEVERE,"Error during script execution", e);
        }
    }

    private void setFileChooserButtonAndTextField() {
        lFileChooserTextField = new JTextField();
        lFileChooserTextField.setEditable(false);
        lFileChooserTextField.setColumns(30);
        lFileChooserButton = new JButton("Select Folder");
        lFileChooserButton.addActionListener(e ->{
            if(e.getSource() == lFileChooserButton){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File("/"));
                int fileAction = fileChooser.showOpenDialog(null); // select file to open
                if(fileAction == JFileChooser.APPROVE_OPTION){
                    String lApprovedPath = fileChooser.getSelectedFile().getAbsolutePath();
                    lFileChooserTextField.setText(lApprovedPath);
                    lFolder = lApprovedPath;
                }

            }
        });

        this.add(lFileChooserButton);
        this.add(lFileChooserTextField);
    }

    private void setBranchComboBox() {
        lBranchComboBox = new JComboBox<>(lBranches);

        lBranchComboBox.addActionListener(e -> {
            if(e.getSource() == lBranchComboBox){
                Object lSelectedItem = ((JComboBox)e.getSource()).getSelectedItem();
                if(lSelectedItem != null){
                    lBranch = lSelectedItem.toString();
                }
            }
        });

        this.add(lBranchComboBox);
    }

    private void setDefaults(){
        this.setTitle(DEFAULT_TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.pack();
        this.setVisible(true);
    }

}