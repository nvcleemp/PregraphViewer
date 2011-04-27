/* PregraphEdgePainter.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.rendering;

import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class PregraphEdgePainter implements EdgePainter{

    private static int MULTI_EDGE_GAP = 20;
    private Stroke stroke = new BasicStroke(2.0f);
    private double loopAngle = Math.PI/10;

    private static final boolean DEBUG = false;

    public void paintEdge(EmbeddedPregraph pregraph, Edge edge, Graphics2D graphics2D) {
        graphics2D.setStroke(stroke);
        List<Vertex> vertices = edge.getVertices();
        if(edge.isSemiEdge()){
            graphics2D.setColor(pregraph.getColourProvider().getColour(edge, 0));
            graphics2D.drawLine(pregraph.getX(vertices.get(0)),
                    pregraph.getY(vertices.get(0)),
                    pregraph.getX(vertices.get(1)),
                    pregraph.getY(vertices.get(1)));
        } else if (edge.isLoop()){
            graphics2D.setColor(pregraph.getColourProvider().getColour(edge, 0));
            double xP, yP, xC, yC;
            if(vertices.get(0).getType().equals(Vertex.VertexType.VERTEX)){
                xP = pregraph.getX(vertices.get(0));
                yP = pregraph.getY(vertices.get(0));
                xC = pregraph.getX(vertices.get(1));
                yC = pregraph.getY(vertices.get(1));
            } else {
                xP = pregraph.getX(vertices.get(1));
                yP = pregraph.getY(vertices.get(1));
                xC = pregraph.getX(vertices.get(0));
                yC = pregraph.getY(vertices.get(0));
            }
            double xB = xP + 4.0/3*(xC-xP);
            double yB = yP + 4.0/3*(yC-yP);

            double d = Math.sqrt((xB-xP)*(xB-xP)+(yB-yP)*(yB-yP))*Math.tan(loopAngle);

            double xE = - yC + yP;
            double yE = - xP + xC;

            double xC1 = xB + d*xE/Math.sqrt(xE*xE+yE*yE);
            double yC1 = yB + d*yE/Math.sqrt(xE*xE+yE*yE);
            double xC2 = xB - d*xE/Math.sqrt(xE*xE+yE*yE);
            double yC2 = yB - d*yE/Math.sqrt(xE*xE+yE*yE);
            if(DEBUG){
                Graphics2D debug = (Graphics2D)graphics2D.create();
                debug.setStroke(new BasicStroke(1f));
                debug.setColor(Color.red);
                debug.fillOval((int)(xB-2), (int)(yB-2), 4, 4);
                debug.drawLine((int)xB, (int)yB, (int)xP, (int)yP);
                debug.drawLine((int)(xP + xE), (int)(yP + yE), (int)xP, (int)yP);
                debug.setColor(Color.GREEN);
                debug.fillOval((int)(xC1-2), (int)(yC1-2), 4, 4);
                debug.fillOval((int)(xC2-2), (int)(yC2-2), 4, 4);
                debug.drawLine((int)xC1, (int)yC1, (int)xP, (int)yP);
                debug.drawLine((int)xC2, (int)yC2, (int)xP, (int)yP);
                System.out.println("d: " + d);
                System.out.println("E: (" + xE + "," + yE + ")");
            }
            CubicCurve2D.Double loop = new CubicCurve2D.Double(xP, yP, xC1, yC1, xC2, yC2, xP, yP);
            graphics2D.draw(loop);
        } else if (edge.getMultiplicity()>1){
            int coordX1 = pregraph.getX(vertices.get(0));
            int coordY1 = pregraph.getY(vertices.get(0));
            int coordX2 = pregraph.getX(vertices.get(1));
            int coordY2 = pregraph.getY(vertices.get(1));
            if(edge.getMultiplicity()%2==1){
                graphics2D.setColor(pregraph.getColourProvider().getColour(edge, edge.getMultiplicity()-1));
                graphics2D.drawLine(coordX1,coordY1,coordX2,coordY2);
            }
            //calculate normal on the line connecting the two vertices
            int normX = coordY1 - coordY2;
            int normY = coordX2 - coordX1;
            double size = Math.sqrt(normX*normX + normY*normY);

            int midX = coordX1 + (coordX2 - coordX1)/2;
            int midY = coordY1 + (coordY2 - coordY1)/2;

            QuadCurve2D.Double quadCurve;
            for(int i = 1; i <= edge.getMultiplicity()/2; i++){
                int colour1 = (i-1)*2;
                int colour2 = i*2-1;
                graphics2D.setColor(pregraph.getColourProvider().getColour(edge, colour1));
                int controlPointPosition = 2 * i - 1 + edge.getMultiplicity()%2;
                //double yDiff = 1.0*(2*i-1)/(edges-1);
                quadCurve = new QuadCurve2D.Double(coordX1, coordY1, midX + normX*controlPointPosition*MULTI_EDGE_GAP/size, midY + normY*controlPointPosition*MULTI_EDGE_GAP/size, coordX2, coordY2);
                graphics2D.draw(quadCurve);
                graphics2D.setColor(pregraph.getColourProvider().getColour(edge, colour2));
                quadCurve = new QuadCurve2D.Double(coordX1, coordY1, midX - normX*controlPointPosition*MULTI_EDGE_GAP/size, midY - normY*controlPointPosition*MULTI_EDGE_GAP/size, coordX2, coordY2);
                graphics2D.draw(quadCurve);
            }
        } else {
            graphics2D.setColor(pregraph.getColourProvider().getColour(edge, 0));
            graphics2D.drawLine(pregraph.getX(vertices.get(0)),
                    pregraph.getY(vertices.get(0)),
                    pregraph.getX(vertices.get(1)),
                    pregraph.getY(vertices.get(1)));
        }
    }

}
