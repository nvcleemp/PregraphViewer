/* BezierTest2.java
 * =========================================================================
 * This file is part of PregraphViewer - http://caagt.ugent.be/pregraphs
 * 
 * Copyright (C) 2010-2011 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BezierTest2 extends JPanel {
    // Set control points

    Point2D.Double P1 = new Point2D.Double(100, 300); // Start Point
    Point2D.Double P2 = new Point2D.Double(500, 300); // Start Point
    double y = 300;
    int edges = 2;

    // Construct frame
    public BezierTest2() {
        setPreferredSize(new Dimension(600, 600));
        setVisible(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.isShiftDown()){
                    edges -=2;
                } else {
                    edges +=2;
                }
                repaint();
            }
        });
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
                y = e.getY();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //quadCurve = new QuadCurve2D.Double(P1.x, P1.y, 200, 2*y - P1.y, P2.x, P2.y);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3f));

        for(int i = 1; i <= edges/2; i++){
            QuadCurve2D.Double quadCurve;
            double yDiff = 1.0*(2*i-1)/(edges-1);
            quadCurve = new QuadCurve2D.Double(P1.x, P1.y, P1.x + (P2.x - P1.x)/2, P1.y - yDiff*2*(P1.y - y), P2.x, P2.y);
            g2.draw(quadCurve);
            quadCurve = new QuadCurve2D.Double(P1.x, P1.y, P1.x + (P2.x - P1.x)/2, P1.y + yDiff*2*(P1.y - y), P2.x, P2.y);
            g2.draw(quadCurve);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bezier test");
        frame.add(new BezierTest2());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } // End of main method
}

