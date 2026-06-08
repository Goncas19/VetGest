package modelo;

import java.time.LocalDate;

public class Vacina extends RegistoClinico {
    private String nomeVacina;
    private String lote;
    private boolean doseReforco;

    public Vacina() {
        this(0, "Vacinacao", LocalDate.now(), 20.0, 0, "Por definir", "Sem lote", false);
    }

    public Vacina(int id, String descricao, LocalDate data, double precoBase,
                  String nomeVacina, String lote, boolean doseReforco) {
        this(id, descricao, data, precoBase, 0, nomeVacina, lote, doseReforco);
    }

    public Vacina(int id, String descricao, LocalDate data, double precoBase, int idAnimal,
                  String nomeVacina, String lote, boolean doseReforco) {
        super(id, descricao, data, precoBase, idAnimal);
        this.nomeVacina = nomeVacina;
        this.lote = lote;
        this.doseReforco = doseReforco;
    }

    public Vacina(Vacina outra) {
        super(outra);
        this.nomeVacina = outra.nomeVacina;
        this.lote = outra.lote;
        this.doseReforco = outra.doseReforco;
    }

    public String getNomeVacina() { return this.nomeVacina; }
    public void setNomeVacina(String nomeVacina) { this.nomeVacina = nomeVacina; }
    public String getLote() { return this.lote; }
    public void setLote(String lote) { this.lote = lote; }
    public boolean isDoseReforco() { return this.doseReforco; }
    public void setDoseReforco(boolean doseReforco) { this.doseReforco = doseReforco; }

    @Override
    public double calcularPrecoFinal() {
        return this.doseReforco ? this.precoBase + 5.0 : this.precoBase;
    }

    @Override
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Vacina{id=" + this.id
                + ", descricao='" + this.descricao + '\''
                + ", data=" + this.data
                + ", precoFinal=" + this.calcularPrecoFinal()
                + ", idAnimal=" + this.idAnimal
                + ", nomeVacina='" + this.nomeVacina + '\''
                + ", lote='" + this.lote + '\''
                + ", doseReforco=" + this.doseReforco + '}';
    }

    @Override
    public Vacina clone() {
        return new Vacina(this);
    }

    @Override
    public String saveTxt() {
        return "VACINA;" + this.camposBaseTxt() + ";" + this.nomeVacina + ";" + this.lote + ";" + this.doseReforco;
    }

    @Override
    public void read(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length != 9 || !"VACINA".equals(partes[0])) {
            throw new IllegalArgumentException("Formato de vacina invalido.");
        }
        if (!"true".equalsIgnoreCase(partes[8]) && !"false".equalsIgnoreCase(partes[8])) {
            throw new IllegalArgumentException("Valor da dose de reforco invalido.");
        }
        this.id = Integer.parseInt(partes[1]);
        this.descricao = partes[2];
        this.data = LocalDate.parse(partes[3]);
        this.precoBase = Double.parseDouble(partes[4]);
        this.idAnimal = Integer.parseInt(partes[5]);
        this.nomeVacina = partes[6];
        this.lote = partes[7];
        this.doseReforco = Boolean.parseBoolean(partes[8]);
    }
}
