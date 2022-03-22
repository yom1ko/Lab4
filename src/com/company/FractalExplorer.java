package com.company;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

public class FractalExplorer {

    private int displaySize;

    private JImageDisplay display;

    private FractalGenerator fractal;

    private Rectangle2D.Double range;

    public FractalExplorer(int size) {

        displaySize = size;


        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    public void createAndShowGUI()
    {

        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        frame.add(display, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Display");

        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);

        frame.add(resetButton, BorderLayout.SOUTH);

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Данные операции правильно разметят содержимое окна
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal()
    {
        //  Должен проходить через каждый пиксель в изображении
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){
                //  Определение координат с плавающей точкой
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                int iteration = fractal.numIterations(xCoord, yCoord);
                //  Установление пикселя в чёрный цвет
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }
                //  Установка цвета в зависимости от кол-ва итераций
                else {

                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        // Нужно для обновления изображения
        display.repaint();
    }
    //  При нажатии кнопки сброса
    private class ResetHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }
    //  Для приблежения изображения
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();

            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            int y = e.getY();

            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    public static void main(String[] args)
    {
        // size = размер изображения в пикселях
        FractalExplorer displayExplorer = new FractalExplorer(1200);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}