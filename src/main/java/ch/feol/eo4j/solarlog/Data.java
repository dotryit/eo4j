package ch.feol.eo4j.solarlog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Data {

   public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");

   private LocalDateTime lastUpdate;

   private double productionPowerAc;

   private double productionPowerDc;

   private double consumptionPowerAc;

   public Data(LocalDateTime lastUpdate, double productionPowerAc, double productionPowerDc, double consumptionPowerAc) {
      this.lastUpdate = lastUpdate;
      this.productionPowerAc = productionPowerAc;
      this.productionPowerDc = productionPowerDc;
      this.consumptionPowerAc = consumptionPowerAc;
   }

   public double getProductionPowerAc() {
      return productionPowerAc;
   }

   public double getProductionPowerDc() {
      return productionPowerDc;
   }

   public double getConsumptionPowerAc() {
      return consumptionPowerAc;
   }

   public static Data parse(String data) throws SolarLogException {

      LocalDateTime lastUpdate = null;
      double productionPowerAc = 0;
      double productionPowerDc = 0;
      double consumptionPowerAc = 0;

      JsonElement element = JsonParser.parseString(data);
      if (element.isJsonObject()) {
         JsonObject logData = element.getAsJsonObject();
         if (logData.has("801")) {
            JsonObject root = logData.getAsJsonObject("801");
            if (root.has("170")) {
               JsonObject properties = root.getAsJsonObject("170");
               lastUpdate = getAsTimestamp(properties, "100", FORMATTER);
               productionPowerAc = getAsDouble(properties, "101");
               productionPowerDc = getAsDouble(properties, "102");
               consumptionPowerAc = getAsDouble(properties, "110");
            } else {
               throw new IllegalStateException("SolarLog data has no properties (170)");
            }
         } else {
            throw new IllegalStateException("SolarLog data has no root (801)");
         }
      } else {
         throw new IllegalStateException("Input data is no JsonObject");
      }
      return new Data(lastUpdate, productionPowerAc, productionPowerDc, consumptionPowerAc);
   }

   private static LocalDateTime getAsTimestamp(JsonObject properties, String name, DateTimeFormatter formatter) throws SolarLogException {
      if (properties.has(name)) {
         return LocalDateTime.parse(properties.getAsJsonPrimitive(name).getAsString(), formatter);
      } else {
         throw new SolarLogException("Timestamp with name " + name + " does not exist");
      }
   }

   private static double getAsDouble(JsonObject properties, String name) throws SolarLogException {
      if (properties.has(name)) {
         return properties.getAsJsonPrimitive(name).getAsDouble();
      } else {
         throw new SolarLogException("Double with name " + name + " does not exist");
      }
   }

   public LocalDateTime getLastUpdate() {
      return lastUpdate;
   }

   @Override
   public String toString() {
      // TODO Auto-generated method stub
      return super.toString();
   }

}
