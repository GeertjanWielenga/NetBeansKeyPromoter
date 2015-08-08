package com.netbeans.key.promoter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import org.openide.awt.Mnemonics;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatusLineElementProvider.class, position = 0)
public class KeyPromoterStatusLineElementProvider implements StatusLineElementProvider {

	private final Logger logger = Logger.getLogger("org.netbeans.ui.actions");

	private JLabel label;
	private Action action;

	public KeyPromoterStatusLineElementProvider() {
		label = new JLabel();
		logger.addHandler(new StreamHandler() {
			@Override
			public void publish(LogRecord record) {
				Object[] parameters = record.getParameters();
				if (parameters[2] instanceof Action) {
					action = (Action) parameters[2];
					JMenuItem menu = new JMenuItem();
					Mnemonics.setLocalizedText(menu, action.getValue(Action.NAME).toString());
					if (doesButtonHasShortcut(action) && !isUsingKeyboard()) {
						String shortcut = getShortcut(action.getValue(Action.ACCELERATOR_KEY).toString());
						String actionName = getActionName(menu.getText());
						String message = formatMessage(shortcut, actionName);
						displayMessage(message);
					}
				}
			}

			private boolean doesButtonHasShortcut(Action a) {
				return a.getValue(Action.ACCELERATOR_KEY) != null;
			}

			private String formatMessage(String shortcut, String actionName) {
				return String.format("%s (%s)", shortcut, actionName);
			}

			private String getShortcut(String shortcut) {
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
				if (true) {
					displayMessageInStatusbar(message);
				} else {
					displayMessageInPopupBallon(message);
				}
			}

			private boolean isUsingKeyboard() {
				// bad bad bad, very bad boy, but no other way
				Thread thread = Thread.currentThread();
				StackTraceElement[] stackTrace = thread.getStackTrace();
				StackTraceElement fixedMethodStackTrace = stackTrace[7];
				String methodName = fixedMethodStackTrace.getMethodName();
				boolean startsWithProcessKeyBinding = methodName.startsWith("processKeyBinding");
				return startsWithProcessKeyBinding;
			}
		});
	}

	private void displayMessageInPopupBallon(String message) {
		NotificationDisplayer.getDefault().notify("You could use shortcut instead", new ImageIcon("/org/nb/kp/car.png"), message, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(e);
			}
		});
	}

	private void displayMessageInStatusbar(String message) {
		label.setText(message);
	}

	@Override
	public Component getStatusLineElement() {
		return label;
	}

}
