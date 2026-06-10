package gui.components.inputs;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;

public class FormGroup extends JPanel {

    private final JLabel label;
    private final JTextField field;

    public FormGroup(
            String labelText,
            String placeholder
    ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label
        label = new JLabel(labelText);
        label.setFont(AppFonts.BODY);
        label.setForeground(AppColors.LABEL_FG);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Input
        field = new ModernTextField(placeholder);
        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(5));
        add(field);
    }

    public JTextField getField() {
        return field;
    }
}