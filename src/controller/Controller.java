package controller;

import lib.Aresta;
import lib.Grafo;
import lib.Vertice;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
        System.out.println("Qual o nome da cidade origem?");
        String origem = s.nextLine();

        System.out.println("Qual o nome da cidade destino?");
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
    }

    public void calcularAGM(){
        grafoMinimo = grafo.calcularAGM();
        System.out.println("Arvore geradora mínima calculada");
        grafoMinimo.printArestas();

    }

    public void caminhoMinimo(Scanner s){
        System.out.println("Qual o nome da cidade origem?");
        String origem = s.nextLine();

        System.out.println("Qual o nome da cidade destino?");
        String destino = s.nextLine();

        grafo.calcularCaminhoMinimo(origem,destino);

    }

    public void caminhoMinimoAGM(Scanner s){
        System.out.println("Qual o nome da cidade origem?");
        String origem = s.nextLine();

        System.out.println("Qual o nome da cidade destino?");
        String destino = s.nextLine();

        if(grafoMinimo==null){
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

            System.out.println("Arquivo carregado no sistema");

        } catch (IOException e) {

            System.out.println("Erro na leitura do arquivo");
        }

    }

    public void salvarArquivo(){
        try {
            BufferedWriter b = new BufferedWriter(new FileWriter("grafoCompleto.txt"));
            ArrayList<Vertice<String>> vertices = grafo.getVertices();
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
            for(int i = 0; i < numCidades; i++){
                String cidade = vertices.get(i).toString();

                for (int j = 0; j < numCidades; j++) {
                    String origem = grafo.getArestas().get(j).getOrigem().toString();
                    String destino = grafo.getArestas().get(j).getDestino().toString();

                    if(cidade.equals(origem) || cidade.equals(destino)) {
                        adj[i][j] = grafo.getArestas().get(j).getPeso();
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

            BufferedWriter bAGM = new BufferedWriter(new FileWriter("agm.txt"));
            float[][] adjAGM = new float[numCidades][numCidades];

            // escrever a quantidade de cidades
            bAGM.write(grafoMinimo.getArestas().size() + "\n");

            // escrever as cidades
            for (Vertice v : grafoMinimo.getVertices()) {
                bAGM.write(v.getValor() + "\n");
            }


            // escreve a matriz de adjacencias
            for(int i = 0; i < numCidades; i++){
                String cidade = vertices.get(i).toString();

                for (int j = 0; j < grafoMinimo.getArestas().size(); j++) {
                    String origem = grafoMinimo.getArestas().get(j).getOrigem().toString();
                    String destino = grafoMinimo.getArestas().get(j).getDestino().toString();

                    if(cidade.equals(origem) || cidade.equals(destino)) {
                        adjAGM[i][j] = grafoMinimo.getArestas().get(j).getPeso();
                    }
                }
            }

            // escreve as adjacencias
            for (int i = 0; i < numCidades; i++){

                for (int j = 0; j < numCidades; j++) {
                    bAGM.write(String.valueOf((int) adjAGM[i][j]));
                    if(j < adjAGM[i].length -1) {
                        bAGM.write(",");
                    }
                }
                bAGM.write("\n");
            }

            bAGM.close();

        } catch (IOException e) {
            System.out.println("Erro na escrita do arquivo");
        }

    }
}
