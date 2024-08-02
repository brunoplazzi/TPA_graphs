package lib;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Grafo <T> {
    private ArrayList<Vertice<T>> vertices;
    private ArrayList<Aresta> arestas;

    public Grafo(){
        this.vertices = new ArrayList<>();
        this.arestas = new ArrayList<>();
    }


    public Vertice<T> adicionarVertice(T valor){
        Vertice<T> novo = new Vertice<T>(valor);
        this.vertices.add(novo);
        System.out.println("Vertice |"+ valor + "| adicionado com sucesso");
        return novo;
    }

    public Vertice<T> obterVertice(T valor){
        Vertice<T> v;
        for(int i=0; i<this.vertices.size(); i++){
            v = this.vertices.get(i);
            if(v.getValor().equals(valor))
                return v;
        }
        return null;
    }

    public void excluirVertice(T valor){
        Vertice v = obterVertice(valor);
        if (v!= null){
            this.vertices.remove(v);

            ArrayList<Aresta> destinos = obterDestinos(v);
            for(Aresta a : destinos){
                this.arestas.remove(a);
            }
            System.out.println("Vertice removido");
        }
        System.out.println("Vertice nao encontrado");

    }

    public void excluirAresta(T origem, T destino, float rota){
        int cont = 0;
        for(Aresta a : this.arestas){
            if(a.getOrigem()==origem && a.getDestino()==destino && a.getPeso()==rota){
                this.arestas.remove(a);
                cont++;
                break;
            }
        }
        if(cont == 0){System.out.println("Aresta nao encontrada");}
        else {System.out.println("Aresta removida");}

    }

    public void adicionarAresta(T origem, T destino, float peso){
        Vertice<T> verticeOrigem, verticeDestino;
        verticeOrigem = obterVertice(origem);
        verticeDestino = obterVertice(destino);
        Aresta novaAresta = new Aresta(verticeOrigem, verticeDestino, peso);

        for (Aresta a: arestas){
            if((a.getOrigem()==verticeOrigem && a.getDestino()==verticeDestino)||(a.getOrigem()==verticeDestino && a.getDestino()==verticeOrigem && a.getPeso()==peso)){
                System.out.println("Já existe uma aresta com essa origem e esse destino. Nova aresta não adicionada");
                return;
            }
        }

        if (verticeOrigem == null){
            verticeOrigem = adicionarVertice(origem);
        }

        verticeDestino = obterVertice(destino);
        if(verticeDestino == null){
            verticeDestino = adicionarVertice(destino);
        }

        this.arestas.add(novaAresta);
        System.out.println("Aresta |"+ origem + " --- (" +peso+ ") ---> " + destino + "| adicionada com sucesso");
    }

    public Aresta obterAresta(T origem, T destino){
        for(Aresta a: this.arestas){
            if((a.getOrigem().getValor().equals(origem) && a.getDestino().getValor().equals(destino))){
                return a;
            }
        }
        return null;
    }

    private ArrayList<Aresta> obterDestinos(Vertice<T> v){
        ArrayList<Aresta> destinos = new ArrayList<Aresta>();
        Aresta atual;
        for(int i = 0; i<this.arestas.size(); i++){
            atual = this.arestas.get(i);
            if(atual.getOrigem().equals(v)){
                destinos.add(atual);
            }

        }
        return destinos;
    }

    public void buscaEmLargura(){
        ArrayList<Vertice> marcados = new ArrayList<>();
        ArrayList<Vertice> fila = new ArrayList<>();
        Vertice atual = this.vertices.get(0);
        fila.add(atual);

        while(!fila.isEmpty()){
            atual = fila.get(0);
            fila.remove(0);
            marcados.add(atual);
            System.out.println(atual.getValor());

            ArrayList<Aresta> destinos = this.obterDestinos(atual);
            Vertice prox;
            for (int i=0; i<destinos.size(); i++) {
                prox = destinos.get(i).getDestino();
                if (!marcados.contains(prox) && !fila.contains(prox)) {
                    fila.add(prox);
                }
            }
        }
    }

    public void printArestas(){
        System.out.println("\nArestas do grafo:");
        for (Aresta a : this.getArestas()) {
            System.out.println(a.toString());
        }
    }

    public ArrayList<Vertice<T>> getVertices() {
        return vertices;
    }

    public ArrayList<Aresta> getArestas() {
        return arestas;
    }


    public void calcularCaminhoMinimo(T origem, T destino) {
        Vertice<T> verticeOrigem = obterVertice(origem);
        Vertice<T> verticeDestino = obterVertice(destino);

        if (verticeOrigem == null || verticeDestino == null) {
            System.out.println("Vértice não encontrado.");
            return;
        }

        ArrayList<Vertice<T>> naoRotulados = new ArrayList<>(vertices);
        ArrayList<Float> distancias = new ArrayList<>(Collections.nCopies(vertices.size(), Float.MAX_VALUE));
        ArrayList<Vertice<T>> noAnterior = new ArrayList<>(Collections.nCopies(vertices.size(), null));

        int origemIndex = vertices.indexOf(verticeOrigem);
        distancias.set(origemIndex, 0f);

        while (!naoRotulados.isEmpty()) {
            Vertice<T> atual = null;
            float menorDistancia = Float.MAX_VALUE;

            // Encontrar o vértice com a menor distância
            for (Vertice<T> vertice : naoRotulados) {
                int index = vertices.indexOf(vertice);
                if (distancias.get(index) < menorDistancia) {
                    menorDistancia = distancias.get(index);
                    atual = vertice;
                }
            }

            if (atual == null || menorDistancia == Float.MAX_VALUE) {
                break; // Não há mais vértices acessíveis
            }

            naoRotulados.remove(atual);

            // Se o vértice atual for o destino, interrompe
            if (atual.equals(verticeDestino)) {
                break;
            }

            // Atualiza distâncias dos vizinhos
            ArrayList<Aresta> destinos = obterDestinos(atual);
            for (Aresta aresta : destinos) {
                Vertice<T> vizinho = aresta.getDestino();
                int vizinhoIndex = vertices.indexOf(vizinho);
                float novaDistancia = distancias.get(vertices.indexOf(atual)) + aresta.getPeso();
                if (novaDistancia < distancias.get(vizinhoIndex)) {
                    distancias.set(vizinhoIndex, novaDistancia);
                    noAnterior.set(vizinhoIndex, atual);
                }
            }
        }

        // Verifica se foi possível chegar ao destino
        if (distancias.get(vertices.indexOf(verticeDestino)) == Float.MAX_VALUE) {
            System.out.println("Não há caminho possível de " + origem + " para " + destino + ".");
            return;
        }

        // Imprime o caminho
        List<Vertice<T>> caminho = new ArrayList<>();
        for (Vertice<T> v = verticeDestino; v != null; v = noAnterior.get(vertices.indexOf(v))) {
            caminho.add(v);
        }
        Collections.reverse(caminho);

        System.out.println("Caminho mínimo de " + origem + " para " + destino + ":");
        float distanciaTotal = distancias.get(vertices.indexOf(verticeDestino));
        for (Vertice<T> v : caminho) {
            System.out.print(v.getValor() + " ");
        }
        System.out.println("\nDistância total: " + distanciaTotal);
    }

    public Grafo<T> calcularAGM() {
        Grafo<T> arvoreGeradoraMinima = new Grafo<>();

        // Inicializar vértices na AGM
        for (Vertice<T> vertice : vertices) {
            arvoreGeradoraMinima.adicionarVertice(vertice.getValor());
        }

        // Ordenar todas as arestas por peso
        List<Aresta> arestasOrdenadas = new ArrayList<>(arestas);
        arestasOrdenadas.sort(Comparator.comparingDouble(Aresta::getPeso));

        // Inicializar a estrutura de componentes
        Map<Vertice<T>, List<Vertice<T>>> componentes = new HashMap<>();
        for (Vertice<T> vertice : vertices) {
            List<Vertice<T>> componente = new ArrayList<>();
            componente.add(vertice);
            componentes.put(vertice, componente);
        }

        // Processar as arestas
        for (Aresta aresta : arestasOrdenadas) {
            Vertice<T> origem = aresta.getOrigem();
            Vertice<T> destino = aresta.getDestino();

            if (origem == null || destino == null) {
                continue; // Ignorar arestas com vértices nulos
            }

            List<Vertice<T>> compOrigem = componentes.get(origem);
            List<Vertice<T>> compDestino = componentes.get(destino);

            if (compOrigem != compDestino) {
                arvoreGeradoraMinima.adicionarAresta(origem.getValor(), destino.getValor(), aresta.getPeso());
                unirComponentes(compOrigem, compDestino, componentes);
            }
        }

        return arvoreGeradoraMinima;
    }

    private void unirComponentes(List<Vertice<T>> comp1, List<Vertice<T>> comp2, Map<Vertice<T>, List<Vertice<T>>> componentes) {
        comp1.addAll(comp2);
        for (Vertice<T> vertice : comp2) {
            componentes.put(vertice, comp1);
        }
    }
}
