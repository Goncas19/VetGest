package gui;

import ficheiros.GestorFicheiros;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final Clinica clinica;
    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JanelaPrincipal() {
        this.clinica = carregarClinicaInicial();
        configurarJanela();
        adicionarBotoes();
    }

    private Clinica carregarClinicaInicial() {
        try {
            return GestorFicheiros.carregarClinica();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível carregar os dados.\nA aplicação será iniciada com dados vazios.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return new Clinica();
        }
    }

    private void configurarJanela() {
        setTitle("VetGest - Sistema de Gestão de Clínica Veterinária");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 450);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { sair(); }
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

    private Cliente procurarDono() {
        if (clinica.getClientes().isEmpty()) {
            mostrarAviso("Ainda não existem clientes registados.");
            return null;
        }
        String termo = JOptionPane.showInputDialog(this, "Introduza o NIF ou Telemóvel do dono:");
        if (termo == null || termo.trim().isEmpty()) return null;
        
        for (Cliente c : clinica.getClientes()) {
            if (c.getNif().equals(termo.trim()) || c.getTelefone().equals(termo.trim())) {
                return c;
            }
        }
        mostrarAviso("Cliente não encontrado com esse NIF ou Telemóvel.");
        return null;
    }

    private Animal escolherAnimalDoDono(Cliente dono) {
        ArrayList<Animal> animaisDono = new ArrayList<>();
        for (Animal a : clinica.getAnimais()) {
            if (a.getIdCliente() == dono.getId()) animaisDono.add(a);
        }
        if (animaisDono.isEmpty()) {
            mostrarAviso("Este cliente não tem animais registados.");
            return null;
        }
        Object[] array = animaisDono.toArray();
        return (Animal) JOptionPane.showInputDialog(this, "Selecione o animal de " + dono.getNome() + ":",
                "Escolher Animal", JOptionPane.QUESTION_MESSAGE, null, array, array[0]);
    }

    private void registarCliente() {
        String nome = pedirTextoObrigatorio("Nome do cliente:");
        if (nome == null) return;
        String nif = pedirTextoObrigatorio("NIF:");
        if (nif == null) return;
        LocalDate dataNasc = pedirData("Data de Nascimento");
        if (dataNasc == null) return;
        String telefone = pedirTextoObrigatorio("Telefone:");
        if (telefone == null) return;
        String email = pedirTextoObrigatorio("Email:");
        if (email == null) return;

        int id = clinica.proximoIdCliente();
        clinica.adicionarCliente(new Cliente(id, nome, nif, dataNasc, telefone, email));
        mostrarInformacao("Cliente registado com o ID " + id + ".");
    }

    private void registarAnimal() {
        Cliente dono = procurarDono();
        if (dono == null) return;

        String nome = pedirTextoObrigatorio("Nome do Animal:");
        if (nome == null) return;
        String especie = pedirTextoObrigatorio("Espécie (ex: Cão, Gato):");
        if (especie == null) return;
        String raca = pedirTextoObrigatorio("Raça:");
        if (raca == null) return;
        LocalDate dataNasc = pedirData("Data de Nascimento do Animal");
        if (dataNasc == null) return;

        int id = clinica.proximoIdAnimal();
        clinica.adicionarAnimal(new Animal(id, nome, especie, raca, dataNasc, dono.getId()));
        mostrarInformacao("Animal registado com sucesso! Idade calculada: " + 
            clinica.procurarAnimalPorId(id).getIdade() + " anos.");
    }

    private void registarConsulta() {
        Cliente dono = procurarDono();
        if (dono == null) return;
        Animal animal = escolherAnimalDoDono(dono);
        if (animal == null) return;

        String[] servicos = {
            "Consulta de Rotina / Check-up - 30.0€ - 30min", 
            "Consulta Pediátrica - 25.0€ - 30min",
            "Consulta de Urgência - 50.0€ - 45min", 
            "Consulta ao Domicílio - 60.0€ - 60min",
            "Revisão Médica - 15.0€ - 15min",
            "Aconselhamento Nutricional - 25.0€ - 30min",
            "Análises Clínicas (Sangue/Urina) - 35.0€ - 20min",
            "Desparasitação - 15.0€ - 15min",
            "Colocação de Microchip - 35.0€ - 15min",
            "Banhos e Tosquias (Estética) - 35.0€ - 90min"
        };
        String servicoEscolhido = escolherOpcao("Serviço:", servicos);
        if (servicoEscolhido == null) return;

        String[] vets = {
            "Dr. João Mendes (Geral)", 
            "Dra. Maria Silva (Geral)", 
            "Enf. Carlos Santos (Estética/Triagem)", 
            "Dra. Inês Costa (Urgências/Domicílios)"
        };
        String vetEscolhido = escolherOpcao("Veterinário:", vets);
        if (vetEscolhido == null) return;

        String diagnostico = pedirTextoObrigatorio("Diagnóstico:");
        if (diagnostico == null) return;


        String[] partes = servicoEscolhido.split(" - ");
        String descricao = partes[0];
        double precoBase = Double.parseDouble(partes[1].replace("€", ""));
        int duracao = Integer.parseInt(partes[2].replace("min", ""));

        int id = clinica.proximoIdRegisto();
        clinica.adicionarRegisto(new Consulta(id, descricao, LocalDate.now(), precoBase, animal.getId(), vetEscolhido, diagnostico, duracao));
        mostrarInformacao("Consulta registada com sucesso!");
    }

    private void registarVacina() {
        Cliente dono = procurarDono();
        if (dono == null) return;
        Animal animal = escolherAnimalDoDono(dono);
        if (animal == null) return;

        String especie = animal.getEspecie().toLowerCase(Locale.ROOT);
        String[] vacinas;

        if (especie.contains("cão") || especie.contains("cao") || especie.contains("cadela") || especie.contains("canideo") || especie.contains("canídeo")) {
            vacinas = new String[]{
                "Polivalente (Esgana, Parvo, Hepatite, Lepto) - 35.0€",
                "Esgana (Distemper Canino) - 20.0€",
                "Parvovirose - 20.0€",
                "Hepatite Infecciosa Canina - 20.0€",
                "Leptospirose - 25.0€",
                "Raiva - 20.0€",
                "Tosse do Canil (Bordetella/Parainfluenza) - 25.0€",
                "Leishmaniose - 35.0€",
                "Herpesvírus Canino - 30.0€",
                "Doença de Lyme (Borreliose) - 30.0€"
            };
        } else if (especie.contains("gato") || especie.contains("gata") || especie.contains("felino")) {
            vacinas = new String[]{
                "Tripla Felina (Panleuco, Herpes, Calicivírus) - 35.0€",
                "Panleucopenia Felina - 20.0€",
                "Herpesvírus Felino (Rinotraqueíte) - 20.0€",
                "Calicivírus Felino - 20.0€",
                "Leucemia Felina (FeLV)- 25.0€",
                "Raiva - 20.0€",
                "Clamidiose Felina - 25.0€",
                "Bordetella - 25.0€"
            };
        } else {
            mostrarAviso("Esta clínica atende exclusivamente cães e gatos. Não existem vacinas para a espécie: " + animal.getEspecie());
            return;
        }

        String vacinaEscolhida = escolherOpcao("Vacina para " + animal.getEspecie() + ":", vacinas);
        if (vacinaEscolhida == null) return;

        String lote = pedirTextoObrigatorio("Lote da Vacina:");
        if (lote == null) return;

        int resposta = JOptionPane.showConfirmDialog(this, "É uma dose de reforço?", "Reforço", JOptionPane.YES_NO_CANCEL_OPTION);
        if (resposta == JOptionPane.CANCEL_OPTION || resposta == JOptionPane.CLOSED_OPTION) return;

        String nomeVacina = vacinaEscolhida.split(" - ")[0];
        double precoBase = Double.parseDouble(vacinaEscolhida.split(" - ")[1].replace("€", ""));

        int id = clinica.proximoIdRegisto();
        clinica.adicionarRegisto(new Vacina(id, "Vacinação: " + nomeVacina, LocalDate.now(), precoBase, animal.getId(), nomeVacina, lote, resposta == JOptionPane.YES_OPTION));
        mostrarInformacao("Vacina registada com sucesso!");
    }

    private void registarConsultaEspecialidade() {
        Cliente dono = procurarDono();
        if (dono == null) return;
        Animal animal = escolherAnimalDoDono(dono);
        if (animal == null) return;

        String[] servicos = {
            "Consulta de Especialidade - 50.0€ - 45min", 
            "Revisão Especializada - 35.0€ - 30min",
            "Esterilização / Castração - 120.0€ - 90min",
            "Cirurgia Geral - 150.0€ - 120min",
            "Destartarização - 80.0€ - 60min",
            "Ecografia - 55.0€ - 30min",
            "Ecocardiografia - 75.0€ - 45min",
            "Radiografia - 45.0€ - 30min",
            "Internamento (Diária) - 60.0€ - 1440min"
        };
        String servicoEscolhido = escolherOpcao("Serviço Base:", servicos);
        if (servicoEscolhido == null) return;

        String[] especialidades = {
            "Geral / Sem Especialidade - Taxa: 0.0€",
            "Dermatologia - Taxa: 15.0€", 
            "Ortopedia - Taxa: 35.0€", 
            "Cardiologia - Taxa: 30.0€",
            "Oftalmologia - Taxa: 25.0€",
            "Odontologia / Estomatologia - Taxa: 20.0€",
            "Oncologia - Taxa: 50.0€",
            "Neurologia - Taxa: 45.0€",
            "Animais Exóticos - Taxa: 25.0€",
            "Cirurgia Avançada - Taxa: 80.0€"
        };
        String espEscolhida = escolherOpcao("Especialidade:", especialidades);
        if (espEscolhida == null) return;

        String[] vets = {
            "Dra. Ana Lopes (Dermatologia)", 
            "Dr. Pedro Santana (Ortopedia/Cirurgia)", 
            "Dra. Sofia Carvalho (Cardio/Ecografia)", 
            "Dr. Miguel Oliveira (Oftalmologia)", 
            "Dra. Beatriz Marques (Onco/Neuro)", 
            "Dr. Rui Moura (Animais Exóticos)"
        };
        String vetEscolhido = escolherOpcao("Veterinário Especialista:", vets);
        if (vetEscolhido == null) return;

        String diagnostico = pedirTextoObrigatorio("Diagnóstico:");
        if (diagnostico == null) return;

        String descricao = servicoEscolhido.split(" - ")[0];
        double precoBase = Double.parseDouble(servicoEscolhido.split(" - ")[1].replace("€", ""));
        int duracao = Integer.parseInt(servicoEscolhido.split(" - ")[2].replace("min", ""));
        
        String nomeEsp = espEscolhida.split(" - ")[0];
        double taxa = Double.parseDouble(espEscolhida.split("Taxa: ")[1].replace("€", ""));

        int id = clinica.proximoIdRegisto();
        clinica.adicionarRegisto(new ConsultaEspecialidade(id, descricao + " (" + nomeEsp + ")", LocalDate.now(), precoBase, animal.getId(), vetEscolhido, diagnostico, duracao, nomeEsp, taxa));
        mostrarInformacao("Consulta de especialidade registada com sucesso!");
    }


    private void listarClientes() {
        if (clinica.getClientes().isEmpty()) { mostrarAviso("Não existem clientes registados."); return; }
        StringBuilder texto = new StringBuilder();
        for (Cliente c : clinica.getClientes()) texto.append(c).append('\n');
        mostrarTexto("Lista de clientes", texto.toString());
    }

    private void listarAnimais() {
        if (clinica.getAnimais().isEmpty()) { mostrarAviso("Não existem animais registados."); return; }
        StringBuilder texto = new StringBuilder();
        for (Animal a : clinica.getAnimais()) {
            Cliente dono = clinica.procurarClientePorId(a.getIdCliente());
            texto.append(a).append(" - Dono: ").append(dono != null ? dono.getNome() : "Sem dono").append('\n');
        }
        mostrarTexto("Lista de animais", texto.toString());
    }

    private void listarHistoricoAnimal() {
        Cliente dono = procurarDono();
        if (dono == null) return;
        Animal animal = escolherAnimalDoDono(dono);
        if (animal == null) return;

        ArrayList<RegistoClinico> hist = clinica.obterHistoricoAnimal(animal.getId());
        if (hist.isEmpty()) { mostrarAviso("Sem histórico."); return; }

        StringBuilder texto = new StringBuilder();
        for (RegistoClinico r : hist) texto.append(r).append('\n');
        mostrarTexto("Histórico de " + animal.getNome(), texto.toString());
    }

    private void mostrarTotalFaturado() {
        mostrarInformacao(String.format(Locale.ROOT, "Total faturado na clínica: %.2f euros", clinica.calcularTotalFaturado()));
    }

    private void demonstrarPolimorfismo() {
        if (clinica.getRegistosClinicos().isEmpty()) { 
            mostrarAviso("Não existem registos para demonstrar."); 
            return; 
        }

        StringBuilder texto = new StringBuilder();
        texto.append("O Polimorfismo em ação! A mesma lista contém objetos diferentes:\n\n");

        // O Polimorfismo acontece aqui: O Java decide sozinho se chama 
        // o toString() da Consulta, da Vacina ou da Especialidade!
        for (RegistoClinico registo : clinica.getRegistosClinicos()) {
            texto.append(registo.toString()).append("\n");
        }

        mostrarTexto("Demonstração de Polimorfismo", texto.toString());
    }

    private boolean guardarDados(boolean mostrarSucesso) {
        try {
            GestorFicheiros.guardarClinica(clinica);
            if (mostrarSucesso) mostrarInformacao("Dados guardados com sucesso!");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void sair() {
        if (guardarDados(false)) dispose();
    }

    private String pedirTextoObrigatorio(String mensagem) {
        while (true) {
            String valor = JOptionPane.showInputDialog(this, mensagem);
            if (valor == null) return null;
            if (valor.trim().isEmpty() || valor.contains(";")) mostrarAviso("Inválido/Vazio ou contém ';'");
            else return valor.trim();
        }
    }

    private LocalDate pedirData(String titulo) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, titulo + " (Formato: dd/mm/aaaa):");
            if (input == null) return null;
            try {
                return LocalDate.parse(input.trim(), formatadorData);
            } catch (Exception e) {
                mostrarAviso("Data inválida. Por favor utilize o formato exato: dd/mm/aaaa (Ex: 15/04/2010)");
            }
        }
    }

    private String escolherOpcao(String titulo, String[] opcoes) {
        return (String) JOptionPane.showInputDialog(this, titulo, "Seleção", 
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    private void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto, 14, 50);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarInformacao(String msg) { JOptionPane.showMessageDialog(this, msg, "VetGest", JOptionPane.INFORMATION_MESSAGE); }
    private void mostrarAviso(String msg) { JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE); }
}
