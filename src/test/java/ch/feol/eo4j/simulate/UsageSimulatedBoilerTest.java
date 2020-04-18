package ch.feol.eo4j.simulate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.feol.eo4j.optimize.Boiler;
import ch.feol.eo4j.optimize.TimerService;
import ch.feol.eo4j.optimize.UpdateResult;

public class UsageSimulatedBoilerTest {

   @Test
   public void test_useAndHeat_onePerson() {
      // Prepare
      UsageSimulator usageSimulator = new WarmWaterUsageSimulator(1, 40);
      Boiler boiler = new Boiler("Test", 120, 2.4);
      TimerService timerService = new SimulationTimerService(); // Start at midnight
      UsageSimulatedBoiler testee = new UsageSimulatedBoiler(usageSimulator, boiler, timerService);
      testee.setPower(true);

      UpdateResult result;

      timerService.sleepHours(6);
      result = testee.update(true);
      assertEquals(0.0, result.getWaterUsage());
      assertEquals(0.0, result.getEnergyUsage());
      assertEquals(50, testee.getOffset());

      timerService.sleepHours(1);
      result = testee.update(true);
      assertEquals(6.0, result.getWaterUsage(), 0.01);
      assertEquals(864.0, result.getEnergyUsage(), 0.01);
      assertEquals(49.23, testee.getOffset(), 0.01);

      timerService.sleepHours(1);
      result = testee.update(true);
      assertEquals(6.0, result.getWaterUsage(), 0.01);
      assertEquals(1296, result.getEnergyUsage(), 0.01);
      assertEquals(49.32, testee.getOffset(), 0.01);

      timerService.sleepHours(1);
      result = testee.update(true);
      assertEquals(1.33, result.getWaterUsage(), 0.01);
      assertEquals(576.0, result.getEnergyUsage(), 0.01);
      assertEquals(49.92, testee.getOffset(), 0.01);

   }

   @Test
   public void test_use_onePerson() {
      // Prepare
      UsageSimulator usageSimulator = new WarmWaterUsageSimulator(1, 40);
      Boiler boiler = new Boiler("Test", 120, 2.4);
      TimerService timerService = new SimulationTimerService(); // Start at midnight
      UsageSimulatedBoiler testee = new UsageSimulatedBoiler(usageSimulator, boiler, timerService);
      testee.setPower(false);

      UpdateResult result;

      // Act
      timerService.sleepHours(6);
      result = testee.update(false);
      assertEquals(0.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(50, testee.getOffset());

      timerService.sleepHours(1);
      result = testee.update(false);
      assertEquals(6.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(47.56, testee.getOffset(), 0.01);

      timerService.sleepHours(15);
      result = testee.update(false);
      assertEquals(34.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(35.82, testee.getOffset(), 0.01);

      timerService.sleepHours(2);
      result = testee.update(false);
      assertEquals(0.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(35.82, testee.getOffset(), 0.01);
   }

   @Test
   public void test_use_threePersons() {
      // Prepare
      UsageSimulator usageSimulator = new WarmWaterUsageSimulator(2, 40);
      Boiler boiler = new Boiler("Test", 120, 2.4);
      TimerService timerService = new SimulationTimerService(); // Start at midnight
      UsageSimulatedBoiler testee = new UsageSimulatedBoiler(usageSimulator, boiler, timerService);
      testee.setPower(false);

      UpdateResult result;

      // Act
      timerService.sleepHours(6);
      result = testee.update(false);
      assertEquals(0.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(50, testee.getOffset());

      timerService.sleepHours(1);
      result = testee.update(false);
      assertEquals(12.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(45.24, testee.getOffset(), 0.01);

      timerService.sleepHours(13);
      result = testee.update(false);
      assertEquals(44.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(31.35, testee.getOffset(), 0.01);

      timerService.sleepHours(6);
      result = testee.update(false);
      assertEquals(24.0, result.getWaterUsage(), 0.01);
      assertEquals(0.0, result.getEnergyUsage(), 0.01);
      assertEquals(25.66, testee.getOffset(), 0.01);

   }

}
