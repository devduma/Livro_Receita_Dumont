package br.com.letscode.Receita.enums;

public enum Categoria {
    DOCE("D"),
    SALGADO("S"),
    BEBIDA("B");

    private final String idCatg;

    Categoria(String idCatg) {
        this.idCatg = idCatg;
    }

    public String getCategoria() {
        return idCatg;
    }
}

