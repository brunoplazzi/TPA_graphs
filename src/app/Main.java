package app;
import java.util.Scanner;

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

        int op = -1;

        while(op != 0){

            op = menu.escolha();

            if (op == 1) {
                //acrescentar cidade
                System.out.println("to implement...\n");

            } else if (op == 2) {
                //acrescentar rota
                System.out.println("to implement...\n");

            } else if (op == 3) {
                //calcular árvore geradora mínima (AGM)
                System.out.println("to implement...\n");

            } else if (op == 4) {
                //calcular caminho mínimo entre duas cidades
                System.out.println("to implement...\n");

            } else if (op == 5) {
                //calcular caminho mínimo entre duas cidades considerando apenas a AGM
                System.out.println("to implement...\n");

            } else if (op == 0) {
                //gravar e sair
                System.out.println("to implement...\n");
            }
        }
    }
}