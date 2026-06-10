package gui.components.table;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setFont(AppFonts.TABLE_CELL);
        label.setBorder(new EmptyBorder(0, 18, 0, 10));
        label.setOpaque(true);


        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        } else {
            boolean isHovered = (table instanceof ModernTable)
                    && ((ModernTable) table).getHoveredRow() == row;

            if (isHovered) {
                label.setBackground(AppColors.ROW_HOVER);
            } else {
                label.setBackground(row % 2 == 0
                        ? AppColors.ROW_EVEN : AppColors.ROW_ODD);
            }
            label.setForeground(AppColors.TITLE_FG);
        }

        // ── Müsaitlik sütununa özel renklendirme
        if (value instanceof String) {
            String text = (String) value;
            if ("Müsait".equals(text)) {
                label.setForeground(AppColors.MUSAIT_FG);
                label.setFont(AppFonts.TABLE_CELL.deriveFont(Font.BOLD));
            } else if ("Kirada".equals(text)) {
                label.setForeground(AppColors.KIRADA_FG);
                label.setFont(AppFonts.TABLE_CELL.deriveFont(Font.BOLD));
            }
        }

        return label;
    }
}
