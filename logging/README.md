sbt "project logging" clean assembly && \
java -jar logging/target/scala-2.12/logging-assembly-1.0.0.jar bao.ho.app.Main

jar tvf logging/target/scala-2.12/logging-assembly-1.0.0.jar | grep "MANIFEST.MF"

cd logging/target/scala-2.12/ && jar xf logging-assembly-1.0.0.jar 

