package br.com.letscode.Receita.enums;

public enum TipoRendimento {
    COPO(0),
    LATA(1),
    UNIDADES(2),
    COLHERES(3),
    XICARAS(4),
    PRATOS(5),
    PORCOES(6),
    PESSOAS(7),
    ML(8),
    MG(9);

    private final int idTipo;

    TipoRendimento(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getTipo() {
        return this.idTipo;
    }

    public static TipoRendimento identificaTipoRendimentoSelecionado(int idTipo){
        for (TipoRendimento origem : TipoRendimento.values()) {
            if (origem.getTipo() ==  idTipo) {
                return origem;
            }
        }
        return null;
    }
}
