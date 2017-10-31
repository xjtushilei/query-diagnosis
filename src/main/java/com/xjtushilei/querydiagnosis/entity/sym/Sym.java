package com.xjtushilei.querydiagnosis.entity.sym;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class Sym {
    private String name;
    private float rate;

    public Sym(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sym)) {
            return false;
        }

        Sym sym = (Sym) o;

        return getName().equals(sym.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Sym{" +
                "name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
