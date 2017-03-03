/*
 * Copyright 2011-15 Fraunhofer ISE
 *
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 *
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jASN1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jASN1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.ber;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BerByteArrayOutputStream extends OutputStream {

    public byte[] buffer;
    public int index;
    private final boolean automaticResize;

    /**
     * Creates a <code>BerByteArrayOutputStream</code> with a byte array of size <code>bufferSize</code>. The buffer
     * will not be resized automatically. Use {@link #BerByteArrayOutputStream(int, boolean)} instead if you want the
     * buffer to be dynamically resized.
     * 
     * @param bufferSize
     *            the size of the underlying buffer
     */
    public BerByteArrayOutputStream(int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize may not be <= 0");
        }
        buffer = new byte[bufferSize];
        index = bufferSize - 1;
        automaticResize = false;
    }

    public BerByteArrayOutputStream(int bufferSize, boolean automaticResize) {
        buffer = new byte[bufferSize];
        index = bufferSize - 1;
        this.automaticResize = automaticResize;
    }

    public BerByteArrayOutputStream(byte[] buffer, int startingIndex) {
        this.buffer = buffer;
        index = startingIndex;
        automaticResize = false;
    }

    public BerByteArrayOutputStream(byte[] buffer, int startingIndex, boolean automaticResize) {
        this.buffer = buffer;
        index = startingIndex;
        this.automaticResize = automaticResize;
    }

    @Override
    public void write(int arg0) throws IOException {
        write((byte) arg0);
    }

    public void write(byte arg0) throws IOException {
        try {
            buffer[index] = arg0;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (automaticResize) {
                resize();
                buffer[index] = arg0;
            }
            else {
                throw new ArrayIndexOutOfBoundsException("buffer.length = " + buffer.length);
            }
        }
        index--;
    }

    private void resize() {
        byte[] newBuffer = new byte[buffer.length * 2];
        System.arraycopy(buffer, index + 1, newBuffer, buffer.length + index + 1, buffer.length - index - 1);
        index += buffer.length;
        buffer = newBuffer;

    }

    @Override
    public void write(byte[] byteArray) throws IOException {
        for (int i = byteArray.length - 1; i >= 0; i--) {
            try {
                buffer[index] = byteArray[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                if (automaticResize) {
                    resize();
                    buffer[index] = byteArray[i];
                }
                else {
                    throw new ArrayIndexOutOfBoundsException("buffer.length = " + buffer.length);
                }
            }
            index--;
        }
    }

    /**
     * Returns a new array containing the subarray of the stream array that contains the coded content.
     * 
     * @return a new array containing the subarray of the stream array
     */
    public byte[] getArray() {
        if (index == -1) {
            return buffer;
        }
        int subBufferLength = buffer.length - index - 1;
        byte[] subBuffer = new byte[subBufferLength];
        System.arraycopy(buffer, index + 1, subBuffer, 0, subBufferLength);
        return subBuffer;

    }

    public ByteBuffer getByteBuffer() {
        return ByteBuffer.wrap(buffer, index + 1, buffer.length - (index + 1));
    }

    public void reset() {
        index = buffer.length - 1;
    }
}
