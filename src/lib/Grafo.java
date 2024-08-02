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
                //System.out.println("Já existe uma aresta com essa origem e esse destino. Nova aresta não adicionada");
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
            System.out.println(a.getOrigem().getValor() + " --- (" +a.getPeso()+ ") ---> " + a.getDestino().getValor());
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

        while (naoRotulados.size() > 0) {
            Vertice<T> atual = naoRotulados.get(0);
            int atualIndex = 0;

            for (int i = 1; i < naoRotulados.size(); i++) {
                if (distancias.get(vertices.indexOf(naoRotulados.get(i))) < distancias.get(atualIndex)) {
                    atual = naoRotulados.get(i);
                    atualIndex = i;
                }
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
                float novaDistancia = distancias.get(vertices.indexOf(atual)) + aresta.getPeso();
                int vizinhoIndex = vertices.indexOf(vizinho);
                if (novaDistancia < distancias.get(vizinhoIndex)) {
                    distancias.set(vizinhoIndex, novaDistancia);
                    noAnterior.set(vizinhoIndex, atual);
                }
            }
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

        // Ordenar todas as arestas por peso
        List<Aresta> arestasOrdenadas = new ArrayList<>(arestas);
        arestasOrdenadas.sort(Comparator.comparingDouble(Aresta::getPeso));

        // Inicializar a estrutura de componentes
        List<List<Vertice<T>>> componentes = new ArrayList<>();
        for (Vertice<T> vertice : vertices) {
            List<Vertice<T>> componente = new ArrayList<>();
            componente.add(vertice);
            componentes.add(componente);
        }

        // Processar as arestas
        float somaTotalPesos = 0;
        for (Aresta aresta : arestasOrdenadas) {
            List<Vertice<T>> compOrigem = encontrarComponente(aresta.getOrigem(), componentes);
            List<Vertice<T>> compDestino = encontrarComponente(aresta.getDestino(), componentes);

            if (compOrigem != compDestino) {
                arvoreGeradoraMinima.adicionarAresta((T) aresta.getOrigem().getValor(), (T) aresta.getDestino().getValor(), aresta.getPeso());
                unirComponentes(compOrigem, compDestino, componentes);
                somaTotalPesos += aresta.getPeso();
            }
        }

        return arvoreGeradoraMinima;
    }

    private List<Vertice<T>> encontrarComponente(Vertice<T> vertice, List<List<Vertice<T>>> componentes) {
        for (List<Vertice<T>> componente : componentes) {
            if (componente.contains(vertice)) {
                return componente;
            }
        }
        return null;
    }

    // Função para unir dois componentes
    private void unirComponentes(List<Vertice<T>> comp1, List<Vertice<T>> comp2, List<List<Vertice<T>>> componentes) {
        comp1.addAll(comp2);
        componentes.remove(comp2);
    }


}
