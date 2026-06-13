package modelo;

import interfaces.Persistente;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Animal implements Cloneable, Persistente {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento;
    private int idCliente;

    public Animal() {
        this(0, "Sem nome", "Sem especie", "Sem raca", LocalDate.now(), 0);
    }

    public Animal(int id, String nome, String especie, String raca, LocalDate dataNascimento, int idCliente) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.idCliente = idCliente;
    }

    public Animal(Animal outro) {
        this(outro.id, outro.nome, outro.especie, outro.raca, outro.dataNascimento, outro.idCliente);
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecie() { return this.especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaca() { return this.raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public LocalDate getDataNascimento() { return this.dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public int getIdCliente() { return this.idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    // O sistema calcula a idade sozinho baseando-se na data de hoje!
    public int getIdade() {
        return (int) ChronoUnit.YEARS.between(this.dataNascimento, LocalDate.now());
    }

    @Override
    public String toString() {
        return this.id + " - " + this.nome + " (" + this.especie + " - " + getIdade() + " anos)";
    }

    @Override
    public Animal clone() {
        try {
            return (Animal) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
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
        return this.id + ";" + this.nome + ";" + this.especie + ";" + this.raca + ";" + this.dataNascimento + ";" + this.idCliente;
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
        this.dataNascimento = LocalDate.parse(partes[4]);
        this.idCliente = Integer.parseInt(partes[5]);
    }
}
