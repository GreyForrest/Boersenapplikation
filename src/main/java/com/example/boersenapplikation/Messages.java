package com.example.boersenapplikation;

import javax.swing.*;

public class Messages {
    public void getErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public int getYesNoMessage(String message) {
        int reply = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.YES_NO_OPTION);
        return reply;
    }
}
