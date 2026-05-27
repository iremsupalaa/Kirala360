package gui.components.card;

import gui.theme.AppColors;
import gui.theme.AppFonts;
import gui.theme.Shadows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatsCard extends JPanel {

    private final Color bgColor;
    private final Color accentColor;

    public StatsCard(
            String title,
            String value,
            Color bgColor,
            Color accentColor
    ) {

        this.bgColor = bgColor;
        this.accentColor = accentColor;

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppFonts.SUBTITLE);
        valueLabel.setForeground(accentColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppFonts.BODY);
        titleLabel.setForeground(
                AppColors.LABEL_FG
        );

        add(valueLabel);
        add(Box.createVerticalStrut(6));
        add(titleLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        Shadows.drawShadow(
                g2,
                getWidth(),
                getHeight(),
                18
        );
        g2.fill(new RoundRectangle2D.Double(
                4,
                4,
                getWidth() - 4,
                getHeight() - 4,
                18,
                18
        ));

        g2.setColor(bgColor);

        g2.fill(new RoundRectangle2D.Double(
                0,
                0,
                getWidth() - 6,
                getHeight() - 6,
                18,
                18
        ));

        g2.dispose();

        super.paintComponent(g);
    }
}