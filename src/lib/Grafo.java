package lib;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grafo<T> {
    private ArrayList<Vertice<T>> vertices;
    private ArrayList<Aresta> arestas;

    public Grafo() {
        this.vertices = new ArrayList<>();
        this.arestas = new ArrayList<>();
    }

    public Vertice<T> adicionarVertice(T valor) {
        Vertice<T> novo = new Vertice<T>(valor);
        this.vertices.add(novo);
        System.out.println("Vertice |" + valor + "| adicionado com sucesso");
        return novo;
    }

    public Vertice<T> obterVertice(T valor) {
        for (Vertice<T> v : this.vertices) {
            if (v.getValor().equals(valor)) {
                return v;
            }
        }
        return null;
    }

    public void excluirVertice(T valor) {
        Vertice<T> v = obterVertice(valor);
        if (v != null) {
            this.vertices.remove(v);

            ArrayList<Aresta> destinos = obterDestinos(v);
            for (Aresta a : destinos) {
                this.arestas.remove(a);
            }
            System.out.println("Vértice removido");
        } else {
            System.out.println("Vértice não encontrado");
        }
    }

    public void excluirAresta(T origem, T destino, float peso) {
        Iterator<Aresta> iterator = this.arestas.iterator();
        while (iterator.hasNext()) {
            Aresta a = iterator.next();
            if ((a.getOrigem().getValor().equals(origem) && a.getDestino().getValor().equals(destino) && a.getPeso() == peso) ||
                    (a.getOrigem().getValor().equals(destino) && a.getDestino().getValor().equals(origem) && a.getPeso() == peso)) {
                iterator.remove();
                System.out.println("Aresta removida");
                return;
            }
        }
        System.out.println("Aresta não encontrada");
    }

    public void adicionarAresta(T origem, T destino, float peso) {
        Vertice<T> verticeOrigem = obterVertice(origem);
        Vertice<T> verticeDestino = obterVertice(destino);

        if (verticeOrigem == null) {
            verticeOrigem = adicionarVertice(origem);
        }
        if (verticeDestino == null) {
            verticeDestino = adicionarVertice(destino);
        }

        // Verifica se a aresta já existe em qualquer direção
        for (Aresta a : arestas) {
            if ((a.getOrigem().equals(verticeOrigem) && a.getDestino().equals(verticeDestino)) ||
                    (a.getOrigem().equals(verticeDestino) && a.getDestino().equals(verticeOrigem))) {
                System.out.println("Já existe uma aresta entre " + origem + " e " + destino + ". Nova aresta não adicionada.");
                return;
            }
        }

        // Adiciona a aresta apenas uma vez para um grafo não direcionado
        Aresta novaAresta = new Aresta(verticeOrigem, verticeDestino, peso);
        this.arestas.add(novaAresta);
        System.out.println("Aresta |" + origem + " --- (" + peso + ") --- " + destino + "| adicionada com sucesso");
    }

    public Aresta obterAresta(T origem, T destino) {
        for (Aresta a : this.arestas) {
            if ((a.getOrigem().getValor().equals(origem) && a.getDestino().getValor().equals(destino)) ||
                    (a.getOrigem().getValor().equals(destino) && a.getDestino().getValor().equals(origem))) {
                return a;
            }
        }
        return null;
    }

    private ArrayList<Aresta> obterDestinos(Vertice<T> v) {
        ArrayList<Aresta> destinos = new ArrayList<>();
        for (Aresta atual : this.arestas) {
            if (atual.getOrigem().equals(v) || atual.getDestino().equals(v)) {
                destinos.add(atual);
            }
        }
        return destinos;
    }

    public void buscaEmLargura() {
        ArrayList<Vertice<T>> marcados = new ArrayList<>();
        ArrayList<Vertice<T>> fila = new ArrayList<>();
        Vertice<T> atual = this.vertices.get(0);
        fila.add(atual);

        while (!fila.isEmpty()) {
            atual = fila.get(0);
            fila.remove(0);
            marcados.add(atual);
            System.out.println(atual.getValor());

            ArrayList<Aresta> destinos = this.obterDestinos(atual);
            for (Aresta a : destinos) {
                Vertice<T> prox = a.getDestino();
                if (!marcados.contains(prox) && !fila.contains(prox)) {
                    fila.add(prox);
                }
            }
        }
    }

    public void printArestas() {
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

        // Inicializar distâncias e predecessores
        Map<Vertice<T>, Float> distancias = new HashMap<>();
        Map<Vertice<T>, Vertice<T>> predecessores = new HashMap<>();
        PriorityQueue<Vertice<T>> fila = new PriorityQueue<>(Comparator.comparing(distancias::get));

        for (Vertice<T> vertice : vertices) {
            distancias.put(vertice, Float.MAX_VALUE);
            predecessores.put(vertice, null);
        }
        distancias.put(verticeOrigem, 0f);
        fila.add(verticeOrigem);

        while (!fila.isEmpty()) {
            Vertice<T> atual = fila.poll();

            if (atual.equals(verticeDestino)) {
                break;
            }

            for (Aresta aresta : obterDestinos(atual)) {
                Vertice<T> vizinho = aresta.getDestino();
                float novaDistancia = distancias.get(atual) + aresta.getPeso();

                // Verificar se a aresta é bidirecional
                if (distancias.get(vizinho) > novaDistancia) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }

                // Verifica a aresta na direção oposta para o grafo não direcionado
                vizinho = aresta.getOrigem();
                novaDistancia = distancias.get(atual) + aresta.getPeso();
                if (distancias.get(vizinho) > novaDistancia) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        // Reconstruir o caminho
        List<Vertice<T>> caminho = new ArrayList<>();
        for (Vertice<T> v = verticeDestino; v != null; v = predecessores.get(v)) {
            caminho.add(v);
        }
        Collections.reverse(caminho);

        // Imprimir o caminho e a distância total
        System.out.println("Caminho mínimo de " + origem + " para " + destino + ":");
        if (distancias.get(verticeDestino) == Float.MAX_VALUE) {
            System.out.println("Não há caminho possível.");
        } else {
            for (Vertice<T> v : caminho) {
                System.out.print(v.getValor() + " -> ");
            }
            System.out.println("\nDistância total: " + distancias.get(verticeDestino));
        }
    }


    public Grafo<T> calcularAGM() {
        Grafo<T> arvoreGeradoraMinima = new Grafo<>();


        for (Vertice<T> vertice : vertices) {
            arvoreGeradoraMinima.adicionarVertice(vertice.getValor());
        }

        // Ordenar por peso
        List<Aresta> arestasOrdenadas = new ArrayList<>(arestas);
        arestasOrdenadas.sort(Comparator.comparingDouble(Aresta::getPeso));


        Map<Vertice<T>, List<Vertice<T>>> componentes = new HashMap<>();
        for (Vertice<T> vertice : vertices) {
            List<Vertice<T>> componente = new ArrayList<>();
            componente.add(vertice);
            componentes.put(vertice, componente);
        }


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
