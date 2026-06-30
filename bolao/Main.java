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
    static boolean modoAdmin = false;
 
    public static void main(String[] args) {
        inicializarSistema();
 
        while (true) {
            if (!modoAdmin && participanteLogado == null) {
                menuInicial();
            } else if (modoAdmin) {
                menuAdmin();
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
        Selecao franca    = new Selecao("França",    "França",    "B");
        Selecao alemanha  = new Selecao("Alemanha",  "Alemanha",  "B");
 
        admin.cadastrarSelecao(brasil);
        admin.cadastrarSelecao(argentina);
        admin.cadastrarSelecao(franca);
        admin.cadastrarSelecao(alemanha);
 
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
 
        admin.cadastrarJogador(new Jogador("Mbappé",       10, atc, franca));
        admin.cadastrarJogador(new Jogador("Dembélé",      11, atc, franca));
        admin.cadastrarJogador(new Jogador("Griezmann",    7,  mei, franca));
        admin.cadastrarJogador(new Jogador("Varane",       4,  def, franca));
        admin.cadastrarJogador(new Jogador("Lloris",       1,  gol, franca));
 
        admin.cadastrarJogador(new Jogador("Havertz",      29, atc, alemanha));
        admin.cadastrarJogador(new Jogador("Gnabry",       10, atc, alemanha));
        admin.cadastrarJogador(new Jogador("Kimmich",      6,  mei, alemanha));
        admin.cadastrarJogador(new Jogador("Rüdiger",      2,  def, alemanha));
        admin.cadastrarJogador(new Jogador("Neuer",        1,  gol, alemanha));
 
        Jogo j1 = new Jogo(brasil, argentina, LocalDateTime.of(2026, 6, 20, 21, 0), "Fase de Grupos");
        Jogo j2 = new Jogo(franca, alemanha,  LocalDateTime.of(2026, 6, 21, 18, 0), "Fase de Grupos");
        admin.cadastrarJogo(j1);
        admin.cadastrarJogo(j2);
        admin.abrirJogoParaPalpites(j1);
        admin.abrirJogoParaPalpites(j2);
    }
 
    // ══════════════════════════════════════════════════
    // MENU INICIAL
    // ══════════════════════════════════════════════════
    static void menuInicial() {
        linha();
        System.out.println("   BOLÃO DA COPA DO MUNDO 2026");
        linha();
        System.out.println(" 1. Entrar como participante");
        System.out.println(" 2. Entrar como administrador");
        System.out.println(" 0. Sair");
        linha();
        System.out.print(" Opção: ");
        String op = sc.nextLine().trim();
 
        switch (op) {
            case "1": 
            entrarComoParticipante();
            break;
            case "2": 
            entrarComoAdmin();
            break;
            case "0": 
            System.out.println("Até logo!");
            System.exit(0);
            break;
            default: 
            System.out.println(" Opção inválida.");
        }
    }
 
    static void entrarComoParticipante() {
        System.out.print("\n Digite seu nome: ");
        String nome = sc.nextLine().trim();
 
        Optional<Participante> encontrado = admin.getParticipantes().stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();
 
        if (encontrado.isPresent()) {
            participanteLogado = encontrado.get();
            System.out.println(" Bem-vindo, " + participanteLogado.getNome() + "!");
        } else {
            System.out.println(" Participante \"" + nome + "\" não encontrado.");
            System.out.println(" Peça ao administrador para te cadastrar.");
        }
    }
 
    static void entrarComoAdmin() {
        System.out.print("\n Senha do administrador: ");
        String senha = sc.nextLine().trim();
        if (admin.autenticar("admin@bolao.com", senha)) {
            modoAdmin = true;
            System.out.println(" Bem-vindo, Administrador!");
        } else {
            System.out.println(" Senha incorreta.");
        }
    }
 
    // ══════════════════════════════════════════════════
    // MENU ADMINISTRADOR
    // ══════════════════════════════════════════════════
    static void menuAdmin() {
        linha();
        System.out.println("   ADMINISTRADOR");
        linha();
        System.out.println(" 1. Adicionar participante");
        System.out.println(" 2. Cadastrar seleção");
        System.out.println(" 3. Cadastrar jogador");
        System.out.println(" 4. Cadastrar jogo");
        System.out.println(" 5. Abrir jogo para palpites");
        System.out.println(" 6. Registrar resultado de jogo");
        System.out.println(" 7. Ver ranking");
        System.out.println(" 8. Listar jogos");
        System.out.println(" 0. Logout");
        linha();
        System.out.print(" Opção: ");
        String op = sc.nextLine().trim();
 
        switch (op) {
            case "1": 
            adminAdicionarParticipante();
            break;
            case "2": 
            adminCadastrarSelecao();
            break;
            case "3": 
            adminCadastrarJogador();
            break;
            case "4": 
            adminCadastrarJogo();
            break;
            case "5": 
            adminAbrirJogo();
            break;
            case "6": 
            adminRegistrarResultado();
            break;
            case "7": 
            exibirRanking();
            break;
            case "8": 
            listarJogos();
            break;
            case "0": 
            { modoAdmin = false; System.out.println(" Logout realizado."); }
            break;
            default: 
            System.out.println(" Opção inválida.");
        }
    }
 
    static void adminAdicionarParticipante() {
        System.out.print("\n Nome do participante: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println(" Nome inválido."); return; }
        admin.adicionarParticipante(new Participante(nome, nome + "@bolao.com", "1234"));
        System.out.println(" ✔ " + nome + " adicionado ao bolão!");
    }
 
    static void adminCadastrarSelecao() {
        System.out.print("\n Nome da seleção: ");
        String nome = sc.nextLine().trim();
        System.out.print(" País: ");
        String pais = sc.nextLine().trim();
        System.out.print(" Grupo (A, B, C...): ");
        String grupo = sc.nextLine().trim().toUpperCase();
        admin.cadastrarSelecao(new Selecao(nome, pais, grupo));
        System.out.println(" ✔ Seleção cadastrada!");
    }
 
    static void adminCadastrarJogador() {
        List<Selecao> selecoes = admin.getSelecoes();
        if (selecoes.isEmpty()) { System.out.println("\n Nenhuma seleção cadastrada."); return; }
 
        System.out.println("\n Seleção do jogador:");
        for (int i = 0; i < selecoes.size(); i++)
            System.out.println("  " + (i + 1) + ". " + selecoes.get(i).getNome());
        System.out.print(" Escolha: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= selecoes.size()) { System.out.println(" Inválido."); return; }
        Selecao sel = selecoes.get(idx);
 
        System.out.print(" Nome do jogador: ");
        String nome = sc.nextLine().trim();
        System.out.print(" Número da camisa: ");
        int num = lerInt();
 
        System.out.println(" Posição:");
        System.out.println("  1. Atacante");
        System.out.println("  2. Meio-Campista");
        System.out.println("  3. Defensor");
        System.out.println("  4. Goleiro");
        System.out.print(" Escolha: ");
        int pos = lerInt();
 
        Posicao posicao = null;
        
        switch (pos) {
            case 1: 
                posicao = new PosicaoAtacante();
                break;
            case 2:
                posicao = new PosicaoMeioCampista();
                break;
            case 3: 
                posicao = new PosicaoDefensor();
                break;
            case 4: 
                posicao = new PosicaoGoleiro();
                break;
        };
        if (posicao == null) { System.out.println(" Posição inválida."); return; }
 
        admin.cadastrarJogador(new Jogador(nome, num, posicao, sel));
        System.out.println(" ✔ Jogador cadastrado!");
    }
 
    static void adminCadastrarJogo() {
        List<Selecao> selecoes = admin.getSelecoes();
        if (selecoes.size() < 2) { System.out.println("\n Cadastre ao menos 2 seleções."); return; }
 
        System.out.println("\n Seleção A:");
        for (int i = 0; i < selecoes.size(); i++)
            System.out.println("  " + (i + 1) + ". " + selecoes.get(i).getNome());
        System.out.print(" Escolha: ");
        int ia = lerInt() - 1;
 
        System.out.println(" Seleção B:");
        for (int i = 0; i < selecoes.size(); i++)
            System.out.println("  " + (i + 1) + ". " + selecoes.get(i).getNome());
        System.out.print(" Escolha: ");
        int ib = lerInt() - 1;
 
        if (ia < 0 || ib < 0 || ia >= selecoes.size() || ib >= selecoes.size() || ia == ib) {
            System.out.println(" Seleção inválida."); return;
        }
 
        System.out.print(" Fase (ex: Fase de Grupos): ");
        String fase = sc.nextLine().trim();
        System.out.print(" Data (dd/MM/yyyy): ");
        String data = sc.nextLine().trim();
        System.out.print(" Hora (HH:mm): ");
        String hora = sc.nextLine().trim();
 
        try {
            String[] d = data.split("/");
            String[] h = hora.split(":");
            LocalDateTime dt = LocalDateTime.of(
                Integer.parseInt(d[2]), Integer.parseInt(d[1]), Integer.parseInt(d[0]),
                Integer.parseInt(h[0]), Integer.parseInt(h[1])
            );
            admin.cadastrarJogo(new Jogo(selecoes.get(ia), selecoes.get(ib), dt, fase));
            System.out.println(" ✔ Jogo cadastrado! Use a opção 5 para abrir os palpites.");
        } catch (Exception e) {
            System.out.println(" Formato de data/hora inválido.");
        }
    }
 
    static void adminAbrirJogo() {
        List<Jogo> agendados = admin.getJogos().stream()
                .filter(j -> j.getStatus() == StatusJogo.AGENDADO).collect(Collectors.toList());
        if (agendados.isEmpty()) { System.out.println("\n Nenhum jogo agendado."); return; }
 
        System.out.println("\n Jogos agendados:");
        for (int i = 0; i < agendados.size(); i++)
            System.out.println("  " + (i + 1) + ". " + agendados.get(i).getSelecaoA().getNome()
                    + " x " + agendados.get(i).getSelecaoB().getNome());
        System.out.print(" Escolha: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= agendados.size()) { System.out.println(" Inválido."); return; }
 
        admin.abrirJogoParaPalpites(agendados.get(idx));
        System.out.println(" ✔ Jogo aberto para palpites!");
    }
 
    static void adminRegistrarResultado() {
        List<Jogo> abertos = admin.getJogos().stream()
                .filter(j -> j.getStatus() == StatusJogo.ABERTO_PARA_PALPITES).collect(Collectors.toList());
        if (abertos.isEmpty()) { System.out.println("\n Nenhum jogo aberto."); return; }
 
        System.out.println("\n Jogos abertos:");
        for (int i = 0; i < abertos.size(); i++)
            System.out.println("  " + (i + 1) + ". " + abertos.get(i).getSelecaoA().getNome()
                    + " x " + abertos.get(i).getSelecaoB().getNome());
        System.out.print(" Escolha: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= abertos.size()) { System.out.println(" Inválido."); return; }
        Jogo jogo = abertos.get(idx);
 
        System.out.println("\n " + jogo.getSelecaoA().getNome() + " x " + jogo.getSelecaoB().getNome());
        System.out.print(" Gols " + jogo.getSelecaoA().getNome() + ": ");
        int golsA = lerInt();
        System.out.print(" Gols " + jogo.getSelecaoB().getNome() + ": ");
        int golsB = lerInt();
 
        List<GolAtleta> gols = new ArrayList<>();
        int totalGols = golsA + golsB;
 
        if (totalGols > 0) {
            List<Jogador> jogadores = new ArrayList<>();
            jogadores.addAll(jogo.getSelecaoA().getJogadores());
            jogadores.addAll(jogo.getSelecaoB().getJogadores());
 
            System.out.println("\n Quem marcou os " + totalGols + " gol(s)?");
            for (int i = 0; i < jogadores.size(); i++)
                System.out.printf("  %2d. %-20s [%s]%n",
                        i + 1, jogadores.get(i).getNome(), jogadores.get(i).getSelecao().getNome());
 
            Map<Integer, Integer> contagem = new HashMap<>();
            int informados = 0;
            while (informados < totalGols) {
                System.out.print(" Gol " + (informados + 1) + ": número do jogador: ");
                int escolha = lerInt() - 1;
                if (escolha < 0 || escolha >= jogadores.size()) {
                    System.out.println(" Inválido."); continue;
                }
                contagem.merge(escolha, 1, Integer::sum);
                informados++;
            }
            for (Map.Entry<Integer, Integer> e : contagem.entrySet())
                gols.add(new GolAtleta(jogadores.get(e.getKey()), e.getValue()));
        }
 
        admin.registrarResultado(jogo, new Placar(golsA, golsB), gols, calculador);
        System.out.println("\n ✔ Resultado registrado! Pontuações calculadas.");
        System.out.println("   " + jogo.getSelecaoA().getNome() + " "
                + golsA + " x " + golsB + " " + jogo.getSelecaoB().getNome());
    }
 
    // ══════════════════════════════════════════════════
    // MENU PARTICIPANTE
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
            case "1" : fazerPalpite();
            case "2" : verPalpites();
            case "3" : verPontuacao();
            case "4" : exibirRanking();
            case "0" : { participanteLogado = null; System.out.println(" Até logo!"); }
            default  : System.out.println(" Opção inválida.");
        }
    }
 
    static void fazerPalpite() {
        List<Jogo> disponiveis = admin.getJogos().stream()
                .filter(j -> j.getStatus() == StatusJogo.ABERTO_PARA_PALPITES)
                .filter(j -> participanteLogado.getPalpites().stream()
                        .noneMatch(p -> p.getJogo().getId() == j.getId()))
                .collect(Collectors.toList());
 
        if (disponiveis.isEmpty()) {
            System.out.println("\n Nenhum jogo disponível para palpite.");
            return;
        }
 
        System.out.println("\n Jogos disponíveis:");
        for (int i = 0; i < disponiveis.size(); i++) {
            Jogo j = disponiveis.get(i);
            System.out.println("  " + (i + 1) + ". " + j.getSelecaoA().getNome()
                    + " x " + j.getSelecaoB().getNome() + " [" + j.getFase() + "]");
        }
        System.out.print(" Escolha o jogo: ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= disponiveis.size()) { System.out.println(" Inválido."); return; }
        Jogo jogo = disponiveis.get(idx);
 
        System.out.println("\n " + jogo.getSelecaoA().getNome() + " x " + jogo.getSelecaoB().getNome());
        System.out.print(" Gols " + jogo.getSelecaoA().getNome() + ": ");
        int golsA = lerInt();
        System.out.print(" Gols " + jogo.getSelecaoB().getNome() + ": ");
        int golsB = lerInt();
 
        List<GolAtleta> golsPalpitados = new ArrayList<>();
        int totalGols = golsA + golsB;
 
        if (totalGols > 0) {
            List<Jogador> jogadores = new ArrayList<>();
            jogadores.addAll(jogo.getSelecaoA().getJogadores());
            jogadores.addAll(jogo.getSelecaoB().getJogadores());
 
            System.out.println("\n Quem vai marcar os " + totalGols + " gol(s)?");
            System.out.println(" (Gols de Defensores e Goleiros valem mais pontos!)");
            for (int i = 0; i < jogadores.size(); i++) {
                Jogador j = jogadores.get(i);
                int pts = (int)(j.getMultiplicador() * admin.getConfigPontuacao().getPontosBaseGol());
                System.out.printf("  %2d. %-20s [%s] (%s - %d pts/gol)%n",
                        i + 1, j.getNome(), j.getSelecao().getNome(),
                        j.getPosicao().getNome(), pts);
            }
 
            Map<Integer, Integer> contagem = new HashMap<>();
            int informados = 0;
            while (informados < totalGols) {
                System.out.print(" Gol " + (informados + 1) + " de " + totalGols + " — número do jogador: ");
                int escolha = lerInt() - 1;
                if (escolha < 0 || escolha >= jogadores.size()) {
                    System.out.println(" Número inválido."); continue;
                }
                contagem.merge(escolha, 1, Integer::sum);
                System.out.println("   → " + jogadores.get(escolha).getNome());
                informados++;
            }
            for (Map.Entry<Integer, Integer> e : contagem.entrySet())
                golsPalpitados.add(new GolAtleta(jogadores.get(e.getKey()), e.getValue()));
        }
 
        System.out.println("\n ══ SEU PALPITE ══");
        System.out.println("  " + jogo.getSelecaoA().getNome() + " " + golsA
                + " x " + golsB + " " + jogo.getSelecaoB().getNome());
        if (!golsPalpitados.isEmpty()) {
            System.out.println("  Artilheiros:");
            golsPalpitados.forEach(g -> System.out.println("    - " + g));
        }
        System.out.print("\n Confirmar? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println(" Palpite cancelado."); return;
        }
 
        try {
            Palpite palpite = new Palpite(participanteLogado, jogo, new Placar(golsA, golsB), golsPalpitados);
            participanteLogado.fazerPalpite(palpite);
            System.out.println(" ✔ Palpite registrado! Boa sorte!");
        } catch (IllegalStateException e) {
            System.out.println(" Erro: " + e.getMessage());
        }
    }
 
    static void verPalpites() {
        List<Palpite> palpites = participanteLogado.getPalpites();
        if (palpites.isEmpty()) { System.out.println("\n Você ainda não fez palpites."); return; }
 
        System.out.println("\n Seus palpites:");
        for (Palpite pal : palpites) {
            Jogo j = pal.getJogo();
            System.out.println("\n  ▸ " + j.getSelecaoA().getNome() + " x " + j.getSelecaoB().getNome()
                    + " [" + j.getStatus() + "]");
            System.out.println("    Palpite: " + j.getSelecaoA().getNome() + " "
                    + pal.getPlacarPalpitado().getGolsA() + " x "
                    + pal.getPlacarPalpitado().getGolsB() + " " + j.getSelecaoB().getNome());
            if (!pal.getGolsPalpitados().isEmpty()) {
                System.out.println("    Artilheiros apostados:");
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
    // COMPARTILHADO
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
 
    static void listarJogos() {
        System.out.println("\n Jogos:");
        List<Jogo> jogos = admin.getJogos();
        if (jogos.isEmpty()) { System.out.println("  Nenhum jogo cadastrado."); return; }
        for (Jogo j : jogos) {
            System.out.println("  ▸ " + j.getSelecaoA().getNome() + " x " + j.getSelecaoB().getNome()
                    + " [" + j.getFase() + "] — " + j.getStatus());
            if (j.getResultadoReal() != null)
                System.out.println("    Resultado: " + j.getResultadoReal());
        }
    }
 
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
 
