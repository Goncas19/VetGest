package modelo;

import java.util.ArrayList;
import java.util.Objects;

public class Clinica {
    private ArrayList<Cliente> clientes;
    private ArrayList<Animal> animais;
    private ArrayList<RegistoClinico> registosClinicos;

    public Clinica() {
        this.clientes = new ArrayList<Cliente>();
        this.animais = new ArrayList<Animal>();
        this.registosClinicos = new ArrayList<RegistoClinico>();
    }

    public Clinica(ArrayList<Cliente> clientes, ArrayList<Animal> animais, ArrayList<RegistoClinico> registosClinicos) {
        this();
        for (Cliente cliente : Objects.requireNonNull(clientes, "A lista de clientes nao pode ser nula.")) {
            adicionarCliente(cliente);
        }
        for (Animal animal : Objects.requireNonNull(animais, "A lista de animais nao pode ser nula.")) {
            adicionarAnimal(animal);
        }
        for (RegistoClinico registo : Objects.requireNonNull(registosClinicos, "A lista de registos nao pode ser nula.")) {
            adicionarRegisto(registo);
        }
    }

    public Clinica(Clinica outra) {
        this();
        for (Cliente cliente : outra.clientes) this.clientes.add(cliente.clone());
        for (Animal animal : outra.animais) this.animais.add(animal.clone());
        for (RegistoClinico registo : outra.registosClinicos) this.registosClinicos.add(registo.clone());
    }

    public ArrayList<Cliente> getClientes() { return this.clientes; }
    public void setClientes(ArrayList<Cliente> clientes) { this.clientes = clientes; }
    public ArrayList<Animal> getAnimais() { return this.animais; }
    public void setAnimais(ArrayList<Animal> animais) { this.animais = animais; }
    public ArrayList<RegistoClinico> getRegistosClinicos() { return this.registosClinicos; }
    public void setRegistosClinicos(ArrayList<RegistoClinico> registosClinicos) { this.registosClinicos = registosClinicos; }

    public void adicionarCliente(Cliente cliente) {
        Objects.requireNonNull(cliente, "O cliente nao pode ser nulo.");
        if (procurarClientePorId(cliente.getId()) != null) {
            throw new IllegalArgumentException("Ja existe um cliente com o ID " + cliente.getId() + ".");
        }
        this.clientes.add(cliente);
    }

    public void adicionarAnimal(Animal animal) {
        Objects.requireNonNull(animal, "O animal nao pode ser nulo.");
        if (procurarAnimalPorId(animal.getId()) != null) {
            throw new IllegalArgumentException("Ja existe um animal com o ID " + animal.getId() + ".");
        }
        if (procurarClientePorId(animal.getIdCliente()) == null) {
            throw new IllegalArgumentException("O dono do animal nao existe.");
        }
        this.animais.add(animal);
    }

    public void adicionarRegisto(RegistoClinico registo) {
        Objects.requireNonNull(registo, "O registo clinico nao pode ser nulo.");
        for (RegistoClinico existente : this.registosClinicos) {
            if (existente.getId() == registo.getId()) {
                throw new IllegalArgumentException("Ja existe um registo clinico com o ID " + registo.getId() + ".");
            }
        }
        if (procurarAnimalPorId(registo.getIdAnimal()) == null) {
            throw new IllegalArgumentException("O animal do registo clinico nao existe.");
        }
        this.registosClinicos.add(registo);
    }

    public Cliente procurarClientePorId(int id) {
        for (Cliente cliente : this.clientes) {
            if (cliente.getId() == id) return cliente;
        }
        return null;
    }

    public Animal procurarAnimalPorId(int id) {
        for (Animal animal : this.animais) {
            if (animal.getId() == id) return animal;
        }
        return null;
    }

    public ArrayList<RegistoClinico> obterHistoricoAnimal(int idAnimal) {
        ArrayList<RegistoClinico> historico = new ArrayList<RegistoClinico>();
        for (RegistoClinico registo : this.registosClinicos) {
            if (registo.getIdAnimal() == idAnimal) historico.add(registo);
        }
        return historico;
    }

    public int proximoIdCliente() {
        int maior = 0;
        for (Cliente cliente : this.clientes) if (cliente.getId() > maior) maior = cliente.getId();
        return maior + 1;
    }

    public int proximoIdAnimal() {
        int maior = 0;
        for (Animal animal : this.animais) if (animal.getId() > maior) maior = animal.getId();
        return maior + 1;
    }

    public int proximoIdRegisto() {
        int maior = 0;
        for (RegistoClinico registo : this.registosClinicos) if (registo.getId() > maior) maior = registo.getId();
        return maior + 1;
    }

    public double calcularTotalFaturado() {
        double total = 0.0;
        for (RegistoClinico registo : this.registosClinicos) total += registo.calcularPrecoFinal();
        return total;
    }

    public void demonstrarPolimorfismo() {
        for (RegistoClinico registo : this.registosClinicos) {
            registo.print();
        }
    }
}
