package com.nerzid;

/**
 * Created by @author nerzid on 28.03.2018.
 */
public class FileNotSelected extends Exception {

    public static final String MESSAGE_NO_DIR_SELECTED = "No folder selected.";
    public static final String MESSAGE_NO_FILE_SELECTED = "No file selected.";

    /**
     * Creates a new instance of <code>FileNotSelected</code> without detail
     * message.
     */
    public FileNotSelected() {

    }

    public FileNotSelected(String msg){
        super(msg);
    }

}