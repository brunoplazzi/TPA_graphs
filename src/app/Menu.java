package app;

import java.util.Scanner;

public class Menu {

    String strMenu;
    int op = -1;

    public Menu(){

        strMenu =
                """ 
                
                MENU____________________________________________________________________
                (1) Acrescentar cidade
                (2) Acrescentar rota
                (3) Calcular árvore geradora mínima (AGM)
                (4) Calcular caminho mínimo entre duas cidades
                (5) Calcular caminho mínimo entre duas cidades considerando apenas a AGM
                (0) Gravar e Sair
                
                Digite o número da opção desejada:
                """;
    }

    public int escolha(){

        Scanner s = new Scanner(System.in);

        System.out.println(strMenu);
        op = Integer.parseInt(s.nextLine());

        while(op < 0 || op > 5){
            System.out.println("OPÇÃO INVÁLIDA");
            System.out.println(strMenu);
            op = Integer.parseInt(s.nextLine());
        }

        return op;
    }
}
