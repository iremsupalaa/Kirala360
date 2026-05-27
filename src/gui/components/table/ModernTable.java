package gui.components.table;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ModernTable extends JTable {

    // Fare hangi satırın üzerinde — renderer bu değeri okur
    private int hoveredRow = -1;

    public ModernTable() {
        setRowHeight(46);
        setFont(AppFonts.TABLE_CELL);
        setGridColor(AppColors.GRID);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(225, 235, 255));
        setSelectionForeground(AppColors.TITLE_FG);
        setBackground(AppColors.CARD_BG);
        setFocusable(false);
        setFillsViewportHeight(true);
        setBorder(null);
        setDefaultRenderer(Object.class, new TableCellRenderer());

        // ── Satır hover takibi ────────────────────────────────────────────────
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    int oldRow = hoveredRow;
                    hoveredRow = row;
                    // Sadece değişen satırları yenile (tablo titremez)
                    if (oldRow >= 0 && oldRow < getRowCount())
                        repaintRow(oldRow);
                    if (hoveredRow >= 0 && hoveredRow < getRowCount())
                        repaintRow(hoveredRow);
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                int old = hoveredRow;
                hoveredRow = -1;
                if (old >= 0 && old < getRowCount()) repaintRow(old);
            }
        });
    }

    /** Sadece tek satırı repaint eder — tüm tablo değil */
    private void repaintRow(int row) {
        Rectangle rect = getCellRect(row, 0, true);
        rect.width = getWidth();
        repaint(rect);
    }

    /** Renderer'ın hover rengi için erişeceği getter */
    public int getHoveredRow() { return hoveredRow; }

    // ── Tablo başlığı ─────────────────────────────────────────────────────────
    @Override
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader header = new JTableHeader(columnModel);
        header.setFont(AppFonts.TABLE_HEADER);
        header.setBackground(AppColors.TH_BG);
        header.setForeground(AppColors.TH_FG);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColors.GRID));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setOpaque(true);
            label.setBackground(AppColors.TH_BG);
            label.setForeground(AppColors.TH_FG);
            label.setFont(AppFonts.TABLE_HEADER);
            label.setBorder(new EmptyBorder(0, 18, 0, 10));

            // Sıralama yön oku
            javax.swing.table.TableRowSorter<?> sorter = null;
            if (table.getRowSorter() instanceof javax.swing.table.TableRowSorter) {
                sorter = (javax.swing.table.TableRowSorter<?>) table.getRowSorter();
            }
            if (sorter != null) {
                java.util.List<? extends RowSorter.SortKey> keys = sorter.getSortKeys();
                if (!keys.isEmpty() && keys.get(0).getColumn() == column) {
                    String arrow = keys.get(0).getSortOrder()
                            == SortOrder.ASCENDING ? "  ↑" : "  ↓";
                    label.setText(value + arrow);
                }
            }
            return label;
        });
        return header;
    }
}
