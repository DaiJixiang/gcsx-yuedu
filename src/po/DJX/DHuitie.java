package po.DJX;

public class DHuitie {
    private int id;
    private int tiezi_id;
    private int huitieren_id;
    private String huitie_time;
    private   String huitie_content;
    private int like_click;
    private int duixiang_id;
    private String author_name;


    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int h_id) {
        this.id = h_id;
    }

    public int getTiezi_id() {
        return tiezi_id;
    }

    public void setTiezi_id(int tiezi_id) {
        this.tiezi_id = tiezi_id;
    }

    public int getHuitieren_id() {
        return huitieren_id;
    }

    public void setHuitieren_id(int huitieren_id) {
        this.huitieren_id = huitieren_id;
    }

    public String getHuitie_time() {
        return huitie_time;
    }

    public void setHuitie_time(String huitie_time) {
        this.huitie_time = huitie_time;
    }

    public String getHuitie_content() {
        return huitie_content;
    }

    public void setHuitie_content(String huitie_content) {
        this.huitie_content = huitie_content;
    }

    public int getLike_click() {
        return like_click;
    }

    public void setLike_click(int like_click) {
        this.like_click = like_click;
    }

    public int getDuixiang_id() {
        return duixiang_id;
    }

    public void setDuixiang_id(int duixiang_id) {
        this.duixiang_id = duixiang_id;
    }

    @Override
    public String toString() {
        return "Huitie{" +
                "id=" + id +
                ", tiezi_id=" + tiezi_id +
                ", huitieren_id=" + huitieren_id +
                ", huitie_time='" + huitie_time + '\'' +
                ", huitie_content='" + huitie_content + '\'' +
                ", like_click=" + like_click +
                ", duixiang_id=" + duixiang_id +
                '}';
    }
}
