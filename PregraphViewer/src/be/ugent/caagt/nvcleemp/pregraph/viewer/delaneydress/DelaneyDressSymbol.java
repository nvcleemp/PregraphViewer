/* DelaneyDressSymbol.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.delaneydress;

import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;

import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class DelaneyDressSymbol extends Pregraph{
    
    private Map<Vertex,Integer> m01;
    private Map<Vertex,Integer> m12;
    
    private int dimension;
    
    private int comment1;
    private int comment2;

    public DelaneyDressSymbol(List<Vertex> vertices, List<Edge> edges, Map<Vertex,Integer> m01, Map<Vertex,Integer> m12, int dimension) {
        super(vertices, edges);
        if(dimension!=2){
            throw new IllegalArgumentException("Currently only dimension 2 is supported");
        }
        this.dimension = dimension;
        this.m01 = m01;
        this.m12 = m12;
    }
    
    public void setComment(int comment1, int comment2){
        this.comment1 = comment1;
        this.comment2 = comment2;
    }

    public int getDimension() {
        return dimension;
    }

    public Integer getM01(Vertex v) {
        return m01.get(v);
    }

    public Integer getM12(Vertex v) {
        return m12.get(v);
    }

}
