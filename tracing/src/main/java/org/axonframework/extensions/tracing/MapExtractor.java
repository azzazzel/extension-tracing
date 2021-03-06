/*
 * Copyright (c) 2010-2020. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.axonframework.extensions.tracing;

import io.opentracing.propagation.TextMap;
import org.axonframework.messaging.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An implementation of {@link TextMap}, to extract tracing fields from {@link MetaData}.
 *
 * @author Christophe Bouhier
 * @since 4.0
 */
public class MapExtractor implements TextMap {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<String, String> extracted = new HashMap<>();

    /**
     * Instantiate a {@link MapExtractor} used to retrieve tracing fields from the given {@link MetaData}.
     *
     * @param metaData the {@link MetaData} to retrieve tracing fields from
     */
    public MapExtractor(MetaData metaData) {
        metaData.entrySet().forEach(
                entry -> {
                    if (entry.getValue() instanceof String) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Extracting metadata entry: {}", entry);
                        }
                        extracted.put(entry.getKey(), (String) entry.getValue());
                    }
                }
        );
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return extracted.entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        throw new UnsupportedOperationException("The iterator() method should only be used with Tracer.extract()");
    }
}
