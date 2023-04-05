import java.util.Objects;

public class Pair<String, Double> {
    private String nameFile;
    private Double lineFile;

    public Pair(String nameFile, Double lineFile) {
        this.nameFile = nameFile;
        this.lineFile = lineFile;
    }

    public String getNameFile() {
        return this.nameFile;
    }

    public Double getLineFile() {
        return this.lineFile;
    }

    public Pair<String,Double> get() {
        return new Pair<>(nameFile,lineFile);
    }

    @Override
    public boolean equals(Object o) {
        Pair<String, Double> obj;
        obj = (Pair<String, Double>)o;
        return nameFile.equals(obj.nameFile);
    }

}
