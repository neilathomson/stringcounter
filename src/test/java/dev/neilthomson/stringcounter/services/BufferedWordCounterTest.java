package dev.neilthomson.stringcounter.services;

public class BufferedWordCounterTest implements WordCounterTest<BufferedWordCounter> {
    @Override
    public BufferedWordCounter createWordCounter() {
        return new BufferedWordCounter(1024);
    }
}
