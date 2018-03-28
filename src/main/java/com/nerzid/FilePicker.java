
package com.nerzid;

import org.apache.commons.io.FileUtils;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by @author nerzid on 28.03.2018.
 */
public class FilePicker {

    /**
     * Prepares and returns java files to be auto-commented in the future
     *
     * @return
     */
    public static ArrayList<File> chooseAndGetJavaFiles() throws FileNotSelected {
        setLookAndFeel();
        ArrayList<File> files_list = null;

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JAVA Source Files (.java)", "java");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(true);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] files_arr = fc.getSelectedFiles();
            files_list = new ArrayList<>(Arrays.asList(files_arr));
        } else {
            throw new FileNotSelected(FileNotSelected.MESSAGE_NO_FILE_SELECTED);
        }

        return files_list;
    }

    public static LinkedList<File> chooseDirAndGetJavaFiles() throws FileNotSelected {
        setLookAndFeel();
        LinkedList<File> files_list = null;

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String[] extn = {"java"};

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            files_list = (LinkedList<File>) FileUtils.listFiles(fc.getSelectedFile(), extn, true);
        } else {
            throw new FileNotSelected(FileNotSelected.MESSAGE_NO_DIR_SELECTED);
        }

        for (File f : files_list) {
            System.out.println("Name: " + f.getName() + " ||| Path: " + f.getAbsolutePath());
        }

        return files_list;
    }

    public static File chooseFile() throws FileNotSelected{
        setLookAndFeel();

        File f = null;

        JFileChooser fc = new  JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JAVA Source Files (.java)", "java");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(false);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
        } else {
            throw new FileNotSelected(FileNotSelected.MESSAGE_NO_FILE_SELECTED);
        }

        return f;
    }

    public static File chooseDir() throws FileNotSelected{
        setLookAndFeel();

        File f = null;

        JFileChooser fc = new  JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
        } else {
            throw new FileNotSelected(FileNotSelected.MESSAGE_NO_FILE_SELECTED);
        }

        return f;
    }

    public static String getFilePath(File f){
        return f == null ? null : f.getPath();
    }

    private static void setLookAndFeel() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (System.getProperty("os.name").contains("Linux")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FilePicker.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
