package com.netbeans.key.promoter;

import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.openide.awt.Mnemonics;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(position = 1, service = StatusLineElementProvider.class)
public class DemoStatusLineElementProvider implements StatusLineElementProvider {

    private static final Logger LOGGER = Logger.getLogger("org.netbeans.ui.actions");
    private static final Logger UILOG = Logger.getLogger("org.netbeans.ui.actions.editor"); // NOI18N
    static {
        UILOG.setLevel(Level.ALL);
    }
    private DisplayPanel displayPanel = new DisplayPanel();

    private Action action;

    public DemoStatusLineElementProvider() {
        final StreamHandlerImpl handler = new StreamHandlerImpl();
        LOGGER.addHandler(handler);
        UILOG.addHandler(handler);
    }

    @Override
    public Component getStatusLineElement() {
        displayPanel.setPreferredSize(new Dimension(1200, 20));
        return displayPanel;
    }

    private class StreamHandlerImpl extends StreamHandler {

        public StreamHandlerImpl() {
        }

        @Override
        public void publish(LogRecord record) {
            Object[] parameters = record.getParameters();
            if (parameters[2] instanceof Action) {
                action = (Action) parameters[2];
                JMenuItem menu = new JMenuItem();
                Mnemonics.setLocalizedText(menu, action.getValue(Action.NAME).toString());
                if (doesButtonHasShortcut(action)) {
                    String shortcut = formatShortcut(getShortcut().toString());
                    String actionName = getActionName(menu.getText());
                    String message = formatMessage(shortcut, actionName);
                    displayMessage(message);
                } else {
                    displayMessage("'" + menu.getText() + "' has no shortcut.");
                }
            } else if (record.getMessage().equals("UI_ACTION_KEY_PRESS")) {
                JMenuItem menu = new JMenuItem();
                Mnemonics.setLocalizedText(menu, parameters[4].toString());
                String shortcut = formatShortcut(parameters[1].toString());
                String message = formatMessage(shortcut, menu.getText());
                displayMessage(message);
            }
        }

        private Object getShortcut() {
            Object result = action.getValue(Action.ACTION_COMMAND_KEY);
            if(result == null) {
                result = action.getValue(Action.ACCELERATOR_KEY);
            }
            return result;
        }

        private boolean doesButtonHasShortcut(Action a) {
            return a.getValue(Action.ACTION_COMMAND_KEY) != null ||
                    a.getValue(Action.ACCELERATOR_KEY) != null;
        }

        private String formatMessage(String shortcut, String actionName) {
            return String.format("%s (%s)", shortcut, actionName);
        }
        
        

        private String formatShortcut(String shortcut) {
            return shortcut
                    .replace("pressed", "")
                    .replace("alt", "Alt -")
                    .replace("ctrl", "Ctrl -")
                    .replace("shift", "Shift -");
        }

        private String getActionName(String actionName) {
            return actionName.replace("...", "");
        }

        private void displayMessage(String message) {
            displayPanel.setText(message);
        }
    }

}
