package com.mirf.core.data

import java.util.ArrayList
import java.util.Dictionary
import java.util.HashSet

//TODO: (avlomakin) find java DataTable class
class DataTable(var columns: HashSet<String> = HashSet(), var rows: ArrayList<HashMap<String, String>> = ArrayList()) {

    fun addColumn(name: String) {
        columns.add(name)
    }

    fun addRow(row: HashMap<String, String>) {
        //TODO: (avlomakin) add row validation
        rows.add(row)
    }
}
