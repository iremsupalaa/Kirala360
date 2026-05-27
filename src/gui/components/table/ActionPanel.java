package gui.components.table;

import gui.icons.EditIcon;
import gui.icons.EyeIcon;
import gui.icons.TrashIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Tablo "İşlemler" sütununda gösterilen görünüm/düzenle/sil buton grubu.
 *
 * Callback'ler ActionCellEditor üzerinden MainFrame'e iletilir:
 *   panel.setOnView  (row -> ...)
 *   panel.setOnEdit  (row -> ...)
 *   panel.setOnDelete(row -> ...)
 */
public class ActionPanel extends JPanel {

    public interface RowAction {
        void execute(int row);
    }

    private RowAction onView;
    private RowAction onEdit;
    private RowAction onDelete;

    private int currentRow = -1;

    private final JButton viewBtn;
    private final JButton editBtn;
    private final JButton deleteBtn;

    public ActionPanel() {
        setOpaque(true);
        setBackground(Color.WHITE);

        // GridBagLayout — tek satır, dikey ortalı
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy  = 0;

        viewBtn   = createButton(new EyeIcon  (14, new Color(59, 130, 246)));
        editBtn   = createButton(new EditIcon (14, new Color(34, 197, 94)));
        deleteBtn = createButton(new TrashIcon(14, new Color(239, 68, 68)));

        gbc.gridx = 0; add(viewBtn,   gbc);
        gbc.gridx = 1; add(editBtn,   gbc);
        gbc.gridx = 2; add(deleteBtn, gbc);

        viewBtn  .addActionListener(e -> { if (onView   != null && currentRow >= 0) onView  .execute(currentRow); });
        editBtn  .addActionListener(e -> { if (onEdit   != null && currentRow >= 0) onEdit  .execute(currentRow); });
        deleteBtn.addActionListener(e -> { if (onDelete != null && currentRow >= 0) onDelete.execute(currentRow); });
    }

    // ── Callback setters ──────────────────────────────────────────────────────
    public void setOnView  (RowAction a) { onView   = a; }
    public void setOnEdit  (RowAction a) { onEdit   = a; }
    public void setOnDelete(RowAction a) { onDelete = a; }

    /** Editor tarafından her render'da güncellenir */
    public void setCurrentRow(int row) { currentRow = row; }

    /** Seçili satır arka plan rengine uyum */
    public void applyBackground(Color bg) { setBackground(bg); }

    // ── Buton fabrikası ───────────────────────────────────────────────────────
    private JButton createButton(Icon icon) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(28, 28));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover: hafif arka plan
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(0, 0, 0, 18));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });

        return btn;
    }
}