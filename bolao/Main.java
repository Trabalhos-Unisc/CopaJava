package bolao;

import bolao.model.*;
import bolao.model.posicao.*;
import bolao.service.CalculadorPontuacao;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Administrador admin;
    static CalculadorPontuacao calculador;
    static Participante participanteLogado = null;

    public static void main(String[] args) {
        inicializarSistema();

        while (true) {
            if (participanteLogado == null) {
                menuInicial();
            } else {
                menuParticipante();
            }
        }
    }

    // ══════════════════════════════════════════════════
    // DADOS INICIAIS
    // ══════════════════════════════════════════════════
    static void inicializarSistema() {
        admin = new Administrador("Admin", "admin@bolao.com", "admin123");
        calculador = new CalculadorPontuacao(admin.getConfigPontuacao());

        Posicao atc = new PosicaoAtacante();
        Posicao mei = new PosicaoMeioCampista();
        Posicao def = new PosicaoDefensor();
        Posicao gol = new PosicaoGoleiro();

        Selecao brasil    = new Selecao("Brasil",    "Brasil",    "A");
        Selecao argentina = new Selecao("Argentina", "Argentina", "A");

        admin.cadastrarSelecao(brasil);
        admin.cadastrarSelecao(argentina);

        admin.cadastrarJogador(new Jogador("Vinícius Jr",  7,  atc, brasil));
        admin.cadastrarJogador(new Jogador("Rodrygo",      11, atc, brasil));
        admin.cadastrarJogador(new Jogador("Casemiro",     5,  mei, brasil));
        admin.cadastrarJogador(new Jogador("Marquinhos",   4,  def, brasil));
        admin.cadastrarJogador(new Jogador("Alisson",      1,  gol, brasil));

        admin.cadastrarJogador(new Jogador("Messi",        10, atc, argentina));
        admin.cadastrarJogador(new Jogador("Di María",     11, atc, argentina));
        admin.cadastrarJogador(new Jogador("De Paul",      7,  mei, argentina));
        admin.cadastrarJogador(new Jogador("Romero",       13, def, argentina));
        admin.cadastrarJogador(new Jogador("E. Martínez",  23, gol, argentina));

        Jogo j1 = new Jogo(brasil, argentina, LocalDateTime.of(2026, 6, 20, 21, 0), "Fase de Grupos");
        admin.cadastrarJogo(j1);
        admin.abrirJogoParaPalpites(j1);

        // Participantes do bolão
        admin.adicionarParticipante(new Participante("João",   "joao@bolao.com",   "1234"));
        admin.adicionarParticipante(new Participante("Maria",  "maria@bolao.com",  "1234"));
        admin.adicionarParticipante(new Participante("Pedro",  "pedro@bolao.com",  "1234"));
    }

    // ══════════════════════════════════════════════════
    // MENU INICIAL — digita o nome para entrar
    // ══════════════════════════════════════════════════
    static void menuInicial() {
        linha();
        System.out.println("   BOLÃO DA COPA DO MUNDO 2026");
        linha();
        System.out.print(" Digite seu nome para entrar (ou 0 para sair): ");
        String nome = sc.nextLine().trim();

        if (nome.equals("0")) { System.out.println("Até logo!"); System.exit(0); }

        Optional<Participante> encontrado = admin.getParticipantes().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();

        if (encontrado.isPresent()) {
            participanteLogado = encontrado.get();
            System.out.println(" Bem-vindo, " + participanteLogado.getNome() + "!");
        } else {
            System.out.println(" Participante \"" + nome + "\" não encontrado.");
        }
    }

    // ══════════════════════════════════════════════════
    // MENU DO PARTICIPANTE
    // ══════════════════════════════════════════════════
    static void menuParticipante() {
        linha();
        System.out.println("   " + participanteLogado.getNome().toUpperCase());
        linha();
        System.out.println(" 1. Fazer palpite");
        System.out.println(" 2. Ver meus palpites");
        System.out.println(" 3. Ver minha pontuação");
        System.out.println(" 4. Ver ranking");
        System.out.println(" 0. Sair (trocar de usuário)");
        linha();
        System.out.print(" Opção: ");
        String op = sc.nextLine().trim();

        switch (op) {
            case "1":
                fazerPalpite();
                break;
            case "2":
                verPalpites();
                break;
            case "3":
                verPontuacao();
                break;
            case "4":
                exibirRanking();
                break;
            case "0":
                participanteLogado = null;
                System.out.println(" Até logo!");
                break;
            default:
                System.out.println(" Opção inválida.");
                break;
        }
    }

    // ══════════════════════════════════════════════════
    // FAZER PALPITE
    // ══════════════════════════════════════════════════
    static void fazerPalpite() {
        List<Jogo> disponiveis = admin.getJogos().stream()
                .filter(j -> j.getStatus() == StatusJogo.ABERTO_PARA_PALPITES)
                .filter(j -> participanteLogado.getPalpites().stream()
                        .noneMatch(p -> p.getJogo().getId() == j.getId()))
                .collect(Collectors.toList()); // Alterado de .toList() para .collect(...)

        if (disponiveis.isEmpty()) {
            System.out.println("\n Nenhum jogo disponível para palpite.");
            return;
        }

        // Escolhe o jogo
        System.out.println("\n Jogos disponíveis:");
        for (int i = 0; i < disponiveis.size(); i++) {
            Jogo j = disponiveis.get(i);
            System.out.println("  " + (i + 1) + ". "
                    + j.getSelecaoA().getNome() + " x " + j.getSelecaoB().getNome()
                    + " [" + j.getFase() + "]");
        }
        System.out.print(" Escolha o jogo: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= disponiveis.size()) { System.out.println(" Inválido."); return; }
        Jogo jogo = disponiveis.get(idx);

        String nomeA = jogo.getSelecaoA().getNome();
        String nomeB = jogo.getSelecaoB().getNome();

        // Quem vai vencer?
        System.out.println("\n Quem vai vencer?");
        System.out.println("  1. " + nomeA);
        System.out.println("  2. " + nomeB);
        System.out.println("  3. Empate");
        System.out.print(" Escolha: ");
        int vencedor = lerInt();
        if (vencedor < 1 || vencedor > 3) { System.out.println(" Inválido."); return; }

        // Placar baseado na escolha
        int golsA, golsB;
        if (vencedor == 1) {
            System.out.print("\n Quantos gols " + nomeA + " vai fazer? ");
            golsA = lerInt();
            System.out.print(" Quantos gols " + nomeB + " vai fazer (menos que " + nomeA + ")? ");
            golsB = lerInt();
            if (golsA <= golsB) { System.out.println(" O placar não condiz com a vitória de " + nomeA + "."); return; }
        } else if (vencedor == 2) {
            System.out.print("\n Quantos gols " + nomeB + " vai fazer? ");
            golsB = lerInt();
            System.out.print(" Quantos gols " + nomeA + " vai fazer (menos que " + nomeB + ")? ");
            golsA = lerInt();
            if (golsB <= golsA) { System.out.println(" O placar não condiz com a vitória de " + nomeB + "."); return; }
        } else {
            System.out.print("\n Quantos gols cada time vai fazer? ");
            golsA = lerInt();
            golsB = golsA;
            System.out.println(" Placar: " + nomeA + " " + golsA + " x " + golsB + " " + nomeB);
        }

        // Quem vai marcar os gols?
        List<GolAtleta> golsPalpitados = new ArrayList<>();
        int totalGols = golsA + golsB;

        if (totalGols > 0) {
            // Monta lista simples: nome do jogador + seleção
            List<Jogador> jogadores = new ArrayList<>();
            jogadores.addAll(jogo.getSelecaoA().getJogadores());
            jogadores.addAll(jogo.getSelecaoB().getJogadores());

            System.out.println("\n Quem vai marcar os " + totalGols + " gol(s)?");
            System.out.println(" Digite o nome do jogador (ou ENTER para pular):");

            Map<Integer, Integer> contagem = new HashMap<>();
            int informados = 0;
            while (informados < totalGols) {
                System.out.print(" Gol " + (informados + 1) + ": ");
                String nomeJog = sc.nextLine().trim();

                if (nomeJog.isEmpty()) {
                    informados++;
                    continue;
                }

                // Busca jogador pelo nome (parcial, sem case)
                Optional<Jogador> achou = jogadores.stream()
                        .filter(j -> j.getNome().toLowerCase().contains(nomeJog.toLowerCase()))
                        .findFirst();

                if (achou.isPresent()) {
                    int jogIdx = jogadores.indexOf(achou.get());
                    contagem.merge(jogIdx, 1, Integer::sum);
                    System.out.println("   → " + achou.get().getNome()
                            + " [" + achou.get().getSelecao().getNome() + "]");
                    informados++;
                } else {
                    System.out.println("   Jogador não encontrado. Tente novamente ou pressione ENTER para pular.");
                }
            }
            for (Map.Entry<Integer, Integer> e : contagem.entrySet())
                golsPalpitados.add(new GolAtleta(jogadores.get(e.getKey()), e.getValue()));
        }

        // Resumo
        System.out.println("\n ══ SEU PALPITE ══");
        System.out.println("  " + nomeA + " " + golsA + " x " + golsB + " " + nomeB);
        if (!golsPalpitados.isEmpty()) {
            System.out.println("  Artilheiros:");
            golsPalpitados.forEach(g -> System.out.println("    - " + g));
        }
        System.out.print("\n Confirmar? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println(" Palpite cancelado."); return;
        }

        try {
            Palpite palpite = new Palpite(participanteLogado, jogo,
                    new Placar(golsA, golsB), golsPalpitados);
            participanteLogado.fazerPalpite(palpite);
            System.out.println(" ✔ Palpite registrado! Boa sorte!");
        } catch (IllegalStateException e) {
            System.out.println(" Erro: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════
    // VER PALPITES
    // ══════════════════════════════════════════════════
    static void verPalpites() {
        List<Palpite> palpites = participanteLogado.getPalpites();
        if (palpites.isEmpty()) { System.out.println("\n Você ainda não fez palpites."); return; }

        System.out.println("\n Seus palpites:");
        for (Palpite pal : palpites) {
            Jogo j = pal.getJogo();
            System.out.println("\n  ▸ " + j.getSelecaoA().getNome()
                    + " x " + j.getSelecaoB().getNome() + " [" + j.getStatus() + "]");
            System.out.println("    Palpite: " + j.getSelecaoA().getNome() + " "
                    + pal.getPlacarPalpitado().getGolsA() + " x "
                    + pal.getPlacarPalpitado().getGolsB() + " " + j.getSelecaoB().getNome());
            if (!pal.getGolsPalpitados().isEmpty()) {
                System.out.println("    Artilheiros:");
                pal.getGolsPalpitados().forEach(g ->
                        System.out.println("      - " + g.getJogador().getNome()
                                + " (" + g.getQuantidade() + " gol(s))"));
            }
            if (j.getStatus() == StatusJogo.ENCERRADO && j.getResultadoReal() != null)
                System.out.println("    Resultado real: " + j.getSelecaoA().getNome() + " "
                        + j.getResultadoReal().getGolsA() + " x "
                        + j.getResultadoReal().getGolsB() + " " + j.getSelecaoB().getNome());
            if (pal.getPontuacao() != null)
                System.out.println("    Pontos: " + pal.getPontuacao().getPontosTotais());
            else
                System.out.println("    (Aguardando resultado)");
        }
    }

    // ══════════════════════════════════════════════════
    // VER PONTUAÇÃO
    // ══════════════════════════════════════════════════
    static void verPontuacao() {
        List<PontuacaoJogo> pontuacoes = participanteLogado.consultarPontuacao();
        if (pontuacoes.isEmpty()) { System.out.println("\n Nenhum jogo pontuado ainda."); return; }

        System.out.println("\n Pontuação de " + participanteLogado.getNome() + ":");
        for (PontuacaoJogo pts : pontuacoes) {
            Jogo j = pts.getJogo();
            System.out.println("\n  " + j.getSelecaoA().getNome() + " x " + j.getSelecaoB().getNome());
            System.out.println(pts.getDetalhes());
        }
        System.out.println("\n  TOTAL: " + participanteLogado.getPontuacaoTotal() + " pts");
    }

    // ══════════════════════════════════════════════════
    // RANKING
    // ══════════════════════════════════════════════════
    static void exibirRanking() {
        System.out.println("\n ┌────┬──────────────────────────┬────────┐");
        System.out.println(" │ Nº │ Participante             │  Pts   │");
        System.out.println(" ├────┼──────────────────────────┼────────┤");
        List<RankingItem> ranking = admin.gerarRanking();
        if (ranking.isEmpty())
            System.out.println(" │        Nenhum participante           │");
        else
            for (RankingItem r : ranking)
                System.out.printf(" │ %2dº │ %-24s │ %6d │%n",
                        r.getPosicao(), r.getParticipante().getNome(), r.getPontuacaoTotal());
        System.out.println(" └────┴──────────────────────────┴────────┘");
    }

    // ══════════════════════════════════════════════════
    // UTILITÁRIOS
    // ══════════════════════════════════════════════════
    static int lerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("  Digite um número: "); }
        }
    }

    static void linha() {
        System.out.println("──────────────────────────────────────────");
    }
}
