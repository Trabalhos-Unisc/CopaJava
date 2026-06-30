package bolao.model;

public class PontuacaoJogo {
    private Jogo jogo;
    private Participante participante;
    private int pontosTotais;
    private int pontosVencedor;
    private int pontosGolsEquipe;
    private int pontosGolsAtletas;
    private int pontosPlacardCompleto;

    public PontuacaoJogo(Jogo jogo, Participante participante,
                         int pontosVencedor, int pontosGolsEquipe,
                         int pontosPlacardCompleto, int pontosGolsAtletas) {
        this.jogo = jogo;
        this.participante = participante;
        this.pontosVencedor = pontosVencedor;
        this.pontosGolsEquipe = pontosGolsEquipe;
        this.pontosPlacardCompleto = pontosPlacardCompleto;
        this.pontosGolsAtletas = pontosGolsAtletas;
        this.pontosTotais = pontosVencedor + pontosGolsEquipe + pontosPlacardCompleto + pontosGolsAtletas;
    }

    public String getDetalhes() {
        return "  Vencedor/Empate : +" + pontosVencedor + " pts\n" +
               "  Gols de equipe  : +" + pontosGolsEquipe + " pts\n" +
               "  Placar completo : +" + pontosPlacardCompleto + " pts\n" +
               "  Gols de atletas : +" + pontosGolsAtletas + " pts\n" +
               "  TOTAL           :  " + pontosTotais + " pts";
    }

    public int getPontosTotais() { return pontosTotais; }
    public int getPontosVencedor() { return pontosVencedor; }
    public int getPontosGolsEquipe() { return pontosGolsEquipe; }
    public int getPontosGolsAtletas() { return pontosGolsAtletas; }
    public int getPontosPlacardCompleto() { return pontosPlacardCompleto; }
    public Jogo getJogo() { return jogo; }
    public Participante getParticipante() { return participante; }

    @Override
    public String toString() {
        return "Jogo: " + jogo.getSelecaoA().getNome() + " vs " + jogo.getSelecaoB().getNome()
                + " | Pontos: " + pontosTotais;
    }
}
