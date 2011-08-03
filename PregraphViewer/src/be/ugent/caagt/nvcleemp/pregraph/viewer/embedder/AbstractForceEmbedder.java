/* AbstractForceEmbedder.java
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

import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractForceEmbedder implements Embedder<Embedding2D> {
    
    protected int[][] changes;
    protected EmbeddedPregraph pregraph;
    protected List<Vertex> vertices;
    protected boolean fixSelectedVertices = false;

    protected abstract void calculateChanges();

    public void embed() {
        if (pregraph == null) {
            throw new IllegalStateException();
        }
        calculateChanges();
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        int vertex = 0;
        while (fixSelectedVertices && (vertex == vertices.size() || pregraph.isSelectedVertex(vertex))) {
            vertex++;
        }
        if (vertex < vertices.size()) {
            pregraph.shiftCoordinates(vertices.get(vertex), changes[vertex][0], changes[vertex][1]);
            minX = maxX = pregraph.getX(vertices.get(vertex));
            minY = maxY = pregraph.getY(vertices.get(vertex));
        }
        while (vertex < vertices.size()) {
            if (!fixSelectedVertices || !pregraph.isSelectedVertex(vertex)) {
                pregraph.shiftCoordinates(vertices.get(vertex), changes[vertex][0], changes[vertex][1]);
                int x = pregraph.getX(vertices.get(vertex));
                int y = pregraph.getY(vertices.get(vertex));
                if (x < minX) {
                    minX = x;
                } else if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                } else if (y > maxY) {
                    maxY = y;
                }
            }
            vertex++;
        }
        //center graph around origin
        int shiftX = -(minX + maxX) / 2;
        int shiftY = -(minY + maxY) / 2;
        for (int i = 0; i < vertices.size(); i++) {
            pregraph.shiftCoordinates(vertices.get(i), shiftX, shiftY);
        }
    }

    public boolean isFixSelectedVertices() {
        return fixSelectedVertices;
    }

    public void setFixSelectedVertices(boolean fixSelectedVertices) {
        this.fixSelectedVertices = fixSelectedVertices;
    }

    public void setGraph(EmbeddedPregraph pregraph) {
        this.pregraph = pregraph;
        vertices = pregraph.getVertices();
        changes = new int[vertices.size()][2];
    }
    
}
