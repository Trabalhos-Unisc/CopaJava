package bolao.model;

import bolao.model.posicao.Posicao;

public class Jogador {
    private static int contadorId = 1;

    private int id;
    private String nome;
    private int numeroCamisa;
    private Posicao posicao;
    private Selecao selecao;

    public Jogador(String nome, int numeroCamisa, Posicao posicao, Selecao selecao) {
        this.id = contadorId++;
        this.nome = nome;
        this.numeroCamisa = numeroCamisa;
        this.posicao = posicao;
        this.selecao = selecao;
    }

    public double getMultiplicador() {
        return posicao.getMultiplicadorPontuacao();
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getNumeroCamisa() { return numeroCamisa; }
    public Posicao getPosicao() { return posicao; }
    public Selecao getSelecao() { return selecao; }

    @Override
    public String toString() {
        return "#" + numeroCamisa + " " + nome + " (" + posicao.getNome() + ") - " + selecao.getNome();
    }
}
