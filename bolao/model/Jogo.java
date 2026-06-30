package bolao.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Jogo {
    private static int contadorId = 1;

    private int id;
    private Selecao selecaoA;
    private Selecao selecaoB;
    private LocalDateTime dataHora;
    private String fase;
    private StatusJogo status;
    private Placar resultadoReal;
    private List<GolAtleta> golsReais;

    public Jogo(Selecao selecaoA, Selecao selecaoB, LocalDateTime dataHora, String fase) {
        this.id = contadorId++;
        this.selecaoA = selecaoA;
        this.selecaoB = selecaoB;
        this.dataHora = dataHora;
        this.fase = fase;
        this.status = StatusJogo.AGENDADO;
        this.golsReais = new ArrayList<>();
    }

    public boolean isPalpiteAberto() {
        return status == StatusJogo.ABERTO_PARA_PALPITES;
    }

    public void abrirParaPalpites() {
        this.status = StatusJogo.ABERTO_PARA_PALPITES;
    }

    public void encerrar(Placar placar, List<GolAtleta> gols) {
        this.resultadoReal = placar;
        this.golsReais = new ArrayList<>(gols);
        this.status = StatusJogo.ENCERRADO;
    }

    public int getId() { return id; }
    public Selecao getSelecaoA() { return selecaoA; }
    public Selecao getSelecaoB() { return selecaoB; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getFase() { return fase; }
    public StatusJogo getStatus() { return status; }
    public Placar getResultadoReal() { return resultadoReal; }
    public List<GolAtleta> getGolsReais() { return new ArrayList<>(golsReais); }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[Jogo #" + id + "] " + selecaoA.getNome() + " vs " + selecaoB.getNome()
                + " | " + fase + " | " + dataHora.format(fmt) + " | Status: " + status;
    }
}
