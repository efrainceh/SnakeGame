package customComponents;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ScoreTable extends JTable{

    static final int initialScore = 0;
    static final String[] columnNames = {"PLAYER", "SCORE"};
    
    int numberOfPlayers;
    boolean npc;

    public ScoreTable() {

        super(0, columnNames.length);

    }
    
    public ScoreTable(int numberOfPlayers, boolean npc) {

        super(numberOfPlayers, columnNames.length);
        this.numberOfPlayers = numberOfPlayers;
        this.npc = npc;
        addColumnHeaders();
        addTableData();
        customizeTable();

    }

    public JScrollPane getTableInScrolllPane() {
        // MAKES IT EASIER TO ADD THE TABLE TO A JPANEL
        setPreferredScrollableViewportSize(getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;

    }

    private void addColumnHeaders() {

        for (int col = 0; col < columnNames.length; col++) {
            this.getColumnModel().getColumn(col).setHeaderValue(columnNames[col]);
        }

    }

    private void addTableData() {

        for (int player = 0; player < numberOfPlayers; player++) {
            // ADD PLAYER NUMBER
            setValueAt(Integer.toString(player + 1), player, 0);
            // ADD PLAYER SCORE
            setValueAt(Integer.toString(initialScore), player, 1);
        }

        if (npc) {
            DefaultTableModel model = (DefaultTableModel)getModel();
            model.addRow(new Object[]{"PC", Integer.toString(initialScore)});
        }
    }

    private void customizeTable() {
        
        setOpaque(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        setRowHeight(40);
        ScoreTableRenderer renderer = new ScoreTableRenderer();
        for (int col = 0; col < columnNames.length; col++) {
            getColumnModel().getColumn(col).setHeaderRenderer(renderer);
            getColumnModel().getColumn(col).setCellRenderer(renderer);
            getColumnModel().getColumn(col).setPreferredWidth(60);
        }

    }

    private class ScoreTableRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int rowIndex, int vColIndex) {

            setFont(new Font("PixelMplus12", Font.BOLD, 18));
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setText(value.toString());
            return this;

        }

    }
    
}
