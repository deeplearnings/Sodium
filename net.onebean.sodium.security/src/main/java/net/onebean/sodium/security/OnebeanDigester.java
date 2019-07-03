package net.onebean.sodium.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class OnebeanDigester {
    private final String algorithm;
    private final int iterations;

    public OnebeanDigester(String algorithm, int iterations) {
        createDigest(algorithm);
        this.algorithm = algorithm;
        this.iterations = iterations;
    }

    public byte[] digest(byte[] value) {
        MessageDigest messageDigest = createDigest(this.algorithm);

        for(int i = 0; i < this.iterations; ++i) {
            value = messageDigest.digest(value);
        }

        return value;
    }

    private static MessageDigest createDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalStateException("No such hashing algorithm", var2);
        }
    }
}
