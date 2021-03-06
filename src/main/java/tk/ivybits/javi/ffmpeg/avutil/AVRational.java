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

package tk.ivybits.javi.ffmpeg.avutil;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class AVRational extends Structure {
    public static class ByReference extends AVRational implements Structure.ByReference {
    }

    public int num;
    public int den;

    public AVRational() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("num", "den");
    }
}