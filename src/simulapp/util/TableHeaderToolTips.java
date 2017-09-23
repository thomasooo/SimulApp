package simulapp.util;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomas Ballon on 15.4.2017. by http://www.java2s.com/Tutorial/Java/0240__Swing/SettingColumnHeaderToolTipsinaJTableComponents.htm
 */

public class TableHeaderToolTips extends MouseMotionAdapter {
    TableColumn curCol;
    Map tips = new HashMap();
    public void setToolTip(TableColumn col, String tooltip) {
        if (tooltip == null) {
            tips.remove(col);
        } else {
            tips.put(col, tooltip);
        }
    }
    public void mouseMoved(MouseEvent evt) {
        JTableHeader header = (JTableHeader) evt.getSource();
        JTable table = header.getTable();
        TableColumnModel colModel = table.getColumnModel();
        int vColIndex = colModel.getColumnIndexAtX(evt.getX());
        TableColumn col = null;
        if (vColIndex >= 0) {
            col = colModel.getColumn(vColIndex);
        }
        if (col != curCol) {
            header.setToolTipText((String) tips.get(col));
            curCol = col;
        }
    }
}

