package gui.components.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final ActionPanel panel;

    public ActionCellEditor() {
        panel = new ActionPanel();

        // Buton tıklanınca editing'i bitir (tablo kilit kalmasın)
        panel.setOnView  (row -> fireEditingStopped());
        panel.setOnEdit  (row -> fireEditingStopped());
        panel.setOnDelete(row -> fireEditingStopped());
    }

    // ── Callback'leri dışarıdan bağlamak için ─────────────────────────────────
    public void setOnView  (ActionPanel.RowAction a) {
        panel.setOnView(row -> { a.execute(row); fireEditingStopped(); });
    }
    public void setOnEdit  (ActionPanel.RowAction a) {
        panel.setOnEdit(row -> { a.execute(row); fireEditingStopped(); });
    }
    public void setOnDelete(ActionPanel.RowAction a) {
        panel.setOnDelete(row -> { a.execute(row); fireEditingStopped(); });
    }

    @Override
    public Object getCellEditorValue() { return ""; }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {

        panel.setCurrentRow(row);
        panel.applyBackground(table.getSelectionBackground());
        return panel;
    }
}