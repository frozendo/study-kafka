# Kafka

**Apache Kafka** é um messaging system feito para trabalhar com streaming de dados distribuído, com o conceito chamado distributed commit log.

O sistema se organiza sobre alguns componentes básicos: 
* **Zookeeper:** armazena dados de configurações sobre os componentes do kafka.
* **Broker:** servidor onde o Kafka é executado, normalmente uma máquina 100% dedicada para isso.
* **Topic:** são "datasets" sequenciais que recebem e armazenam as mensagens e entrega aos interessados.
* **Partitions:** utilizados para separar e escalar topics, sendo arquivos de logs onde as mensagens são armazenadas.
* **Producer:** produz e envia os eventos para o Kafka.
* **Consumer:** se inscreve em um topic e recebe as mensagens que chegam a esse topic.

Além dos componentes básicos, existem aplicações que complementam o Kafka, trazendo novas funcionalidades e aumentando seus casos de uso: 
* **Kafka Streams:** processa continuamente dados que estão armazenados em um ou mais topics, realizando diversas operações e salvando as informações em outros topics e/ou tabelas.
* **Schema Registry:** aplicação que compartilha schemas de dados com producers e consumers.
* **KSQL:** mecanismo de streaming SQL que envolve a API do Kafka Streams, sendo uma outra forma de criar streaming.
* **Kafka Connector:** ferramenta genérica para transferência de dados, podendo retirar de uma fonte e enviando para o Kafka, ou retirando para o Kafka e salvando em uma fonte de dados.

Nesse repositório estão alguns exemplos de utilização do Kafka, com diferentes cenários de producers e consumers.

## Containers

Assim como muitas aplicações hoje em dia, Kafka também pode ser executado em containers. 
Apesar disso, a maioria dos ambientes produtivos utilizam outras alternativas, como as implementações fornecidas pelos provedores de nuvem. 
Porém containers são uma ótima opção para executar em alguns cenários, principalmente para aprendizado. 

Na pasta [docker](https://github.com/frozendo/study-kafka/tree/main/docker) temos dois exemplos de execução do Kafka em containers. 

[**custom-images**](https://github.com/frozendo/study-kafka/tree/main/docker/custom-images) cria todo o ambiente do zero, utilizando como base uma imagem Debian.
Existe um arquivo Dockerfile que configura imagens para Kafka e Zookeeper, instalando as ferramentas correspondentes e definindo os scripts de inicialização.  
Por fim, um arquivo _docker-compose.yml_ cria um cluster Kafka e um ensemble Zookeeper.  

[**confluent-images**](https://github.com/frozendo/study-kafka/tree/main/docker/confluent-images) utiliza as imagens existentes da confluent.
Por esse motivo, existe apenas o _docker-compose.yml_ que cria o cluster e o ensemble.

## Exemplos 

Abaixo a lista de exemplos desse repositório, com classes e topics utilizados, e o que está sendo feito em cada um deles.

### round-robin-topic-example 

Esse é um dos exemplos mais básicos do Kafka. Aqui as mensagens são enviadas para o topic sem definir uma key, apenas enviando os dados. 
Cabe ao Kafka balancear a carga entre as três partitions do topic, e para isso ele utiliza o algoritmo de round-robin. 
Esse producer está na classe [RoundRobinProducer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/RoundRobinProducer.java).

Para consumir os dados desse topic, utilizamos o [SubscribeConsumer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/consumer/SubscribeConsumer.java), que também é a forma mais básica de consumir dados do Kafka.
Ele se inscreve no topic utilizando o método **subscribe**, e apenas um consumidor fica responsável por ler os dados das três partitions.

### key-hash-example

Aqui além de enviar a mensagem, definimos uma key para ela. Assim, o Kafka utiliza essa key para balancear a carga entre as partitons do topic. 
A classe [KeyHashProducer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/KeyHashProducer.java) tem o producer para esse topic, e utiliza o código do produto como key da mensagem. Um produto com o mesmo código sempre cai na mesma partition.

O consumer por sua vez também utiliza o método subscriber, porém aqui temos três consumers fazendo parte de um grupo, e cada consumer recebe mensagens de apenas uma partition. 
Caso um dos consumers caia, o rebalanceamento é feito automaticamente. 
Outra diferença desse consumer é que os commits são feitos de forma manual, chamando explicitamente o método **commitSync**.
A classe [ManualCommitConsumer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/consumer/ManualCommitConsumer.java) é responsável por criar e executar esses consumers. 

### partition-direct-send-example

Esse topic utiliza o conceito de envio direto. A partition para onde a mensagem será enviada é definida pelo Producer, e esse valor é enviado junto ao enviar a mensagem.
Nesse caso, foi feito uma lógica similar a round-robin para mostrar o conceito, mas poderíamos utilizar qualquer lógica que fizesse sentido ao negócio. 
O producer está na classe [PartitionDirectSendProducer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/PartitionDirectSendProducer.java).

Por conta do envio direto de mensagens para um partition, o consumer desse topic mostra o conceito de **assing** do Kafka.
Ao invés de se inscrever em um topic e deixar o Kafka distribuir as partitions para os consumers, cada consumer diz ao Kafka de qual partition deseja ler as mensagens. 
O consumer recebe eventos apenas dessa partition e caso ele caia não irá ocorrer um rebalanceamento. 
O consumer está na classe [AssignConsumer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/consumer/AssignConsumer.java).

### custom-partitioner-example

Nesse topic é utilizado um partitioner customizado, ou seja, a definição de como as mensagens são balanceadas entre as partitions do topic é definido por nós, sem utilizar as soluções fornecidas pelo Kafka.
A classe [CustomPartitionerProducer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/CustomPartitionerProducer.java) produz os eventos para esse topic, utilizando o Partitioner [ProductDepartmentPartitioner](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/config/ProductDepartmentPartitioner.java).

O consumidor desse topic realiza os commits de forma manual e assíncrona, utilizando o método **commitAsync**. 
A lógica desse consumer está na classe [ManualAsyncCommitConsumer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/config/ProductDepartmentPartitioner.java).

### custom-serializer-example

Neste exemplo utilizamos serializadores e deserializadores customizados. 
O producer está na classe [CustomSerializerProducer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/CustomSerializerProducer.java) e utiliza a classe [Base64Serializer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/producer/config/Base64Serializer.java) para serializar os dados. 

Por outro lado, o consumer está na classe [CustomDeserializerConsumer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/consumer/CustomDeserializerConsumer.java), que utiliza a classe [Base64Deserializer](https://github.com/frozendo/study-kafka/blob/main/src/main/java/com/frozendo/study/consumer/config/Base64Deserializer.java) para deserializar os dados antes de consumir o evento.
