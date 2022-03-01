package br.com.letscode.Receita.view;

import br.com.letscode.Receita.controller.Catalogo;
import br.com.letscode.Receita.domain.Ingrediente;
import br.com.letscode.Receita.domain.Receita;
import br.com.letscode.Receita.domain.Rendimento;
import br.com.letscode.Receita.enums.Categoria;
import br.com.letscode.Receita.enums.TipoMedida;
import br.com.letscode.Receita.enums.TipoRendimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditReceitaView {
    private final Receita receita;

    public EditReceitaView(Receita receita) {
        this.receita = new Receita(receita);
    }

    public EditReceitaView(){
        this.receita = null;
    }

    public Receita edit() {

        do {
            new ReceitaView(receita).fullView(System.out);
        } while (showMenu());
        String option = ConsoleUtils.getUserOption("Salva receita?%nS - Sim   N - Não", "S", "N");
        return (option.equalsIgnoreCase("S"))? receita : null;
    }

    private boolean showMenu() {
        String[] options = new String[8];
        ConsoleUtils.clear();
        StringBuilder sb = new StringBuilder("#".repeat(100));
        sb.append("%nEdição da receita %n");

        sb.append("%n").append("  N : Nome Receita  %n");
        options[0] = "N";
        sb.append("  C : Categoria  %n");
        options[1] = "C";
        sb.append("  T : Tempo Preparo  %n");
        options[2] = "T";
        sb.append("  R : Rendimento  %n");
        options[3] = "R";
        sb.append("  I : Ingredientes  %n");
        options[4] = "I";
        sb.append("  P : Preparo  %n");
        options[5] = "P";

        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[6] = "X";
        sb.append("#".repeat(100)).append("%n");

        String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
        switch (opcao) {
            case "N":
                nomeReceita();
                break;
            case "C":
                categoria();
                break;
            case "T":
                tempoPreparo();
                break;
            case "R":
                rendimento();
                break;
            case "I":
                ingrediente();
                break;
            case "P":
                preparo();
                break;
            case "X":
                System.out.println("Retorna Catálogo");
                return false;
            default:
                System.out.println("Opção inválida!!!");
        }
        return true;
    }

    private void nomeReceita(){
        String nomeReceita = ConsoleUtils.getUserInput("Novo nome da Receita");
        if (!nomeReceita.isBlank()) {
            Receita other;
            other = new Catalogo().getReceita(nomeReceita);
            //Se encontrar, mostra mensagem.
            if (other != null) {
                System.out.println("Nome já existe no catálogo");
            }
            else {
                receita.setNome(nomeReceita);
            }
        }
    }

    private void categoria(){
        System.out.println("Nova categoria ");
        Categoria categoria = validaCategoria();

        receita.setCategoria(categoria);
    }

    public Categoria validaCategoria(){
        System.out.println("Categoria da receita: ");
        Categoria[] categorias = Categoria.values();
        String[] options = new String[categorias.length];

        for (int i = 0; i < categorias.length; i++) {
            options[i] = categorias[i].getCategoria();
            System.out.printf("%s - %s%n", categorias[i].getCategoria(), categorias[i].name());
        }

        String catgReceita = ConsoleUtils.getUserOption("%nEscolha a opção", options);
        Categoria nomeCategoria = Categoria.identificaCategoriaSelecionada(catgReceita);
        System.out.println("Categoria: " + nomeCategoria);
        return nomeCategoria;
    }

    private void tempoPreparo(){
        double tempoPreparo = validaTempoPreparo();
        receita.setTempoPreparo(tempoPreparo);
    }

    public double validaTempoPreparo(){
        // Edita Tempo de preparo
        boolean argumentoOK = false;
        double tempoPreparo = 0d;

        while (!argumentoOK){
            String tempoPreparoStr = ConsoleUtils.getUserInput("Tempo de preparo (minutos)");
            try {
                tempoPreparo = Double.parseDouble(tempoPreparoStr);
                argumentoOK = true;
            }
            catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
        return tempoPreparo;
    }

    public void rendimento() {
        Rendimento rendimento = informaRendimento();
        receita.setRendimento(rendimento);
    }

    /** Trata informações de Rendimento: quantidades De e Até e unidade/tipo */
    private Rendimento informaRendimento() {
        int qdeDe = (int) validaQuantidade("Serve: De (quantidade)");

        int qdeAte = (int) validaQuantidade("Até");

        // Tipo Rendimento
        System.out.println("Unidade Rendimento: ");
        TipoRendimento[] tipos = TipoRendimento.values();
        String[] options = new String[tipos.length];

        for (int i = 0; i < tipos.length; i++) {
            options[i] = String.valueOf(tipos[i].getTipo());
            System.out.printf("%s - %s%n", tipos[i].getTipo(), tipos[i].name());
        }

        String tipo = ConsoleUtils.getUserOption("%nEscolha a opção", options);
        TipoRendimento nomeTipo = TipoRendimento.identificaTipoRendimentoSelecionado(Integer.parseInt(tipo));
        System.out.println("Tipo: " + nomeTipo);

        return new Rendimento(qdeDe, qdeAte, nomeTipo);
    }

    private static int buscaPosicaoMedida(int idTipo, TipoMedida[] tipo) {
        int posicao = -1;
        for (int i = 0; i < TipoMedida.values().length; i++) {
            if (tipo[i].getMedida() == idTipo) {
                posicao = i;
                break;
            }
        }
        return posicao;
    }

    private static double validaQuantidade(String mensagem){
        boolean argumentoOK = false;
        double qde = 0;

        while (!argumentoOK) {
            String qdeStr = ConsoleUtils.getUserInput(mensagem);
            try {
                qde = Double.parseDouble(qdeStr);
                argumentoOK = true;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
        return qde;
    }

    /** trata informações de Ingredientes: Nome, quantidade e unidade */
    public Ingrediente informaIngredientes(){
        String nomeIngrediente;
        do{
            nomeIngrediente = ConsoleUtils.getUserInput("Nome do Ingrediente");
        } while (nomeIngrediente.isBlank());

        double qdeIngrediente = validaQuantidade("Quantidade");

        // Unidade de Medida
        System.out.println("Unidade Medida: ");
        TipoMedida[] tipos = TipoMedida.values();
        String[] options = new String[tipos.length];

        for (int i = 0; i < tipos.length; i++) {
            options[i] = String.valueOf(tipos[i].getMedida());
            System.out.printf("%s - %s%n", tipos[i].getMedida(), tipos[i].name());
        }

        String tipo = ConsoleUtils.getUserOption("%nEscolha a opção", options);
        TipoMedida unidadeMedida = TipoMedida.identificaUnidadeMedidaSelecionado(Integer.parseInt(tipo));
        System.out.println("Medida: " + unidadeMedida);

        return new Ingrediente(nomeIngrediente, qdeIngrediente, unidadeMedida);
    }

    // menu de ingredientes: adição ou remoção de ingredientes
    private void ingrediente(){
        new ReceitaView(receita).ingredientesView(System.out);
        String opcao = ConsoleUtils.getUserOption("Adicionar (+) / Remover (-) / Sair (S)?%n", "+", "-", "S");
        //Trata edição se não optou por sair
        if (!opcao.equalsIgnoreCase("S")) {
            switch (opcao) {
                case "+":
                    incluiIngredientes();
                    break;
                case "-":
                    if (receita.getIngredientes().size()>0)
                        excluiIngrediente();
                    else
                        System.out.println("Receita sem ingredientes");
                    break;
                default:
                    System.out.println("Opção inválida!!!");
            }
        }
    }

    private void incluiIngredientes(){
        String adiciona;

        List<Ingrediente> ingredientes = new ArrayList<>();
        do{
            ingredientes.add(informaIngredientes());
            adiciona = ConsoleUtils.getUserOption("Deseja adicionar um novo ingrediente?\nS - Sim   N - Não", "S", "N");
        } while (adiciona.equalsIgnoreCase("S"));

        receita.addIngredientes(ingredientes);
    }

    private void excluiIngrediente(){
        String adiciona;

        List<Ingrediente> ingredientes = new ArrayList<>();
        do{
            int idIngrediente = validaIdIngrediente("Número do ingrediente na sequência ");
            int seq = 1;
            for (Ingrediente ingrediente : receita.getIngredientes()) {
                if (seq == idIngrediente) {
                    System.out.printf("%s excluído%n", ingrediente.getNome());
                    ingredientes.add(ingrediente); // adiciona ingredientes na lista de exclusão
                    break;
                }
                seq++;
            }
            adiciona = ConsoleUtils.getUserOption("Deseja excluir um outro ingrediente?\nS - Sim   N - Não", "S", "N");
        } while (adiciona.equalsIgnoreCase("S"));

        receita.excluiIngredientes(ingredientes); // exclui todos os ingredientes da lista 'ingredientes'
    }

    /* valida o número informado pelo usuário na sequência de ingredientes da receita */
    private int validaIdIngrediente(String mensagem){
        boolean argumentoOK = false;
        int index = 0;

        while (!argumentoOK) {
            String indexStr = ConsoleUtils.getUserInput(mensagem);
            try {
                index = Integer.parseInt(indexStr);
                if (index > receita.getIngredientes().size() || index <= 0) {
                    System.out.printf("Número deve ser entre 1 e %d%n",
                            receita.getIngredientes().size());
                }
                else {
                    argumentoOK = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
        return index;
    }

    /* menu de Edição do Preparo: inclusão ou exclusão de uma linha detalhe do preparo */
    private void preparo(){
        new ReceitaView(receita).preparoView(System.out);
        String opcao = ConsoleUtils.getUserOption("Adicionar (+) / Remover (-) / Sair (S)?%n", "+", "-", "S");
        //Trata edição se não optou por sair
        if (!opcao.equalsIgnoreCase("S")) {
            switch (opcao) {
                case "+":
                    adicionaPreparo();
                    break;
                case "-":
                    if (receita.getPreparo().size()>0)
                        excluiLinhaPreparo();
                    else
                        System.out.println("Receita sem informações de preparo");
                    break;
                default:
                    System.out.println("Opção inválida!!!");
            }
        }
    }

    private void adicionaPreparo(){
        String linhaPreparo;
        int numLinha = 0;

        linhaPreparo = ConsoleUtils.getUserInput("Detalhe do preparo a ser incluído. Para sair, tecle <ENTER>");
        while (!linhaPreparo.isBlank()) {
            if (receita.getPreparo().size() > 0) {
                numLinha = validaLinhaPreparo("Informe a linha onde o detalhe será inserido", receita.getPreparo().size()+1);
                numLinha--;
            }
            receita.getPreparo().add(numLinha,linhaPreparo); // inclui detalhe na posição da lista numLinha-1
            new ReceitaView(receita).preparoView(System.out);
            linhaPreparo = ConsoleUtils.getUserInput("\nDetalhe do preparo a ser incluído. Para sair, tecle <ENTER>");
        }
    }

    private int validaLinhaPreparo(String mensagem, int numLinhamax){
        boolean argumentoOK = false;
        int linha=0;

        while (!argumentoOK) {
            String indexStr = ConsoleUtils.getUserInput(mensagem);
            try {
                linha = Integer.parseInt(indexStr);
                if (linha > numLinhamax || linha <= 0) {
                       System.out.printf("Número deve ser entre 1 e %d%n",
                                numLinhamax);
                }
                else {
                    argumentoOK = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
        return linha;
    }

    private void excluiLinhaPreparo(){
        int numLinha;
        String exclui = "S";

        while (exclui.equalsIgnoreCase("S")) {

            numLinha = validaLinhaPreparo("Informe a linha a ser excluída do preparo da receita."
                    ,receita.getPreparo().size());
            if (receita.getPreparo().size() > 0) {
                receita.getPreparo().remove(numLinha - 1); // exclui detalhe na posição da lista numLinha-1
            }
            new ReceitaView(receita).preparoView(System.out);
            if (receita.getPreparo().size() == 0){
                exclui = "N";
            }
            else {
                exclui = ConsoleUtils.getUserOption("Deseja continuar excluindo?\nS - Sim   N - Não", "S", "N");
            }
        }
    }
}
