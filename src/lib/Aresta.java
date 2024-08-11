package lib;

public class Aresta{
    private Vertice origem, destino;
    private float peso;

    public Aresta(Vertice o, Vertice d, float peso){
        this.origem = o;
        this.destino = d;
        this.peso = peso;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public float getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return this.getOrigem().getValor() + " --- (" +this.getPeso()+ ") --- " + this.getDestino().getValor();
    }
}
