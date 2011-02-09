/* ScaleToFitEmbedder.java
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

/**
 *
 * @author nvcleemp
 */
public class ScaleToFitEmbedder implements Embedder<Embedding2D> {

    private int width;
    private int height;
    private EmbeddedPregraph pregraph;

    public ScaleToFitEmbedder() {
        //
    }

    public ScaleToFitEmbedder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void embed() {
        if (pregraph == null) {
            throw new IllegalStateException();
        }
        int maxX = 0, maxY = 0;
        for (Vertex vertex : pregraph.getVertices()) {
            int x = Math.abs(pregraph.getX(vertex));
            if (x > maxX) {
                maxX = x;
            }
            int y = Math.abs(pregraph.getY(vertex));
            if (y > maxY) {
                maxY = y;
            }
        }
        for (Vertex vertex : pregraph.getVertices()) {
            int x = (int)(0.9*pregraph.getX(vertex));
            int y = (int)(0.9*pregraph.getY(vertex));
            pregraph.setCoordinates(vertex, x*width/(2*maxX), y*height/(2*maxY));
        }
    }

    public void setGraph(EmbeddedPregraph pregraph) {
        this.pregraph = pregraph;
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }
}
