package com.example.a20134833.studentattend;

/**
 * Created by 20134833 on 2018-05-05.
 */

public class Item {

    private String m;
    private String n;
    private String d;
    private String l;

    public Item(String m, String n, String d, String l)   {
        this.m = m;
        this.n = n;
        this.d = d;
        this.l = l;
    }

    public String getM()    {
        return m;
    }

    public String getN()    {
        return n;
    }

    public String getD()    {
        return d;
    }

    public String getL()    {
        return l;
    }

    public void Setm(String M)    {
        this.m = M;
    }

    public void Setn(String N)    {
        this.n = N;
    }

    public void setd(String D)    {
        this.d = D;
    }

    public void setl(String L)    {
        this.l = L;
    }
}
