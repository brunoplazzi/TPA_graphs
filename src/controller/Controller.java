package controller;

import lib.Grafo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            grafo.adicionarVertice(nome);
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
        float peso = Float.parseFloat(s.nextLine());

        grafo.adicionarAresta(origem, destino, peso);
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

    }
}
