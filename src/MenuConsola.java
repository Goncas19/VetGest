import ficheiros.GestorFicheiros;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import modelo.Animal;
import modelo.Cliente;
import modelo.Clinica;
import modelo.Consulta;
import modelo.ConsultaEspecialidade;
import modelo.RegistoClinico;
import modelo.Vacina;

public class MenuConsola {
    private Clinica clinica;
    private final Scanner scanner;

    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        try {
            this.clinica = GestorFicheiros.carregarClinica();
        } catch (IOException e) {
            this.clinica = new Clinica();
            System.out.println("Nao foi possivel carregar os dados: " + e.getMessage());
        }
    }

    public void iniciar() {
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiroMinimo("Opcao: ", 0);
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
            case 9: System.out.printf(Locale.ROOT, "Total faturado: %.2f euros%n", clinica.calcularTotalFaturado()); break;
            case 10: guardarDados(); break;
            case 11: carregarDados(); break;
            case 12: clinica.demonstrarPolimorfismo(); break;
            case 0: System.out.println("A sair do VetGest."); break;
            default: System.out.println("Opcao invalida.");
        }
    }

    private void registarCliente() {
        int id = clinica.proximoIdCliente();
        String nome = lerTextoObrigatorio("Nome: ");
        String telefone = lerTextoObrigatorio("Telefone: ");
        String email = lerTextoObrigatorio("Email: ");
        clinica.adicionarCliente(new Cliente(id, nome, telefone, email));
        System.out.println("Cliente registado com o ID " + id + ".");
    }

    private void registarAnimal() {
        if (clinica.getClientes().isEmpty()) {
            System.out.println("E necessario registar um cliente primeiro.");
            return;
        }

        listarClientes();
        int idCliente = lerInteiroMinimo("ID do dono: ", 1);
        if (clinica.procurarClientePorId(idCliente) == null) {
            System.out.println("Cliente nao encontrado.");
            return;
        }

        int id = clinica.proximoIdAnimal();
        String nome = lerTextoObrigatorio("Nome do animal: ");
        String especie = lerTextoObrigatorio("Especie: ");
        String raca = lerTextoObrigatorio("Raca: ");
        int idade = lerInteiroMinimo("Idade: ", 0);
        clinica.adicionarAnimal(new Animal(id, nome, especie, raca, idade, idCliente));
        System.out.println("Animal registado com o ID " + id + ".");
    }

    private void registarConsulta() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = lerTextoObrigatorio("Descricao: ");
        double precoBase = lerDoubleMinimo("Preco base: ", 0.0);
        String veterinario = lerTextoObrigatorio("Veterinario: ");
        String diagnostico = lerTextoObrigatorio("Diagnostico: ");
        int duracao = lerInteiroMinimo("Duracao em minutos: ", 0);
        clinica.adicionarRegisto(new Consulta(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao));
        System.out.println("Consulta registada com o ID " + id + ".");
    }

    private void registarVacina() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = lerTextoObrigatorio("Descricao: ");
        double precoBase = lerDoubleMinimo("Preco base: ", 0.0);
        String nomeVacina = lerTextoObrigatorio("Nome da vacina: ");
        String lote = lerTextoObrigatorio("Lote: ");
        boolean reforco = lerBoolean("Dose de reforco (s/n): ");
        clinica.adicionarRegisto(new Vacina(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), nomeVacina, lote, reforco));
        System.out.println("Vacina registada com o ID " + id + ".");
    }

    private void registarConsultaEspecialidade() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = lerTextoObrigatorio("Descricao: ");
        double precoBase = lerDoubleMinimo("Preco base: ", 0.0);
        String veterinario = lerTextoObrigatorio("Veterinario: ");
        String diagnostico = lerTextoObrigatorio("Diagnostico: ");
        int duracao = lerInteiroMinimo("Duracao em minutos: ", 0);
        String especialidade = lerTextoObrigatorio("Especialidade: ");
        double taxa = lerDoubleMinimo("Taxa de especialidade: ", 0.0);
        clinica.adicionarRegisto(new ConsultaEspecialidade(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario,
                diagnostico, duracao, especialidade, taxa));
        System.out.println("Consulta de especialidade registada com o ID " + id + ".");
    }

    private Animal escolherAnimal() {
        if (clinica.getAnimais().isEmpty()) {
            System.out.println("E necessario registar um animal primeiro.");
            return null;
        }

        listarAnimais();
        int idAnimal = lerInteiroMinimo("ID do animal: ", 1);
        Animal animal = clinica.procurarAnimalPorId(idAnimal);
        if (animal == null) System.out.println("Animal nao encontrado.");
        return animal;
    }

    private void listarClientes() {
        if (clinica.getClientes().isEmpty()) {
            System.out.println("Nao existem clientes registados.");
            return;
        }
        System.out.println("Clientes:");
        for (Cliente cliente : clinica.getClientes()) System.out.println(cliente);
    }

    private void listarAnimais() {
        if (clinica.getAnimais().isEmpty()) {
            System.out.println("Nao existem animais registados.");
            return;
        }
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

    private String lerTextoObrigatorio(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("O valor nao pode ficar vazio.");
            } else if (valor.contains(";")) {
                System.out.println("O valor nao pode conter ponto e virgula.");
            } else {
                return valor;
            }
        }
    }

    private int lerInteiroMinimo(String mensagem, int minimo) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= minimo) return valor;
                System.out.println("Introduza um valor igual ou superior a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Introduza um numero inteiro.");
            }
        }
    }

    private double lerDoubleMinimo(String mensagem, double minimo) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (Double.isFinite(valor) && valor >= minimo) return valor;
                System.out.println("Introduza um valor igual ou superior a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Introduza um numero.");
            }
        }
    }

    private boolean lerBoolean(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (resposta.equals("s") || resposta.equals("sim")) return true;
            if (resposta.equals("n") || resposta.equals("nao")) return false;
            System.out.println("Resposta invalida. Use s ou n.");
        }
    }
}
