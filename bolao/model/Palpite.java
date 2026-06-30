package bolao.model;

import java.util.ArrayList;
import java.util.List;

public class Palpite {
    private static int contadorId = 1;

    private int id;
    private Participante participante;
    private Jogo jogo;
    private Placar placarPalpitado;
    private List<GolAtleta> golsPalpitados;
    private PontuacaoJogo pontuacao;

    public Palpite(Participante participante, Jogo jogo,
                   Placar placarPalpitado, List<GolAtleta> golsPalpitados) {
        this.id = contadorId++;
        this.participante = participante;
        this.jogo = jogo;
        this.placarPalpitado = placarPalpitado;
        this.golsPalpitados = new ArrayList<>(golsPalpitados);
    }

    public void setPontuacao(PontuacaoJogo pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getId() { return id; }
    public Participante getParticipante() { return participante; }
    public Jogo getJogo() { return jogo; }
    public Placar getPlacarPalpitado() { return placarPalpitado; }
    public List<GolAtleta> getGolsPalpitados() { return new ArrayList<>(golsPalpitados); }
    public PontuacaoJogo getPontuacao() { return pontuacao; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Palpite #").append(id)
          .append(" | ").append(participante.getNome())
          .append(" | ").append(jogo.getSelecaoA().getNome())
          .append(" ").append(placarPalpitado)
          .append(" ").append(jogo.getSelecaoB().getNome());
        if (!golsPalpitados.isEmpty()) {
            sb.append(" | Gols: ");
            golsPalpitados.forEach(g -> sb.append(g).append("; "));
        }
        return sb.toString();
    }
}
