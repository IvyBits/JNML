package tk.ivybits.javi.media.ffmedia;

import tk.ivybits.javi.exc.StreamException;
import tk.ivybits.javi.ffmpeg.LibAVCodec;
import tk.ivybits.javi.ffmpeg.avcodec.AVCodec;
import tk.ivybits.javi.ffmpeg.avformat.AVStream;
import tk.ivybits.javi.media.Media;
import tk.ivybits.javi.media.stream.Stream;

import java.io.Closeable;

import static tk.ivybits.javi.ffmpeg.LibAVCodec.avcodec_close;
import static tk.ivybits.javi.ffmpeg.LibAVCodec.avcodec_find_decoder;
import static tk.ivybits.javi.ffmpeg.LibAVCodec.avcodec_open2;

/**
 * Represents an arbitrary stream in a container.
 */
public class FFStream implements Stream {
    public final FFMedia container;
    public final AVStream ffstream;
    public final AVCodec codec;
    protected boolean closed;

    FFStream(FFMedia container, AVStream ffstream) {
        this.container = container;
        this.ffstream = ffstream;
        codec = avcodec_find_decoder(ffstream.codec.codec_id);
        if (codec == null || avcodec_open2(ffstream.codec.getPointer(), codec.getPointer(), null) < 0) {
            throw new StreamException("unsupported " + type() + "codec");
        }
        ffstream.codec.read();
    }

    /**
     * Fetches the parent container.
     *
     * @return Said parent container.
     */
    public Media container() {
        return container;
    }

    /**
     * Fetches the type of this stream.
     *
     * @return A Type. Current possible values are STREAM_VIDEO and STREAM_AUDIO.
     */
    public Stream.Type type() {
        return Stream.Type.values()[ffstream.codec.codec_type];
    }

    /**
     * Fetches the index of this stream in the parent container
     *
     * @return
     */
    public int index() {
        return ffstream.index;
    }

    /**
     * Fetches the name of the codec.
     *
     * @return The name of the codec.
     * @since 1.0
     */
    public String codecName() {
        return ffstream.codec.codec.name;
    }

    /**
     * Fetches a descriptive name of the codec.
     *
     * @return The name; format may differ substantially from codec to codec.
     * @since 1.0
     */
    public String longCodecName() {
        return ffstream.codec.codec.long_name;
    }

    public void close() {
        if (!closed) {
            avcodec_close(ffstream.codec.getPointer());
        }
    }

    /**
     * Enum for all possible streams that a container may hold.
     *
     * @version 1.0
     * @since 1.0
     */
    public static enum Type {
        STREAM_VIDEO,
        STREAM_AUDIO,
        STREAM_DATA,
        STREAM_SUBTITLE,
        STREAM_ATTACHMENT
    }
}
