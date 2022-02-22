package br.com.letscode.Receita.view;

import br.com.letscode.Receita.controller.Catalogo;
import br.com.letscode.Receita.domain.Receita;
import br.com.letscode.Receita.domain.Rendimento;
import br.com.letscode.Receita.domain.Ingrediente;
import br.com.letscode.Receita.enums.Categoria;
import br.com.letscode.Receita.enums.TipoRendimento;
import br.com.letscode.Receita.enums.TipoMedida;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogoView {
    private final Catalogo controller;
    private Receita ative;
    private int numReceitaAtual;

    public CatalogoView(Catalogo controller) {
        this.controller = controller;
        if (controller.getTotal() > 0) {
            numReceitaAtual = 1;
            ative = controller.getReceita(numReceitaAtual);
        } else {
            numReceitaAtual = 0;
            ative = null;
        }
    }

    public CatalogoView(){
        controller = null;
        ative = null;
    }

    private boolean showMenu() {
        String[] options = new String[7];
        StringBuilder sb = new StringBuilder("#".repeat(100));
        ConsoleUtils.clear();

        sb.append("%n").append("  + : Adicionar  %n");
        options[0] = "+";

        if (ative != null) {
            sb.append("  E : Editar  %n");
            options[1] = "E";
            sb.append("  - : Remover  %n");
            options[2] = "-";
        }

        if (controller.getTotal() > 1) {
            sb.append("  P : Próxima  %n");
            options[3] = "P";
            sb.append("  A : Anterior  %n");
            options[4] = "A";
            sb.append("  L : Localizar  %n");
            options[5] = "L";
        }

        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[6] = "X";
        sb.append("#".repeat(100)).append("%n");

        String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
        switch (opcao) {
            case "+":
                add();
                break;
            case "-":
                del();
                break;
            case "E":
                edit();
                break;
            case "P":
                next();
                break;
            case "A":
                previous();
                break;
            case "L":
                find();
                break;
            case "X":
                System.out.println("Obrigado!! Volte sempre.");
                return false;
            default:
                System.out.println("Opção inválida!!!");
        }
        return true;
    }

    private void add() {

        Receita receita = informaReceita();
        if (receita != null) {
            Rendimento rendimento = informaRendimento();
            receita.setRendimento(rendimento);

            String adiciona;

            List<Ingrediente> ingredientes = new ArrayList<>();
            do{
                ingredientes.add(informaIngredientes());
                adiciona = ConsoleUtils.getUserOption("Deseja adicionar um novo ingrediente?\nS - Sim   N - Não", "S", "N");
            } while (adiciona.equalsIgnoreCase("S"));

            receita.setIngredientes(ingredientes);

            // Preparo
            List<String> preparo = new ArrayList<>();
            String linhaPreparo = ConsoleUtils.getUserInput("Preparo da receita.  Digite FIM para encerrar.");
            do{
                preparo.add(linhaPreparo);
                linhaPreparo = ConsoleUtils.getUserInput("");
            } while (!linhaPreparo.equalsIgnoreCase("FIM"));

            receita.setPreparo(preparo);

            //Cria uma nova receita no catálogo e torna essa a ativa.
            Receita nova = new EditReceitaView(receita).edit();
            if (nova != null) {
                controller.add(nova);
                numReceitaAtual++;
                ative = nova;
            }
        }
    }

    private Receita informaReceita() {
        String nomeReceita = ConsoleUtils.getUserInput("Nome da nova receita");
        Receita other = null;
        if (!nomeReceita.isBlank()) {
            //Procura no Catalogo a receita com o mesmo nome.
            other = controller.getReceita(nomeReceita);
            //Se encontrar, mostra mensagem.
            if (other != null) {
                String opcao = ConsoleUtils.getUserOption("Receita já existente!%nVocê deseja visualizar?%nS - Sim   N - Não", "S", "N");
                //Se confirmar, ativa receita com o nome digitado
                if (opcao.equalsIgnoreCase("S")) {
                    Receita nova = new EditReceitaView(other).edit();
                    if (nova != null) {
                        controller.del(ative.getNome());
                        controller.add(nova);
                        //Torna a nova receita a ativa.
                        ative = nova;
                        numReceitaAtual = 0;
                    }
                }
                other = null;
            }
            else {
                // Lê a categoria da receita
                Categoria categoria = validaCategoria();

                // Tempo de preparo
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
                other = new Receita(nomeReceita, categoria, tempoPreparo);
            }
        }
        return other;
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
        System.out.println("Categoria: " + categorias[buscaPosicaoCategoria(catgReceita)].name());
        return categorias[buscaPosicaoCategoria(catgReceita)];
    }

    public Rendimento informaRendimento() {
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
        int posicao = buscaPosicaoTipo(Integer.parseInt(tipo));
        System.out.println("Tipo: " + tipos[posicao].name());


        return new Rendimento(qdeDe, qdeAte, tipos[posicao]);
    }

    public Ingrediente informaIngredientes(){
        String nomeIngrediente;
        do{
            nomeIngrediente = ConsoleUtils.getUserInput("Nome do Ingrediente");
        } while (nomeIngrediente.isBlank());

        double qdeIngrediente = validaQuantidade("Quantidade");

        // Tipo Rendimento
        System.out.println("Unidade Medida: ");
        TipoMedida[] tipos = TipoMedida.values();
        String[] options = new String[tipos.length];

        for (int i = 0; i < tipos.length; i++) {
            options[i] = String.valueOf(tipos[i].getMedida());
            System.out.printf("%s - %s%n", tipos[i].getMedida(), tipos[i].name());
        }

        String tipo = ConsoleUtils.getUserOption("%nEscolha a opção", options);
        int posicao = buscaPosicaoMedida(Integer.parseInt(tipo));
        System.out.println("Medida: " + tipos[posicao].name());

        return new Ingrediente(nomeIngrediente, qdeIngrediente, tipos[posicao]);
    }

    private void find() {
        //Capturar o nome da receita.
        String name = ConsoleUtils.getUserInput("Qual o nome da receita que deseja localizar?");
        //Procura no Catalogo a receita com o mesmo nome.
        ative = controller.getReceita(name);
        numReceitaAtual = 0;
    }

    private void next() {
        //Se estiver com uma receita ativa, ativa a próxima receita.
        //Se NÃO estiver com uma receita ativa, ativa a primeira receita.
        if (ative != null) numReceitaAtual++;
        try {
            ative = controller.getReceita(numReceitaAtual);
        } catch (IllegalArgumentException e) {
            ative = null;
        }
        if (ative == null) {
            numReceitaAtual = 1;
            ative = controller.getReceita(numReceitaAtual);
        }
    }

    private void previous() {
        //Se estiver com uma receita ativa, ativa a anterior.
        //Se NÃO estiver com uma receita ativa, ativa a última receita.
        if (ative != null) numReceitaAtual--;
        try {
            ative = controller.getReceita(numReceitaAtual);
        } catch (IllegalArgumentException e) {
            ative = null;
        }
        if (ative == null) {
            numReceitaAtual = controller.getTotal();
            ative = controller.getReceita(numReceitaAtual);
        }
    }

    private void del() {
        //Se NÃO estiver com uma receita ativa, mostra mensagem.
        //Se estiver com uma receita ativa, confirma a operação.
        String opcao = ConsoleUtils.getUserOption("Você deseja realmente APAGAR a receita " + ative.getNome() + "?\nS - Sim   N - Não", "S", "N");
        //Se confirmar, solicita ao Catalogo apagar a receita.
        if (opcao.equalsIgnoreCase("S")) {
            controller.del(ative.getNome());
            ative = null;
            if (controller.getTotal() > 0) {
                numReceitaAtual = 0;
                next();
            }
        }
    }

    private void edit() {
        //Se NÃO estiver com uma receita ativa, mostra mensagem.
        //Se estiver com uma receita ativa, abra a tela de edição.
        Receita nova = new EditReceitaView(ative).edit();
        if (nova != null) {
            controller.del(ative.getNome());
            controller.add(nova);
            //Torna a nova receita a ativa.
            ative = nova;
            numReceitaAtual = 0;
        }
    }

    private int buscaPosicaoCategoria(String idCategoria){
        Categoria[] categoria = Categoria.values();
        int posicao = -1;
        for (int i = 0; i < Categoria.values().length; i++) {
            if ((categoria[i].getCategoria()).equalsIgnoreCase(idCategoria)) {
                posicao = i;
                break;
            }
        }
        return posicao;
    }

    private static int buscaPosicaoTipo(int idTipo){
        TipoRendimento[] tipo = TipoRendimento.values();
        int posicao = -1;
        for (int i = 0; i < TipoRendimento.values().length; i++) {
            if (tipo[i].getTipo() == idTipo) {
                posicao = i;
                break;
            }
        }
        return posicao;
    }

    private static int buscaPosicaoMedida(int idTipo) {
        TipoMedida[] tipo = TipoMedida.values();
        int posicao = -1;
        for (int i = 0; i < TipoMedida.values().length; i++) {
            if (tipo[i].getMedida() == idTipo) {
                posicao = i;
                break;
            }
        }
        return posicao;
    }

    public void view() {
        do {
            //Exibe o layout montado.
            new ReceitaView(ative).fullView(System.out);
            //Exibe o menu de opções.
        } while (showMenu());
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
}
