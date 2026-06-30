package bolao.model;

import bolao.service.CalculadorPontuacao;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {

    private List<Selecao> selecoes;
    private List<Jogo> jogos;
    private List<Participante> participantes;
    private ConfigPontuacao configPontuacao;

    public Administrador(String nome, String email, String senha) {
        super(nome, email, senha);
        this.selecoes = new ArrayList<>();
        this.jogos = new ArrayList<>();
        this.participantes = new ArrayList<>();
        this.configPontuacao = ConfigPontuacao.padrao();
    }

    public void cadastrarSelecao(Selecao selecao) {
        selecoes.add(selecao);
        System.out.println("[Admin] Seleção cadastrada: " + selecao);
    }

    public void cadastrarJogador(Jogador jogador) {
        jogador.getSelecao().addJogador(jogador);
        System.out.println("[Admin] Jogador cadastrado: " + jogador);
    }

    public void cadastrarJogo(Jogo jogo) {
        jogos.add(jogo);
        System.out.println("[Admin] Jogo cadastrado: " + jogo);
    }

    public void abrirJogoParaPalpites(Jogo jogo) {
        jogo.abrirParaPalpites();
        System.out.println("[Admin] Jogo aberto para palpites: " + jogo);
    }

    public void adicionarParticipante(Participante participante) {
        participantes.add(participante);
        System.out.println("[Admin] Participante adicionado: " + participante);
    }

    public void registrarResultado(Jogo jogo, Placar placar, List<GolAtleta> gols,
                                    CalculadorPontuacao calculador) {
        jogo.encerrar(placar, gols);
        System.out.println("[Admin] Resultado registrado: " + jogo.getSelecaoA().getNome()
                + " " + placar + " " + jogo.getSelecaoB().getNome());

        // Calcula pontuação de todos os participantes para este jogo
        for (Participante p : participantes) {
            p.calcularPontuacaoJogo(jogo, calculador);
        }
    }

    public List<RankingItem> gerarRanking() {
        List<Participante> ordenados = new ArrayList<>(participantes);
        ordenados.sort((a, b) -> b.getPontuacaoTotal() - a.getPontuacaoTotal());

        List<RankingItem> ranking = new ArrayList<>();
        for (int i = 0; i < ordenados.size(); i++) {
            Participante p = ordenados.get(i);
            ranking.add(new RankingItem(i + 1, p, p.getPontuacaoTotal()));
        }
        return ranking;
    }

    public List<Selecao> getSelecoes() { return new ArrayList<>(selecoes); }
    public List<Jogo> getJogos() { return new ArrayList<>(jogos); }
    public List<Participante> getParticipantes() { return new ArrayList<>(participantes); }
    public ConfigPontuacao getConfigPontuacao() { return configPontuacao; }
    public void setConfigPontuacao(ConfigPontuacao config) { this.configPontuacao = config; }
}
