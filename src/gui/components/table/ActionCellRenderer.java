package gui.components.table;

import gui.theme.AppColors;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionCellRenderer implements TableCellRenderer {

    private final ActionPanel panel = new ActionPanel();

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        // Zebra + hover rengiyle uyumlu arka plan
        if (isSelected) {
            panel.applyBackground(table.getSelectionBackground());
        } else {
            boolean isHovered = (table instanceof ModernTable)
                    && ((ModernTable) table).getHoveredRow() == row;
            if (isHovered) {
                panel.applyBackground(AppColors.ROW_HOVER);
            } else {
                panel.applyBackground(row % 2 == 0
                        ? AppColors.ROW_EVEN : AppColors.ROW_ODD);
            }
        }
        return panel;
    }
}