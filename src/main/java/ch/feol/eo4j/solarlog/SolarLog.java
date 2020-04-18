package ch.feol.eo4j.solarlog;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;

public class SolarLog {

   // One instance, reuse
   private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

   private String address;

   public SolarLog(String address) {
      this.address = address;
   }

   public Data read() throws SolarLogException {

      Builder builder = HttpRequest.newBuilder();
      builder.POST(HttpRequest.BodyPublishers.ofString(""));
      builder.uri(URI.create("http://" + address + "/getjp"));
      builder.header("Content-Type", "application/json");
      HttpRequest request = builder.build();

      HttpResponse<String> response;
      try {
         response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      } catch (IOException | InterruptedException e) {
         throw new SolarLogException("Sending the request failed", e);
      }

      if (response.statusCode() != 200) {
         throw new SolarLogException("Post request status code is " + response.statusCode());
      }
      return Data.parse(response.body());
   }
}
