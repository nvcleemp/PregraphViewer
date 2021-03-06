/* ViewerPanel.java
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

import be.ugent.caagt.nvcleemp.pregraph.viewer.ViewerSettings.Setting;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.VertexPainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.EdgePainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphEdgePainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraphListener;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphNumberedVertexPainter;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphHighlightedVertexPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 *
 * @author nvcleemp
 */
public class ViewerPanel extends JPanel implements EmbeddedPregraphListener{
    private EmbeddedPregraph graph;
    private VertexPainter selectionPainter;
    private VertexPainter focusPainter;
    private VertexPainter vertexPainter;
    private EdgePainter edgePainter;

    private ViewerSettings viewerSettings;
    private ViewerSettingsListener viewerSettingsListener = new ViewerSettingsListener() {

        public void settingChanged(Setting setting) {
            repaint(); //TODO: maybe check which setting once there are more settings
        }
    };

    public ViewerPanel() {
        this(new ViewerSettings());
    }

    public ViewerPanel(ViewerSettings settings) {
        selectionPainter = new PregraphHighlightedVertexPainter(Color.YELLOW);
        focusPainter = new PregraphHighlightedVertexPainter(Color.ORANGE);
        vertexPainter = new PregraphNumberedVertexPainter();
        edgePainter = new PregraphEdgePainter();
        ViewerPanelVertexMouseHandler vertexMouseHandler = new ViewerPanelVertexMouseHandler(this);
        addMouseListener(vertexMouseHandler);
        addMouseMotionListener(vertexMouseHandler);
        this.viewerSettings = settings;
        viewerSettings.addViewerSettingsListener(viewerSettingsListener);
        
        ToolTipManager.sharedInstance().registerComponent(this);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderGraph(g, getWidth(), getHeight());
        if(viewerSettings.showLegend()){
            renderColourLegend(g);
        }
    }

    private void renderColourLegend(Graphics g){
        g = g.create();
        g.setColor(Color.BLACK);
        final int verticalOffset = 10;
        final int horizontalOffset = 5;
        final int colourBarHeight = 5;
        final int colourBarWidth = 30;
        final int verticalItemShift = 20;
        final int colourCount = graph.getColourProvider().getColourCount();
        int textWidth = SwingUtilities.computeStringWidth(
                g.getFontMetrics(),
                Integer.toString(colourCount));
        g.drawRect(0, 0,
                3*horizontalOffset + colourBarWidth + textWidth,
                2*verticalOffset + verticalItemShift*(colourCount-1));
        for (int i = 0; i < colourCount; i++) {
            g.setColor(graph.getColourProvider().getColour(i));
            g.fillRect(horizontalOffset, verticalOffset + i*verticalItemShift,
                            colourBarWidth, colourBarHeight);
            g.drawString(Integer.toString(i+1),
                    2*horizontalOffset + colourBarWidth,
                    i*verticalItemShift + verticalOffset + colourBarHeight);
        }
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
            for (Vertex vertex : graph.getSelectedVertices()) {
                selectionPainter.paintVertex(graph, vertex, (Graphics2D) g.create());
            }
            if(graph.getFocusedVertex()!=null){
                focusPainter.paintVertex(graph, graph.getFocusedVertex(), (Graphics2D)g.create());
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
        
        EmbeddedPregraph oldGraph = this.graph;
        if(this.graph!=null){
            this.graph.removeEmbeddedPregraphListener(this);
        }
        this.graph = graph;
        if(graph!=null){
            graph.addEmbeddedPregraphListener(this);
        }
        fireGraphChanged(this.graph, oldGraph);
        repaint();
    }

    public EmbeddedPregraph getGraph() {
        return graph;
    }

    public void embeddingChanged(EmbeddedPregraph source) {
        if(source.equals(graph))
            repaint();
    }

    public void focusedVertexChanged(EmbeddedPregraph source, Vertex newFocusedVertex) {
        if(source.equals(graph))
            repaint();
    }

    public void selectedVerticesChanged(EmbeddedPregraph source) {
        if(source.equals(graph))
            repaint();
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        if(graph!=null){
            for (Vertex vertex : graph.getVertices()) {
                if(((graph.getX(vertex)-e.getX()+getWidth()/2)*(graph.getX(vertex)-e.getX()+getWidth()/2)<36) &&
                        ((graph.getY(vertex)-e.getY()+getHeight()/2)*(graph.getY(vertex)-e.getY()+getHeight()/2)<36)){
                    return graph.getMetaData(vertex);
                }
            }
        }
        return super.getToolTipText(e);
    }
    
    private List<ViewerPanelListener> listeners = new ArrayList<ViewerPanelListener>();
    
    public void addViewerPanelListener(ViewerPanelListener l){
        listeners.add(l);
    }
    
    public void removeViewerPanelListener(ViewerPanelListener l){
        listeners.remove(l);
    }
    
    private void fireGraphChanged(EmbeddedPregraph newGraph, EmbeddedPregraph oldGraph){
        for (ViewerPanelListener l : listeners) {
            l.graphChanged(newGraph, oldGraph);
        }
    }
}
