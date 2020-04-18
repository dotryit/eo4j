package ch.feol.eo4j.solarlog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class DataTest {

   public static String TEST_DATA = "{\"801\":{\"170\":{\"100\":\"17.04.20 17:18:46\",\"101\":530,\"102\":537,\"103\":227,\"104\":342,\"105\":14036,\"106\":20904,\"107\":317210,\"108\":4057518931,\"109\":4057974897,\"110\":361,\"111\":5836,\"112\":10881,\"113\":163066,\"114\":3893661935,\"115\":3893755722,\"116\":2720}}}";

   public static LocalDate TEST_DATE = LocalDate.of(2020, 04, 17);

   public static LocalTime TEST_TIME = LocalTime.of(17, 18, 46);

   public static LocalDateTime TEST_TIMESTAMP = LocalDateTime.of(TEST_DATE, TEST_TIME);

   @Test
   public void test_parse() throws SolarLogException {

		Data result = Data.parse(TEST_DATA);

		assertEquals(TEST_TIMESTAMP, result.getLastUpdate());
		assertEquals(530, result.getProductionPowerAc());
		assertEquals(537, result.getProductionPowerDc());
		assertEquals(361, result.getConsumptionPowerAc());
	}
}
