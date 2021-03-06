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

package tk.ivybits.javi.media.transcoder;

import tk.ivybits.javi.media.stream.Frame;

import java.nio.ByteBuffer;

public interface Filter {
    Filter VIDEO_NEGATE = new Filter() {
        @Override
        public void apply(Frame frame) {
            for (Frame.Plane plane : frame) {
                ByteBuffer buf = plane.buffer();
                int l = buf.limit();
                for (int i = 0; i != l; i++) {
                    buf.put(i, (byte) (255 - (buf.get(i) & 0xFF)));
                }
            }
        }
    };

    void apply(Frame buffer);
}
