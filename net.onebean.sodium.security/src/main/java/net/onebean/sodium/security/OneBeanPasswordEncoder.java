package net.onebean.sodium.security;

import net.onebean.util.PropUtil;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.util.EncodingUtils;
import org.springframework.stereotype.Service;

/**
 * 密码加解密工具类
 * @author 0neBean
 */
@Service
public class OneBeanPasswordEncoder implements PasswordEncoder {
    private final OnebeanDigester digester;
    private final byte[] secret;
    private final BytesKeyGenerator saltGenerator;
    private static final int DEFAULT_ITERATIONS = 1024;

    public OneBeanPasswordEncoder() {
        this("SHA-256", PropUtil.getInstance().getConfig("spring.security.password.encryption.secret",PropUtil.DEFLAULT_NAME_SPACE));
    }

    public String encode(CharSequence rawPassword) {
        return this.encode(rawPassword, this.saltGenerator.generateKey());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] digested = this.decode(encodedPassword);
        byte[] salt = EncodingUtils.subArray(digested, 0, this.saltGenerator.getKeyLength());
        return this.matches(digested, this.digest(rawPassword, salt));
    }

    private OneBeanPasswordEncoder(String algorithm, CharSequence secret) {
        this.digester = new OnebeanDigester(algorithm, DEFAULT_ITERATIONS);
        this.secret = Utf8.encode(secret);
        this.saltGenerator = KeyGenerators.secureRandom();
    }

    private String encode(CharSequence rawPassword, byte[] salt) {
        byte[] digest = this.digest(rawPassword, salt);
        return new String(Hex.encode(digest));
    }

    private byte[] digest(CharSequence rawPassword, byte[] salt) {
        byte[] digest = this.digester.digest(EncodingUtils.concatenate(new byte[][]{salt, this.secret, Utf8.encode(rawPassword)}));
        return EncodingUtils.concatenate(new byte[][]{salt, digest});
    }

    private byte[] decode(CharSequence encodedPassword) {
        return Hex.decode(encodedPassword);
    }

    private boolean matches(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        } else {
            int result = 0;

            for(int i = 0; i < expected.length; ++i) {
                result |= expected[i] ^ actual[i];
            }

            return result == 0;
        }
    }

}
