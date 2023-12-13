package module.assistant;

public class SiriCommandEntity {

    private String id;

    private String text;

    //置信度，可靠性
    private int reliability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReliability() {
        return reliability;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "SiriCommandEntity{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", reliability=" + reliability +
                '}';
    }
}
