package test;

public class Officer extends Person{
    public int score;
    private int sex;

    public Officer(String name) {
        super(name);
    }

    public int getScore(String type){
        return score;
    }
    private int getSex(int man){
        return 1+man;
    }
}
