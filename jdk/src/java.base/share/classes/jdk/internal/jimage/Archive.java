/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.internal.jimage;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * An Archive of all content, classes, resources, configuration files, and
 * other, for a module.
 */
public interface Archive {

    /**
     * Entry is contained in an Archive
     */
    public abstract class Entry {

        public static enum EntryType {

            MODULE_NAME,
            CLASS_OR_RESOURCE,
            NATIVE_LIB,
            NATIVE_CMD,
            CONFIG,
            SERVICE;
        }

        private final String name;
        private final EntryType type;
        private final Archive archive;
        private final String path;

        public Entry(Archive archive, String path, String name, EntryType type) {
            this.archive = archive;
            this.path = path;
            this.name = name;
            this.type = type;
        }

        public Archive archive() {
            return archive;
        }

        public String path() {
            return path;
        }

        public EntryType type() {
            return type;
        }

        /**
         * Returns the name of this entry.
         */
        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return "type " + type.name() + " path " + path;
        }

        /**
         * Returns the number of uncompressed bytes for this entry.
         */
        public abstract long size();

        public abstract InputStream stream() throws IOException;
    }

    /**
     * The module name.
     */
    String moduleName();

    /**
     * Stream of Entry.
     * The stream of entries needs to be closed after use
     * since it might cover lazy I/O based resources.
     * So callers need to use a try-with-resources block.
     */
    Stream<Entry> entries();

    /**
     * Open the archive
     */
    void open() throws IOException;

    /**
     * Close the archive
     */
    void close() throws IOException;
}
