package br.com.letscode.Receita.domain;

import br.com.letscode.Receita.enums.Categoria;

import java.util.ArrayList;
import java.util.List;

public class Receita {
    private String nome;
    private Categoria categoria;
    private double tempoPreparo;
    private Rendimento rendimento;
    private List<Ingrediente> ingredientes;
    private List<String> preparo;

    public Receita(String nome, Categoria categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    public Receita(String nome, Categoria categoria, double tempoPreparo) {
        this.nome = nome;
        this.categoria = categoria;
        this.tempoPreparo = tempoPreparo;
        this.ingredientes=new ArrayList<>();
        this.preparo = new ArrayList<>();
    }

    public Receita(Receita origem) {
        this.nome = origem.nome;
        this.categoria = origem.categoria;
        this.tempoPreparo = origem.tempoPreparo;
        this.rendimento = origem.rendimento;
        this.ingredientes = origem.ingredientes;
        this.preparo = origem.preparo;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public double getTempoPreparo() {
        return tempoPreparo;
    }

    public Rendimento getRendimento() {
        return rendimento;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public List<String> getPreparo() {
        return preparo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setTempoPreparo(double tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public void setRendimento(Rendimento rendimento) {
        this.rendimento = rendimento;
    }

    public void addIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes.addAll(ingredientes);
    }

    public void excluiIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes.removeAll(ingredientes);
    }

    public void setPreparo(List<String> preparo) {
        this.preparo = preparo;
    }

    @Override
    public String toString() {
        return "Receita{" +
                "nome='" + nome + '\'' +
                ", categoria=" + categoria +
                ", tempoPreparo=" + tempoPreparo +
                ", rendimento=" + rendimento +
                ", ingredientes=" + ingredientes +
                ", preparo=" + preparo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receita receita = (Receita) o;

        return nome != null ? nome.equals(receita.nome) : receita.nome == null;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
    }
}
