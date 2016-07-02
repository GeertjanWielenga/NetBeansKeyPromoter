package com.netbeans.key.promoter;

public class DisplayPanel extends javax.swing.JPanel {

    public DisplayPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 255, 51));
        setLayout(new java.awt.BorderLayout());

        displayLabel.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        displayLabel.setForeground(new java.awt.Color(0, 0, 0));
        displayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(displayLabel, org.openide.util.NbBundle.getMessage(DisplayPanel.class, "DisplayPanel.displayLabel.text")); // NOI18N
        add(displayLabel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel displayLabel;
    // End of variables declaration//GEN-END:variables

    void setText(String text) {
        displayLabel.setText(text);
    }

}
