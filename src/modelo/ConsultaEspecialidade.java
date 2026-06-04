package modelo;

import java.time.LocalDate;

public class ConsultaEspecialidade extends Consulta {
    private String especialidade;
    private double taxaEspecialidade;

    public ConsultaEspecialidade() {
        this(0, "Consulta de especialidade", LocalDate.now(), 45.0, 0, "Por definir", "Por definir", 45, "Dermatologia", 20.0);
    }

    public ConsultaEspecialidade(int id, String descricao, LocalDate data, double precoBase,
                                 String veterinario, String diagnostico, int duracaoMinutos,
                                 String especialidade, double taxaEspecialidade) {
        this(id, descricao, data, precoBase, 0, veterinario, diagnostico, duracaoMinutos, especialidade, taxaEspecialidade);
    }

    public ConsultaEspecialidade(int id, String descricao, LocalDate data, double precoBase, int idAnimal,
                                 String veterinario, String diagnostico, int duracaoMinutos,
                                 String especialidade, double taxaEspecialidade) {
        super(id, descricao, data, precoBase, idAnimal, veterinario, diagnostico, duracaoMinutos);
        this.especialidade = especialidade;
        this.taxaEspecialidade = taxaEspecialidade;
    }

    public ConsultaEspecialidade(ConsultaEspecialidade outra) {
        super(outra);
        this.especialidade = outra.especialidade;
        this.taxaEspecialidade = outra.taxaEspecialidade;
    }

    public String getEspecialidade() { return this.especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public double getTaxaEspecialidade() { return this.taxaEspecialidade; }
    public void setTaxaEspecialidade(double taxaEspecialidade) { this.taxaEspecialidade = taxaEspecialidade; }

    @Override
    public double calcularPrecoFinal() {
        return super.calcularPrecoFinal() + this.taxaEspecialidade;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "ConsultaEspecialidade{id=" + this.getId()
                + ", descricao='" + this.getDescricao() + '\''
                + ", data=" + this.getData()
                + ", precoFinal=" + this.calcularPrecoFinal()
                + ", idAnimal=" + this.getIdAnimal()
                + ", veterinario='" + this.getVeterinario() + '\''
                + ", diagnostico='" + this.getDiagnostico() + '\''
                + ", duracaoMinutos=" + this.getDuracaoMinutos()
                + ", especialidade='" + this.especialidade + '\''
                + ", taxaEspecialidade=" + this.taxaEspecialidade + '}';
    }

    @Override
    public ConsultaEspecialidade clone() {
        return new ConsultaEspecialidade(this);
    }

    @Override
    public String saveTxt() {
        return "ESPECIALIDADE;" + this.camposBaseTxt() + ";" + this.getVeterinario() + ";" + this.getDiagnostico() + ";" + this.getDuracaoMinutos() + ";" + this.especialidade + ";" + this.taxaEspecialidade;
    }

    @Override
    public void read(String linha) {
        String[] partes = linha.split(";");
        this.setId(Integer.parseInt(partes[1]));
        this.setDescricao(partes[2]);
        this.setData(LocalDate.parse(partes[3]));
        this.setPrecoBase(Double.parseDouble(partes[4]));
        this.setIdAnimal(Integer.parseInt(partes[5]));
        this.setVeterinario(partes[6]);
        this.setDiagnostico(partes[7]);
        this.setDuracaoMinutos(Integer.parseInt(partes[8]));
        this.especialidade = partes[9];
        this.taxaEspecialidade = Double.parseDouble(partes[10]);
    }
}
