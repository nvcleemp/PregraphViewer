/* EmbeddedPregraph.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.embedder;

import be.ugent.caagt.nvcleemp.graphio.Graph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.rendering.PregraphColourProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nvcleemp
 */
public class EmbeddedPregraph implements Graph {

    private final Pregraph pregraph;
    private final Embedding2D embedding;
    private final PregraphColourProvider colourProvider;
    private Vertex focusedVertex;
    private Set<Vertex> selectedVertices = new HashSet<Vertex>();

    public EmbeddedPregraph(Pregraph pregraph) {
        this.pregraph = pregraph;
        embedding = new Embedding2D(pregraph);

        int max = 0;
        for (Edge edge : pregraph.getEdges()) {
            for (Integer integer : edge.getColours()) {
                if(max < integer){
                    max = integer;
                }
            }
        }
        colourProvider = new PregraphColourProvider(max);
    }

    public List<Vertex> getVertices(){
        return new ArrayList<Vertex>(pregraph.getVertices());
    }

    public List<Edge> getEdges(){
        return new ArrayList<Edge>(pregraph.getEdges());
    }

    public int getX(Vertex v){
        return embedding.getCoordinates(v).getX();
    }

    public int getY(Vertex v){
        return embedding.getCoordinates(v).getY();
    }

    public void setX(Vertex v, int x){
        embedding.getCoordinates(v).setX(x);
        fireEmbeddingChanged();
    }

    public void setY(Vertex v, int y){
        embedding.getCoordinates(v).setY(y);
        fireEmbeddingChanged();
    }

    public void setCoordinates(Vertex v, int x, int y){
        Coordinates2D coordinates = embedding.getCoordinates(v);
        coordinates.setX(x);
        coordinates.setY(y);
        fireEmbeddingChanged();
    }

    public void shiftCoordinates(Vertex v, int x, int y){
        Coordinates2D coordinates = embedding.getCoordinates(v);
        coordinates.setX(coordinates.getX() + x);
        coordinates.setY(coordinates.getY() + y);
        fireEmbeddingChanged();
    }

    public PregraphColourProvider getColourProvider(){
        return colourProvider;
    }
    private List<EmbeddedPregraphListener> listeners = new ArrayList<EmbeddedPregraphListener>();

    protected void fireEmbeddingChanged(){
        for (EmbeddedPregraphListener l : listeners) {
            l.embeddingChanged(this);
        }
    }

    protected void fireFocusedVertexChanged(Vertex newFocusedVertex) {
        for (EmbeddedPregraphListener l : listeners) {
            l.focusedVertexChanged(this, newFocusedVertex);
        }
    }

    protected void fireSelectedVerticesChanged() {
        for (EmbeddedPregraphListener l : listeners) {
            l.selectedVerticesChanged(this);
        }
    }

    public void addEmbeddedPregraphListener(EmbeddedPregraphListener l){
        listeners.add(l);
    }

    public void removeEmbeddedPregraphListener(EmbeddedPregraphListener l){
        listeners.remove(l);
    }

    public String getMetaData(Vertex vertex) {
        return pregraph.getMetaData(vertex);
    }

    public Vertex getFocusedVertex() {
        return focusedVertex;
    }

    public void setFocusedVertex(Vertex focusedVertex) {
        if (focusedVertex == null ? null == this.focusedVertex : focusedVertex.equals(this.focusedVertex)) {
            return;
        }
        this.focusedVertex = focusedVertex;
        fireFocusedVertexChanged(focusedVertex);
    }

    public Set<Vertex> getSelectedVertices() {
        return selectedVertices;
    }

    public void addSelectedVertex(Vertex selectedVertex) {
        if (!selectedVertices.contains(selectedVertex)) {
            selectedVertices.add(selectedVertex);
            fireSelectedVerticesChanged();
        }
    }

    public void clearSelectedVertices() {
        if (selectedVertices.isEmpty()) {
            return;
        }
        selectedVertices.clear();
        fireSelectedVerticesChanged();
    }
}
