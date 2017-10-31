package com.xjtushilei.querydiagnosis.entity.icd10;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L4 {
    private String code;
    private String name;

    public L4(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "L4{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
