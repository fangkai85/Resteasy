package org.jboss.resteasy.test.smoke;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.UriFor;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.test.EmbeddedContainer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Simple smoke test
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class TestClient
{

   private static Dispatcher dispatcher;

   @BeforeClass
   public static void before() throws Exception
   {
      dispatcher = EmbeddedContainer.start();
   }

   @AfterClass
   public static void after() throws Exception
   {
      EmbeddedContainer.stop();
   }

   @Test
   public void testNoDefaultsResource() throws Exception
   {
      int oldSize = dispatcher.getRegistry().getSize();
      dispatcher.getRegistry().addPerRequestResource(SimpleResource.class);
      Assert.assertTrue(oldSize < dispatcher.getRegistry().getSize());

      SimpleClient client = ProxyFactory.create(SimpleClient.class, "http://localhost:8081");

      Assert.assertEquals("basic", client.getBasic());
      Assert.assertEquals("/basic", UriFor.resolve(SimpleClient.class, "getBasic"));
      client.putBasic("hello world");
      Assert.assertEquals("hello world", client.getQueryParam("hello world"));
      Assert.assertEquals(1234, client.getUriParam(1234));

      dispatcher.getRegistry().removeRegistrations(SimpleResource.class);
      Assert.assertEquals(oldSize, dispatcher.getRegistry().getSize());
   }


}