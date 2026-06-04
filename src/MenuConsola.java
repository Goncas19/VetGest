import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.io.IOException;
import modelo.*;
import ficheiros.GestorFicheiros;

public class MenuConsola {
    private Clinica clinica;
    private Scanner scanner;

    public MenuConsola() {
        this.clinica = new Clinica();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Opcao: ");
            executarOpcao(opcao);
        } while (opcao != 0);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== VetGest - Clinica Veterinaria ===");
        System.out.println("1 - Registar cliente");
        System.out.println("2 - Registar animal");
        System.out.println("3 - Registar consulta");
        System.out.println("4 - Registar vacina");
        System.out.println("5 - Registar consulta de especialidade");
        System.out.println("6 - Listar clientes");
        System.out.println("7 - Listar animais");
        System.out.println("8 - Listar historico de um animal");
        System.out.println("9 - Mostrar total faturado");
        System.out.println("10 - Guardar dados");
        System.out.println("11 - Carregar dados");
        System.out.println("12 - Demonstrar polimorfismo");
        System.out.println("0 - Sair");
    }

    private void executarOpcao(int opcao) {
        switch (opcao) {
            case 1: registarCliente(); break;
            case 2: registarAnimal(); break;
            case 3: registarConsulta(); break;
            case 4: registarVacina(); break;
            case 5: registarConsultaEspecialidade(); break;
            case 6: listarClientes(); break;
            case 7: listarAnimais(); break;
            case 8: listarHistoricoAnimal(); break;
            case 9: System.out.println("Total faturado: " + clinica.calcularTotalFaturado() + " euros"); break;
            case 10: guardarDados(); break;
            case 11: carregarDados(); break;
            case 12: clinica.demonstrarPolimorfismo(); break;
            case 0: System.out.println("A sair do VetGest."); break;
            default: System.out.println("Opcao invalida.");
        }
    }

    private void registarCliente() {
        int id = clinica.proximoIdCliente();
        String nome = lerTexto("Nome: ");
        String telefone = lerTexto("Telefone: ");
        String email = lerTexto("Email: ");
        clinica.adicionarCliente(new Cliente(id, nome, telefone, email));
        System.out.println("Cliente registado com o ID " + id + ".");
    }

    private void registarAnimal() {
        if (clinica.getClientes().isEmpty()) {
            System.out.println("E necessario registar um cliente primeiro.");
            return;
        }
        listarClientes();
        int idCliente = lerInteiro("ID do dono: ");
        if (clinica.procurarClientePorId(idCliente) == null) {
            System.out.println("Cliente nao encontrado.");
            return;
        }
        int id = clinica.proximoIdAnimal();
        String nome = lerTexto("Nome do animal: ");
        String especie = lerTexto("Especie: ");
        String raca = lerTexto("Raca: ");
        int idade = lerInteiro("Idade: ");
        clinica.adicionarAnimal(new Animal(id, nome, especie, raca, idade, idCliente));
        System.out.println("Animal registado com o ID " + id + ".");
    }

    private void registarConsulta() {
        Animal animal = escolherAnimal();
        if (animal == null) return;
        int id = clinica.proximoIdRegisto();
        String descricao = lerTexto("Descricao: ");
        double precoBase = lerDouble("Preco base: ");
        String veterinario = lerTexto("Veterinario: ");
        String diagnostico = lerTexto("Diagnostico: ");
        int duracao = lerInteiro("Duracao em minutos: ");
        RegistoClinico consulta = new Consulta(id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao);
        clinica.adicionarRegisto(consulta);
        System.out.println("Consulta registada com o ID " + id + ".");
    }

    private void registarVacina() {
        Animal animal = escolherAnimal();
        if (animal == null) return;
        int id = clinica.proximoIdRegisto();
        String descricao = lerTexto("Descricao: ");
        double precoBase = lerDouble("Preco base: ");
        String nomeVacina = lerTexto("Nome da vacina: ");
        String lote = lerTexto("Lote: ");
        boolean reforco = lerBoolean("Dose de reforco (s/n): ");
        RegistoClinico vacina = new Vacina(id, descricao, LocalDate.now(), precoBase, animal.getId(), nomeVacina, lote, reforco);
        clinica.adicionarRegisto(vacina);
        System.out.println("Vacina registada com o ID " + id + ".");
    }

    private void registarConsultaEspecialidade() {
        Animal animal = escolherAnimal();
        if (animal == null) return;
        int id = clinica.proximoIdRegisto();
        String descricao = lerTexto("Descricao: ");
        double precoBase = lerDouble("Preco base: ");
        String veterinario = lerTexto("Veterinario: ");
        String diagnostico = lerTexto("Diagnostico: ");
        int duracao = lerInteiro("Duracao em minutos: ");
        String especialidade = lerTexto("Especialidade: ");
        double taxa = lerDouble("Taxa de especialidade: ");
        RegistoClinico consulta = new ConsultaEspecialidade(id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao, especialidade, taxa);
        clinica.adicionarRegisto(consulta);
        System.out.println("Consulta de especialidade registada com o ID " + id + ".");
    }

    private Animal escolherAnimal() {
        if (clinica.getAnimais().isEmpty()) {
            System.out.println("E necessario registar um animal primeiro.");
            return null;
        }
        listarAnimais();
        int idAnimal = lerInteiro("ID do animal: ");
        Animal animal = clinica.procurarAnimalPorId(idAnimal);
        if (animal == null) System.out.println("Animal nao encontrado.");
        return animal;
    }

    private void listarClientes() {
        System.out.println("Clientes:");
        for (Cliente cliente : clinica.getClientes()) System.out.println(cliente);
    }

    private void listarAnimais() {
        System.out.println("Animais:");
        for (Animal animal : clinica.getAnimais()) {
            Cliente dono = clinica.procurarClientePorId(animal.getIdCliente());
            String nomeDono = dono == null ? "Sem dono" : dono.getNome();
            System.out.println(animal + " - Dono: " + nomeDono);
        }
    }

    private void listarHistoricoAnimal() {
        Animal animal = escolherAnimal();
        if (animal == null) return;
        ArrayList<RegistoClinico> historico = clinica.obterHistoricoAnimal(animal.getId());
        if (historico.isEmpty()) {
            System.out.println("Este animal ainda nao tem registos clinicos.");
            return;
        }
        for (RegistoClinico registo : historico) registo.print();
    }

    private void guardarDados() {
        try {
            GestorFicheiros.guardarClinica(clinica);
            System.out.println("Dados guardados com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao guardar dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            clinica = GestorFicheiros.carregarClinica();
            System.out.println("Dados carregados com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Introduza um numero inteiro.");
            }
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(scanner.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Introduza um numero.");
            }
        }
    }

    private boolean lerBoolean(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem).trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) return true;
            if (resposta.equals("n") || resposta.equals("nao")) return false;
            System.out.println("Resposta invalida. Use s ou n.");
        }
    }
}
