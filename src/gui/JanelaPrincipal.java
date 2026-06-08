package gui;

import ficheiros.GestorFicheiros;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import modelo.Animal;
import modelo.Cliente;
import modelo.Clinica;
import modelo.Consulta;
import modelo.ConsultaEspecialidade;
import modelo.RegistoClinico;
import modelo.Vacina;

@SuppressWarnings("serial")
public class JanelaPrincipal extends JFrame {
    private Clinica clinica;

    public JanelaPrincipal() {
        this.clinica = carregarClinicaInicial();
        configurarJanela();
        adicionarBotoes();
    }

    private Clinica carregarClinicaInicial() {
        try {
            return GestorFicheiros.carregarClinica();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível carregar os dados.\n" + e.getMessage()
                            + "\nA aplicação será iniciada com dados vazios.",
                    "Erro ao carregar dados",
                    JOptionPane.WARNING_MESSAGE);
            return new Clinica();
        }
    }

    private void configurarJanela() {
        setTitle("VetGest - Sistema de Gestão de Clínica Veterinária");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(560, 430);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sair();
            }
        });
    }

    private void adicionarBotoes() {
        JPanel painel = new JPanel(new GridLayout(0, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painel.add(criarBotao("Registar Cliente", this::registarCliente));
        painel.add(criarBotao("Listar Clientes", this::listarClientes));
        painel.add(criarBotao("Registar Animal", this::registarAnimal));
        painel.add(criarBotao("Listar Animais", this::listarAnimais));
        painel.add(criarBotao("Registar Consulta", this::registarConsulta));
        painel.add(criarBotao("Histórico de um Animal", this::listarHistoricoAnimal));
        painel.add(criarBotao("Registar Vacina", this::registarVacina));
        painel.add(criarBotao("Mostrar Total Faturado", this::mostrarTotalFaturado));
        painel.add(criarBotao("Registar Especialidade", this::registarConsultaEspecialidade));
        painel.add(criarBotao("Demonstrar Polimorfismo", this::demonstrarPolimorfismo));
        painel.add(criarBotao("Guardar Dados", () -> guardarDados(true)));
        painel.add(criarBotao("Sair do VetGest", this::sair));

        add(painel, BorderLayout.CENTER);
    }

    private JButton criarBotao(String texto, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    private void registarCliente() {
        String nome = pedirTextoObrigatorio("Nome do cliente:");
        if (nome == null) return;
        String telefone = pedirTextoObrigatorio("Telefone:");
        if (telefone == null) return;
        String email = pedirTextoObrigatorio("Email:");
        if (email == null) return;

        int id = clinica.proximoIdCliente();
        clinica.adicionarCliente(new Cliente(id, nome, telefone, email));
        mostrarInformacao("Cliente registado com o ID " + id + ".");
    }

    private void registarAnimal() {
        if (clinica.getClientes().isEmpty()) {
            mostrarAviso("É necessário registar um cliente primeiro.");
            return;
        }

        Integer idCliente = pedirInteiroMinimo("ID do dono:\n\n" + textoClientes(), 1);
        if (idCliente == null) return;
        if (clinica.procurarClientePorId(idCliente) == null) {
            mostrarAviso("Cliente não encontrado.");
            return;
        }

        String nome = pedirTextoObrigatorio("Nome do animal:");
        if (nome == null) return;
        String especie = pedirTextoObrigatorio("Espécie:");
        if (especie == null) return;
        String raca = pedirTextoObrigatorio("Raça:");
        if (raca == null) return;
        Integer idade = pedirInteiroMinimo("Idade:", 0);
        if (idade == null) return;

        int id = clinica.proximoIdAnimal();
        clinica.adicionarAnimal(new Animal(id, nome, especie, raca, idade, idCliente));
        mostrarInformacao("Animal registado com o ID " + id + ".");
    }

    private Animal escolherAnimal() {
        if (clinica.getAnimais().isEmpty()) {
            mostrarAviso("Não existem animais registados.");
            return null;
        }

        Integer idAnimal = pedirInteiroMinimo("ID do animal:\n\n" + textoAnimais(), 1);
        if (idAnimal == null) return null;

        Animal animal = clinica.procurarAnimalPorId(idAnimal);
        if (animal == null) mostrarAviso("Animal não encontrado.");
        return animal;
    }

    private void registarConsulta() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        String descricao = pedirTextoObrigatorio("Descrição da consulta:");
        if (descricao == null) return;
        Double precoBase = pedirDoubleMinimo("Preço base:", 0.0);
        if (precoBase == null) return;
        String veterinario = pedirTextoObrigatorio("Nome do veterinário:");
        if (veterinario == null) return;
        String diagnostico = pedirTextoObrigatorio("Diagnóstico:");
        if (diagnostico == null) return;
        Integer duracao = pedirInteiroMinimo("Duração em minutos:", 0);
        if (duracao == null) return;

        int id = clinica.proximoIdRegisto();
        clinica.adicionarRegisto(new Consulta(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario, diagnostico, duracao));
        mostrarInformacao("Consulta registada com o ID " + id + ".");
    }

    private void registarVacina() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        String descricao = pedirTextoObrigatorio("Descrição:");
        if (descricao == null) return;
        Double precoBase = pedirDoubleMinimo("Preço base:", 0.0);
        if (precoBase == null) return;
        String nomeVacina = pedirTextoObrigatorio("Nome da vacina:");
        if (nomeVacina == null) return;
        String lote = pedirTextoObrigatorio("Lote:");
        if (lote == null) return;

        int resposta = JOptionPane.showConfirmDialog(
                this, "É uma dose de reforço?", "Reforço", JOptionPane.YES_NO_CANCEL_OPTION);
        if (resposta == JOptionPane.CANCEL_OPTION || resposta == JOptionPane.CLOSED_OPTION) return;

        int id = clinica.proximoIdRegisto();
        boolean reforco = resposta == JOptionPane.YES_OPTION;
        clinica.adicionarRegisto(new Vacina(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), nomeVacina, lote, reforco));
        mostrarInformacao("Vacina registada com o ID " + id + ".");
    }

    private void registarConsultaEspecialidade() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        String descricao = pedirTextoObrigatorio("Descrição:");
        if (descricao == null) return;
        Double precoBase = pedirDoubleMinimo("Preço base:", 0.0);
        if (precoBase == null) return;
        String veterinario = pedirTextoObrigatorio("Veterinário:");
        if (veterinario == null) return;
        String diagnostico = pedirTextoObrigatorio("Diagnóstico:");
        if (diagnostico == null) return;
        Integer duracao = pedirInteiroMinimo("Duração em minutos:", 0);
        if (duracao == null) return;
        String especialidade = pedirTextoObrigatorio("Especialidade:");
        if (especialidade == null) return;
        Double taxa = pedirDoubleMinimo("Taxa de especialidade:", 0.0);
        if (taxa == null) return;

        int id = clinica.proximoIdRegisto();
        clinica.adicionarRegisto(new ConsultaEspecialidade(
                id, descricao, LocalDate.now(), precoBase, animal.getId(), veterinario,
                diagnostico, duracao, especialidade, taxa));
        mostrarInformacao("Consulta de especialidade registada com o ID " + id + ".");
    }

    private void listarClientes() {
        if (clinica.getClientes().isEmpty()) {
            mostrarAviso("Não existem clientes registados.");
            return;
        }
        mostrarTexto("Lista de clientes", textoClientes());
    }

    private void listarAnimais() {
        if (clinica.getAnimais().isEmpty()) {
            mostrarAviso("Não existem animais registados.");
            return;
        }
        mostrarTexto("Lista de animais", textoAnimais());
    }

    private String textoClientes() {
        StringBuilder texto = new StringBuilder();
        for (Cliente cliente : clinica.getClientes()) {
            texto.append(cliente).append('\n');
        }
        return texto.toString();
    }

    private String textoAnimais() {
        StringBuilder texto = new StringBuilder();
        for (Animal animal : clinica.getAnimais()) {
            Cliente dono = clinica.procurarClientePorId(animal.getIdCliente());
            String nomeDono = dono == null ? "Sem dono" : dono.getNome();
            texto.append(animal).append(" - Dono: ").append(nomeDono).append('\n');
        }
        return texto.toString();
    }

    private void listarHistoricoAnimal() {
        Animal animal = escolherAnimal();
        if (animal == null) return;

        ArrayList<RegistoClinico> historico = clinica.obterHistoricoAnimal(animal.getId());
        if (historico.isEmpty()) {
            mostrarAviso("Este animal não tem registos clínicos.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        for (RegistoClinico registo : historico) {
            texto.append(registo).append('\n');
        }
        mostrarTexto("Histórico de " + animal.getNome(), texto.toString());
    }

    private void mostrarTotalFaturado() {
        mostrarInformacao(String.format(
                Locale.ROOT, "Total faturado na clínica: %.2f euros", clinica.calcularTotalFaturado()));
    }

    private void demonstrarPolimorfismo() {
        if (clinica.getRegistosClinicos().isEmpty()) {
            mostrarAviso("Não existem registos clínicos.");
            return;
        }
        clinica.demonstrarPolimorfismo();
        mostrarInformacao("Polimorfismo executado. Consulte o terminal para ver o resultado.");
    }

    private boolean guardarDados(boolean mostrarSucesso) {
        try {
            GestorFicheiros.guardarClinica(clinica);
            if (mostrarSucesso) mostrarInformacao("Dados guardados com sucesso.");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this, "Erro ao guardar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void sair() {
        if (guardarDados(false)) {
            dispose();
        }
    }

    private String pedirTextoObrigatorio(String mensagem) {
        while (true) {
            String valor = JOptionPane.showInputDialog(this, mensagem);
            if (valor == null) return null;

            valor = valor.trim();
            if (valor.isEmpty()) {
                mostrarAviso("O valor não pode ficar vazio.");
            } else if (valor.contains(";")) {
                mostrarAviso("O valor não pode conter ponto e vírgula.");
            } else {
                return valor;
            }
        }
    }

    private Integer pedirInteiroMinimo(String mensagem, int minimo) {
        while (true) {
            String texto = JOptionPane.showInputDialog(this, mensagem);
            if (texto == null) return null;

            try {
                int valor = Integer.parseInt(texto.trim());
                if (valor >= minimo) return valor;
                mostrarAviso("Introduza um valor igual ou superior a " + minimo + ".");
            } catch (NumberFormatException e) {
                mostrarAviso("Introduza um número inteiro válido.");
            }
        }
    }

    private Double pedirDoubleMinimo(String mensagem, double minimo) {
        while (true) {
            String texto = JOptionPane.showInputDialog(this, mensagem);
            if (texto == null) return null;

            try {
                double valor = Double.parseDouble(texto.trim().replace(",", "."));
                if (Double.isFinite(valor) && valor >= minimo) return valor;
                mostrarAviso("Introduza um valor igual ou superior a " + minimo + ".");
            } catch (NumberFormatException e) {
                mostrarAviso("Introduza um número válido.");
            }
        }
    }

    private void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto, 14, 50);
        area.setEditable(false);
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarInformacao(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "VetGest", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
