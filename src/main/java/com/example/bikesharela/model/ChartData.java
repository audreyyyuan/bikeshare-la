package com.example.bikesharela.model;

import java.util.List;
import java.util.Map;

public class ChartData {

    private List<Column> cols; // list of columns
    private List<Row> rows; // list of rows

    public ChartData() {

    }

    public ChartData(List<Column> cols, List<Row> rows) {

        this.cols = cols;
        this.rows = rows;
    }

    public List<Column> getCols() {

        return cols;
    }
    public void setCols(List<Column> cols) {

        this.cols = cols;
    }
    public List<Row> getRows() {

        return rows;
    }
    public void setRows(List<Row> rows) {

        this.rows = rows;
    }

    public class Column {
        private String type; // type of column
        private String id; // id of column
        private String label; // label of column
        private Map<String, Object> p; // properties of column

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
    }

    public class Row {
        private List<Cell> c; // list of cells
        private Map<String, Object> p; // properties of row

        // constructor, getters, and setters

        public List<Cell> getC() {
            return c;
        }

        public void setC(List<Cell> cells) {
            this.c = cells;
        }

        public class Cell {
            private Object v; // value of cell
            private String f; // formatted value of cell
            private Map<String, Object> p; // properties of cell

            // constructor, getters, and setters
            public Object getV() {
                return v;
            }

            public void setV(Object val) {
                this.v = val;
            }
        }
    }
}
