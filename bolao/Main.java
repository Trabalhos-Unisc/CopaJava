package bolao;

import bolao.model.*;
import bolao.model.posicao.*;
import bolao.service.CalculadorPontuacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   BOLÃO DA COPA DO MUNDO 2026");
        System.out.println("========================================\n");

        // ── 1. ADMINISTRADOR ──────────────────────────────────────────────
        Administrador admin = new Administrador("Admin Copa", "admin@bolao.com", "admin123");
        System.out.println("Administrador criado: " + admin);
        System.out.println("Autenticação admin: " + admin.autenticar("admin@bolao.com", "admin123"));
        System.out.println();

        // ── 2. CONFIGURAÇÃO DE PONTUAÇÃO ─────────────────────────────────
        ConfigPontuacao config = new ConfigPontuacao(3, 1, 5, 2);
        admin.setConfigPontuacao(config);
        System.out.println("Config de pontuação: " + config);
        System.out.println();

        // ── 3. SELEÇÕES ──────────────────────────────────────────────────
        System.out.println("--- Cadastro de Seleções ---");
        Selecao brasil = new Selecao("Brasil", "Brasil", "A");
        Selecao argentina = new Selecao("Argentina", "Argentina", "A");
        Selecao franca = new Selecao("França", "França", "B");
        admin.cadastrarSelecao(brasil);
        admin.cadastrarSelecao(argentina);
        admin.cadastrarSelecao(franca);
        System.out.println();

        // ── 4. JOGADORES ─────────────────────────────────────────────────
        System.out.println("--- Cadastro de Jogadores ---");

        // Posições (polimorfismo via interface)
        Posicao atacante    = new PosicaoAtacante();     // mult 1.0
        Posicao meioCampo   = new PosicaoMeioCampista(); // mult 1.5
        Posicao defensor    = new PosicaoDefensor();     // mult 2.0
        Posicao goleiro     = new PosicaoGoleiro();      // mult 3.0

        Jogador vini   = new Jogador("Vinícius Jr", 7,  atacante,  brasil);
        Jogador rodrygo= new Jogador("Rodrygo",     11, atacante,  brasil);
        Jogador casemiro=new Jogador("Casemiro",    5,  meioCampo, brasil);
        Jogador marquinhos=new Jogador("Marquinhos",4,  defensor,  brasil);
        Jogador alisson= new Jogador("Alisson",     1,  goleiro,   brasil);

        Jogador messi  = new Jogador("Lionel Messi",10, atacante,  argentina);
        Jogador diMaria= new Jogador("Di María",    11, atacante,  argentina);
        Jogador dePaul = new Jogador("De Paul",     7,  meioCampo, argentina);
        Jogador romero = new Jogador("Romero",      13, defensor,  argentina);

        admin.cadastrarJogador(vini);
        admin.cadastrarJogador(rodrygo);
        admin.cadastrarJogador(casemiro);
        admin.cadastrarJogador(marquinhos);
        admin.cadastrarJogador(alisson);
        admin.cadastrarJogador(messi);
        admin.cadastrarJogador(diMaria);
        admin.cadastrarJogador(dePaul);
        admin.cadastrarJogador(romero);
        System.out.println();

        // ── 5. JOGO ──────────────────────────────────────────────────────
        System.out.println("--- Cadastro de Jogo ---");
        Jogo jogo1 = new Jogo(brasil, argentina,
                LocalDateTime.of(2026, 6, 20, 21, 0), "Fase de Grupos");
        admin.cadastrarJogo(jogo1);
        admin.abrirJogoParaPalpites(jogo1);
        System.out.println();

        // ── 6. PARTICIPANTES ─────────────────────────────────────────────
        System.out.println("--- Cadastro de Participantes ---");
        Participante joao  = new Participante("João",   "joao@email.com",   "1234");
        Participante maria = new Participante("Maria",  "maria@email.com",  "5678");
        Participante pedro = new Participante("Pedro",  "pedro@email.com",  "9012");
        admin.adicionarParticipante(joao);
        admin.adicionarParticipante(maria);
        admin.adicionarParticipante(pedro);
        System.out.println();

        // ── 7. PALPITES ──────────────────────────────────────────────────
        System.out.println("--- Palpites dos Participantes ---");

        // João: Brasil 2x1 Argentina, Vini e Casemiro marcam
        Palpite palpiteJoao = new Palpite(
                joao, jogo1,
                new Placar(2, 1),
                Arrays.asList(new GolAtleta(vini, 1), new GolAtleta(casemiro, 1))
        );
        joao.fazerPalpite(palpiteJoao);
        System.out.println("Palpite João: " + palpiteJoao);

        // Maria: Brasil 1x1 Argentina, Messi marca
        Palpite palpiteMaria = new Palpite(
                maria, jogo1,
                new Placar(1, 1),
                Arrays.asList(new GolAtleta(messi, 1))
        );
        maria.fazerPalpite(palpiteMaria);
        System.out.println("Palpite Maria: " + palpiteMaria);

        // Pedro: Brasil 3x0 Argentina, Rodrygo 2 gols, Marquinhos 1
        Palpite palpitePedro = new Palpite(
                pedro, jogo1,
                new Placar(3, 0),
                Arrays.asList(new GolAtleta(rodrygo, 2), new GolAtleta(marquinhos, 1))
        );
        pedro.fazerPalpite(palpitePedro);
        System.out.println("Palpite Pedro: " + palpitePedro);
        System.out.println();

        // Teste: palpite duplicado deve lançar exceção
        System.out.println("--- Testando palpite duplicado ---");
        try {
            Palpite duplicado = new Palpite(joao, jogo1, new Placar(0, 0), new ArrayList<>());
            joao.fazerPalpite(duplicado);
        } catch (IllegalStateException e) {
            System.out.println("Exceção capturada corretamente: " + e.getMessage());
        }
        System.out.println();

        // ── 8. RESULTADO REAL ─────────────────────────────────────────────
        System.out.println("--- Registrando Resultado Real ---");
        // Resultado: Brasil 2x1 Argentina | Vini 1 gol, Messi 1 gol
        Placar resultadoReal = new Placar(2, 1);
        List<GolAtleta> golsReais = Arrays.asList(
                new GolAtleta(vini, 1),
                new GolAtleta(messi, 1)
        );

        CalculadorPontuacao calculador = new CalculadorPontuacao(config);
        admin.registrarResultado(jogo1, resultadoReal, golsReais, calculador);
        System.out.println();

        // ── 9. CONSULTA DE PONTUAÇÃO ─────────────────────────────────────
        System.out.println("========================================");
        System.out.println("         PONTUAÇÃO DETALHADA");
        System.out.println("========================================");

        imprimirPontuacao(joao);
        imprimirPontuacao(maria);
        imprimirPontuacao(pedro);

        // ── 10. RANKING ──────────────────────────────────────────────────
        System.out.println("========================================");
        System.out.println("             RANKING FINAL");
        System.out.println("========================================");
        List<RankingItem> ranking = admin.gerarRanking();
        ranking.forEach(r -> System.out.println(r));
        System.out.println();

        // ── 11. DEMONSTRAÇÃO DOS MULTIPLICADORES (POLIMORFISMO) ──────────
        System.out.println("========================================");
        System.out.println("   MULTIPLICADORES POR POSIÇÃO (POO)");
        System.out.println("========================================");
        List<Posicao> posicoes = Arrays.asList(
                new PosicaoAtacante(), new PosicaoMeioCampista(),
                new PosicaoDefensor(), new PosicaoGoleiro()
        );
        for (Posicao p : posicoes) {
            System.out.printf("%-15s → multiplicador %.1fx (gol = %.0f pts)%n",
                    p.getNome(), p.getMultiplicadorPontuacao(),
                    p.getMultiplicadorPontuacao() * config.getPontosBaseGol());
        }
    }

    private static void imprimirPontuacao(Participante p) {
        System.out.println("\n>> " + p.getNome().toUpperCase());
        List<PontuacaoJogo> pontuacoes = p.consultarPontuacao();
        if (pontuacoes.isEmpty()) {
            System.out.println("  Nenhum jogo pontuado ainda.");
        } else {
            for (PontuacaoJogo pts : pontuacoes) {
                System.out.println("  Jogo: " + pts.getJogo().getSelecaoA().getNome()
                        + " vs " + pts.getJogo().getSelecaoB().getNome());
                System.out.println(pts.getDetalhes());
            }
        }
        System.out.println("  Pontuação total acumulada: " + p.getPontuacaoTotal() + " pts");
    }
}
