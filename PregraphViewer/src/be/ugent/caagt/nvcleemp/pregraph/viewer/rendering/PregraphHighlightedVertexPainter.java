/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.caagt.nvcleemp.pregraph.viewer.rendering;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author nvcleemp
 */
public class PregraphHighlightedVertexPainter implements VertexPainter{
    
    private static final int CIRCLE_RADIUS = 16;
    
    private Color color;

    public PregraphHighlightedVertexPainter(Color color) {
        this.color = color;
    }

    public void paintVertex(EmbeddedPregraph pregraph, Vertex vertex, Graphics2D graphics2D) {
        graphics2D.setColor(color);
        if(vertex.getType().equals(Vertex.VertexType.VERTEX)){
            graphics2D.fillOval(pregraph.getX(vertex)-CIRCLE_RADIUS, pregraph.getY(vertex)-CIRCLE_RADIUS, 2*CIRCLE_RADIUS, 2*CIRCLE_RADIUS);
        } else {
            graphics2D.fillRect(pregraph.getX(vertex)-CIRCLE_RADIUS, pregraph.getY(vertex)-CIRCLE_RADIUS, 2*CIRCLE_RADIUS, 2*CIRCLE_RADIUS);
        }
    }
}
