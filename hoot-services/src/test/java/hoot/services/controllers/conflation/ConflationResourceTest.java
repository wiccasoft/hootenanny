/*
 * This file is part of Hootenanny.
 *
 * Hootenanny is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * --------------------------------------------------------------------
 *
 * The following copyright notices are generated automatically. If you
 * have a new notice to add, please use the format:
 * " * @copyright Copyright ..."
 * This will properly maintain the copyright information. DigitalGlobe
 * copyrights will be updated automatically.
 *
 * @copyright Copyright (C) 2016, 2017 DigitalGlobe (http://www.digitalglobe.com/)
 */
package hoot.services.controllers.conflation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import hoot.services.UnitTest;
import hoot.services.utils.HootCustomPropertiesSetter;


@Ignore
public class ConflationResourceTest {

    @Test
    @Category(UnitTest.class)
    public void testDateFormatter() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.UTC);
        String text = now.format(formatter);
        System.out.println("Formatted UTC date & time: " + text);
        //LocalDateTime parsedDate = LocalDateTime.parse(text, formatter);
    }


    // An OSM API DB input must always be a reference layer. Default ref layer = 1.

    @Test(expected = WebApplicationException.class)
    @Category(UnitTest.class)
    public void testOsmApiDbInputAsSecondary() throws Exception {
        try {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.TRUE);
            ConflateParams conflateParams = new ConflateParams();
            conflateParams.setInputType1("DB");
            conflateParams.setInput1("1");
            conflateParams.setInputType2("OSM_API_DB");
            conflateParams.setInputType2("-1");
            conflateParams.setOutputName("OutputLayer");
            conflateParams.setCollectStats(false);
            conflateParams.setAdvancedOptions("-D convert.bounding.box=0,0,0,0");

            String inputParams = "{" +
                    "\"INPUT1_TYPE\":\"DB\"," +
                    "\"INPUT1\":\"1\"," +
                    "\"INPUT2_TYPE\":\"OSM_API_DB\"," +
                    "\"INPUT2\":\"-1\"," +
                    "\"OUTPUT_NAME\":\"OutputLayer\"," +
                    "\"COLLECT_STATS\":\"false\"," +
                    "\"ADV_OPTIONS\":\"-D \\\"convert.bounding.box=0,0,0,0\\\"\"" + "}";

            ConflationResource spy = Mockito.spy(new ConflationResource());
            spy.conflate(conflateParams, "error");
        }
        catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            assertTrue(e.getResponse().getEntity().toString().contains("OSM_API_DB not allowed as secondary input type"));
            throw e;
        }
        finally {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.FALSE);
        }
    }

    @Test(expected = WebApplicationException.class)
    @Category(UnitTest.class)
    public void testOsmApiDbInputAsSecondary2() throws Exception {
        try {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.TRUE);
            ConflateParams conflateParams = new ConflateParams();

            String inputParams = "{" +
                       "\"REFERENCE_LAYER\":\"2\"," +
                       "\"INPUT1_TYPE\":\"OSM_API_DB\"," +
                       "\"INPUT1\":\"1\"," +
                       "\"INPUT2_TYPE\":\"DB\"," +
                       "\"INPUT2\":\"-1\"," +
                       "\"OUTPUT_NAME\":\"OutputLayer\"," +
                       "\"CONFLATION_TYPE\":\"Reference\"," +
                       "\"MATCH_THRESHOLD\":\"0.6\"," +
                       "\"MISS_THRESHOLD\":\"0.6\"," +
                       "\"USER_EMAIL\":\"test@test.com\"," +
                       "\"COLLECT_STATS\":\"false\"," +
                       "\"ADV_OPTIONS\":\"-D \\\"convert.bounding.box=0,0,0,0\\\"\"" +
                    "}";

            ConflationResource spy = Mockito.spy(new ConflationResource());
            spy.conflate(conflateParams, "error");
        }
        catch (WebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            assertTrue(e.getResponse().getEntity().toString().contains("OSM_API_DB not allowed as secondary input type"));
            throw e;
        }
        finally {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.FALSE);
        }
    }

    @Test(expected = WebApplicationException.class)
    @Category(UnitTest.class)
    public void testConflateOsmApiDbMissingMap() throws Exception {
        try {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.TRUE);
            ConflateParams conflateParams = new ConflateParams();

            String inputParams = "{" +
                        "\"INPUT1_TYPE\":\"OSM_API_DB\"," +
                        "\"INPUT1\":\"-1\"," +
                        "\"INPUT2_TYPE\":\"DB\"," +
                        "\"INPUT2\":\"-999\"," +
                        "\"OUTPUT_NAME\":\"OutputLayer\"," +
                        "\"CONFLATION_TYPE\":\"Reference\"," +
                        "\"MATCH_THRESHOLD\":\"0.6\"," +
                        "\"MISS_THRESHOLD\":\"0.6\"," +
                        "\"USER_EMAIL\":\"test@test.com\"," +
                        "\"COLLECT_STATS\":\"false\"," +
                        "\"ADV_OPTIONS\":\"-D \\\"convert.bounding.box=0,0,0,0\\\"\"" +
                    "}";

            ConflationResource spy = Mockito.spy(new ConflationResource());
            spy.conflate(conflateParams, "error");
        }
        catch (WebApplicationException e) {
            assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getResponse().getStatus());
            assertTrue(e.getResponse().getEntity().toString().contains("Error during process call!  Params: "));
            throw e;
        }
        finally {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.FALSE);
        }
    }

    @Test(expected = WebApplicationException.class)
    @Category(UnitTest.class)
    public void testConflateOsmApiDbNotEnabled() throws Exception {
        try {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.FALSE);
            ConflateParams conflateParams = new ConflateParams();

            String inputParams = "{" +
                       "\"INPUT1_TYPE\":\"OSM_API_DB\"," +
                       "\"INPUT1\":\"-1\"," +
                       "\"INPUT2_TYPE\":\"DB\"," +
                       "\"INPUT2\":\"2\"," +
                       "\"OUTPUT_NAME\":\"OutputLayer\"," +
                       "\"CONFLATION_TYPE\":\"Reference\"," +
                       "\"MATCH_THRESHOLD\":\"0.6\"," +
                       "\"MISS_THRESHOLD\":\"0.6\"," +
                       "\"USER_EMAIL\":\"test@test.com\"," +
                       "\"COLLECT_STATS\":\"false\"," +
                       "\"ADV_OPTIONS\":\"-D \\\"convert.bounding.box=0,0,0,0\\\"\"" +
                   "}";

            ConflationResource spy = Mockito.spy(new ConflationResource());

            List<Long> mapIds = new ArrayList<>();
            mapIds.add(1L);

            spy.conflate(conflateParams, "error");
        }
        catch (WebApplicationException e) {
            assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getResponse().getStatus());
/*
            assertTrue(e.getResponse().getEntity().toString().contains(
                    "Attempted to conflate an OSM API database data source but OSM API database support is disabled"));
*/
            throw e;
        }
        finally {
            HootCustomPropertiesSetter.setProperty("OSM_API_DB_ENABLED", Boolean.TRUE);
        }
    }
}