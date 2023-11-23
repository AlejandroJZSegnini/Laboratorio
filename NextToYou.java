import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

interface Graph<T> {
    boolean add(T vertex);
    boolean connect(T from, T to);
    boolean disconnect(T from, T to);
    boolean contains(T vertex);
    List<T> getInwardEdges(T to);
    List<T> getOutwardEdges(T from);
    List<T> getVerticesConnectedTo(T vertex);
    List<T> getAllVertices();
    boolean remove(T vertex);
    int size();
    Graph<T> subgraph(Collection<T> vertices);
}

class AdjacencyListGraph<T> implements Graph<T> {
    private Map<T, List<T>> adjacencyMap;

    //Metodo Constructor
    public AdjacencyListGraph() {
        adjacencyMap = new HashMap<>();
    }

    //Metodo add
    public boolean add(T vertex) {
        //Verificamos si el vertice que queremos agregar ya pertenece al HashMap.
        if (!contains(vertex)) {
            //Si no pertenece es agregado al HashMap y se retorna true.
            adjacencyMap.put(vertex, new ArrayList<>());
            return true;
        }
        //Si pertenece se retorna false.
        return false;
    }

    //Metodo Connect
    public boolean connect(T from, T to) {
        //Verificamos si los vertices from y to pertenecen al HashMap.
        if (contains(from) && contains(to)) {
            /**Buscamos la lista de los sucesores del vertice from usando el metodo 
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(from);
            //Verificamos los sucesores para ver si el arco from-to ya existe.
            if (!sucesores.contains(to)) {
                //Si no existe lo agregamos usando .add
                sucesores.add(to);
                return true;
            }
        }
        /**Si alguno de los vertices (from o to) no pertenecen al HashMap, o
         * el arco from-to ya existe entonces se retorna false.*/
        return false;
    }

    //Metodo Disconnect
    public boolean disconnect(T from, T to) {
        //Verificamos si los vertices from y to pertenecen al HashMap.
        if (contains(from) && contains(to)) {
            /**Buscamos la lista de los sucesores del vertice from usando el metodo
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(from);
            /**Usamos el metodo .remove() de los ArrayList para eliminar el arco from-to.
             * Esta llamada devuelve true si el elemento se encontraba en la lista y se 
             * eliminó exitosamente, y devuelve false si el elemento no se encontraba en
             *  la lista*/
            return sucesores.remove(to);
        }
        return false;
    }

    //Metodo Contains
    public boolean contains(T vertex) {
        /**Usamos el método .containsKey() propio de la clase HashMap para verificar si 
         * la clave vertex está presente en el mapa. Si lo esta retorna true, si no
         * retorna false.*/
        boolean x = adjacencyMap.containsKey(vertex);
        return x;
    }

    //Metodo GetInwardEdges
    public List<T> getInwardEdges(T to) {
        /**Creamos una lista nueva para almacenar los vértices que tienen arcos 
         * hacia el vertice to.*/
        List<T> predecesores = new ArrayList<>();
        /**Usamos el metodo .keyset() de la clase HashMap para iterar sobre todos los 
         * vertices del mapa.*/
        for (T vertex : adjacencyMap.keySet()) {
            /**Buscamos la lista de los sucesores del vertice vertex usando el metodo 
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(vertex);
            /**Usamos el metodo .contains() de la clase ArrayList para verificar si la 
             * lista de sucesores contiene al vertice to.*/
            if (sucesores.contains(to)) {
                /**Si es asi, entonces vertex es un predecesor de to, por lo que lo 
                 * agregamos a la lista predecesores.*/
                predecesores.add(vertex);
            }
        }
        return predecesores;
    }

    //Metodo GetOutwardEdges
    public List<T> getOutwardEdges(T from) {
        //Usamos contains() para verificar que el vertice from pertenezca al HashMap.
        if (contains(from)) {
            /**Si pertenece, usamos el metodo .get() de la clase HashMap para retornar
             * la lista de sucesores del vertice from.*/
            return adjacencyMap.get(from);
        }
        //Si vertex no pertenece al HashMap se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //Metodo GetVerticesConnectedTo
    public List<T> getVerticesConnectedTo(T vertex) {
        //Usamos contains() para verificar que el vertice vertex pertenezca al HashMap.
        if (contains(vertex)) {
            /**Creamos un conjunto nuevo para almacenar los vértices que estan conectados
             * al vertice vertex e introducimos los sucesores*/
            Set<T> adjacentVertex = new HashSet<>(getOutwardEdges(vertex));
            //Se agregan los predecesores.
            adjacentVertex.addAll(getInwardEdges(vertex));
            //Se retorna el conjunto como lista.
            return new ArrayList<>(adjacentVertex);
        }
        //Si vertex no pertenece al HashMap se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //Metodo GetAllVertices
    public List<T> getAllVertices() {
        /**Usamos el metodo .keyset() de la clase HashMap para devolver un ArrayList 
         * con todos los elementos dentro del HashMap */
        return new ArrayList<>(adjacencyMap.keySet());
    }

    //Metodo Remove
    public boolean remove(T vertex) {
        //Usamos contains() para verificar que el vertice vertex pertenezca al HashMap.
        if (contains(vertex)) {
            /**Si el vertice vertex pertenece, usamos el metodo .remove() de la clase
             * HashMap para eliminarlo.*/
            adjacencyMap.remove(vertex);
            /**Usamos el metodo .values() de la clase HashMap para iterar sobre todos
             * los valores del HashMap.*/
            for (List<T> sucesores : adjacencyMap.values()) {
                /**Usamos el método .remove() en cada lista de sucesores para eliminar 
                 * el vértice vertex de todas las listas de sucesores.*/
                sucesores.remove(vertex);
            }
            return true;
        }
        //Si el vertice vertex no pertenece se retorna false.
        return false;
    }

    //Metodo Size
    public int size() {
        //Usamos el metodo .size() de la clase HashMap.
        return adjacencyMap.size();
    }

    //Metodo Subgraph
    public Graph<T> subgraph(Collection<T> vertices) {
        //Creamos un nuevo objeto tipo Graph<T>.
        Graph<T> subgraph = new AdjacencyListGraph<>();
        //Iteramos sobre la collection de vertices tipo T.
        for (T vertex : vertices) {
            //Usamos contains para verificar si el vertice vertex pertenece al grafo original.
            if (contains(vertex)) {
                //Si pertenece agregamos el vertice vertex a los vertices del subgrafo.
                subgraph.add(vertex);
                //Usamos el metodo getOutwardEdges para conseguir los sucesores de vertex.
                List<T> sucesores = getOutwardEdges(vertex);
                //Iteramos sobre cada sucesor de vertex.
                for (T sucesor : sucesores) {
                    //Verificamos si la collection vertices contiene a sucesor.
                    if (vertices.contains(sucesor)) {
                        //Si el sucesor pertenece, lo agregamos al subgrafo y conectamos.
                        subgraph.add(sucesor);
                        subgraph.connect(vertex, sucesor);
                    }
                }
            }
        }
        return subgraph;
    }

}

class Comercio {
    private String nombre;
    private String prede;
    private String vis;
    private int time;

    public Comercio(String nombre) {
        this.nombre = nombre;
        this.prede = nombre;
        this.vis = "White";
        this.time = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPred() {
        return prede;
    }

    public String getVis() {
        return vis;
    }

    public int getTime() {
        return time;
    }

    public void changePred(String n) {
        this.prede = n;
    }

    public void changeVis(String n) {
        this.vis = n;
    }

    public void changeTime(int n) {
        this.time = n;
    }
}

public class NextToYou {
    
    public static void dfsVisita(Graph<Comercio> graph) {
        int time = 0;
        for (Comercio comercio : graph.getAllVertices()) {
            if (comercio.getVis() == "White") {
                time = dfs(comercio, time, graph);
            }
        }
    }

    public static int dfs(Comercio v, Integer time, Graph<Comercio> graph) {
        time++;
        v.changeVis("Grey");
        for (Comercio sucesor : graph.getOutwardEdges(v)) {
            if (sucesor.getVis() == "White") {
                time = dfs(sucesor, time, graph);
            }
        }
        v.changeVis("Black");
        time++;
        v.changeTime(time);
        return time;
    }

    public static void simetrico(Graph<Comercio> graph) {
        for (Comercio comercio : graph.getAllVertices()) {
            for (Comercio siguiente : graph.getOutwardEdges(comercio)) {
                graph.disconnect(comercio, siguiente);
                graph.connect(siguiente, comercio);
            }
        }
    }

    public static int[] tiempos(Graph<Comercio> graph) {
        int n = graph.size();
        int[] f = new int[n];
        int i = 0;
        for (Comercio comercio : graph.getAllVertices()) {
            f[i] = comercio.getTime();
            i++;
        }
        Arrays.sort(f);
        return f;
    }

    public static void evilDFSVisita(Graph<Comercio> graph, int[] f) {
        int i = graph.size() - 1;
        while (i > 0) {
            for (Comercio comercio : graph.getAllVertices()) {
                if ((comercio.getVis() == "Black") && (f[i] == comercio.getTime())) {
                    i = evilDFS(comercio, graph, i);
                }
            }
        }
    }

    public static int evilDFS(Comercio v, Graph<Comercio> graph, int i) {
        i--;
        v.changeVis("Grey");
        for (Comercio sucesor : graph.getOutwardEdges(v)) {
            if (sucesor.getVis() == "Black") {
                sucesor.changePred(v.getNombre());
                i = evilDFS(sucesor, graph, i);
            }
        }
        v.changeVis("White");
        return i;
    }

    public static void compConex(Graph<Comercio> graph) {
        dfsVisita(graph);
        simetrico(graph);
        int[] f = tiempos(graph);
        evilDFSVisita(graph, f);
        repartidores(graph);
    }

    public static int numeroCC(Graph<Comercio> graph, Comercio comercio) {
        int sum = 0;
        for (Comercio comercio2 : graph.getAllVertices()) {
            if (comercio2.getPred() == comercio.getNombre() && comercio2.getVis() != "Black") {
                comercio.changeVis("Black");
                sum++;
                sum = sum + numeroCC(graph, comercio2);
            }
        }
        return sum;
    }

    public static void repartidores(Graph<Comercio> graph) {
        int num = 0;
        int total = 0;
        for (Comercio comercio : graph.getAllVertices()) {
            if (comercio.getNombre() == comercio.getPred()) {
                comercio.changeVis("Black");
                num++;
                num = num + numeroCC(graph, comercio);
                if (num < 3) {
                    total = total + 10;
                } else if (2 < num && num < 6) {
                    total = total + 20;
                } else {
                    total = total + 30;
                }
                num = 0;
            }
        }
        System.out.println(total);
    }

    public static void main(String[] args) {
        Graph<Comercio> graph = new AdjacencyListGraph<>();
        try {
            File inputFile = new File("Caracas.txt");
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String lines = scanner.nextLine();
                String[] names = lines.split(", ");

                if (names.length == 2) {
                    Comercio first = new Comercio(names[0]);
                    Comercio second = new Comercio(names[1]);
                    graph.add(first);
                    graph.add(second);
                    graph.connect(first, second);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return;
        }
        
        compConex(graph);
    }
}
