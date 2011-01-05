/* EmbeddedPregraphXmlWriter.java
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

import be.ugent.caagt.nvcleemp.graphio.GraphWriter;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author nvcleemp
 */
public class EmbeddedPregraphXmlWriter implements GraphWriter<EmbeddedPregraph>, EmbeddedPregraphXml{

    private final OutputStream out;

    public EmbeddedPregraphXmlWriter(OutputStream out) {
        this.out = out;
    }

    public void writeGraph(EmbeddedPregraph graph) throws IOException {
        outputElement(createGraphElement(graph));
        out.close();
    }

    public void writeGraphList(List<? extends EmbeddedPregraph> graphs) throws IOException {
        Element root = new Element(LIST_ELEMENT);
        for (EmbeddedPregraph pregraph : graphs) {
            root.addContent(createGraphElement(pregraph));
        }
        outputElement(root);
        out.close();
    }

    private void outputElement(Element root) throws IOException {
        Document doc = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getCompactFormat());
        xmlOutputter.output(doc, out);
    }

    private Element createGraphElement(EmbeddedPregraph graph){
        Element graphElement = new Element(GRAPH_ELEMENT);

        Element verticesElement = new Element(VERTICES_ELEMENT);
        Map<Vertex, Integer> vertexMapping = new HashMap<Vertex, Integer>();
        int currentVertex = 1;
        for (Vertex vertex : graph.getVertices()) {
            vertexMapping.put(vertex, currentVertex);
            Element vertexElement;
            if(vertex.getType().equals(Vertex.VertexType.LOOP_VERTEX)){
                vertexElement = new Element(LOOP_VERTEX_ELEMENT);
            } else if(vertex.getType().equals(Vertex.VertexType.SEMI_EDGE_VERTEX)){
                vertexElement = new Element(SEMI_EDGE_VERTEX_ELEMENT);
            } else {
                vertexElement = new Element(VERTEX_ELEMENT);
            }
            vertexElement.setAttribute(X, Integer.toString(graph.getX(vertex)));
            vertexElement.setAttribute(Y, Integer.toString(graph.getY(vertex)));
            vertexElement.setAttribute(ID, Integer.toString(currentVertex));
            verticesElement.addContent(vertexElement);
            currentVertex++;
        }
        graphElement.addContent(verticesElement);

        Element edgesElement = new Element(EDGES_ELEMENT);
        for (Edge edge : graph.getEdges()) {
            Element edgeElement = new Element(EDGE_ELEMENT);
            edgeElement.setAttribute(VERTEX1_ATTRIBUTE, vertexMapping.get(edge.getVertices().get(0)).toString());
            edgeElement.setAttribute(VERTEX2_ATTRIBUTE, vertexMapping.get(edge.getVertices().get(1)).toString());
            edgeElement.setAttribute(MULTIPLICITY_ATTRIBUTE, Integer.toString(edge.getMultiplicity()));
            edgesElement.addContent(edgeElement);
        }
        graphElement.addContent(edgesElement);
        return graphElement;
    }

}
