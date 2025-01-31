package kos_online.ui;

public class UserComboItem {
    private final int id;
    private final String name;
    
    public UserComboItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return name;
    }
}