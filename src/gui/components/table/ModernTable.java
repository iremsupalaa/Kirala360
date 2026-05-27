package gui.components.table;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTable extends JTable {

    public ModernTable() {

        setRowHeight(46);

        setFont(AppFonts.TABLE_CELL);

        setGridColor(AppColors.GRID);

        setShowVerticalLines(false);

        setShowHorizontalLines(true);

        setIntercellSpacing(new Dimension(0, 0));

        setSelectionBackground(
                new Color(225, 235, 255)
        );

        setSelectionForeground(
                AppColors.TITLE_FG
        );

        setBackground(AppColors.CARD_BG);

        setFocusable(false);

        setFillsViewportHeight(true);

        setBorder(null);

        setDefaultRenderer(
                Object.class,
                new TableCellRenderer()
        );
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {

        JTableHeader header =
                new JTableHeader(columnModel);

        header.setFont(AppFonts.TABLE_HEADER);

        header.setBackground(AppColors.TH_BG);

        header.setForeground(AppColors.TH_FG);

        header.setPreferredSize(
                new Dimension(0, 44)
        );

        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        AppColors.GRID
                )
        );

        header.setDefaultRenderer((table, value,
                                   isSelected, hasFocus,
                                   row, column) -> {

            JLabel label = new JLabel(
                    value.toString()
            );

            label.setOpaque(true);

            label.setBackground(AppColors.TH_BG);

            label.setForeground(AppColors.TH_FG);

            label.setFont(AppFonts.TABLE_HEADER);

            label.setBorder(
                    new EmptyBorder(
                            0,
                            18,
                            0,
                            10
                    )
            );

            return label;
        });

        return header;
    }
}