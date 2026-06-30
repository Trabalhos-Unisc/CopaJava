package bolao.service;

import bolao.model.*;

import java.util.List;

public class CalculadorPontuacao implements Calculavel {

    private ConfigPontuacao config;

    public CalculadorPontuacao(ConfigPontuacao config) {
        this.config = config;
    }

    @Override
    public PontuacaoJogo calcular(Palpite palpite, Jogo jogo) {
        if (jogo.getResultadoReal() == null) {
            throw new IllegalStateException("O jogo ainda não foi encerrado com resultado real.");
        }

        int pontosVencedor = calcularPontosVencedor(palpite, jogo);
        int pontosGolsEquipe = calcularPontosGolsEquipe(palpite, jogo);
        int pontosPlacardCompleto = calcularPontosPlacardCompleto(palpite, jogo);
        int pontosGolsAtletas = calcularPontosGolsAtletas(palpite, jogo);

        PontuacaoJogo pontuacao = new PontuacaoJogo(
                jogo,
                palpite.getParticipante(),
                pontosVencedor,
                pontosGolsEquipe,
                pontosPlacardCompleto,
                pontosGolsAtletas
        );

        palpite.setPontuacao(pontuacao);
        return pontuacao;
    }

    private int calcularPontosVencedor(Palpite palpite, Jogo jogo) {
        Placar real = jogo.getResultadoReal();
        Placar palpitado = palpite.getPlacarPalpitado();

        Selecao vencedorReal = real.getVencedor(jogo.getSelecaoA(), jogo.getSelecaoB());
        Selecao vencedorPalpitado = palpitado.getVencedor(jogo.getSelecaoA(), jogo.getSelecaoB());

        // Ambos empate, ou ambos acertaram o vencedor
        boolean acertouEmpate = (vencedorReal == null && vencedorPalpitado == null);
        boolean acertouVencedor = (vencedorReal != null && vencedorReal.equals(vencedorPalpitado));

        return (acertouEmpate || acertouVencedor) ? config.getPontosVencedor() : 0;
    }

    private int calcularPontosGolsEquipe(Palpite palpite, Jogo jogo) {
        Placar real = jogo.getResultadoReal();
        Placar palpitado = palpite.getPlacarPalpitado();

        int pontos = 0;
        if (real.getGolsA() == palpitado.getGolsA()) pontos += config.getPontosGolsUmaEquipe();
        if (real.getGolsB() == palpitado.getGolsB()) pontos += config.getPontosGolsUmaEquipe();
        return pontos;
    }

    private int calcularPontosPlacardCompleto(Palpite palpite, Jogo jogo) {
        return jogo.getResultadoReal().equals(palpite.getPlacarPalpitado())
                ? config.getPontosPlacardCompleto()
                : 0;
    }

    private int calcularPontosGolsAtletas(Palpite palpite, Jogo jogo) {
        List<GolAtleta> golsReais = jogo.getGolsReais();
        List<GolAtleta> golsPalpitados = palpite.getGolsPalpitados();

        int pontos = 0;
        for (GolAtleta palpitadoGol : golsPalpitados) {
            for (GolAtleta realGol : golsReais) {
                if (palpitadoGol.getJogador().getId() == realGol.getJogador().getId()) {
                    // Acertou o atleta — verifica quantidade de gols
                    int golsAcertados = Math.min(palpitadoGol.getQuantidade(), realGol.getQuantidade());
                    double multiplicador = palpitadoGol.getJogador().getMultiplicador();
                    pontos += (int) (golsAcertados * config.getPontosBaseGol() * multiplicador);
                    break;
                }
            }
        }
        return pontos;
    }

    public ConfigPontuacao getConfig() { return config; }
    public void setConfig(ConfigPontuacao config) { this.config = config; }
}
