/* ForceEmbedder.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.embedder;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class ForceEmbedder implements Embedder<Embedding2D>{

    private static final double edge_length = 100;
    private static final double semi_edge_length = 50;
    private static final double loop_edge_length = 50;
    private static final double non_edge_length = 500;
    private static final double force = 0.1;
    private static final double friction = 0.80;

    private EmbeddedPregraph pregraph;
    private List<Vertex> vertices;
    int[][] changes;

    public void embed() {
        if(pregraph == null) throw new IllegalStateException();

        for (int i = 0; i < vertices.size(); i++) {
            Vertex v1 = vertices.get(i);
            for (Vertex v : vertices) {
                if(!v1.equals(v)){
                    boolean print = false;
//                    boolean print = (v1.getEdges().size()==1 && v.getEdges().size()==1);
                    double length;
                    if(v1.getEdgeContaining(v)!=null) {
                        if(v.getType().equals(Vertex.VertexType.LOOP_VERTEX) || v1.getType().equals(Vertex.VertexType.LOOP_VERTEX)){
                            length = loop_edge_length;
                        } else if(v.getType().equals(Vertex.VertexType.SEMI_EDGE_VERTEX) || v1.getType().equals(Vertex.VertexType.SEMI_EDGE_VERTEX)){
                            length = semi_edge_length;
                        } else {
                            length = edge_length;
                        }
                    } else {
                        length = non_edge_length;
                    }
                    if(print) System.out.println("==========");
                    double dx = pregraph.getX(v1) - pregraph.getX(v);
                    double dy = pregraph.getY(v1) - pregraph.getY(v);
                    if(print) System.out.println("dx: " + dx + "\ndy: " + dy);
                    double change = Math.hypot(dx, dy);
                    if(print) System.out.println("change: " + change);
                    if(change == 0){
                        dx = 1.0;
                        dy = 0.0;
                    } else {
                        dx /= change;
                        dy /= change;
                    }
                    if(print) System.out.println("dx: " + dx + "\ndy: " + dy);
                    change = (change - length)/length*100;
                    if(print) System.out.println("change: " + change);
                    dx *= change * force;
                    dy *= change * force;
                    if(print) System.out.println("dx: " + dx + "\ndy: " + dy);
                    if(print) System.out.println("==========");
                    changes[i][0] -= dx;
                    changes[i][1] -= dy;
                }
            }
        }

        for (int i = 0; i < changes.length; i++) {
            changes[i][0] *= friction;
            changes[i][1] *= friction;
        }

        int minX = 0, maxX = 0, minY = 0, maxY = 0;

        for (int i = 0; i < vertices.size(); i++) {
            pregraph.shiftCoordinates(vertices.get(i), changes[i][0], changes[i][1]);
            if(i==0){
                minX = maxX = pregraph.getX(vertices.get(i));
                minY = maxY = pregraph.getY(vertices.get(i));
            } else {
                int x = pregraph.getX(vertices.get(i));
                int y = pregraph.getY(vertices.get(i));
                if(x<minX){
                    minX = x;
                } else if(x>maxX){
                    maxX = x;
                }

                if(y<minY){
                    minY = y;
                } else if(y>maxY){
                    maxY = y;
                }
            }
        }

        //center graph around origin

        int shiftX = -(minX + maxX)/2;
        int shiftY = -(minY + maxY)/2;
        
        for (int i = 0; i < vertices.size(); i++) {
            pregraph.shiftCoordinates(vertices.get(i), shiftX, shiftY);
        }
    }

    public void setGraph(EmbeddedPregraph pregraph) {
        this.pregraph = pregraph;
        vertices = pregraph.getVertices();
        changes = new int[vertices.size()][2];
    }

}
