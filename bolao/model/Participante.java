package bolao.model;

import bolao.service.CalculadorPontuacao;

import java.util.ArrayList;
import java.util.List;

public class Participante extends Usuario {

    private int pontuacaoTotal;
    private List<Palpite> palpites;

    public Participante(String nome, String email, String senha) {
        super(nome, email, senha);
        this.pontuacaoTotal = 0;
        this.palpites = new ArrayList<>();
    }

    public void fazerPalpite(Palpite palpite) {
        if (!palpite.getJogo().isPalpiteAberto()) {
            throw new IllegalStateException("Este jogo não está aberto para palpites.");
        }
        // Evita palpite duplicado no mesmo jogo
        boolean jaPalpitou = palpites.stream()
                .anyMatch(p -> p.getJogo().getId() == palpite.getJogo().getId());
        if (jaPalpitou) {
            throw new IllegalStateException("Você já fez um palpite neste jogo.");
        }
        palpites.add(palpite);
    }

    public void calcularPontuacaoJogo(Jogo jogo, CalculadorPontuacao calculador) {
        palpites.stream()
                .filter(p -> p.getJogo().getId() == jogo.getId())
                .findFirst()
                .ifPresent(p -> {
                    PontuacaoJogo pts = calculador.calcular(p, jogo);
                    pontuacaoTotal += pts.getPontosTotais();
                });
    }

    public List<PontuacaoJogo> consultarPontuacao() {
        List<PontuacaoJogo> lista = new ArrayList<>();
        for (Palpite p : palpites) {
            if (p.getPontuacao() != null) {
                lista.add(p.getPontuacao());
            }
        }
        return lista;
    }

    public List<Palpite> getPalpites() { return new ArrayList<>(palpites); }
    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public void setPontuacaoTotal(int v) { this.pontuacaoTotal = v; }
}
