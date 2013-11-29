package tk.ivybits.javi;

import tk.ivybits.javi.exc.StreamException;
import tk.ivybits.javi.media.Media;
import tk.ivybits.javi.media.MediaFactory;
import tk.ivybits.javi.media.stream.AudioStream;
import tk.ivybits.javi.media.stream.SubtitleStream;
import tk.ivybits.javi.media.stream.VideoStream;
import tk.ivybits.javi.swing.StreamListener;
import tk.ivybits.javi.swing.SwingMediaPanel;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import static tk.ivybits.javi.ffmpeg.FFmpeg.*;

/**
 * Minimalistic media player.
 * <p/>
 * To run from command line, pass in the sources to play as arguments to the program.
 * Each source will open in a separate window.
 * Streaming is supported.
 *
 * @version 1.0
 * @since 1.0
 */
public class JPlay {
    public static void main(String[] args) throws IOException, LineUnavailableException {
        if (args.length < 1) {
            System.err.println("File not specified.");
            System.exit(1);
        }

        System.err.printf("Running avcodec-%s, avformat-%s, avutil-%s.\n", AVCODEC_VERSION, AVFORMAT_VERSION, AVUTIL_VERSION);

        for (String source : args) {
            play(source);
        }
    }

    public static void play(String source) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
        }
        File videoFile = new File(source);
        Media media = MediaFactory.open(videoFile);
        final long length = media.length();
        System.err.printf("Video is %s milliseconds (%s seconds) long.\n", length, length / 1000.0);

        final JFrame frame = new JFrame(videoFile.getName());
        frame.setLayout(new BorderLayout());

        final SwingMediaPanel videoPanel = new SwingMediaPanel(media);

        System.err.println("Streams");
        int area = 0;
        VideoStream video = null;
        for (final VideoStream str : media.videoStreams()) {
            int size = str.width() * str.height();
            if (size > area) {
                area = size;
                video = str;
            }
            System.err.printf("\tStream #%s: (%sx%s) - %s (%s)\n",
                    str.index(), str.width(), str.height(), str.codecName(), str.longCodecName());
        }

        for (final AudioStream str : media.audioStreams()) {
            System.err.printf("\tStream #%s: %s - %s (%s)\n",
                    str.index(), str.audioFormat(), str.codecName(), str.longCodecName());
        }

        for (final SubtitleStream str : media.subtitleStreams()) {
            System.err.printf("\tStream #%s: %s (%s)\n", str.index(), str.codecName(), str.longCodecName());
        }

        if (video != null)
            videoPanel.setVideoStream(video);
        if (!media.audioStreams().isEmpty())
            videoPanel.setAudioStream(media.audioStreams().get(0));
        if (!media.subtitleStreams().isEmpty())
            videoPanel.setSubtitleStream(media.subtitleStreams().get(0));

        MouseAdapter seeker = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doSeek(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                doSeek(e);
            }

            private void doSeek(MouseEvent e) {
                double ratio = e.getX() / (double) videoPanel.getWidth();
                long position = (long) (length * ratio);
                System.err.printf("Seek to %s milliseconds (%s seconds).\n", position, position / 1000.0);
                try {
                    videoPanel.seek(position);
                } catch (StreamException seekFailed) {
                    System.err.println("Seek failed.");
                }
            }
        };
        videoPanel.setBackground(Color.BLACK);
        videoPanel.addMouseListener(seeker);
        videoPanel.addMouseMotionListener(seeker);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    videoPanel.setPlaying(!videoPanel.isPlaying());
                }
            }
        });
        frame.add(BorderLayout.CENTER, videoPanel);
        int width, height;
        if (video != null) {
            width = video.width();
            height = video.height();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            if (width > screen.width - 20 || height > screen.height - 60) {
                width = screen.width - 20;
                height = screen.height - 60;
            }
        } else {
            width = 640;
            height = 480;
        }

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.err.printf("Frame loss: %.2f%%\n", videoPanel.frameLossRate() * 100);
            }
        });

        videoPanel.addStreamListener(new StreamListener() {
            @Override
            public void onEnd() {
                System.err.println("Playback finished.");
                frame.remove(videoPanel);
                frame.add(BorderLayout.CENTER, new JLabel("Playback finished.", JLabel.CENTER));
                frame.revalidate();
            }
        });
        videoPanel.start();
    }
}