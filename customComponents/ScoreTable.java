package customComponents;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ScoreTable extends JTable{

    private static final long serialVersionUID = 746947442185323329L;
    private final int rowHeight = 40;
    private final int colWidth = 60;
	static private final int initialScore = 0;
    static private final String[] columnNames = {"PLAYER", "SCORE"};
    
    private int numberOfPlayers;
    private boolean npcMode;

    public ScoreTable() {

        super(0, columnNames.length);

    }
    
    public ScoreTable(int numberOfPlayers, boolean npcMode) {

        super(numberOfPlayers, columnNames.length);
        this.numberOfPlayers = numberOfPlayers;
        this.npcMode = npcMode;
        addColumnHeaders();
        addTableData();
        customizeTable();

    }

    private void addColumnHeaders() {

        for (int col = 0; col < columnNames.length; col++) {
        	
            this.getColumnModel().getColumn(col).setHeaderValue(columnNames[col]);
            
        }

    }

    private void addTableData() {

    	int col;
    	
        for (int player = 0; player < numberOfPlayers; player++) {
        	
            // ADD PLAYER NUMBER
        	col = 0;
            setValueAt(Integer.toString(player + 1), player, col);
            // ADD PLAYER SCORE
            col = 1;
            setValueAt(Integer.toString(initialScore), player, col);
            
        }

        if (npcMode) {
        	
            DefaultTableModel model = (DefaultTableModel)getModel();
            model.addRow(new Object[]{"PC", Integer.toString(initialScore)});
            
        }
        
    }

    private void customizeTable() {
        
        setOpaque(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        setRowHeight(rowHeight);
        ScoreTableRenderer renderer = new ScoreTableRenderer();
        
        for (int col = 0; col < columnNames.length; col++) {
        	
            getColumnModel().getColumn(col).setHeaderRenderer(renderer);
            getColumnModel().getColumn(col).setCellRenderer(renderer);
            getColumnModel().getColumn(col).setPreferredWidth(colWidth);
            
        }

    }
    
    public JScrollPane getTableInScrolllPane() {
    	
        // MAKES IT EASIER TO ADD THE TABLE TO A JPANEL
        setPreferredScrollableViewportSize(getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;

    }

    private class ScoreTableRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 7163985961517491827L;

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
