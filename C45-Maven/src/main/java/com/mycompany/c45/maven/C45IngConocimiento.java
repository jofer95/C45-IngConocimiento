/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.c45.maven;

import static com.mycompany.c45.maven.NegocioC45.raiz;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import modelos.Nodo;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author jorgebarraza
 */
public class C45IngConocimiento extends JFrame
        implements GInteraction {

    private GScene scene_;
    private NegocioC45 negocioC45;
    private int nivel = 0;

    public C45IngConocimiento() {
        super("G Graphics Library - Demo 8");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the GUI
        JPanel topLevel = new JPanel();
        topLevel.setLayout(new BorderLayout());
        getContentPane().add(topLevel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JLabel("Proyecto C4.5 Ingenieria del conocimiento"));

        JPanel graphicsPanel = new JPanel();
        topLevel.add(buttonPanel, BorderLayout.NORTH);

        // Create the graphic canvas
        GWindow window = new GWindow();
        topLevel.add(window.getCanvas(), BorderLayout.CENTER);

        // Create scene with default viewport and world extent settings
        scene_ = new GScene(window, "Scene");

        double w0[] = {0.0, 1200.0, 0.0};
        double w1[] = {1200.0, 1200.0, 0.0};
        double w2[] = {0.0, 0.0, 0.0};
        scene_.setWorldExtent(w0, w1, w2);

        GStyle style = new GStyle();
        style.setForegroundColor(new Color(0, 0, 0));
        style.setBackgroundColor(new Color(255, 255, 255));
        style.setFont(new Font("Dialog", Font.BOLD, 14));
        scene_.setStyle(style);

        // Create som graphic objects
        if (NegocioC45.raiz != null) {
            imprimirNodos(NegocioC45.raiz);
        }
        pack();
        setSize(new Dimension(500, 500));
        setVisible(true);

        window.startInteraction(this);
    }

    private GObject actualGObject;
    private GObject objectoAnterior;
    private double x = 500;
    private double y = 100;

    public void imprimirNodos(Nodo nodoActual) {
        if (actualGObject == null) {
            actualGObject = scene_;
            GObject object1 = new TestObject(nodoActual.getNombre(), actualGObject, x, y);
            actualGObject = object1;
            objectoAnterior = object1;
            x = 1000;
        }
        if (nodoActual.getOpcionesNodos().size() > 0) {
            nivel++;
            x -= 60;
            y += 170;
            objectoAnterior = actualGObject;
            for (Nodo obj : nodoActual.getOpcionesNodos()) {
                obj.setPadre(objectoAnterior);
            }
            for (Nodo obj : nodoActual.getOpcionesNodos()) {
                GObject object1 = new TestObject(obj.getNombre(), actualGObject, x, y);
                actualGObject = object1;
                imprimirNodos(obj);
                actualGObject = obj.getPadre();
                x -= 150;
            }
            y -= 170;
        }
    }

    public void event(GScene scene, int event, int x, int y) {
        if (event == GWindow.BUTTON1_UP
                || event == GWindow.BUTTON2_UP) {
            boolean isSelected = event == GWindow.BUTTON1_UP;

            GSegment selectedSegment = scene_.findSegment(x, y);
            if (selectedSegment == null) {
                return;
            }

            GStyle style = selectedSegment.getOwner().getStyle();
            if (style == null) {
                return;
            }

            if (isSelected) {
                style.setBackgroundColor(new Color((float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random()));
            } else {
                style.unsetBackgroundColor();
            }

            scene_.refresh();
        }
    }

    private class TestObject extends GObject {

        private TestObject parent_;
        private double x_, y_;
        private GSegment circle_;
        private GSegment line_;

        TestObject(String name, GObject parent, double x, double y) {
            parent_ = parent instanceof TestObject ? (TestObject) parent : null;

            x_ = x;
            y_ = y;

            line_ = new GSegment();
            addSegment(line_);

            circle_ = new GSegment();
            addSegment(circle_);

            circle_.setText(new GText(name, GPosition.MIDDLE));

            setStyle(new GStyle());

            parent.add(this);
        }

        double getX() {
            return x_;
        }

        double getY() {
            return y_;
        }

        public void draw() {
            if (parent_ != null) {
                line_.setGeometry(parent_.getX(), parent_.getY(), x_, y_);
            }

            circle_.setGeometryXy(Geometry.createCircle(x_, y_, 40.0));
        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        new NegocioC45();
        new C45IngConocimiento();
    }
}
