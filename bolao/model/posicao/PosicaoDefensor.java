package bolao.model.posicao;

public class PosicaoDefensor implements Posicao {
    private final double multiplicador = 2.0;

    @Override
    public double getMultiplicadorPontuacao() {
        return multiplicador;
    }

    @Override
    public String getNome() {
        return "Defensor";
    }
}
