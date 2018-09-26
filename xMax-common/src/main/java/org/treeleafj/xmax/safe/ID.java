package org.treeleafj.xmax.safe;

/**
 * ID工具
 * <p>
 * Created by leaf on 2016/2/19.
 */
public class ID {

    private static Sequence sequence = new Sequence();

    /**
     * 生成的ID的后缀
     */
    private static String SUFFIX = null;

    /**
     * 生成一个有序,分布式唯一,且24位长度的id(只包含数字和字母,字母都为小写字母)
     *
     * @return
     */
    public static String get() {
        return org.apache.commons.codec.binary.Hex.encodeHexString(ObjectId.get().toByteArray());
    }

    /**
     * 生成一个有序,分布式唯一,且16位长度的短id(url安全的,区分大小写,包含"-")
     *
     * @return
     */
    public static String getShort() {
        return Base64.encodeURLSafe(ObjectId.get().toByteArray());
    }

    /**
     * 18位ID(如果不指定后缀的话)
     * <p>
     * <p>
     * 高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法。 <br>
     * 优化开源项目 http://git.oschina.net/yu120/sequence
     * </p>
     *
     * @author hubin
     */
    public static long getSequence() {
        return sequence.nextId();
    }

    /**
     * 18位ID
     * <p>
     * <p>
     * 高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法。 <br>
     * 优化开源项目 http://git.oschina.net/yu120/sequence
     * </p>
     *
     * @author hubin
     */
    public static String getSequenceString() {
        if (SUFFIX != null) {
            return String.valueOf(getSequence()) + SUFFIX;
        }
        return String.valueOf(getSequence());
    }

    public static void setSuffix(String suffix) {
        ID.SUFFIX = suffix;
    }
}
