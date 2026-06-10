package gui.components.table;

import gui.theme.AppColors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        thumbColor = new Color(190, 200, 220);
        trackColor = AppColors.CARD_BG;
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    @Override
    protected void paintThumb(Graphics g,
                              JComponent c,
                              Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(thumbColor);
        g2.fillRoundRect(
                thumbBounds.x + 4,
                thumbBounds.y,
                thumbBounds.width - 8,
                thumbBounds.height,
                10,
                10
        );
        g2.dispose();
    }
    @Override
    protected void paintTrack(Graphics g,
                              JComponent c,
                              Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(
                trackBounds.x,
                trackBounds.y,
                trackBounds.width,
                trackBounds.height
        );
    }
}