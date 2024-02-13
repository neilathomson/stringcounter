package dev.neilthomson.stringcounter.services;

public class SimpleParallelWordCounterTest implements WordCounterTest<SimpleParallelWordCounter> {
    @Override
    public SimpleParallelWordCounter createWordCounter() {
        return new SimpleParallelWordCounter();
    }
}
