package model;

import java.util.ArrayList;
import java.util.Arrays;

public class TableManager {
    public ArrayList<Table> tables;
    TableMap tableMap;


    public TableManager() {
        tableMap = new TableMap();
        tables = tableMap.tables;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public Table getTable(int num)
    {
        Table target = null;
        for (Table table : tables) {
            if (table.getTableNumber() == num) {
                target = table;
            }
        }
        return target;
    }

    public boolean isInTables (int tableNum)
    {
        boolean ret = false;
        for (Table t : tables)
        {
            if (t.getTableNumber() == tableNum)
            {
                ret = true;
            }
        }
        return ret;
    }

    //Show Restaurant's table map
    public String showTableMap() {
        return this.tableMap.getTableMap();
    }

    public TableMap getTableMap() {
        return tableMap;
    }
}
