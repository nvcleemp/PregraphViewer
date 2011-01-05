/* ViewerPanel.java
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

import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.VertexPainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.EdgePainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphEdgePainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphVertexPainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraphListener;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphNumberedVertexPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ViewerPanel extends JPanel implements EmbeddedPregraphListener{
    private EmbeddedPregraph graph;
    private VertexPainter vertexPainter;
    private EdgePainter edgePainter;
    private Vertex selectedVertex;

    public ViewerPanel() {
        vertexPainter = new PregraphNumberedVertexPainter();
        edgePainter = new PregraphEdgePainter();
        VertexMouseHandler vertexMouseHandler = new VertexMouseHandler();
        addMouseListener(vertexMouseHandler);
        addMouseMotionListener(vertexMouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderGraph(g, getWidth(), getHeight());
    }

    private void renderGraph(Graphics g, int width, int height) {
        g.setColor(new Color(200, 255, 200));
        g.fillRect(0, 0, width, height);
        if (graph != null) {
            g = g.create();
            g.translate(width / 2, height / 2);
            for (Edge edge : graph.getEdges()) {
                edgePainter.paintEdge(graph, edge, (Graphics2D) g.create());
            }
            for (Vertex vertex : graph.getVertices()) {
                vertexPainter.paintVertex(graph, vertex, (Graphics2D) g.create());
            }
        }
    }

    public RenderedImage getImage(){
        BufferedImage im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        renderGraph(g, getWidth(), getHeight());
        return im;
    }

    public void setGraph(EmbeddedPregraph graph) {
        if(this.graph != null && this.graph.equals(graph)) return;
        
        if(this.graph!=null)
            this.graph.removeEmbeddedPregraphListener(this);
        this.graph = graph;
        if(graph!=null)
            graph.addEmbeddedPregraphListener(this);
        repaint();
    }

    public void embeddingChanged(EmbeddedPregraph source) {
        if(source.equals(graph))
            repaint();
    }

    private class VertexMouseHandler extends MouseAdapter implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if(selectedVertex!=null){
                graph.setCoordinates(selectedVertex, e.getX()-getWidth()/2, e.getY()-getHeight()/2);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(graph!=null){
                for (Vertex vertex : graph.getVertices()) {
                    if(((graph.getX(vertex)-e.getX()+getWidth()/2)*(graph.getX(vertex)-e.getX()+getWidth()/2)<36) &&
                            ((graph.getY(vertex)-e.getY()+getHeight()/2)*(graph.getY(vertex)-e.getY()+getHeight()/2)<36)){
                            selectedVertex = vertex;
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selectedVertex = null;
        }
        
    }
}
