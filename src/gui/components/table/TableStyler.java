package gui.components.table;

import gui.theme.AppColors;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TableStyler {

    private TableStyler() {}

    public static void styleTable(
            JTable table,
            JScrollPane scrollPane
    ) {

        // Scroll
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppColors.CARD_BG);

        scrollPane.getVerticalScrollBar().setUI(
                new ModernScrollBarUI()
        );

        scrollPane.getVerticalScrollBar().setPreferredSize(
                new Dimension(10, 0)
        );

        // Table
        table.setRowHeight(42);
        table.setFocusable(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Selection
        table.setSelectionBackground(
                new Color(225, 235, 255)
        );

        table.setSelectionForeground(
                AppColors.TITLE_FG
        );
    }

    public static void setColumnWidths(
            JTable table,
            int... widths
    ) {

        TableColumnModel model =
                table.getColumnModel();

        for (int i = 0; i < widths.length; i++) {

            model.getColumn(i)
                    .setPreferredWidth(widths[i]);
        }
    }
}