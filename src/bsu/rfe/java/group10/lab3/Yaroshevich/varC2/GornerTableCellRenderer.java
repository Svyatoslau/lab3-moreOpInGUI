package bsu.rfe.java.group10.lab3.Yaroshevich.varC2;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRenderer implements TableCellRenderer {

    // Ищем ячейки, строковое представление которых равно needle(иголке)
    private String needle = null;
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    private Boolean simlpeNumbers= false;

    public GornerTableCellRenderer() {
        // Разместить надпись внутри панели
        panel.add(label);
        // Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
        // Не использовать группировку (не отделять тысячи)
        // Т.е. показывать число как "1000", а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
        // Установить в качестве разделителя дробной части точку, а не запятую
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

    }

    public void setNeedle(String needle){
        this.needle=needle;
    }

    public void findSimple(boolean flag){
        simlpeNumbers=flag;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Преобразовать число в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
        // Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if(Double.parseDouble(formattedDouble)<0){
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        }else if((Double.parseDouble(formattedDouble)>0)){
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }else if(Double.parseDouble(formattedDouble)==0){
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        }
        if (column==1 && needle!=null && needle.equals(formattedDouble)){
            // Номер столбца = 1 (т.е. второй столбец)
            // + иголка не null (т.е. мы что-то ищем)
            // + значение иголки совпадает со значением ячейки таблицы -
            // окрасть задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        } else {
            // Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        if(simlpeNumbers){
            final double error = 0.1;
            double numberFromTable = Double.parseDouble(formattedDouble);
            Integer bIntegerPart=(int)numberFromTable;
            Integer eIntegerPart=(int)numberFromTable+1;
            // Если наще число отклоняеться от целой части больше чем на 0.1
            // То это число точно не будет входить в погрешность 0.1 для целых чисел
            if(bIntegerPart>=numberFromTable-error&&bIntegerPart>=0.9){
                // Проверим число на простоту, выполнив тест на простоту
                // В Java уже реализован тест Рабина-Миллера в классe BigInteger
                BigInteger bigInteger= BigInteger.valueOf(bIntegerPart);
                boolean simpleNumber=   bigInteger.isProbablePrime((int)Math.log(bIntegerPart));
                // Если число простое окрасим его поле в оранжевый
                if(simpleNumber) panel.setBackground(Color.ORANGE);
                else panel.setBackground(Color.WHITE);
            }else if(eIntegerPart<=numberFromTable+error&&eIntegerPart>=0.9){
                BigInteger bigInteger= BigInteger.valueOf(eIntegerPart);
                boolean simpleNumber=bigInteger.isProbablePrime((int)Math.log(eIntegerPart));
                if(simpleNumber) panel.setBackground(Color.ORANGE);
                else panel.setBackground(Color.WHITE);
            }else panel.setBackground(Color.WHITE);
        }
        return panel;
    }
}
