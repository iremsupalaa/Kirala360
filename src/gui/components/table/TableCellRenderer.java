package gui.components.table;

import gui.components.badges.StatusBadge;
import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {

        Component c = super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                hasFocus,
                row,
                column
        );

        setBorder(BorderFactory.createEmptyBorder(
                0,
                16,
                0,
                16
        ));

        setFont(AppFonts.BODY);

        setHorizontalAlignment(
                column == 0
                        ? CENTER
                        : LEFT
        );

        if (!isSelected) {

            setBackground(
                    row % 2 == 0
                            ? AppColors.ROW_EVEN
                            : AppColors.ROW_ODD
            );

            setForeground(AppColors.TITLE_FG);
        }

        // STATUS BADGE
        if (
                column == 4
                        && value != null
                        && table.getColumnCount() == 5
        ) {

            JPanel wrap = new JPanel(
                    new GridBagLayout()
            );

            wrap.setOpaque(true);

            wrap.setBackground(
                    row % 2 == 0
                            ? AppColors.ROW_EVEN
                            : AppColors.ROW_ODD
            );

            boolean musait =
                    value.toString().equals("Müsait");

            wrap.add(
                    musait
                            ? StatusBadge.musait()
                            : StatusBadge.kirada()
            );

            return wrap;
        }

        return c;
    }
}