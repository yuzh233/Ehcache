package ehcache;

/**
 * @Author: z.yu
 * @Date: 2018/06/12 9:59
 */
public class Dog {
    private long lou;
    private String name;
    private short leg;

    @Override
    public String toString() {
        return "Dog{" +
                "lou=" + lou +
                ", name='" + name + '\'' +
                ", leg=" + leg +
                '}';
    }

    public Dog() {
    }

    public Dog(long lou, String name, short leg) {
        this.lou = lou;
        this.name = name;
        this.leg = leg;
    }

    public long getLou() {
        return lou;
    }

    public void setLou(long lou) {
        this.lou = lou;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getLeg() {
        return leg;
    }

    public void setLeg(short leg) {
        this.leg = leg;
    }
}
