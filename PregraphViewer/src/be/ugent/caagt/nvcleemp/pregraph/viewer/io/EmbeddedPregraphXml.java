/* EmbeddedPregraphXml.java
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

/**
 *
 * @author nvcleemp
 */
public interface EmbeddedPregraphXml {
    String GRAPH_ELEMENT = "embeddedpregraph";
    String LIST_ELEMENT = "embeddedpregraphs";

    String VERTICES_ELEMENT = "vertices";
    String LOOP_VERTEX_ELEMENT = "loopvertex";
    String SEMI_EDGE_VERTEX_ELEMENT = "semiedgevertex";
    String VERTEX_ELEMENT = "vertex";
    String X = "X";
    String Y = "Y";
    String ID = "id";

    String EDGES_ELEMENT = "edges";
    String EDGE_ELEMENT = "edge";
    String VERTEX1_ATTRIBUTE = "vertex1";
    String VERTEX2_ATTRIBUTE = "vertex2";
    String MULTIPLICITY_ATTRIBUTE = "multiplicity";

}
