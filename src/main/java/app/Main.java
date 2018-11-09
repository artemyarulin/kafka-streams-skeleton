package app;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Time;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.streams.integration.utils.IntegrationTestUtils;
import org.apache.kafka.test.TestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

class Main {
    static EmbeddedKafkaCluster kafka = new EmbeddedKafkaCluster(1);
    static void startStream() {
        StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream("input")
                // Go crazy with Kafka Stream here!
                .mapValues((key, value) -> "Hello " + value)
                .to("output");
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers());
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        new KafkaStreams(builder.build(), config).start();
    }
    public static void main(String[] args) {
        try {
            kafka.start();
            kafka.createTopics("from", "to");
            startStream();
            IntegrationTestUtils.produceValuesSynchronously("input",
                    Collections.singletonList("World"),
                    TestUtils.producerConfig(kafka.bootstrapServers(), StringSerializer.class, StringSerializer.class),
                    Time.SYSTEM);
            List<String> res = IntegrationTestUtils.waitUntilMinValuesRecordsReceived(
                    TestUtils.consumerConfig(kafka.bootstrapServers(), StringDeserializer.class, StringDeserializer.class), "output", 1);
            System.out.println("Stream returned: " + res);
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Err: " + ex.toString());
        }
    }
}