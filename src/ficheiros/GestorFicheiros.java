package ficheiros;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import modelo.Animal;
import modelo.Cliente;
import modelo.Clinica;
import modelo.Consulta;
import modelo.ConsultaEspecialidade;
import modelo.RegistoClinico;
import modelo.Vacina;

public final class GestorFicheiros {
    private static final Path PASTA_DADOS = Paths.get("data");
    private static final Path FICHEIRO_CLIENTES = PASTA_DADOS.resolve("clientes.txt");
    private static final Path FICHEIRO_ANIMAIS = PASTA_DADOS.resolve("animais.txt");
    private static final Path FICHEIRO_REGISTOS = PASTA_DADOS.resolve("registos.txt");

    private GestorFicheiros() {
    }

    public static void guardarClinica(Clinica clinica) throws IOException {
        Files.createDirectories(PASTA_DADOS);
        guardarClientes(clinica.getClientes());
        guardarAnimais(clinica.getAnimais());
        guardarRegistos(clinica.getRegistosClinicos());
    }

    public static Clinica carregarClinica() throws IOException {
        Clinica clinica = new Clinica();
        carregarClientes(clinica);
        carregarAnimais(clinica);
        carregarRegistos(clinica);
        return clinica;
    }

    private static void guardarClientes(ArrayList<Cliente> clientes) throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        for (Cliente cliente : clientes) linhas.add(cliente.saveTxt());
        Files.write(FICHEIRO_CLIENTES, linhas, StandardCharsets.UTF_8);
    }

    private static void guardarAnimais(ArrayList<Animal> animais) throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        for (Animal animal : animais) linhas.add(animal.saveTxt());
        Files.write(FICHEIRO_ANIMAIS, linhas, StandardCharsets.UTF_8);
    }

    private static void guardarRegistos(ArrayList<RegistoClinico> registos) throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        for (RegistoClinico registo : registos) linhas.add(registo.saveTxt());
        Files.write(FICHEIRO_REGISTOS, linhas, StandardCharsets.UTF_8);
    }

    private static void carregarClientes(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_CLIENTES)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_CLIENTES, StandardCharsets.UTF_8);
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (!linha.trim().isEmpty()) {
                try {
                    Cliente cliente = new Cliente();
                    cliente.read(linha);
                    clinica.adicionarCliente(cliente);
                } catch (RuntimeException e) {
                    throw erroLinha(FICHEIRO_CLIENTES, i + 1, e);
                }
            }
        }
    }

    private static void carregarAnimais(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_ANIMAIS)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_ANIMAIS, StandardCharsets.UTF_8);
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (!linha.trim().isEmpty()) {
                try {
                    Animal animal = new Animal();
                    animal.read(linha);
                    clinica.adicionarAnimal(animal);
                } catch (RuntimeException e) {
                    throw erroLinha(FICHEIRO_ANIMAIS, i + 1, e);
                }
            }
        }
    }

    private static void carregarRegistos(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_REGISTOS)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_REGISTOS, StandardCharsets.UTF_8);
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (!linha.trim().isEmpty()) {
                try {
                    RegistoClinico registo = criarRegistoPorTipo(linha);
                    clinica.adicionarRegisto(registo);
                } catch (RuntimeException e) {
                    throw erroLinha(FICHEIRO_REGISTOS, i + 1, e);
                }
            }
        }
    }

    private static RegistoClinico criarRegistoPorTipo(String linha) {
        String[] partes = linha.split(";", 2);
        if (partes.length == 0) {
            throw new IllegalArgumentException("Tipo de registo clinico em falta.");
        }

        RegistoClinico registo;
        if ("CONSULTA".equals(partes[0])) {
            registo = new Consulta();
        } else if ("VACINA".equals(partes[0])) {
            registo = new Vacina();
        } else if ("ESPECIALIDADE".equals(partes[0])) {
            registo = new ConsultaEspecialidade();
        } else {
            throw new IllegalArgumentException("Tipo de registo clinico desconhecido: " + partes[0]);
        }

        registo.read(linha);
        return registo;
    }

    private static IOException erroLinha(Path ficheiro, int numeroLinha, RuntimeException causa) {
        return new IOException(
                "Dados invalidos em " + ficheiro + ", linha " + numeroLinha + ": " + causa.getMessage(),
                causa);
    }
}
