package modelo;

import java.time.LocalDate;

public class Consulta extends RegistoClinico {
    private String veterinario;
    private String diagnostico;
    private int duracaoMinutos;

    public Consulta() {
        this(0, "Consulta geral", LocalDate.now(), 30.0, 0, "Por definir", "Por definir", 30);
    }

    public Consulta(int id, String descricao, LocalDate data, double precoBase,
                    String veterinario, String diagnostico, int duracaoMinutos) {
        this(id, descricao, data, precoBase, 0, veterinario, diagnostico, duracaoMinutos);
    }

    public Consulta(int id, String descricao, LocalDate data, double precoBase, int idAnimal,
                    String veterinario, String diagnostico, int duracaoMinutos) {
        super(id, descricao, data, precoBase, idAnimal);
        this.veterinario = veterinario;
        this.diagnostico = diagnostico;
        this.duracaoMinutos = duracaoMinutos;
    }

    public Consulta(Consulta outra) {
        super(outra);
        this.veterinario = outra.veterinario;
        this.diagnostico = outra.diagnostico;
        this.duracaoMinutos = outra.duracaoMinutos;
    }

    public String getVeterinario() { return this.veterinario; }
    public void setVeterinario(String veterinario) { this.veterinario = veterinario; }
    public String getDiagnostico() { return this.diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public int getDuracaoMinutos() { return this.duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    @Override
    public double calcularPrecoFinal() {
        if (this.duracaoMinutos > 60) return this.precoBase + 15.0;
        return this.precoBase;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Consulta{id=" + this.id
                + ", descricao='" + this.descricao + '\''
                + ", data=" + this.data
                + ", precoFinal=" + this.calcularPrecoFinal()
                + ", idAnimal=" + this.idAnimal
                + ", veterinario='" + this.veterinario + '\''
                + ", diagnostico='" + this.diagnostico + '\''
                + ", duracaoMinutos=" + this.duracaoMinutos + '}';
    }

    @Override
    public Consulta clone() {
        return new Consulta(this);
    }

    @Override
    public String saveTxt() {
        return "CONSULTA;" + this.camposBaseTxt() + ";" + this.veterinario + ";" + this.diagnostico + ";" + this.duracaoMinutos;
    }

    @Override
    public void read(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length != 9 || !"CONSULTA".equals(partes[0])) {
            throw new IllegalArgumentException("Formato de consulta invalido.");
        }
        this.id = Integer.parseInt(partes[1]);
        this.descricao = partes[2];
        this.data = LocalDate.parse(partes[3]);
        this.precoBase = Double.parseDouble(partes[4]);
        this.idAnimal = Integer.parseInt(partes[5]);
        this.veterinario = partes[6];
        this.diagnostico = partes[7];
        this.duracaoMinutos = Integer.parseInt(partes[8]);
    }
}
