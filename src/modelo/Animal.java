package modelo;

import interfaces.Persistente;

public class Animal implements Cloneable, Persistente {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private int idade;
    private int idCliente;

    public Animal() {
        this(0, "Sem nome", "Sem especie", "Sem raca", 0, 0);
    }

    public Animal(int id, String nome, String especie, String raca, int idade, int idCliente) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.idCliente = idCliente;
    }

    public Animal(Animal outro) {
        this(outro.id, outro.nome, outro.especie, outro.raca, outro.idade, outro.idCliente);
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecie() { return this.especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaca() { return this.raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public int getIdade() { return this.idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public int getIdCliente() { return this.idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    @Override
    public String toString() {
        return this.id + " - " + this.nome + " (" + this.especie + ", " + this.raca + ")";
    }

    @Override
    public Animal clone() {
        return new Animal(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Animal)) return false;
        Animal outro = (Animal) obj;
        return this.id == outro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    @Override
    public String saveTxt() {
        return this.id + ";" + this.nome + ";" + this.especie + ";" + this.raca + ";" + this.idade + ";" + this.idCliente;
    }

    @Override
    public void read(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length != 6) {
            throw new IllegalArgumentException("Formato de animal invalido.");
        }
        this.id = Integer.parseInt(partes[0]);
        this.nome = partes[1];
        this.especie = partes[2];
        this.raca = partes[3];
        this.idade = Integer.parseInt(partes[4]);
        this.idCliente = Integer.parseInt(partes[5]);
    }
}
