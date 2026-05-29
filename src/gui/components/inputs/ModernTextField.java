package gui.components.inputs;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {

    private boolean focused = false;

    private final String placeholder;

    public ModernTextField(String placeholder) {

        this.placeholder = placeholder;
        setFont(AppFonts.BODY);
        setOpaque(false);
        setBorder(new EmptyBorder(
                10,
                14,
                10,
                14
        ));
        setForeground(AppColors.TITLE_FG);

        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                focused = true;
                repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                focused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Background
        g2.setColor(Color.WHITE);

        g2.fill(new RoundRectangle2D.Float(
                0,
                0,
                getWidth(),
                getHeight(),
                16,
                16
        ));

        // Border
        g2.setColor(
                focused
                        ? AppColors.ACCENT
                        : AppColors.INPUT_BDR
        );

        g2.setStroke(new BasicStroke(
                focused ? 2f : 1f
        ));

        g2.draw(new RoundRectangle2D.Float(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                16,
                16
        ));

        g2.dispose();

        super.paintComponent(g);

        // Placeholder
        if (
                getText().isEmpty()
                        && !focused
                        && placeholder != null
        ) {

            Graphics2D g3 = (Graphics2D) g.create();

            g3.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g3.setColor(new Color(150, 160, 180));

            g3.setFont(AppFonts.BODY);

            Insets ins = getInsets();

            FontMetrics fm = g3.getFontMetrics();

            g3.drawString(
                    placeholder,
                    ins.left,
                    (getHeight() + fm.getAscent()) / 2 - 2
            );

            g3.dispose();
        }
    }
}