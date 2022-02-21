package br.com.letscode.Receita;

import br.com.letscode.Receita.controller.Catalogo;
import br.com.letscode.Receita.view.CatalogoView;

public class LivroReceita {
    public static void main(String[] args) {
        Catalogo catalogo = new Catalogo();
        CatalogoView view = new CatalogoView(catalogo);
        view.view();
    }
}
