package gui;

import modelo.*;
import ficheiros.GestorFicheiros;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JanelaPrincipal extends JFrame {
    private Clinica clinica;

    public JanelaPrincipal() {
        // 1. Tenta carregar automaticamente os dados guardados ao abrir
        try {
            clinica = GestorFicheiros.carregarClinica();
        } catch (IOException e) {
            clinica = new Clinica();
            JOptionPane.showMessageDialog(this, "Aviso: Ficheiros de dados não encontrados. A iniciar base de dados vazia.");
        }

        // 2. Configuração Básica da Janela Principal
        setTitle("VetGest - Sistema de Gestão de Clínica Veterinária");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra no ecrã
        
        // Organiza os botões em 7 linhas e 2 colunas de forma organizada
        setLayout(new GridLayout(7, 2, 10, 10)); 

        // 3. Criação de Todos os Botões Baseados no MenuConsola
        JButton btnRegCliente = new JButton("Registar Cliente");
        JButton btnRegAnimal = new JButton("Registar Animal");
        JButton btnRegConsulta = new JButton("Registar Consulta");
        JButton btnRegVacina = new JButton("Registar Vacina");
        JButton btnRegEspecialidade = new JButton("Registar Especialidade");
        JButton btnListClientes = new JButton("Listar Clientes");
        JButton btnListAnimais = new JButton("Listar Animais");
        JButton btnHistAnimal = new JButton("Histórico de um Animal");
        JButton btnFaturacao = new JButton("Mostrar Total Faturado");
        JButton btnPolimorfismo = new JButton("Demonstrar Polimorfismo");
        JButton btnGuardar = new JButton("Guardar Dados");
        JButton btnSair = new JButton("Sair do VetGest");

        // 4. Associação das Ações aos Botões
        btnRegCliente.addActionListener(e -> registarCliente());
        btnRegAnimal.addActionListener(e -> registarAnimal());
        btnRegConsulta.addActionListener(e -> registarConsulta());
        btnRegVacina.addActionListener(e -> registarVacina());
        btnRegEspecialidade.addActionListener(e -> registarConsultaEspecialidade());
        btnListClientes.addActionListener(e -> listarClientes());
        btnListAnimais.addActionListener(e -> listarAnimais());
        btnHistAnimal.addActionListener(e -> listarHistoricoAnimal());
        
        btnFaturacao.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Total Faturado na Clínica: " + clinica.calcularTotalFaturado() + " euros");
        });

        btnPolimorfismo.addActionListener(e -> {
            clinica.demonstrarPolimorfismo();
            JOptionPane.showMessageDialog(this, "Polimorfismo executado! Verifique o terminal do VS Code para ver o output do método print().");
        });

        btnGuardar.addActionListener(e -> guardarDados());
        
        btnSair.addActionListener(e -> {
            guardarDados(); // Guarda por segurança antes de fechar
            System.exit(0);
        });

        // 5. Adicionar os Elementos ao Ecrã Gráfico
        add(btnRegCliente); add(btnListClientes);
        add(btnRegAnimal);  add(btnListAnimais);
        add(btnRegConsulta); add(btnHistAnimal);
        add(btnRegVacina);   add(btnFaturacao);
        add(btnRegEspecialidade); add(btnPolimorfismo);
        add(btnGuardar);     add(new JLabel("")); // Espaço vazio para alinhar
        add(btnSair);
    }

    // --- MÉTODOS AUXILIARES DE INTERFAZAÇÃO (Substitutos seguros do Scanner) ---

    private void registarCliente() {
        int id = clinica.proximoIdCliente();
        String nome = JOptionPane.showInputDialog(this, "Nome do Cliente:");
        if (nome == null || nome.trim().isEmpty()) return;
        String telefone = JOptionPane.showInputDialog(this, "Telefone:");
        String email = JOptionPane.showInputDialog(this, "Email:");

        clinica.adicionarCliente(new Cliente(id, nome, telefone, email));
        JOptionPane.showMessageDialog(this, "Cliente registado com o ID " + id + ".");
    }

    private void registarAnimal() {
        if (clinica.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "É necessário registar um cliente primeiro!");
            return;
        }
        String idDonoStr = JOptionPane.showInputDialog(this, "Introduza o ID do Dono (Cliente):");
        if (idDonoStr == null) return;
        int idCliente = Integer.parseInt(idDonoStr);

        if (clinica.procurarClientePorId(idCliente) == null) {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
            return;
        }

        int id = clinica.proximoIdAnimal();
        String nome = JOptionPane.showInputDialog(this, "Nome do Animal:");
        String especie = JOptionPane.showInputDialog(this, "Espécie:");
        String raca = JOptionPane.showInputDialog(this, "Raça:");
        int idade = Integer.parseInt(JOptionPane.showInputDialog(this, "Idade:"));

        clinica.adicionarAnimal(new Animal(id, nome, especie, raca, idade, idCliente));
        JOptionPane.showMessageDialog(this, "Animal registado com o ID " + id + ".");
    }

    private Animal escolherAnimal() {
        if (clinica.getAnimais().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem animais registados.");
            return null;
        }
        String idAnimalStr = JOptionPane.showInputDialog(this, "Introduza o ID do Animal:");
        if (idAnimalStr == null) return null;
        int idAnimal = Integer.parseInt(idAnimalStr);
        Animal animal = clinica.procurarAnimalPorId(idAnimal);
        if (animal == null) JOptionPane.showMessageDialog(this, "Animal não encontrado.");
        return animal;
    }

    private void registarConsulta() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = JOptionPane.showInputDialog(this, "Descrição da Consulta:");
        double precoBase = Double.parseDouble(JOptionPane.showInputDialog(this, "Preço Base:"));
        String veterinario = JOptionPane.showInputDialog(this, "Nome do Veterinário:");
        String diagnostico = JOptionPane.showInputDialog(this, "Diagnóstico:");
        int duracao = Integer.parseInt(JOptionPane.showInputDialog(this, "Duração (minutos):"));

        RegistoClinico consulta = new Consulta(id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao);
        clinica.adicionarRegisto(consulta);
        JOptionPane.showMessageDialog(this, "Consulta registada com o ID " + id + ".");
    }

    private void registarVacina() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = JOptionPane.showInputDialog(this, "Descrição:");
        double precoBase = Double.parseDouble(JOptionPane.showInputDialog(this, "Preço Base:"));
        String nomeVacina = JOptionPane.showInputDialog(this, "Nome da Vacina:");
        String lote = JOptionPane.showInputDialog(this, "Lote:");
        int resp = JOptionPane.showConfirmDialog(this, "É uma dose de reforço?", "Reforço", JOptionPane.YES_NO_OPTION);
        boolean reforco = (resp == JOptionPane.YES_OPTION);

        RegistoClinico vacina = new Vacina(id, descricao, LocalDate.now(), precoBase, animal.getId(), nomeVacina, lote, reforco);
        clinica.adicionarRegisto(vacina);
        JOptionPane.showMessageDialog(this, "Vacina registada com o ID " + id + ".");
    }

    private void registarConsultaEspecialidade() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        int id = clinica.proximoIdRegisto();
        String descricao = JOptionPane.showInputDialog(this, "Descrição:");
        double precoBase = Double.parseDouble(JOptionPane.showInputDialog(this, "Preço Base:"));
        String veterinario = JOptionPane.showInputDialog(this, "Veterinário:");
        String diagnostico = JOptionPane.showInputDialog(this, "Diagnóstico:");
        int duracao = Integer.parseInt(JOptionPane.showInputDialog(this, "Duração (minutos):"));
        String especialidade = JOptionPane.showInputDialog(this, "Especialidade (ex: Cardiologia):");
        double taxa = Double.parseDouble(JOptionPane.showInputDialog(this, "Taxa de Especialidade:"));

        RegistoClinico consulta = new ConsultaEspecialidade(id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao, especialidade, taxa);
        clinica.adicionarRegisto(consulta);
        JOptionPane.showMessageDialog(this, "Consulta de especialidade registada com o ID " + id + ".");
    }

    private void listarClientes() {
        if (clinica.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem clientes registados.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== LISTA DE CLIENTES ===\n");
        for (Cliente c : clinica.getClientes()) sb.append(c.toString()).append("\n");
        JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()));
    }

    private void listarAnimais() {
        if (clinica.getAnimais().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existem animais registados.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== LISTA DE ANIMAIS ===\n");
        for (Animal a : clinica.getAnimais()) {
            Cliente dono = clinica.procurarClientePorId(a.getIdCliente());
            String nomeDono = (dono == null) ? "Sem Dono" : dono.getNome();
            sb.append(a.toString()).append(" - Dono: ").append(nomeDono).append("\n");
        }
        JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()));
    }

    private void listarHistoricoAnimal() {
        Animal animal = escolherAnimal();
        if (animal == null) return;
        ArrayList<RegistoClinico> historico = clinica.obterHistoricoAnimal(animal.getId());
        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este animal não tem registos clínicos.");
            return;
        }
        StringBuilder sb = new StringBuilder("Histórico de " + animal.getNome() + ":\n\n");
        for (RegistoClinico r : historico) sb.append(r.toString()).append("\n");
        JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()));
    }

    private void guardarDados() {
        try {
            GestorFicheiros.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao guardar dados: " + e.getMessage());
        }
    }
}