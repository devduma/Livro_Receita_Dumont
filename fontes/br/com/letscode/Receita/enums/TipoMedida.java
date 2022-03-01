package br.com.letscode.Receita.enums;

public enum TipoMedida {
    UNIDADE(0),
    GRAMA(1),
    LATA(2),
    KILO(3),
    COLHERES(4),
    COLHER_DE_SOPA(5),
    COLHER_DE_CHA(6),
    MILILITRO(7),
    XICARA(8),
    PESSOAS(9),
    ML(10),
    POTE(11);

    private final int idMedida;

    TipoMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public int getMedida() {
        return this.idMedida;
    }

    public static TipoMedida identificaUnidadeMedidaSelecionado(int idMedida){
        for (TipoMedida origem : TipoMedida.values()) {
            if (origem.getMedida() ==  idMedida) {
                return origem;
            }
        }
        return null;
    }

}
