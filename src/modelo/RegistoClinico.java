package modelo;

import java.time.LocalDate;
import interfaces.Cobravel;
import interfaces.Persistente;

public abstract class RegistoClinico implements Cloneable, Cobravel, Persistente {
    protected int id;
    protected String descricao;
    protected LocalDate data;
    protected double precoBase;
    protected int idAnimal;
    protected static int totalRegistos = 0;

    public RegistoClinico() {
        this(0, "Sem descricao", LocalDate.now(), 0.0, 0);
    }

    public RegistoClinico(int id, String descricao, LocalDate data, double precoBase) {
        this(id, descricao, data, precoBase, 0);
    }

    public RegistoClinico(int id, String descricao, LocalDate data, double precoBase, int idAnimal) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.precoBase = precoBase;
        this.idAnimal = idAnimal;
        totalRegistos++;
    }

    public RegistoClinico(RegistoClinico outro) {
        this(outro.id, outro.descricao, outro.data, outro.precoBase, outro.idAnimal);
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getDescricao() { return this.descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getData() { return this.data; }
    public void setData(LocalDate data) { this.data = data; }
    public double getPrecoBase() { return this.precoBase; }
    public void setPrecoBase(double precoBase) { this.precoBase = precoBase; }
    public int getIdAnimal() { return this.idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }
    public static int getTotalRegistos() { return totalRegistos; }

    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "RegistoClinico{id=" + this.id
                + ", descricao='" + this.descricao + '\''
                + ", data=" + this.data
                + ", precoBase=" + this.precoBase
                + ", idAnimal=" + this.idAnimal + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        RegistoClinico outro = (RegistoClinico) obj;
        return this.id == outro.id;
    }

    @Override
    public int hashCode() {
        return 31 * this.getClass().hashCode() + Integer.hashCode(this.id);
    }

    @Override
    public RegistoClinico clone() {
        try {
            return (RegistoClinico) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    protected String camposBaseTxt() {
        return this.id + ";" + this.descricao + ";" + this.data + ";" + this.precoBase + ";" + this.idAnimal;
    }
}
