/* BezierTest.java
 * =========================================================================
 * This file is part of PregraphViewer - http://caagt.ugent.be/pregraphs
 * 
 * Copyright (C) 2010-2011 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.nvcleemp.pregraph.viewer;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.CubicCurve2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BezierTest extends JPanel {
    // Set control points

    Point2D.Double P1 = new Point2D.Double(150, 75); // Start Point
    int width = 20;
    int length = 50;
    double p2x = 150;
    double p2y = 125;

    // Construct frame
    public BezierTest() {
        setPreferredSize(new Dimension(600, 600));
        setVisible(true);
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                /*
                int mult = 1;
                if(e.isShiftDown()){
                    mult = -1;
                }
                if(e.getButton()==MouseEvent.BUTTON1){
                    width += 10*mult;
                } else {
                    length += 10*mult;
                }
                 *
                 */
                p2x = e.getX();
                p2y = e.getY();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Set points
        Point2D.Double ctrl1 = new Point2D.Double(100, 25); // Control Point 1
        Point2D.Double ctrl2 = new Point2D.Double(200, 25); // Control Point 2

        CubicCurve2D.Double cubicCurve; // Cubic curve

        cubicCurve = new CubicCurve2D.Double(P1.x, P1.y, p2x - width, p2y, p2x + width, p2y, P1.x, P1.y);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3f));
        g2.draw(cubicCurve);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bezier test");
        frame.add(new BezierTest());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } // End of main method
}

