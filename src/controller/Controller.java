package controller;

import lib.Aresta;
import lib.Grafo;
import lib.Vertice;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Controller {
    private Grafo<String> grafo;
    private Grafo<String> grafoMinimo;

    public Controller(){
        this.grafo = new Grafo<>();

        this.grafoMinimo = new Grafo<>();

    }

    public void acresentarCidade(Scanner s){
        System.out.println("Nome da cidade: ");
        String nome = s.nextLine();

        if(grafo.obterVertice(nome) == null){
            if(nome!=null){grafo.adicionarVertice(nome);}
            else {
                System.out.println("Nome não escrito. Cidade não criada");
                return;
            }
        }
        else {
            System.out.println("Cidade já adicionada");
        }
    }

    public void acresentarRota(Scanner s){
        try{
            System.out.println("Qual o nome da primeira cidade?");
            String origem = s.nextLine();

            System.out.println("Qual o nome da segunda cidade?");
            String destino = s.nextLine();

            System.out.println("Qual o peso da rota?");
            String pesoString = s.nextLine();
            float peso = Float.parseFloat(pesoString);

            if(origem.isEmpty() || destino.isEmpty() || pesoString.isEmpty()){
                System.out.println("Algum valor não foi escrito. Rota não criada");
            }
            else {
                grafo.adicionarAresta(origem, destino, peso);
            }

        } catch (NumberFormatException e){
            System.out.println("Erro: Peso invalido");
        }

    }

    public void calcularAGM(){
        grafoMinimo = grafo.calcularAGM();
        System.out.println("\nArvore geradora mínima calculada");
        grafoMinimo.printArestas();

    }

    public void caminhoMinimo(Scanner s){
        System.out.println("Qual o nome da primeira cidade?");
        String origem = s.nextLine();

        System.out.println("Qual o nome da segunda cidade?");
        String destino = s.nextLine();

        grafo.calcularCaminhoMinimo(origem,destino);

    }

    public void caminhoMinimoAGM(Scanner s){
        System.out.println("Qual o nome da primeira cidade?");
        String origem = s.nextLine();

        System.out.println("Qual o nome da segunda cidade?");
        String destino = s.nextLine();

        if (grafoMinimo == null || grafoMinimo.obterVertice(origem) == null || grafoMinimo.obterVertice(destino) == null){
            grafoMinimo = grafo.calcularAGM();
        }
        grafoMinimo.calcularCaminhoMinimo(origem, destino);
    }

    public void lerArquivo(){
        try{
            FileReader f = new FileReader("entrada.txt");
            BufferedReader b = new BufferedReader(f);

            //le o numero de cidades
            int numCidades = Integer.parseInt(b.readLine());
            String[] cidades = new String[numCidades];

            //le as cidades a as adiciona ao grafo
            for(int i = 0; i< numCidades; i++){

                String cidade = b.readLine();
                cidades[i] = cidade;
                this.grafo.adicionarVertice(cidade);
            }

            for(int i = 0; i < numCidades; i++){

                String[] valores = b.readLine().split(",");

                for(int j = 0; j < numCidades; j++){
                    int valor = Integer.parseInt(valores[j].trim());
                    if (valor!=0){this.grafo.adicionarAresta(cidades[i], cidades[j], valor);}
                }
            }

            System.out.println("\nArquivo carregado no sistema");

        } catch (IOException e) {

            System.out.println("Erro na leitura do arquivo");
        }
    }

    public void salvar(){
        salvarMatriz2(this.grafo, "grafoCompleto.txt");
        salvarMatriz2(this.grafoMinimo, "agm.txt");
    }

    public void salvarMatriz(Grafo<String> grafo, String nomeArq) {
        try {
            BufferedWriter b = new BufferedWriter(new FileWriter(nomeArq));
            ArrayList<Vertice<String>> vertices = grafo.getVertices();
            int qtdArestas = grafo.getArestas().size();
            int numCidades = vertices.size();
            float[][] adj = new float[numCidades][numCidades];
            // escreve o numero de cidades
            b.write(numCidades + "\n");

            // escreve as cidades
            for(Vertice v : vertices) {
                String cidade = v.toString();
                b.write(cidade + "\n");
            }

            // escreve a matriz de adjacencias
            for (int i = 0; i < numCidades; i++){
                String cidade = vertices.get(i).toString();
                for (int j = 0; j < qtdArestas; j++) {
                    String origem = grafo.getArestas().get(j).getOrigem().toString();
                    String destino = grafo.getArestas().get(j).getDestino().toString();
                    if (cidade.equals(origem)) {
                        for (int k = 0; k < numCidades; k++) {
                            if (destino.equals(vertices.get(k).toString())) {
                                adj[i][k] = grafo.getArestas().get(j).getPeso();
                            }
                        }
                    }
                    else if (cidade.equals(destino)) {
                        for (int k = 0; k < numCidades; k++) {
                            if (origem.equals(vertices.get(k).toString())) {
                                adj[i][k] = grafo.getArestas().get(j).getPeso();
                            }
                        }
                    }
                }
            }

            // escreve as adjacencias
            for (int i = 0; i < numCidades; i++){

                for (int j = 0; j < numCidades; j++) {
                    b.write(String.valueOf((int) adj[i][j]));
                    if(j < adj[i].length -1) {
                        b.write(",");
                    }
                }
                b.write("\n");
            }

            b.close();


        } catch (IOException e) {
            System.out.println("Erro na escrita do arquivo");

        }
    }

    public void salvarMatriz2(Grafo<String> grafo, String nomeArq) {
        try {
            BufferedWriter b = new BufferedWriter(new FileWriter(nomeArq));
            ArrayList<Vertice<String>> vertices = grafo.getVertices();
            int qtdArestas = grafo.getArestas().size();
            int numCidades = vertices.size();
            float[][] adj = new float[numCidades][numCidades];

            // inicializa a matriz de adjacencias
            for (float[] row : adj) {
                Arrays.fill(row, 0);  // ou outro valor default para indicar ausência de conexão
            }

            // preenche a matriz de adjacencias
            for (Aresta aresta : grafo.getArestas()) {
                int origemIndex = vertices.indexOf(aresta.getOrigem());
                int destinoIndex = vertices.indexOf(aresta.getDestino());
                adj[origemIndex][destinoIndex] = aresta.getPeso();
                adj[destinoIndex][origemIndex] = aresta.getPeso();  // se for grafo não-direcionado
            }

            // escreve o numero de cidades
            b.write(numCidades + "\n");

            // escreve as cidades
            for (Vertice v : vertices) {
                b.write(v.toString() + "\n");
            }

            // escreve a matriz de adjacências
            for (int i = 0; i < numCidades; i++) {
                for (int j = 0; j < numCidades; j++) {
                    b.write(String.valueOf((int) adj[i][j]));
                    if (j < adj[i].length - 1) {
                        b.write(",");
                    }
                }
                b.write("\n");
            }

            b.close();
        } catch (IOException e) {
            System.out.println("Erro na escrita do arquivo");

        }
    }

}
