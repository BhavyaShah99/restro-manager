package model;
import java.util.ArrayList;
import java.util.Arrays;

public class TableMap {
    public String tableMap;
    public Table table0 = new Table(0);
    public Table table1 = new Table(1);
    public Table table2 = new Table(2);
    public Table table3 = new Table(3);
    public Table table4 = new Table(4);
    public Table table5 = new Table(5);
    public Table table6 = new Table(6);
    public Table table7 = new Table(7);
    public Table table8 = new Table(8);

    public ArrayList<Table> tables = new ArrayList<Table>(Arrays.asList(table0, table1, table2, table3, table4, table5, table6, table7,table8));

    public TableMap() {
        this.tables = tables;
        this.table0 = table0;
        this.table1 = table1;
        this.table2 = table2;
        this.table3 = table3;
        this.table4 = table4;
        this.table5 = table5;
        this.table6 = table6;
        this.table7 = table7;
        this.table8 = table8;


    }

    public String getTableMap() {
        //Setting tables at here
        String map = "";
        for (int i = 0; i < tables.size(); ) {
            map += "[ " + tables.get(i).getTableNumber() + " Ordered: " + tables.get(i).isOrdered() + " ]" + "\t";
            map += "[ " + tables.get(i + 1).getTableNumber() + " Ordered: " + tables.get(i + 1).isOrdered() + " ]" + "\n\n";

            i += 2;
        }
        return map;
    }

}

