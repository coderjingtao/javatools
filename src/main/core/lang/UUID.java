package main.core.lang;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UUID implements Serializable {

    private final long mostSigBits;

    private final long leastSigBits;

    private UUID(byte[] data){
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16-bit long";
        for(int i = 0; i < 8; i++){
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for(int i = 8; i < 16; i++){
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        mostSigBits = msb;
        leastSigBits = lsb;
    }

    public static UUID fastUUID(){
        return randomUUID(false);
    }

    /**
     * The factory method of a 16-bit UUID
     * @param isSecure if it is true, a more secure random will be produced.
     * @return
     */
    public static UUID randomUUID(boolean isSecure){
        final Random random = isSecure ? new SecureRandom() : ThreadLocalRandom.current();
        final byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        /* clear version */
        randomBytes[6] &= 0x0f;
        /* set to version 4 */
        randomBytes[6] |= 0x40;
        /* clear variant */
        randomBytes[8] &= 0x3f;
        /* set to IETF variant */
        randomBytes[8] |= 0x80;
        return new UUID(randomBytes);
    }

    public String toString(boolean isSimple){
        final StringBuilder sb = new StringBuilder(isSimple ? 32 : 36);
        //time_low
        sb.append(digits(mostSigBits >> 32 , 8));
        if(!isSimple){
            sb.append('-');
        }
        sb.append(digits(mostSigBits >> 16, 4));
        if(!isSimple){
            sb.append('-');
        }
        sb.append(digits(mostSigBits,4));
        if(!isSimple){
            sb.append('-');
        }
        sb.append(digits(leastSigBits >> 48, 4));
        if(!isSimple){
            sb.append('-');
        }
        sb.append(digits(leastSigBits,12));
        return sb.toString();
    }

    /**
     * 返回指定数字对应的hex值
     * @param val
     * @param digits
     * @return
     */
    private static String digits(long val, int digits){
        long hex = 1L << (digits * 4);
        return Long.toHexString( hex | (val & (hex -1))).substring(1);
    }
}
