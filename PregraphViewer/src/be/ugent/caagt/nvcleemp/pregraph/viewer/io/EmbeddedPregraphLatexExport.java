/* EmbeddedPregraphLatexExport.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.io;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;

/**
 *
 * @author nvcleemp
 */
public class EmbeddedPregraphLatexExport {

    private static final int MULTI_EDGE_GAP = 20;
    private static double loopAngle = Math.PI/10;

    private EmbeddedPregraphLatexExport(){
        //
    }

    public static String export(EmbeddedPregraph pregraph){
        StringBuffer latex = new StringBuffer("\\begin{tikzpicture}[scale=0.1]\n");
        for (Vertex vertex : pregraph.getVertices()) {
            if(vertex.getType().equals(Vertex.VertexType.VERTEX)){
                latex.append(String.format("    \\node [circle,fill] (%s) at (%d,%d);\n",
                        Integer.toHexString(vertex.hashCode()), pregraph.getX(vertex), pregraph.getY(vertex)));
            }
        }
        for (Edge edge : pregraph.getEdges()) {
            if(edge.isLoop()){
                Vertex v1 = edge.getLoopVertex();
                Vertex v2 = edge.getOtherVertex(v1);
                double xP = pregraph.getX(v2);
                double yP = pregraph.getY(v2);
                double xC = pregraph.getX(v1);
                double yC = pregraph.getY(v1);
                double xB = xP + 4.0/3*(xC-xP);
                double yB = yP + 4.0/3*(yC-yP);

                double d = Math.sqrt((xB-xP)*(xB-xP)+(yB-yP)*(yB-yP))*Math.tan(loopAngle);

                double xE = - yC + yP;
                double yE = - xP + xC;

                double xC1 = xB + d*xE/Math.sqrt(xE*xE+yE*yE);
                double yC1 = yB + d*yE/Math.sqrt(xE*xE+yE*yE);
                double xC2 = xB - d*xE/Math.sqrt(xE*xE+yE*yE);
                double yC2 = yB - d*yE/Math.sqrt(xE*xE+yE*yE);

                latex.append(String.format("    \\draw (%s) .. controls (%f,%f) .. (%f,%f) .. (%s);\n",
                        Integer.toHexString(v2.hashCode()), xC1, yC1, xC2, yC2, Integer.toHexString(v2.hashCode())));

            } else if(edge.isSemiEdge()){
                latex.append(String.format("    \\draw (%s) to (%d,%d);\n",
                        Integer.toHexString((edge.getOtherVertex(edge.getSemiEdgeVertex())).hashCode()),
                        pregraph.getX(edge.getSemiEdgeVertex()),
                        pregraph.getY(edge.getSemiEdgeVertex())));
            } else if(edge.getMultiplicity()>1){
                Vertex v1 = edge.getVertices().get(0);
                Vertex v2 = edge.getOtherVertex(v1);
                if(edge.getMultiplicity()%2==1){
                    latex.append(String.format("    \\draw (%s) to (%s);\n",
                        Integer.toHexString(v1.hashCode()), Integer.toHexString(v2.hashCode())));
                }
                double y1 = pregraph.getY(v1);
                double x1 = pregraph.getX(v1);
                double y2 = pregraph.getY(v2);
                double x2 = pregraph.getX(v2);
                //calculate normal on the line connecting the two vertices
                double normX = y1 - y2;
                double normY = x1 - x2;
                double size = Math.sqrt(normX*normX + normY*normY);

                double midX = x1 + (x2 - x1)/2;
                double midY = y1 + (y2 - y1)/2;

                for(int i = 1; i <= edge.getMultiplicity()/2; i++){
                    int controlPointPosition = 2 * i - 1 + edge.getMultiplicity()%2;
                    {
                        double xC = midX + normX*controlPointPosition*MULTI_EDGE_GAP/size;
                        double yC = midY + normY*controlPointPosition*MULTI_EDGE_GAP/size;

                        double xC1 = x1/3 + 2*xC/3;
                        double yC1 = y1/3 + 2*yC/3;
                        double xC2 = x2/3 + 2*xC/3;
                        double yC2 = y2/3 + 2*yC/3;

                        latex.append(String.format("    \\draw (%s) .. controls (%f,%f) .. (%f,%f) .. (%s);\n",
                                Integer.toHexString(v1.hashCode()), xC1, yC1, xC2, yC2, Integer.toHexString(v2.hashCode())));
                    }
                    {
                        double xC = midX - normX*controlPointPosition*MULTI_EDGE_GAP/size;
                        double yC = midY - normY*controlPointPosition*MULTI_EDGE_GAP/size;

                        double xC1 = x1/3 + 2*xC/3;
                        double yC1 = y1/3 + 2*yC/3;
                        double xC2 = x2/3 + 2*xC/3;
                        double yC2 = y2/3 + 2*yC/3;

                        latex.append(String.format("    \\draw (%s) .. controls (%f,%f) .. (%f,%f) .. (%s);\n",
                                Integer.toHexString(v1.hashCode()), xC1, yC1, xC2, yC2, Integer.toHexString(v2.hashCode())));
                    }
                }
            } else {
                Vertex v1 = edge.getVertices().get(0);
                Vertex v2 = edge.getOtherVertex(v1);
                latex.append(String.format("    \\draw (%s) to (%s);\n",
                        Integer.toHexString(v1.hashCode()), Integer.toHexString(v2.hashCode())));
            }
        }

        latex.append("\\end{tikzpicture}\n");
        
        return latex.toString();
    }
}
