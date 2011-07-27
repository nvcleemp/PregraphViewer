/* ViewerPanelVertexMouseHandler.java
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

import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraphListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
class ViewerPanelVertexMouseHandler extends MouseAdapter implements MouseMotionListener, EmbeddedPregraphListener {
    private Map<Vertex, Integer> oldXCoordinates = new HashMap<Vertex, Integer>();
    private Map<Vertex, Integer> oldYCoordinates = new HashMap<Vertex, Integer>();
    private Vertex focusedVertex;
    private List<Vertex> focusedVertexNeighbours = new ArrayList<Vertex>();
    ViewerPanel viewerPanel;

    ViewerPanelVertexMouseHandler(ViewerPanel viewerPanel) {
        this.viewerPanel = viewerPanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (focusedVertex != null) {
            if (e.isShiftDown()) {
                oldXCoordinates.clear();
                oldYCoordinates.clear();
                int xShift = (e.getX() - viewerPanel.getWidth() / 2) - viewerPanel.getGraph().getX(focusedVertex);
                int yShift = (e.getY() - viewerPanel.getHeight() / 2) - viewerPanel.getGraph().getY(focusedVertex);
                for (Vertex vertex : focusedVertexNeighbours) {
                    if (Vertex.VertexType.LOOP_VERTEX.equals(vertex.getType()) || Vertex.VertexType.SEMI_EDGE_VERTEX.equals(vertex.getType())) {
                        viewerPanel.getGraph().shiftCoordinates(vertex, xShift, yShift);
                    }
                }
                viewerPanel.getGraph().shiftCoordinates(focusedVertex, xShift, yShift);
            } else if (e.isControlDown()) {
                int yShift = (e.getY() - viewerPanel.getHeight() / 2) - viewerPanel.getGraph().getY(focusedVertex);
                for (Vertex vertex : focusedVertexNeighbours) {
                    if (Vertex.VertexType.LOOP_VERTEX.equals(vertex.getType()) || Vertex.VertexType.SEMI_EDGE_VERTEX.equals(vertex.getType())) {
                        if (oldXCoordinates.containsKey(vertex) && oldYCoordinates.containsKey(vertex)) {
                            int oldX = oldXCoordinates.get(vertex) - viewerPanel.getGraph().getX(focusedVertex);
                            int oldY = oldYCoordinates.get(vertex) - viewerPanel.getGraph().getY(focusedVertex);
                            int newX = (int) (oldX * Math.cos(0.1 * yShift) - oldY * Math.sin(0.1 * yShift));
                            int newY = (int) (oldX * Math.sin(0.1 * yShift) + oldY * Math.cos(0.1 * yShift));
                            viewerPanel.getGraph().setCoordinates(vertex, viewerPanel.getGraph().getX(focusedVertex) + newX, viewerPanel.getGraph().getY(focusedVertex) + newY);
                        } else {
                            for (Vertex otherVertex : focusedVertexNeighbours) {
                                oldXCoordinates.put(otherVertex, viewerPanel.getGraph().getX(otherVertex));
                                oldYCoordinates.put(otherVertex, viewerPanel.getGraph().getY(otherVertex));
                            }
                        }
                    }
                }
            } else {
                oldXCoordinates.clear();
                oldYCoordinates.clear();
                viewerPanel.getGraph().setCoordinates(focusedVertex, e.getX() - viewerPanel.getWidth() / 2, e.getY() - viewerPanel.getHeight() / 2);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (viewerPanel.getGraph() != null) {
            if (!e.isShiftDown()) {
                //clear selection
                viewerPanel.getGraph().clearSelectedVertices();
            }
            for (Vertex vertex : viewerPanel.getGraph().getVertices()) {
                if (((viewerPanel.getGraph().getX(vertex) - e.getX() + viewerPanel.getWidth() / 2) * (viewerPanel.getGraph().getX(vertex) - e.getX() + viewerPanel.getWidth() / 2) < 36) && ((viewerPanel.getGraph().getY(vertex) - e.getY() + viewerPanel.getHeight() / 2) * (viewerPanel.getGraph().getY(vertex) - e.getY() + viewerPanel.getHeight() / 2) < 36)) {
                    viewerPanel.getGraph().setFocusedVertex(vertex);
                    viewerPanel.getGraph().addSelectedVertex(vertex);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        viewerPanel.getGraph().setFocusedVertex(null);
        oldXCoordinates.clear();
        oldYCoordinates.clear();
    }

    public void focusedVertexChanged(EmbeddedPregraph source, Vertex newFocusedVertex) {
        if (focusedVertex == null ? null == newFocusedVertex : focusedVertex.equals(newFocusedVertex)) {
            return;
        }
        focusedVertex = newFocusedVertex;
        focusedVertexNeighbours.clear();
        if (focusedVertex != null) {
            for (Edge edge : focusedVertex.getEdges()) {
                Vertex otherVertex = edge.getOtherVertex(focusedVertex);
                focusedVertexNeighbours.add(otherVertex);
            }
        }
    }

    public void embeddingChanged(EmbeddedPregraph source) {
        //do nothing
    }

    public void selectedVerticesChanged(EmbeddedPregraph source) {
        //do nothing
    }
    
}
