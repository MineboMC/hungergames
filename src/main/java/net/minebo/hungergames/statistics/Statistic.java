package net.minebo.hungergames.statistics;

/*
    If we ever add more statistics later this will be more useful.
 */

public class Statistic {

    public Integer value = 0;

    public Integer get() { return value; }
    public Integer set(Integer value) { this.value = value; return value; }
    public Integer add(Integer value) { this.value += value; return value; }

}

