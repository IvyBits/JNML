/*
 * This file is part of JAVI.
 *
 * JAVI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * JAVI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JAVI.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package tk.ivybits.javi.swing;

/**
 * Listener for events generated by SwingMediaPanel playback.
 *
 * @version 1.0
 * @since 1.0
 */
public interface StreamListener {
    /**
     * Called when playback is started.
     *
     * @since 1.0
     */
    void onStart();

    /**
     * Called when playback is finished.
     *
     * @since 1.0
     */
    void onEnd();

    /**
     * Called before a stream seeks.
     *
     * @param to The destination timestamp of the stream, in milliseconds.
     * @since 1.0
     */
    void onSeek(long to);
}
