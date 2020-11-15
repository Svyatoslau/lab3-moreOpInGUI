package bsu.rfe.java.group10.lab3.Yaroshevich.varC2;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Если не задано ни одного аргумента командной строки-
        // Продолжать вычисление невозможно, коэффициенты неизвестны
        if(args.length==0){
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента" );
            System.exit(-1);
        }
        // Зарезервировать места в массиве коэффициентов столько,
        // сколько аргументов командной строки
        Double[] coefficients = new Double[args.length];
        int i =0;
        try{
            // Перебрать все аргументы, пытаясь преобразовать их в Double
            for(String arg: args){
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex){
            // Если преобразовать невозможно - сообщить об ошибке и заврешиться
            System.out.println("Ошибка преобразования строки'"+ args[i]+"' в число типа Double");
            System.exit(-2);
        }
        // Создать экземпляр главного окна, передав ему массив коэффициентов
        MainFrame frame = new MainFrame(coefficients);
        // Задать действие, выполняемые при закрытии окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // показать глваное окно
        frame.setVisible(true);

    }
}
