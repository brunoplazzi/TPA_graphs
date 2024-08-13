package app;

import java.util.Scanner;
import controller.Controller;

public class Main {


    public static void main(String[] args) {

        //implementar biblioteca de grafos
        //métodos mínimos:
        //adicionar um vértice ao grafo
        //adicionar aresta ao grafo
        //calcular árvore geradora mínima
        //calcular caminho mínimo

        //aplicativo

        //carregar arquivo txt

        Menu menu = new Menu();
        Controller controller = new Controller();
        Scanner s = new Scanner(System.in);

        controller.lerArquivo();

        int op = -1;

        while(op != 0){

            try{
                op = menu.escolha();

                if (op == 1) {
                    controller.acresentarCidade(s);

                } else if (op == 2) {
                    controller.acresentarRota(s);

                } else if (op == 3) {
                    controller.calcularAGM();

                } else if (op == 4) {
                    controller.caminhoMinimo(s);

                } else if (op == 5) {
                    controller.caminhoMinimoAGM(s);

                } else if (op == 0) {
                    controller.salvar();
                }

            } catch (NumberFormatException e){
                System.out.println("Formato invalido, tente novamente");
                op = -1;
            }


        }
    }
}