package gui.components.table;

import gui.icons.EditIcon;
import gui.icons.EyeIcon;
import gui.icons.TrashIcon;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel {

    public ActionPanel() {

        setOpaque(false);
        setPreferredSize(new Dimension(90, 36));
        setOpaque(true);
        setBackground(Color.WHITE);

        setLayout(new FlowLayout(
                FlowLayout.CENTER,
                6,
                0
        ));

        JButton viewBtn = createButton(
                new EyeIcon(14, new Color(59, 130, 246))
        );

        JButton editBtn = createButton(
                new EditIcon(14, new Color(34, 197, 94))
        );

        JButton deleteBtn = createButton(
                new TrashIcon(14, new Color(239, 68, 68))
        );

        add(viewBtn);
        add(editBtn);
        add(deleteBtn);
    }

    private JButton createButton(Icon icon) {

        JButton button = new JButton(icon);

        button.setPreferredSize(
                new Dimension(28, 28)
        );

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }
}