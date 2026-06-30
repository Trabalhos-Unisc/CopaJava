package bolao.model.posicao;

public class PosicaoAtacante implements Posicao {
    private final double multiplicador = 1.0;

    @Override
    public double getMultiplicadorPontuacao() {
        return multiplicador;
    }

    @Override
    public String getNome() {
        return "Atacante";
    }
}
