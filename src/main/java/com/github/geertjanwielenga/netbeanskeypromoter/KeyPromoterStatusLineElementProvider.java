package com.github.geertjanwielenga.netbeanskeypromoter;

import java.awt.Component;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import org.openide.awt.Mnemonics;
import org.openide.awt.StatusLineElementProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatusLineElementProvider.class, position = 0)
public class KeyPromoterStatusLineElementProvider implements StatusLineElementProvider {

    private JLabel label;
    private FileObject fo;
    

    public KeyPromoterStatusLineElementProvider() {
        label = new JLabel();
        Logger logger = Logger.getLogger("org.netbeans.ui.actions");
        logger.addHandler(new StreamHandler() {
            @Override
            public void publish(LogRecord record) {
                Object[] parameters = record.getParameters();
                if (parameters[2] instanceof Action) {
                    Action a = (Action) parameters[2];
                    JMenuItem menu = new JMenuItem();
                    Mnemonics.setLocalizedText(
                            menu,
                            a.getValue(Action.NAME).toString());
                    String name = menu.getText();
                    if (a.getValue(Action.ACCELERATOR_KEY) != null) {
                        String accelerator = a.getValue(Action.ACCELERATOR_KEY).toString();
//                        label.setBorder(new TitledBorder(name));
                        label.setText(
                                accelerator.replace("pressed", "").replace("alt", "Alt -").replace("ctrl", "Ctrl -").replace("shift", "Shift -") +
                                        " ("+name.replace("...", "")+")  ");
//                        NotificationDisplayer.getDefault().notify(
//                                name,
//                                new ImageIcon("/org/nb/kp/car.png"),
//                                accelerator,
//                                new ActionListener() {
//                                    @Override
//                                    public void actionPerformed(ActionEvent e) {
//                                        a.actionPerformed(e);
//                                    }
//                                });
                    }
                }
            }
        });
    }

    @Override
    public Component getStatusLineElement() {
        return label;
    }

}
