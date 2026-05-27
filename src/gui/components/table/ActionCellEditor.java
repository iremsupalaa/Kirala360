package gui.components.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ActionCellEditor
        extends AbstractCellEditor
        implements TableCellEditor {

    private final ActionPanel panel;

    public ActionCellEditor() {

        panel = new ActionPanel();
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column
    ) {

        return panel;
    }
}