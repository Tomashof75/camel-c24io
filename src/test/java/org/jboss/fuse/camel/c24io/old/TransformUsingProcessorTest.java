/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
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
package org.jboss.fuse.camel.c24io.old;

import java.util.List;

import biz.c24.io.gettingstarted.transaction.Transactions;

import biz.c24.io.gettingstarted.transaction.transactions.StatGenTransform;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.jboss.fuse.camel.c24io.C24IOSource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import static org.jboss.fuse.camel.c24io.C24IOTransform.transform;

/**
 * @version $Revision$
 */
public class TransformUsingProcessorTest extends CamelTestSupport {
    @Test
    public void testC24() throws Exception {
        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        resultEndpoint.assertIsSatisfied();

        List<Exchange> list = resultEndpoint.getReceivedExchanges();
        Exchange exchange = list.get(0);
        Message in = exchange.getIn();

        String text = in.getBody(String.class);
        log.info("Received: " + text);
    }

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("file:src/test/data?noop=true").
                        process(C24IOSource.c24Source(Transactions.class)).
                        process(transform(StatGenTransform.class)).

                        to("mock:result");
            }
        };
    }
}
