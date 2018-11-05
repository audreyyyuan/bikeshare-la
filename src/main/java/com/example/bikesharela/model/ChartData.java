package com.example.bikesharela.model;

import java.util.List;
import java.util.Map;

public class ChartData {

    private List<Column> cols;
    private List<Row> rows;

    public ChartData() { }

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
        private String type;
        private String id;
        private String label;
        private Map<String, Object> p;

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
        private List<Cell> c;
        private Map<String, Object> p;

        // constructor, getters, and setters
        public List<Cell> getC() {
            return c;
        }

        public void setC(List<Cell> cells) {
            this.c = cells;
        }

        public Map<String, Object> getP() {
            return p;
        }

        public void setP(Map<String, Object> p) {
            this.p = p;
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

            public void setF(String f) {
                this.f = f;
            }

            public String getF() {
                return f;
            }

            public void setP(Map<String, Object> p) {
                this.p = p;
            }

            public Map<String, Object> getP() {
                return p;
            }
        }
    }
}
