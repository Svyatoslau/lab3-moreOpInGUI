package bsu.rfe.java.group10.lab3.Yaroshevich.varC2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame extends JFrame {

    // Константы с исходным размером окна приложения
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    // Массив коэффициентов многочлена
    private Double[] coefficients;
    // Объект диалогового окна для выбора файлов
    // Команент не создаеться изначально, т.к. может и не понадобиться
    // пользователю если тот не собирается сохранять
    private JFileChooser fileChooser = null;
    // Элементы меню
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem aboutProgramMenuItem;
    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    //Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    // Модель данных с результатами вычислений
    private GornerTableModel data;

    public MainFrame(Double[] coefficients){
        super("Табулирование многочлена на отрезке по схеме Горнера");
        // Запомнить во внутреннем поле переданные коэффициенты
        this.coefficients = coefficients;
        // Установить размеры окна
        setSize(WIDTH,HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width-WIDTH)/2,(kit.getScreenSize().height-HEIGHT)/2);

        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        // Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
        // Добавить в меню пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        // Добавить его в главное меню
        menuBar.add(fileMenu);
        // Добавить пункт "Таблица"
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        // Добавить пункт "Cправка"
        JMenu helpMenu =new JMenu("Справка");
        menuBar.add(helpMenu);

        //Создать новое "действие" по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null){
                    // Если диалоговое окно "Открыть файл" ещё не создано, то создать его
                    fileChooser = new JFileChooser();
                    // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);{
                    // Если результат его показа успешный, сохранить данные в текстовый файл
                    saveToTextFile(fileChooser.getSelectedFile());

                }
            }
        };
        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        // По умолчанию пункт в меню является недоступным (данных ещё нет)
        saveToTextMenuItem.setEnabled(false);

        // Cоздать новое действие по выводу дилогового окна с информацией автора
        Action aboutProgramAction = new AbstractAction("О программе") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon=new ImageIcon(kit.getImage("C:\\study\\Java\\lab3 - more Opptions in GUI\\my_photo.jpg"));
                JOptionPane.showMessageDialog(MainFrame.this,
                        new String[]{"- Ярошевич Святослав","- 10 группа"},
                        "О программе",JOptionPane.INFORMATION_MESSAGE,icon);
            }
        };
        // Добавление в меню
        aboutProgramMenuItem=helpMenu.add(aboutProgramAction);
        aboutProgramMenuItem.setEnabled(true);
        // Создать новое "действие" по сохранию в текстовый файл
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(fileChooser==null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалогавое окно
                if(fileChooser.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
                    saveToGraphicFile(fileChooser.getSelectedFile());
                }
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        // По умолчанию пункт меню являеться недоступным(данных ещё нет)
        saveToGraphicsMenuItem.setEnabled(false);

        // Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(MainFrame.this,
                        "Введите значение для поиска","Поиск значения",
                        JOptionPane.QUESTION_MESSAGE);
                // Установить введенное значение в качестве иголки в визуализаторе
                renderer.setNeedle(value);
                // Обновить таблицу
                getContentPane().repaint();
            }
        };
        // Добавить действия в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        // По умолчанию пункт меню являеться недоступным (данных ещё нет)
        searchValueMenuItem.setEnabled(false);

        // Создание графического интерфейса (дописать)
        // Создание полей
        JLabel labelForFrom = new JLabel("X изменяеться на интервале от:");
        textFieldFrom = new JTextField("0.0",10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0",10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1",10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        // Укладка коробок
        Box hBoxRange = Box.createHorizontalBox();
        // Задать для контейнера тип рамки "объёмная"
        hBoxRange.setBorder(BorderFactory.createBevelBorder(1));
        hBoxRange.add(Box.createHorizontalGlue());
        hBoxRange.add(labelForFrom);
        hBoxRange.add(Box.createHorizontalStrut(10));
        hBoxRange.add(textFieldFrom);
        hBoxRange.add(Box.createHorizontalStrut(20));
        hBoxRange.add(labelForTo);
        hBoxRange.add(Box.createHorizontalStrut(10));
        hBoxRange.add(textFieldTo);
        hBoxRange.add(Box.createHorizontalStrut(20));
        hBoxRange.add(labelForStep);
        hBoxRange.add(Box.createHorizontalStrut(10));
        hBoxRange.add(textFieldStep);
        hBoxRange.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равныым удвоенному
        // минимальному, чтобы при компоновке область совсем не сдавили
        hBoxRange.setPreferredSize(new Dimension(
                new Double(hBoxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hBoxRange.getMinimumSize().getHeight()).intValue()*2));
        // Установить область в верхнюю(северную) часть компоновки
        getContentPane().add(hBoxRange, BorderLayout.NORTH);

        // Создать кнопку "Вычислить"
        JButton  buttonCalc = new JButton("Вычислить");
        // Задать действие на нажатие "Вычислить" и привязать к кнопке
        buttonCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try{
                    // Считать значения границ отрезка, шага из полей ввода
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step =Double.parseDouble(textFieldStep.getText());
                    // На основе считанных данных создать модель таблицы
                    data = new GornerTableModel(from,to,step,coefficients);
                    // Создать новый экземпляр таблицы
                    JTable table = new JTable(data);
                    // Установить в качестве визуализатора ячеек для класса
                    // Double разработанный визуализатор
                    table.setDefaultRenderer(Double.class,renderer);
                    // Установить размер строки таблицы в 30 пикселов
                    table.setRowHeight(30);
                    // Удалить все вложенные элементы из контейнера hBoxResult
                    hBoxResult.removeAll();
                    // Добавить в hBoxResult таблицу,
                    // "обёрнутую" в панель с полосами прокрутки
                    hBoxResult.add(new JScrollPane(table));
                    // Обновить область содержания главного окна
                    getContentPane().validate();
                    // Пометить ряд элементов меню как доступных
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                }catch (NumberFormatException ex){
                    // В случае ошибки преобразования показать сообщения об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // установить в полях ввода значения по умолчанию
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                // Удалить все вложенные элементы контейнера hBoxResult
                hBoxResult.removeAll();
                // Добавить в контейнер пустую панель
                hBoxResult.add(new JPanel());
                // Пометить элементы меню как не доступные
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                // Обновить область содержания главного окна
                getContentPane().validate();
            }
        });
        // Поместить соданные кнопки в контейнер
        Box hBoxButoons = Box.createHorizontalBox();
        hBoxButoons.setBorder(BorderFactory.createBevelBorder(1));
        hBoxButoons.add(Box.createHorizontalGlue());
        hBoxButoons.add(buttonCalc);
        hBoxButoons.add(Box.createHorizontalStrut(30));
        hBoxButoons.add(buttonReset);
        hBoxButoons.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равным удвоенному
        // минимальному, чтобы при компоновке окна область совсем не сдавили
        hBoxButoons.setPreferredSize(new Dimension(
                new Double(hBoxButoons.getMaximumSize().getWidth()).intValue(),
                new Double(hBoxButoons.getMinimumSize().getHeight()).intValue()*2));
        // Разместить контейнер с кнопками в нижней(южной) области компоновки
        getContentPane().add(hBoxButoons,BorderLayout.SOUTH);
        // Область для вывода результатов, пока что пустая
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        // Установить конейнер hBoxResult в главный области компоновки
        getContentPane().add(hBoxResult,BorderLayout.CENTER);
    }
    protected void saveToTextFile(File selectedFile){
        try{
            // Создать новый сивольный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);
            // Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for(int i=0;i<coefficients.length;i++){
                out.print(coefficients[i] + "X^"+(coefficients.length-i-1));
                if(i!=coefficients.length-1)
                    out.print("+");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до "+
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            // Записать в поток вывода значения вточках
            for(int i=0;i<data.getRowCount();i++){
                out.println("Значение в точках " + data.getValueAt(i,0)+
                        " равно " + data.getValueAt(i,1));
            }
            // Закрыть поток
            out.close();
        }catch (FileNotFoundException e){
            // Исключительную ситуацию "Файл не найден" в данном случае можно
            // не обрабатывать, так как мы файл создаём, а не открываем для чтения
        }
    }
    protected void  saveToGraphicFile(File selectedFile){
        try{
            // Создать байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for(int i =0;i<data.getRowCount();i++){
                // Записть в поток вывода значения X в точке
                out.writeDouble((Double)data.getValueAt(i,0));
                // Записать значения многочлена в точке
                out.writeDouble((Double)data.getValueAt(i,1));
                out.close();
            }
        }catch (Exception e){

        }
    }
}
