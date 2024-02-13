package dev.neilthomson.stringcounter.services;

public class SimpleWordCounterTest implements WordCounterTest<SimpleWordCounter> {
    @Override
    public SimpleWordCounter createWordCounter() {
        return new SimpleWordCounter();
    }
}
