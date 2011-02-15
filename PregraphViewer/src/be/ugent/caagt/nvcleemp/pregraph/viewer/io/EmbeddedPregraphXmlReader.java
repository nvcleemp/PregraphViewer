/* EmbeddedPregraphXmlReader.java
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

package be.ugent.caagt.nvcleemp.pregraph.viewer.io;

import be.ugent.caagt.nvcleemp.graphio.GraphReader;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.Viewer;
import be.ugent.caagt.nvcleemp.pregraph.viewer.embedder.EmbeddedPregraph;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author nvcleemp
 */
public class EmbeddedPregraphXmlReader implements GraphReader, EmbeddedPregraphXml {

    private Document document;

    public EmbeddedPregraphXmlReader(File f) {
        try {
            document = new SAXBuilder().build(f);
        } catch (JDOMException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EmbeddedPregraph readSingleGraph() throws IOException {
        if(document==null) throw new IllegalStateException("No document is attached");

        Element graphElement;
        if(document.getRootElement().getName().equals(GRAPH_ELEMENT)){
            graphElement = document.getRootElement();
        } else if(document.getRootElement().getName().equals(LIST_ELEMENT)){
            graphElement = document.getRootElement().getChild(GRAPH_ELEMENT);
            if(graphElement==null) throw new IllegalArgumentException("This document does not contain an embedded pregraph");
        } else {
            throw new IllegalArgumentException("This document does not contain an embedded pregraph");
        }

        return readSingleEmbeddedPregraph(graphElement);
    }

    public List<? extends EmbeddedPregraph> readAllGraphs() throws IOException {
        if(document==null) throw new IllegalStateException("No document is attached");

        if(!document.getRootElement().getName().equals(LIST_ELEMENT)){
            throw new IllegalArgumentException("This document does not contain a list of embedded pregraphs");
        }

        List<EmbeddedPregraph> pregraphs = new ArrayList<EmbeddedPregraph>();

        for (Element element : (List<Element>)document.getRootElement().getChildren(GRAPH_ELEMENT)) {
            pregraphs.add(readSingleEmbeddedPregraph(element));
        }
        return pregraphs;
    }

    public EmbeddedPregraph readNextGraph() throws IOException {
        throw new UnsupportedOperationException("EmbeddedPregraphXmlReader does not support reading the graphs as a stream.");
    }

    private EmbeddedPregraph readSingleEmbeddedPregraph(Element graphElement){
        //parse vertices
        Element verticesElement = graphElement.getChild(VERTICES_ELEMENT);
        Vertex vertices[] = new Vertex[verticesElement.getChildren().size()];
        for (Element element : (List<Element>)verticesElement.getChildren(VERTEX_ELEMENT)) {
            int id = Integer.parseInt(element.getAttributeValue(ID));
            vertices[id-1] = new Vertex(Vertex.VertexType.VERTEX);
            vertices[id-1].setAnnotation(Integer.toString(id));
        }
        for (Element element : (List<Element>)verticesElement.getChildren(LOOP_VERTEX_ELEMENT)) {
            int id = Integer.parseInt(element.getAttributeValue(ID));
            vertices[id-1] = new Vertex(Vertex.VertexType.LOOP_VERTEX);
        }
        for (Element element : (List<Element>)verticesElement.getChildren(SEMI_EDGE_VERTEX_ELEMENT)) {
            int id = Integer.parseInt(element.getAttributeValue(ID));
            vertices[id-1] = new Vertex(Vertex.VertexType.SEMI_EDGE_VERTEX);
        }

        //parse edges
        Element edgesElement = graphElement.getChild(EDGES_ELEMENT);
        List<Edge> edges = new ArrayList<Edge>();
        for (Element element : (List<Element>)edgesElement.getChildren(EDGE_ELEMENT)) {
            int v1 = Integer.parseInt(element.getAttributeValue(VERTEX1_ATTRIBUTE));
            int v2 = Integer.parseInt(element.getAttributeValue(VERTEX2_ATTRIBUTE));
            int m = Integer.parseInt(element.getAttributeValue(MULTIPLICITY_ATTRIBUTE));
            Edge edge = new Edge(vertices[v1 - 1], vertices[v2 - 1], m);
            edges.add(edge);
            vertices[v1 - 1].addEdge(edge);
            vertices[v2 - 1].addEdge(edge);
        }

        List<Vertex> vertexList = new ArrayList<Vertex>();
        for (Vertex vertex : vertices) {
            vertexList.add(vertex);
        }

        EmbeddedPregraph pregraph = new EmbeddedPregraph(new Pregraph(vertexList, edges));
        for (Element element : (List<Element>)verticesElement.getChildren()) {
            int id = Integer.parseInt(element.getAttributeValue(ID));
            parseVertexCoordinates(pregraph, vertices[id-1], element);
        }

        return pregraph;
    }

    private void parseVertexCoordinates(EmbeddedPregraph g, Vertex v, Element e){
        int x = Integer.parseInt(e.getAttributeValue(X));
        int y = Integer.parseInt(e.getAttributeValue(Y));
        g.setCoordinates(v, x, y);
    }

}
