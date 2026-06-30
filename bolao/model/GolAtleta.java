package bolao.model;

public class GolAtleta {
    private Jogador jogador;
    private int quantidade;

    public GolAtleta(Jogador jogador, int quantidade) {
        this.jogador = jogador;
        this.quantidade = quantidade;
    }

    public Jogador getJogador() { return jogador; }
    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return jogador.getNome() + " (" + quantidade + " gol(s))";
    }
}
