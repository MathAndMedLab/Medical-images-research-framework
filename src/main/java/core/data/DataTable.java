package core.data;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;

//TODO: (avlomakin) find java DataTable class
public class DataTable {

    //temporary public
    public HashSet<String> columns;
    public ArrayList<Dictionary<String, String>> rows;

    public DataTable() {
        columns = new HashSet<>();
        rows = new ArrayList<>();
    }

    public void addColumn(String name)
    {
        columns.add(name);
    }

    public void addRow(Dictionary<String, String> row)
    {
        //TODO: (avlomakin) add row validation
        rows.add(row);
    }
}
