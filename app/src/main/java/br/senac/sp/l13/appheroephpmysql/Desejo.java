package br.senac.sp.l13.appheroephpmysql;

public class Desejo {
    private int id;
    private String name, desejo, prioridade;

    public Desejo() {
    }

    public Desejo(int id, String name, String realname, int rating, String teamaffiliation) {
        this.id = id;
        this.name = name;
        this.desejo = desejo;
        this.prioridade = prioridade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesejo() {
        return desejo;
    }

    public void setDesejo(String desejo) {
        this.desejo = desejo;
    }


    public String getPrioridade() {
        return prioridade;
    }

    public void setTeamaffiliation(String teamaffiliation) {
        this.prioridade = prioridade;
    }
}
