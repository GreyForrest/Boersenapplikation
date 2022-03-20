package com.example.boersenapplikation;

import javax.swing.*;

public class Messages {
    public static void getErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
