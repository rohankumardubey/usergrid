/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.usergrid.persistence.cassandra.index;


import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.HColumn;


/**
 * Interface for scanning all index buckets.
 *
 * @author tnine
 */
public interface IndexScanner
        extends Iterable<List<HColumn<ByteBuffer, ByteBuffer>>>, Iterator<List<HColumn<ByteBuffer, ByteBuffer>>> {

    /** Reset the scanner back to the start */
    void reset();

    /**
     * Return the page size of the index
     * @return
     */
    int getPageSize();

    /**
     * Return true if this iterator iterates in reverse
     * @return
     */
    boolean isReversed();


}
