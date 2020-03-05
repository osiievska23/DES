package com.vosievskaya.des.implementation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Runner {

    private static String dataTransporObject = "";

    public static void main(String[] args) {
        JFrame frame = new JFrame("DSA implementation");

        JLabel messageLabel = new JLabel("Enter your message, please)");
        JLabel keyLabel = new JLabel("Key word:");
        JLabel label2 = new JLabel("Key word:");

        JTextField messageData = new JTextField();
        JTextField keyData = new JTextField();
        JTextField encodedData = new JTextField();
        JTextField decodedData = new JTextField();

        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");

        messageLabel.setBounds(35, 10, 200, 40);
        messageData.setBounds(30, 40, 420, 40);
        keyLabel.setBounds(35, 80, 200, 40);
        keyData.setBounds(30, 110, 420, 40);
        encodeButton.setBounds(35, 160, 200, 40);
        decodeButton.setBounds(250, 160, 200, 40);
        encodedData.setBounds(30, 210, 420, 40);
        decodedData.setBounds(30, 260, 420, 40);

        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageData.getText();
                String key = keyData.getText();
                DES encoder = new DES();
                String[] res = encoder.encode(message, key);
                keyData.setText(res[0]);
                dataTransporObject = res[1];
                encodedData.setText(res[2]);
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = dataTransporObject;
                DES decoder = new DES();
                String result = decoder.decode(data, encodedData.getText());
                decodedData.setText(result);
            }
        });

        frame.add(messageData);
        frame.add(keyLabel);
        frame.add(label2);
        frame.add(messageLabel);
        frame.add(encodedData);
        frame.add(decodedData);
        frame.add(keyData);
        frame.add(encodeButton);
        frame.add(decodeButton);

        frame.setSize(500, 350);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
