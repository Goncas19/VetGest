package modelo;

import interfaces.Persistente;

public class Cliente implements Cloneable, Persistente {
    private int id;
    private String nome;
    private String telefone;
    private String email;

    public Cliente() {
        this(0, "Sem nome", "Sem telefone", "Sem email");
    }

    public Cliente(int id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Cliente(Cliente outro) {
        this(outro.id, outro.nome, outro.telefone, outro.email);
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return this.telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return this.id + " - " + this.nome + " | " + this.telefone + " | " + this.email;
    }

    @Override
    public Cliente clone() {
        return new Cliente(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cliente)) return false;
        Cliente outro = (Cliente) obj;
        return this.id == outro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    @Override
    public String saveTxt() {
        return this.id + ";" + this.nome + ";" + this.telefone + ";" + this.email;
    }

    @Override
    public void read(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length != 4) {
            throw new IllegalArgumentException("Formato de cliente invalido.");
        }
        this.id = Integer.parseInt(partes[0]);
        this.nome = partes[1];
        this.telefone = partes[2];
        this.email = partes[3];
    }
}
