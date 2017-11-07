package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.List;

/**
 * @author shilei
 * @Date 2017/11/7.
 */
public class Input {
    private List<String> input;
    private List<Double> l3rate;
    private List<String> l3name;
    private List<String> l3code;
    private List<Sym> syms;

    public Input() {
    }

    @Override
    public String toString() {
        return "Input{" +
                "input=" + input +
                ", l3rate=" + l3rate +
                ", l3name=" + l3name +
                ", l3code=" + l3code +
                ", syms=" + syms +
                '}';
    }

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public List<Double> getL3rate() {
        return l3rate;
    }

    public void setL3rate(List<Double> l3rate) {
        this.l3rate = l3rate;
    }

    public List<String> getL3name() {
        return l3name;
    }

    public void setL3name(List<String> l3name) {
        this.l3name = l3name;
    }

    //    public List<String> getL3code() {
    //        return l3code;
    //    }
    //
    //    public void setL3code(List<String> l3code) {
    //        this.l3code = l3code;
    //    }

    public List<Sym> getSyms() {
        return syms;
    }

    public void setSyms(List<Sym> syms) {
        this.syms = syms;
    }
}
