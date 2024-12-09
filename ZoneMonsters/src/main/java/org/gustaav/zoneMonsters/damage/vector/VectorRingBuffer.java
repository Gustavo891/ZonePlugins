package org.gustaav.zoneMonsters.damage.vector;


import lombok.Getter;
import org.bukkit.util.Vector;

public class VectorRingBuffer {
    //class RingBuffer creates a ring buffer of Vectors of a given size.
    //The buffer is filled with random Vectors.

    @Getter
    private int size;
    private final Vector[] buffer;
    @Getter
    private int writeIndex;
    @Getter
    private int readIndex;

    public VectorRingBuffer(int size, VectorGenerator vectorGenerator) {
        this.size = size;
        buffer = new Vector[size];
        for(int i = 0; i < size; i++) {
            buffer[i] = vectorGenerator.getVector();
        }
        writeIndex = 0;
        readIndex = 0;
    }

    public Vector get(int index) {
        return buffer[index];
    }

    public void set(int index, Vector vector) {
        buffer[index] = vector;
    }

    public Vector getNext() {
        Vector next = buffer[readIndex];
        readIndex++;
        if(readIndex >= size) {
            readIndex = 0;
        }
        return next;
    }

    public void setNext(Vector vector) {
        buffer[writeIndex] = vector;
        writeIndex++;
        if(writeIndex >= size) {
            writeIndex = 0;
        }
    }

    public void reset() {
        for(int i = 0; i < size; i++) {
            buffer[i] = new Vector(0,0,0);
        }
        writeIndex = 0;
        readIndex = 0;
    }
}
