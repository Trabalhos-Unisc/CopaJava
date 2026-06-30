package bolao.model;

public class ConfigPontuacao {
    private int pontosVencedor;
    private int pontosGolsUmaEquipe;
    private int pontosPlacardCompleto;
    private int pontosBaseGolAtleta;

    public ConfigPontuacao(int pontosVencedor, int pontosGolsUmaEquipe,
                           int pontosPlacardCompleto, int pontosBaseGolAtleta) {
        this.pontosVencedor = pontosVencedor;
        this.pontosGolsUmaEquipe = pontosGolsUmaEquipe;
        this.pontosPlacardCompleto = pontosPlacardCompleto;
        this.pontosBaseGolAtleta = pontosBaseGolAtleta;
    }

    // Configuração padrão
    public static ConfigPontuacao padrao() {
        return new ConfigPontuacao(3, 1, 5, 2);
    }

    public int getPontosVencedor() { return pontosVencedor; }
    public int getPontosGolsUmaEquipe() { return pontosGolsUmaEquipe; }
    public int getPontosPlacardCompleto() { return pontosPlacardCompleto; }
    public int getPontosBaseGol() { return pontosBaseGolAtleta; }

    public void setPontosVencedor(int v) { this.pontosVencedor = v; }
    public void setPontosGolsUmaEquipe(int v) { this.pontosGolsUmaEquipe = v; }
    public void setPontosPlacardCompleto(int v) { this.pontosPlacardCompleto = v; }
    public void setPontosBaseGolAtleta(int v) { this.pontosBaseGolAtleta = v; }

    @Override
    public String toString() {
        return "ConfigPontuacao{" +
                "vencedor=" + pontosVencedor +
                ", golsEquipe=" + pontosGolsUmaEquipe +
                ", placarCompleto=" + pontosPlacardCompleto +
                ", baseGolAtleta=" + pontosBaseGolAtleta +
                '}';
    }
}
