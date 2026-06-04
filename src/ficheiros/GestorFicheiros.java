package ficheiros;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class GestorFicheiros {
    private static final Path PASTA_DADOS = Paths.get("data");
    private static final Path FICHEIRO_CLIENTES = PASTA_DADOS.resolve("clientes.txt");
    private static final Path FICHEIRO_ANIMAIS = PASTA_DADOS.resolve("animais.txt");
    private static final Path FICHEIRO_REGISTOS = PASTA_DADOS.resolve("registos.txt");

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
        Files.write(FICHEIRO_CLIENTES, linhas);
    }

    private static void guardarAnimais(ArrayList<Animal> animais) throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        for (Animal animal : animais) linhas.add(animal.saveTxt());
        Files.write(FICHEIRO_ANIMAIS, linhas);
    }

    private static void guardarRegistos(ArrayList<RegistoClinico> registos) throws IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        for (RegistoClinico registo : registos) linhas.add(registo.saveTxt());
        Files.write(FICHEIRO_REGISTOS, linhas);
    }

    private static void carregarClientes(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_CLIENTES)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_CLIENTES);
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                Cliente cliente = new Cliente();
                cliente.read(linha);
                clinica.adicionarCliente(cliente);
            }
        }
    }

    private static void carregarAnimais(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_ANIMAIS)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_ANIMAIS);
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                Animal animal = new Animal();
                animal.read(linha);
                clinica.adicionarAnimal(animal);
            }
        }
    }

    private static void carregarRegistos(Clinica clinica) throws IOException {
        if (!Files.exists(FICHEIRO_REGISTOS)) return;
        List<String> linhas = Files.readAllLines(FICHEIRO_REGISTOS);
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                RegistoClinico registo = criarRegistoPorTipo(linha);
                if (registo != null) clinica.adicionarRegisto(registo);
            }
        }
    }

    private static RegistoClinico criarRegistoPorTipo(String linha) {
        String[] partes = linha.split(";");
        RegistoClinico registo = null;

        if (partes[0].equals("CONSULTA")) registo = new Consulta();
        else if (partes[0].equals("VACINA")) registo = new Vacina();
        else if (partes[0].equals("ESPECIALIDADE")) registo = new ConsultaEspecialidade();

        if (registo != null) registo.read(linha);
        return registo;
    }
}
