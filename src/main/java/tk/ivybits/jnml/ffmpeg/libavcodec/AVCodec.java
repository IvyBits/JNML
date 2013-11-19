package tk.ivybits.jnml.ffmpeg.libavcodec;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.LongByReference;
import tk.ivybits.jnml.ffmpeg.libavutil.AVClass;
import tk.ivybits.jnml.ffmpeg.libavutil.AVRational;

import java.util.Arrays;
import java.util.List;

public class AVCodec extends Structure {
    public static class ByReference extends AVPacket implements Structure.ByReference {
    }

    public static class ByValue extends AVPacket implements Structure.ByValue {
    }

    public String name;

    public String long_name;
    public int type;
    public int id;

    public int capabilities;
    public AVRational.ByReference supported_framerates;
    public Pointer pix_fmts;

    public Pointer supported_samplerates;
    public Pointer sample_fmts;
    public LongByReference channel_layouts;
    public byte max_lowres;
    public AVClass.ByReference priv_class;
    public Pointer /* AVProfile.ByReference */ profiles;

    public AVCodec(Pointer address) {
        super(address);
        read();
    }

    public AVCodec() {
        super();
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("name", "long_name", "type", "id", "capabilities",
                "supported_framerates", "pix_fmts", "supported_samplerates",
                "sample_fmts", "channel_layouts", "max_lowres", "priv_class", "profiles");
    }
}
