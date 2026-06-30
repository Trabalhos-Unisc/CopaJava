package bolao.model;

public class RankingItem {
    private int posicao;
    private Participante participante;
    private int pontuacaoTotal;

    public RankingItem(int posicao, Participante participante, int pontuacaoTotal) {
        this.posicao = posicao;
        this.participante = participante;
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public int getPosicao() { return posicao; }
    public Participante getParticipante() { return participante; }
    public int getPontuacaoTotal() { return pontuacaoTotal; }

    @Override
    public String toString() {
        return posicao + "º " + participante.getNome() + " - " + pontuacaoTotal + " pts";
    }
}
