package tk.ivybits.jnml.ffmpeg.libavcodec;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Tudor
 * 16/11/13
 */
public class AVPacket extends Structure {
    public static class ByReference extends AVPacket implements Structure.ByReference {}
    public static class ByValue extends AVPacket implements Structure.ByValue {}

    public Pointer buf;
    public long pts;
    public long dts;
    public Pointer data;
    public int size;
    public int stream_index;
    public int flags;

    /*
        uint8_t *data;
        int      size;
        enum AVPacketSideDataType type;
     */
    public Pointer side_data;
    public int side_data_elems;
    public int duration;
    public Pointer destruct;
    public Pointer priv;
    public long convergence_duration;

    public AVPacket(Pointer address) {
        super(address);
        read();
    }

    public AVPacket() {
        super();
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("buf", "pts", "dts", "data", "size",
                "stream_index", "flags", "side_data", "side_data_elems",
                "duration", "destruct", "priv", "convergence_duration");
    }
}