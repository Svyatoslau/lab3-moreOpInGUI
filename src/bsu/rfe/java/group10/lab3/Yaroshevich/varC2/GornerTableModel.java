package bsu.rfe.java.group10.lab3.Yaroshevich.varC2;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from,Double to, Double step, Double[] coefficients){
        this.from=from;
        this.to=to;
        this.step=step;
        this.coefficients=coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step*rowIndex;
        switch (columnIndex) {
            case 0:
                return x;
            case 1:{
                Double result = 0.0;
                // Вычисление значения в точке по схеме Горнера.
                // Вспомнить 1-ый курс и реализовать
                for (int i = 0; i < coefficients.length; i++) {
                    result = result * x + coefficients[i];
                }
                return result;
            }
            case 2:{
                Double result =0.0;
                for (int i = 0; i < coefficients.length; i++) {
                    result+=Math.pow(x,coefficients.length-i-1)*coefficients[i];
                }
                return result;
            }
            default: {
                return Math.abs((Double)getValueAt(rowIndex,1)-(Double)getValueAt(rowIndex,2));
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }

    @Override
    public String getColumnName(int column) {
       switch (column) {
           case 0:
               return "Значение X";
           case 1:
               return "Значение многочлена";
           case 2:
               return "С помощью Math.pow()";
           default:
               return "Разница между значениями";
       }
    }
}
