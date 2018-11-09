# Kafka Streams Skeleton

Start development for Kafka Streams in 3 seconds without any(!) extra services.

# How to

1. `gradle run`
2. There is no step two

# Warning

[Matthias J. Sax from Confluent](https://twitter.com/MatthiasJSax) warned on [Twitter](https://twitter.com/MatthiasJSax/status/1060922150045929472) that `EmbededKafkaCluster` is not part of public API:
```
No code from any test package is public API and may change without deprecation 
or notice even in bug fixes releases. That's why we added `streams-test-utils`
artifact in 1.1 release: 
https://cwiki.apache.org/confluence/display/KAFKA/KIP-247%3A+Add+public+test+utils+for+Kafka+Streams
Docs: https://kafka.apache.org/20/documentation/streams/developer-guide/testing.html

I guess it's in the book [Kafka Streams in Action] because it's the only way to
do integration testing atm. And I am not saying "don't use it". Just want to 
make you aware of the guarantee you get: none.

We hope to integrate it in  `streams-test-utils` eventually.
```
