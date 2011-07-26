/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.caagt.nvcleemp.pregraph.viewer.io;

import be.ugent.caagt.nvcleemp.graphio.GraphReader;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Edge;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Pregraph;
import be.ugent.caagt.nvcleemp.graphio.pregraph.Vertex;
import be.ugent.caagt.nvcleemp.pregraph.viewer.delaneydress.DelaneyDressSymbol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author nvcleemp
 */
public class DelaneyDressSymbolReader implements GraphReader{
    
    private Scanner scanner;

    public DelaneyDressSymbolReader(File f) throws FileNotFoundException {
        scanner = new Scanner(f);
    }

    public Pregraph readSingleGraph() throws IOException {
        return readSingleDDSymbol(scanner.nextLine());
    }

    public List<? extends Pregraph> readAllGraphs() throws IOException {
        List<Pregraph> list = new ArrayList<Pregraph>();
        while(scanner.hasNextLine()){
            list.add(readSingleDDSymbol(scanner.nextLine()));
        }
        return list;
    }

    public Pregraph readNextGraph() throws IOException {
        if(scanner.hasNextLine()){
            return readSingleDDSymbol(scanner.nextLine());
        } else {
            throw new NoSuchElementException("End of file was reached.");
        }
    }
    
    private Pregraph readSingleDDSymbol(String s){
        System.out.println(s);
        int dimension;
        int order;
        List<Vertex> vertices = new ArrayList<Vertex>();
        List<Edge> edges = new ArrayList<Edge>();
        Map<Vertex, Integer> m01 = new HashMap<Vertex, Integer>();
        Map<Vertex, Integer> m12 = new HashMap<Vertex, Integer>();
        if(!(s.endsWith(">") && s.startsWith("<"))){
            throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
        }
        String[] parts = s.substring(1, s.length()-1).split(":");
        if(parts.length!=4){
            throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
        }
        
        parts[1] = parts[1].trim();
        String[] part1 = parts[1].split(" ");
        if(part1.length==1){
            order = Integer.parseInt(part1[0]);
            dimension = 2;
        } else if(part1.length==2){
            order = Integer.parseInt(part1[0]);
            dimension = Integer.parseInt(part1[1]);
        } else {
            throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
        }
        for (int i = 0; i < order; i++) {
            final Vertex vertex = new Vertex(Vertex.VertexType.VERTEX);
            vertex.setAnnotation(Integer.toString(i+1));
            vertices.add(vertex);
        }
        
        parts[2] = parts[2].trim();
        String[] part2 = parts[2].split(",");
        Edge[][] existingEdges = new Edge[order][order];
        int[][] neighbours = new int[order][dimension+1];
        if(part2.length!=dimension+1){
            throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
        } else {
            for (int sigma = 0; sigma<dimension+1; sigma++) {
                String[] elements = part2[sigma].split(" ");
                boolean[] assigned = new boolean[order];
                for (int i = 0; i < order; i++) {
                    assigned[i] = false;
                }
                int nextElement = 0;
                try{
                    for (int i = 0; i < order; i++) {
                        if(!assigned[i]){
                            int neighbour = Integer.parseInt(elements[nextElement])-1;
                            if(neighbour>order || neighbour<i){
                                throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
                            } else if(i==neighbour){
                                Vertex newVertex = new Vertex(Vertex.VertexType.SEMI_EDGE_VERTEX);
                                Edge e = new Edge(vertices.get(i), newVertex);
                                e.addColour(sigma+1);
                                edges.add(e);
                                neighbours[i][sigma]=i;
                            } else if(existingEdges[i][neighbour]==null){
                                Edge e = new Edge(vertices.get(i), vertices.get(neighbour));
                                e.addColour(sigma+1);
                                edges.add(e);
                                existingEdges[i][neighbour] = e;
                                neighbours[i][sigma]=neighbour;
                                neighbours[neighbour][sigma]=i;
                            } else {
                                Edge e = existingEdges[i][neighbour];
                                e.increaseMultiplicity();
                                e.addColour(sigma+1);
                                neighbours[i][sigma]=neighbour;
                                neighbours[neighbour][sigma]=i;
                            }
                            assigned[i]=true;
                            assigned[neighbour]=true;
                            nextElement++;
                        }
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.", e);
                }
            }
        }
        
        parts[3] = parts[3].trim();
        String[] part3 = parts[3].split(",");
        if(part3.length!=dimension){
            throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.");
        } else {
            //m01
            String[] elements = part3[0].split(" ");
            boolean[] assigned = new boolean[order];
            for (int i = 0; i < order; i++) {
                assigned[i] = false;
            }
            int nextElement = 0;
            try{
                for (int i = 0; i < order; i++) {
                    if(!assigned[i]){
                        int mValue = Integer.parseInt(elements[nextElement]);
                        //assign value to complete orbit
                        m01.put(vertices.get(i), mValue);
                        assigned[i]=true;
                        int neighbour = neighbours[i][0];
                        while(!assigned[neighbour]){
                            assigned[neighbour]=true;
                            m01.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][1];
                            assigned[neighbour]=true;
                            m01.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][0];
                        }
                        neighbour = neighbours[i][1];
                        while(!assigned[neighbour]){
                            assigned[neighbour]=true;
                            m01.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][0];
                            assigned[neighbour]=true;
                            m01.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][1];
                        }
                        nextElement++;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.", e);
            }
            
            //m12
            elements = part3[1].split(" ");
            assigned = new boolean[order];
            for (int i = 0; i < order; i++) {
                assigned[i] = false;
            }
            nextElement = 0;
            try{
                for (int i = 0; i < order; i++) {
                    if(!assigned[i]){
                        int mValue = Integer.parseInt(elements[nextElement]);
                        //assign value to complete orbit
                        m12.put(vertices.get(i), mValue);
                        assigned[i]=true;
                        int neighbour = neighbours[i][1];
                        while(!assigned[neighbour]){
                            assigned[neighbour]=true;
                            m12.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][2];
                            assigned[neighbour]=true;
                            m12.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][1];
                        }
                        neighbour = neighbours[i][2];
                        while(!assigned[neighbour]){
                            assigned[neighbour]=true;
                            m12.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][1];
                            assigned[neighbour]=true;
                            m12.put(vertices.get(neighbour), mValue);
                            neighbour = neighbours[neighbour][2];
                        }
                        nextElement++;
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e){
                throw new IllegalArgumentException("This string is not a correct Delaney-Dress symbol.", e);
            }
        }
        
        return new DelaneyDressSymbol(vertices, edges, m01, m12, dimension);
    }
    
}
